package com.qianfan365.jcstore.controller.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qianfan365.jcstore.common.bean.OrderDateBean;
import com.qianfan365.jcstore.common.bean.OrderInfoBean;
import com.qianfan365.jcstore.common.bean.OrderPrintBean;
import com.qianfan365.jcstore.common.bean.RefundPrintBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.ShopOrderListBean;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderQRCodeUrl;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderStatusConstant;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderType;
import com.qianfan365.jcstore.common.constant.PermissionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.Customer;
import com.qianfan365.jcstore.common.pojo.OrderBespokeDetail;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.PageForm;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.common.util.WeiYuRequst;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.CustomerService;
import com.qianfan365.jcstore.service.OrderInfoService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ProductService;
import com.qianfan365.jcstore.service.RefundService;
import com.qianfan365.jcstore.service.ShopService;
import com.qianfan365.jcstore.service.UserService;
import com.qianfan365.jcstore.service.OrderInfoService.UpdateInventoryException;

/**
 * 订单相关
 * 
 * @author guanliyu
 *
 */
@Controller
@RequestMapping("/mobile/order")
public class OrderInfoController extends BaseController {
  
  @Value("${common.domain-url}")
  String domainUrl;

  private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
  @Autowired
  private ShopService shopService;

  @Autowired
  private OrderInfoService orderInfoService;

  @Autowired
  private UserService userService;

  @Autowired
  private PermissionInfoService permissionInfoService;
  
  @Autowired
  private ProductService productService;
  
  @Autowired
  private RefundService refundService;
  
  @Autowired
  private CustomerService customerService;

  /**
   * 增加订单
   * 
   * @param session
   * @param orderInfo
   * @param param
   * @return
   */
  @RequestMapping(value = "add", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.PLACE_ORDER})
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.GENERAL_ORDER},setuser=true)
  public ResultData add(HttpSession session, OrderInfo orderInfo, String param) {
    if(orderInfo.getReceiceAmoun() == null || orderInfo.getCustomer() == null
        || orderInfo.getPhone() == null || StringUtils.isBlank(param)){
      return ResultData.build().parameterError();
    }
    if (orderInfo.getDescription() != null && orderInfo.getDescription().length() > 150) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getAmountAdvanced() != null && orderInfo.getAmountAdvanced() > orderInfo.getReceiceAmoun()) {
      return ResultData.build().failure();
    }
    if(orderInfo.getDiscount()>1||orderInfo.getDiscount()<=0){
      return ResultData.build().failure();
    }
    if (orderInfo.getExpectedDeliveryDate() != null && orderInfo.getExpectedDeliveryDate().before(TimeUtils.formatDayBegin(new Date()))){ // 预计发货日期只能选今天或今天以后的日期
      return ResultData.build().parameterError();
    }
    if(StringUtils.isNotBlank(orderInfo.getAdjunctImg()) && orderInfo.getAdjunctImg().split(",").length>5){
      return ResultData.build().parameterError();
    }
    if(orderInfo.getAmountAdvanced()==null){
    	orderInfo.setAmountAdvanced(0.00);
    }
    User user = getLoginUser(session);
    Customer customer = new Customer();
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    if(orderInfo.getCustomerId()==null){
      customer.setName(orderInfo.getCustomer());
      customer.setPhone(orderInfo.getPhone());
      customer.setAddress(orderInfo.getContactWith());
      if(!customerService.check(customer)){
        return ResultData.build().parameterError();
      }
    }else if(!moduleids.contains(ModuleType.CUSTOMER)){
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    orderInfo.setSalesman(user.getStaffname());
    orderInfo.setNumber(formatter.format(new Date()) + (new Random().nextInt(899999)+100000));// 设置订单号精确到秒+随机6位数字
    
    //计算总计
    orderInfo.setTotal(0.0);
    JSONObject paramJson = JSON.parseObject(param);
    JSONArray detailArray = paramJson.getJSONArray("detail");
    List<Integer> productIds=Lists.newArrayList();
    detailArray.stream().forEach(json->{
      JSONObject j=(JSONObject)json;
      double price = j.getDoubleValue("price");
      int number = j.getIntValue("number");
      int productId = j.getIntValue("productId");
      productIds.add(productId);
      orderInfo.setTotal(orderInfo.getTotal()+price*number);
    });   
    int result = 1;
    Shop shop = shopService.findShop(user);
    try{
      result = orderInfoService.addOrderInfo(orderInfo, param, user, shop, productIds, customer);
    }catch (IllegalArgumentException e) {
      List<Integer> deletedProductIds = productService.findByPidProductWithIds(productIds);
      return ResultData.build().setStatus(2).put("deletedIds", deletedProductIds);
    } catch(UpdateInventoryException uie) {
      List<Product> products = productService.findByIds(productIds,shop.getId());
      return ResultData.build().setStatus(Status.INVENTORY_SHORTAGE).put("product", products);
    }
    if(result == 10601){
      return ResultData.build().setStatus(Status.TRIAL_ACCOUNT_CUSTOMER_NUM_BEYOND);
    }
    if(result==25000){
      return ResultData.build().setStatus(Status.CUSTOM_NOT_FIND);
    }
    return ResultData.build().success().put("oId", orderInfoService.findByOrderNumber(orderInfo.getNumber()).getId());
  }
  
  /**
   * 极简开单
   * @version 1.4
   * @param session
   * @param orderInfo
   * @return
   */
  @RequestMapping(value = "add/minimal", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.PLACE_ORDER})
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.MINIMAL_ORDER},setuser=true)
  public ResultData addMinimal(HttpSession session, OrderInfo orderInfo) {
    if(orderInfo.getReceiceAmoun() == null || orderInfo.getInvoiceImg() == null){
      return ResultData.build().parameterError();
    }
    if (orderInfo.getDescription() != null && orderInfo.getDescription().length() > 150) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getAmountAdvanced() != null && orderInfo.getAmountAdvanced() > orderInfo.getReceiceAmoun()) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getExpectedDeliveryDate() != null && orderInfo.getExpectedDeliveryDate().before(TimeUtils.formatDayBegin(new Date()))){ // 预计发货日期只能选今天或今天以后的日期
      return ResultData.build().parameterError();
    }
    if(orderInfo.getAmountAdvanced()==null){
    	orderInfo.setAmountAdvanced(0.00);
    }
    User user = getLoginUser(session);
    
    if(orderInfoService.addOrderMinimal(orderInfo, user)==1){
      return ResultData.build().success().put("oId", orderInfo.getId());
    }else {
      return ResultData.build().failure();
    }
  }

  /**
   * 订单列表  ||  订单搜索
   * @version 1.1
   * @param session
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO}, setuser = true)
  public ResultData findAllOrderInfo(HttpSession session,
      @RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, 
      Integer customerId, String customer, String phone,
      Integer status, Long querytime, String pad) {
    if (begin != null && end != null && begin.after(end)) {//开始时间在结束时间之后
      return ResultData.build().parameterError();
    }
    if (1 == currentPage || null == querytime || 0 == querytime) {
      querytime = new Date().getTime();
    }
    User user = getLoginUser(session);
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
    boolean bespokeModule = moduleids.contains(ModuleType.BESPOKE_ORDER);
    boolean cancelModule = moduleids.contains(ModuleType.ORDER_CANCEL);
    if(!generalModule && !bespokeModule && (customer!=null || phone!=null)){
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    if(!bespokeModule && status!=null && status==OrderStatusConstant.orderStatusPre){
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    //只有同时没有定制模块和订单模块是订单列表才没有作废标签,但全部里仍能查看以前的作废单[只考虑模块就行,不用考虑权限(例如:有查看全员权限,没作废权限,仍能看到别人作废的订单)]
    if(!bespokeModule && !cancelModule && status!=null && status==OrderStatusConstant.orderStatusCancel){
    	return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    
    List<Integer> userIds = new ArrayList<Integer>();// 定义所有userID集合
    // 查询是否有查看所有订单的权限
    if (user.getBelongs() == 0 || permissionInfoService
        .findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) != null) {
      int belongs = (user.getBelongs() == 0)?user.getId():user.getBelongs();
      List<User> userLists = userService.findUserBelongId(belongs);
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(belongs);
    }else{ //个人内容权限
      userIds.add(user.getId());
    }
    if(status!=null && status == -1){
      Page<OrderInfo> page = orderInfoService.getRefundablePage(currentPage, limit, customer, userIds
        , querytime, generalModule, minimalModule);
      PageForm pageform = orderInfoService.findAllOrder(page,null,null);
      return ResultData.build().parseMap(pageform.toJson()).put("querytime", querytime).put("general", generalModule);
    }
    List<Integer> perms = Arrays.asList(PermissionType.FINISH_ORDER,PermissionType.PLACE_ORDER,
    		PermissionType.BESPOKE_FINISH_ORDER);
    Map<Integer, Boolean> map = permissionInfoService.findMapByUidPids(perms ,user.getId(), user.getBelongs());
    if(!bespokeModule){//经协商取消定制模块时,以前下的定制单仍能查看,但不能做任何操作,故有此判断
    	map.put(PermissionType.BESPOKE_FINISH_ORDER,false);
    }
    Page<OrderInfo> page = orderInfoService.getOrderPage(currentPage, limit, customerId, customer,
    		phone, begin, end, userIds, status, querytime, cancelModule, bespokeModule, pad);
    PageForm pageform = orderInfoService.findAllOrder(page,user,map);
    Map<String,Boolean> addOrder = Maps.newHashMap();
    if(map.get(PermissionType.PLACE_ORDER)){
      addOrder.put("minimalButton",minimalModule);
      addOrder.put("generalButton",generalModule);
    }else{
      addOrder.put("minimalButton",false);
      addOrder.put("generalButton",false);
    }
    boolean customerModule = moduleids.contains(ModuleType.CUSTOMER);
    return ResultData.build().parseMap(pageform.toJson()).put("querytime", querytime)
        .put("addOrder", addOrder).put("cancel", cancelModule).put("bespoke", bespokeModule)
        .put("general", generalModule).put("customer", customerModule);
  }
  
  /**
   * 查询全部,待完成,已完成订单 根据是否有查看所有订单的权限或者是否是店长，或者就是个普通个人内容权限
   * @version 1.0
   * @param session
   * @param currentPage
   * @param limit
   * @return
   */
  @RequestMapping(value = "all", method = RequestMethod.GET)
  @ResponseBody
  @Deprecated
  public ResultData findAllOrderInfo(HttpSession session,
      @RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit, Integer status) {
    User user = getLoginUser(session);
    List<Integer> userIds = new ArrayList<Integer>();// 定义所有userID集合
    if (permissionInfoService
        .findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) != null) {// 查询是否有查看所有订单的权限
      User users = userService.findUserById(user.getId());
      if(users==null){
        ResultData.build().setStatus(ResultData.Status.USER_STATUS_DELETE);
      }
      List<User> userLists = userService.findUserBelongId(users.getBelongs());
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(user.getBelongs());
    }

    if (user.getBelongs() == 0) {// 查询出父用户下面的子用户
      List<User> userLists = userService.findUserBelongId(user.getId());
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(user.getId());
    }
//    userIds.add(user.getBelongs());
     userIds.add(user.getId());
    PageForm page = orderInfoService.findAllOrder(currentPage, limit, userIds, status);
    return ResultData.build().parseMap(page.toJson()).put("permissionInfo",
            permissionInfoService.findMapByUidPid(PermissionType.FINISH_ORDER,
              user.getId(), user.getBelongs()));
  }

  /**
   * 订单确认
   * 
   * @param session
   * @param Id
   * @return
   */
  @RequestMapping(value = "finish", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.FINISH_ORDER})
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO}, setuser = true)
  public ResultData orderInfoFinish(HttpSession session, Integer id, String signatureImg) {
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(id);
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
    boolean printModule = moduleids.contains(ModuleType.ORDER_PRINT);
    if(order.getMinimalFlag()==OrderType.BESPOKE){
      return ResultData.build().parameterError();
    }
    if(order.getMinimalFlag()==OrderType.ROUTINE?!generalModule:!minimalModule){
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    boolean sendMessage = !user.getIsTrialAccount() && moduleids.contains(ModuleType.MESSAGE);
    Integer belongs = user.getBelongs();
    List<Integer> userIds = new ArrayList<Integer>();// 定义所有userID集合
    if (user.getBelongs() == 0) {// 店长权限
      belongs = user.getId();
      List<User> userLists = userService.findUserBelongId(user.getId());
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(user.getId());
      if (orderInfoService.finish(order, userIds, belongs, signatureImg, sendMessage,printModule) == 1) {
        return ResultData.build().success();
      } else {
        return ResultData.build().failure();
      }
    }
    int[] permission =
        {PermissionConstant.PermissionType.FINISH_ORDER, PermissionConstant.PermissionType.VIEW_ALL};
    if (permissionInfoService.checkPermission(user.getId(), permission)) {// 有查看全部订单以及确认订单的权限
      User users = userService.findUserById(user.getId());
      if(users==null){
        ResultData.build().setStatus(ResultData.Status.USER_STATUS_DELETE);
      }
      List<User> userLists = userService.findUserBelongId(belongs);
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(belongs);
      if (orderInfoService.finish(order, userIds, belongs, signatureImg, sendMessage,printModule) == 1) {
        return ResultData.build().success();
      } else {
        return ResultData.build().failure();
      }
    } else {
      userIds.add(user.getId());
      int num = orderInfoService.finish(order, userIds, belongs, signatureImg, sendMessage,printModule);
      if (num == 1) {
        return ResultData.build().success();
      }else if(num == -1){
        return ResultData.build().failure();
      }else {  //此时失败可能是查看全部订单权限变更为查看个人内容所导致的  所以这次失败要校验session
        return permissionInfoService.permissionChanged(session,
          Lists.newArrayList(PermissionConstant.PermissionType.VIEW_ALL), 1);
      }
    }

  }

  /**
   * 查询订单明细
   * 
   * @param id
   * @param type 1.2期添加  1-从消息通知相关调的  0,null-正常调用
   * @return
   */
  @RequestMapping(value = "detail", method = RequestMethod.GET)
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO},setuser=true)
  public Object orderInfoDetail(HttpSession session, Integer id, Integer type) {
    User user = getLoginUser(session);
    OrderInfoBean order = orderInfoService.orderInfoDetail(id);
    Map<String, Object> map = new HashMap<String, Object>();
    if(order==null){
      return ResultData.build().parameterError();
    }
    ResultData result = null;
    if(type==null || type==0){
      result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    } else if(type==1){
      result = permissionInfoService.orderPermissionCheckmes(session, user, order.getUserId());
    } else {
      return ResultData.build().parameterError();
    }
    if(result!=null){
      return result;
    }
    //1.1期 增加退货单信息
    order.setRefundBean(refundService.findByOid(order.getId()));
    
    map.put("aadata", order);
    //1.1期 加了退货和订单作废的权限
    Map<Integer, Boolean> permap = Maps.newHashMap();
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean printModule = moduleids.contains(ModuleType.ORDER_PRINT);
    boolean editAdjunct = false;
    if (order.getOrderStatus() == OrderStatusConstant.orderStatusCancel) {
      permap.put(PermissionType.REFUND, false);
      permap.put(PermissionType.FINISH_ORDER, false);
      permap.put(PermissionType.ORDER_CANCEL, false);
      return ResultData.build().parseMap(map).put("permissionInfo", permap).put("editAdjunct", editAdjunct).put("print",printModule);
    }
    permap = permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.PLACE_ORDER,
      PermissionType.FINISH_ORDER,PermissionType.REFUND,PermissionType.ORDER_CANCEL),user.getId(), user.getBelongs());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
    boolean cancelModule = moduleids.contains(ModuleType.ORDER_CANCEL);
    boolean refundModule = moduleids.contains(ModuleType.ORDER_REFUND);
    //如果有退货权限(意味着本来是true) 
    if(permap.get(PermissionType.REFUND) && (!refundModule //没有退货模块(要改成false)
        || (order.getMinimalFlag()==OrderType.ROUTINE?
            (!generalModule ||                  //是常规单： 没有常规开单模块  或  此订单所有的商品都退到上限了 (要改成false)
                order.getDetail().stream().filter(d -> d.getNumber()>d.getRefundNum()).count()==0)
            :!minimalModule))){                 //是极简单： 没有极简开单退货模块  (要改成false)
      permap.put(PermissionType.REFUND, false);
    }
    if(order.getOrderStatus() == OrderStatusConstant.orderStatusYes //订单完成时(要改成false)
        //是常规单： 没有常规开单模块，是极简单： 没有极简开单退货模块(要改成false)
        || (order.getMinimalFlag()==OrderType.ROUTINE?!generalModule:!minimalModule)){ 
      permap.put(PermissionType.FINISH_ORDER, false);
      permap.put(PermissionType.ORDER_CANCEL, false);
    }
    if(!cancelModule){ //没有作废模块(要改成false)
      permap.put(PermissionType.ORDER_CANCEL, false);
    }
    if(order.getOrderStatus()==OrderStatusConstant.orderStatusNO && order.getMinimalFlag() == OrderType.ROUTINE
        && permap.get(PermissionType.FINISH_ORDER) && permap.get(PermissionType.PLACE_ORDER) && generalModule){
      editAdjunct = true;
    }
    permap.remove(PermissionType.PLACE_ORDER);
    return ResultData.build().parseMap(map).put("permissionInfo",permap).put("editAdjunct",editAdjunct).put("print",printModule);
    
  }
  
  /**
   * 打印订单凭证的信息
   * @version 1.4.2
   * @param session
   * @param id
   * @return
   */
  @RequestMapping(value = "print", method = RequestMethod.GET)
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.ORDER_PRINT},setuser=true)
  public ResultData printInfo(HttpSession session, Integer id){
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(id);
    if(order==null || order.getMinimalFlag()==OrderType.MINIMA){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    OrderPrintBean print = new OrderPrintBean(order);
    //放业务员电话
    print.setSalesmanPhone(userService.findByUid(order.getUserId()).getMobile());
    //放店铺信息
    Shop shop = shopService.findById(order.getShopId());
    print.setShopName(shop.getName());//店铺名称
    print.setShopAddress(shop.getAddress());//店铺地址
//    if(SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.GENERAL_ORDER)){
      print.setAnnouncements(shop.getAnnouncements());//注意事项
//    }
    //放订单商品信息
    print.setDetail(orderInfoService.findAllDetail(order.getId()));
    //放退货单信息
    print.setRefundBean(refundService.findPrintByOid(order.getId()));
    //放退货商品信息
    for (RefundPrintBean refund : print.getRefundBean()) {
      refund.setRefundDetail(refundService.findDetailsById(refund.getId()));
    }
    return ResultData.build().put("printInfo", print);
  }

  /**
   * 搜索订单
   * 
   * @param session
   * @param currentPage
   * @param limit
   * @param orderInfo
   * @param Begin 下单开始时间
   * @param end 下单结束时间
   * @return
   */
  @RequestMapping(value = "search", method = RequestMethod.GET)
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO})
  public ResultData searchOrder(HttpSession session,
      @RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit, OrderInfo orderInfo,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date begin,
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end) {
    User user = getLoginUser(session);
    List<Integer> userIds = new ArrayList<Integer>();// 定义所有userID集合
    if (user.getBelongs() == 0) {
      List<User> userLists = userService.findUserBelongId(user.getId());
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(user.getId());
      Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
      map.put(PermissionType.FINISH_ORDER, true);
      return ResultData.build().parseMap(
              orderInfoService.searchOrderInfo(currentPage, limit, orderInfo, begin, end, userIds)
                  .toJson()).put("permissionInfo", map);
    }
    if (permissionInfoService
        .findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) != null) {
      User users = userService.findUserById(user.getId());
      List<User> userLists = userService.findUserBelongId(users.getBelongs());
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(user.getBelongs());
      return ResultData.build().parseMap(
          orderInfoService.searchOrderInfo(currentPage, limit, orderInfo, begin, end, userIds).toJson())
          .put("permissionInfo",
              permissionInfoService.findMapByUidPid(PermissionType.FINISH_ORDER, user.getId(),
                  user.getBelongs()));
    } else {
      return ResultData.build().parseMap(
              orderInfoService.searchOrderInfo(currentPage, limit, orderInfo, begin, end,
                  Arrays.asList(user.getId())).toJson())
          .put("permissionInfo",
              permissionInfoService.findMapByUidPid(PermissionType.FINISH_ORDER, user.getId(),
                  user.getBelongs()));
    }

  }

  /**
   * 作废订单
   * @param session
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "cancel", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.ORDER_CANCEL})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.ORDER_CANCEL}, setuser = true)
  public ResultData cancel(HttpSession session, Integer id, String cancelReason) {
    if (cancelReason == null || cancelReason.length()>50){ // 作废原因必填且不超过50字
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(id);
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
    if(order.getMinimalFlag()==OrderType.ROUTINE?!generalModule:!minimalModule){
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    if(order == null || order.getOrderStatus() != OrderStatusConstant.orderStatusNO){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    order.setCancelReason(cancelReason);
    return ResultData.build().setStatus(orderInfoService.cancel(order));
  }
  
  /**
   * 进再次购买页的数据
   * @param session
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "againinfo", method = RequestMethod.GET)
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.GENERAL_ORDER,ModuleType.AGAIN_BUY},setuser=true)
  @PermissionPassport(permissionids = {PermissionType.PLACE_ORDER})
  public ResultData againInfo(HttpSession session, Integer id) {
    User user = getLoginUser(session);
    List<String> moduleids =  SoftTypeConstant.softMap().get(user.getSoftId());
    OrderInfo order = orderInfoService.findById(id);
    if(order==null || order.getMinimalFlag()==OrderType.MINIMA){
      return ResultData.build().parameterError();
    }
    order.setSalesman(user.getStaffname());
    Integer userBelongs = (user.getBelongs() == 0)?user.getId():user.getBelongs();
    Integer orderBelongs = userService.findUser(order.getUserId()).getBelongs();
    orderBelongs = (orderBelongs == 0)?order.getUserId():orderBelongs;
    if (!userBelongs.equals(orderBelongs)){ //不是自己店铺下的单 
      return ResultData.build().parameterError();
    }
    
    Customer customer = null;
    if(order.getCustomerId()!=null){
      customer = customerService.findById(order.getCustomerId());
    }
    //1.4.2期客户信息从订单表取
//    order.setCustomer(customer.getName());
//    order.setPhone(customer.getPhone());
//    order.setContactWith(customer.getAddress());
    if (user.getBelongs() != 0){
      boolean view = permissionInfoService.findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) == null;
      if (!order.getUserId().equals(user.getId()) && view) {
        return permissionInfoService.permissionChanged(session,Lists.newArrayList(PermissionConstant.PermissionType.VIEW_ALL), 1);
      }
      //当用户权限从查看全部调整为查看个人，那么之前下过的单中的客户已无权限查看的，点击【再次购买】，客户信息是不能带进来的。
      if(view && customer!=null && !user.getId().equals(customer.getUid())){
        order.setCustomer(null);
        order.setCustomerId(null);
        order.setPhone(null);
        order.setContactWith(null);
      }
    }
    if(customer!=null && !customer.getStatus()){ //客户信息已被删除，不需要带入删除信息
      order.setCustomer(null);
      order.setCustomerId(null);
      order.setPhone(null);
      order.setContactWith(null);
    }
    if(!moduleids.contains(ModuleType.CUSTOMER)){
      order.setCustomerId(null);
    }
    //客户类型不勾选 则 【常规开单】里折扣率都会默认成100% 
    if(!moduleids.contains(ModuleType.CUSTOMER_GROUP)){
      order.setDiscount(1.00);
    }
    // 获取订单的商品信息   若  商品信息已被删除，不需要带入删除信息
    List<Product> details = orderInfoService.findAllDetail(id).stream()
        .map(d -> {Product p=productService.findById(d.getProductId()); p.setSalePrice(d.getPrice()); p.setCostPrice(null); return p;})
        .filter(d -> d.getStatus()==1).collect(Collectors.toList());
    boolean customerModule = SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.CUSTOMER);
    return ResultData.build().put("order", order).put("details", details).put("customer", customerModule);
  }
  
  /**
   * 编辑订单附件图片
   * @param session
   * @param id
   * @param adjunctImg
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "adjunct", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.PLACE_ORDER,PermissionType.FINISH_ORDER})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.GENERAL_ORDER})
  public ResultData saveAdjunctImg(HttpSession session, Integer id, String adjunctImg) {
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(id);
    if(order == null || order.getOrderStatus() != OrderStatusConstant.orderStatusNO
        || order.getMinimalFlag()==OrderType.MINIMA){
      return ResultData.build().parameterError();
    }
    if(StringUtils.isNotBlank(adjunctImg) && adjunctImg.split(",").length>5){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    if (adjunctImg==null) {
      adjunctImg = "";
    }
    if (orderInfoService.saveAdjunctImg(id, adjunctImg)==0) {
      return ResultData.build().failure();
    }
    return ResultData.build().success();
  }
  
  /**
   * 跳订单凭证页
   * @param session
   * @return
   */
  @RequestMapping(value = "certificate", method = RequestMethod.GET)
  public String certificate(HttpSession session) {
    return "mobile/order-certificate";
  }
  
  /**
   * 获取查看二维码的url,并保存查看凭证
   * @param session
   * @param oid
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "proof", method = RequestMethod.POST)
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO})
  public ResultData saveProof(HttpSession session, Integer oid) {
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(oid);
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    String proof = order.getProof();
    if (StringUtils.isBlank(proof)) {
      proof = orderInfoService.saveProof(oid);
    }
    if (proof==null) {
      return ResultData.build().failure();
    }
    return ResultData.build().put("url", String.format(OrderQRCodeUrl.url, domainUrl, oid, proof));
    
  }
  
  /**
   * 定制订单开单
   * @param session
   * @param orderInfo
   * @param bespoke
   * @return
   */
  @RequestMapping(value = "add/bespoke", method = RequestMethod.POST)
  @ResponseBody
  @PermissionPassport(permissionids = {PermissionType.BESPOKE_PLACE_ORDER})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.BESPOKE_ORDER})
  public ResultData addBespoke(HttpSession session, OrderInfo orderInfo, OrderBespokeDetail bespoke) {
  	bespoke.setId(null);
    if(orderInfo.getReceiceAmoun() == null || bespoke.getCustomProductId()==null
        || StringUtils.isBlank(orderInfo.getCustomer()) || StringUtils.isBlank(orderInfo.getPhone()) 
        || StringUtils.isBlank(bespoke.getName()) || StringUtils.isBlank(bespoke.getImage()) 
        || StringUtils.isBlank(bespoke.getMeasureInfo())){
      return ResultData.build().parameterError();
    }
    if (orderInfo.getDescription() != null && orderInfo.getDescription().length() > 150) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getAmountAdvanced() != null && orderInfo.getAmountAdvanced() > orderInfo.getReceiceAmoun()) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getExpectedDeliveryDate() != null && orderInfo.getExpectedDeliveryDate().before(TimeUtils.formatDayBegin(new Date()))){ // 预计发货日期只能选今天或今天以后的日期
    	return ResultData.build().setStatus(Status.DELIVERY_DATE_EARLY);
    }
    if(StringUtils.isNotBlank(orderInfo.getAdjunctImg()) && orderInfo.getAdjunctImg().split(",").length>10){
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
  //根据新需求 定制订单不关联客户id 客户信息传什么就存什么
//    Customer customer = new Customer();
//    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
//    if(orderInfo.getCustomerId()==null){
//      customer.setName(orderInfo.getCustomer());
//      customer.setPhone(orderInfo.getPhone());
//      customer.setAddress(orderInfo.getContactWith());
//      if(!customerService.check(customer)){
//        return ResultData.build().parameterError();
//      }
//    }else if(!moduleids.contains(ModuleType.CUSTOMER)){
//      return ResultData.build().setStatus(Status.MODULE_CHANGED);
//    }
    
    orderInfo.setOrderStatus(OrderStatusConstant.orderStatusNO);
    if(orderInfo.getAmountAdvanced()==null){
    	orderInfo.setAmountAdvanced(0.00);
    }
    int result = orderInfoService.addOrderBespoke(orderInfo, bespoke, user);//, customer);
    if(result == 10601){
      return ResultData.build().setStatus(Status.TRIAL_ACCOUNT_CUSTOMER_NUM_BEYOND);
    }
    if(result==25000){
      return ResultData.build().setStatus(Status.CUSTOM_NOT_FIND);
    }
    return ResultData.build().success().put("oId", orderInfo.getId());
  }
  
  /**
   * 定制订单预下单
   * @param session
   * @param orderInfo
   * @param bespoke
   * @return
   */
  @RequestMapping(value = "pre/bespoke", method = RequestMethod.POST)
  @ResponseBody
  @PermissionPassport(permissionids = {PermissionType.BESPOKE_PLACE_ORDER})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.BESPOKE_ORDER})
  public ResultData preBespoke(HttpSession session, OrderInfo orderInfo, OrderBespokeDetail bespoke) {
  	bespoke.setId(null);
    if(bespoke.getCustomProductId()==null || StringUtils.isBlank(bespoke.getName())
    		|| StringUtils.isBlank(bespoke.getImage()) ){
      return ResultData.build().parameterError();
    }
    if (orderInfo.getDescription() != null && orderInfo.getDescription().length() > 150) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getAmountAdvanced() != null && orderInfo.getReceiceAmoun() != null
    		&& orderInfo.getAmountAdvanced() > orderInfo.getReceiceAmoun()) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getDiscount() != null && (orderInfo.getDiscount()>1||orderInfo.getDiscount()<=0) ) {
      return ResultData.build().parameterError();
    }
    if (orderInfo.getExpectedDeliveryDate() != null && orderInfo.getExpectedDeliveryDate().before(TimeUtils.formatDayBegin(new Date()))){ // 预计发货日期只能选今天或今天以后的日期
      return ResultData.build().setStatus(Status.DELIVERY_DATE_EARLY);
    }
    if(StringUtils.isNotBlank(orderInfo.getAdjunctImg()) && orderInfo.getAdjunctImg().split(",").length>10){
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
  //根据新需求 定制订单不关联客户id 客户信息传什么就存什么
//    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
//    if(orderInfo.getCustomerId()!=null && !moduleids.contains(ModuleType.CUSTOMER)){
//      return ResultData.build().setStatus(Status.MODULE_CHANGED);
//    }
    
    orderInfo.setOrderStatus(OrderStatusConstant.orderStatusPre);
//    if(bespoke.getMeasureInfo()==null){
//    	bespoke.setMeasureInfo("");
//    }
    orderInfoService.addOrderBespoke(orderInfo, bespoke, user);//, null);
    return ResultData.build().success().put("oId", orderInfo.getId());
  }
  
  
  @RequestMapping(value = "bespoke/detail", method = RequestMethod.GET)
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO},setuser=true)
  public Object orderBespokeDetail(HttpSession session, Integer id) {
    User user = getLoginUser(session);
    OrderInfoBean order = orderInfoService.orderInfoDetail(id);
    if(order==null){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    
    Map<Integer, Boolean> permap = Maps.newHashMap();
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean printModule = moduleids.contains(ModuleType.BESPOKE_ORDER_PRINT);
    boolean bespokeModule = moduleids.contains(ModuleType.BESPOKE_ORDER);
    permap.put(PermissionType.BESPOKE_PLACE_ORDER, false);
    permap.put(PermissionType.BESPOKE_FINISH_ORDER, false);
    permap.put(PermissionType.BESPOKE_ORDER_CANCEL, false);
    if(bespokeModule){ //经协商取消定制模块时,以前下的定制单仍能查看,但不能做任何操作,故有此判断
	    if (order.getOrderStatus() == OrderStatusConstant.orderStatusNO) {
	    	permap = permissionInfoService.findMapByUidPid(PermissionType.BESPOKE_FINISH_ORDER, user.getId(), user.getBelongs());
	    	permap.put(PermissionType.BESPOKE_PLACE_ORDER, false);
	      permap.put(PermissionType.BESPOKE_ORDER_CANCEL, false);
	    }
	    if (order.getOrderStatus() == OrderStatusConstant.orderStatusPre) {
	    	permap = permissionInfoService.findMapByUidPids(Arrays.asList(PermissionType.BESPOKE_PLACE_ORDER,
	          PermissionType.BESPOKE_ORDER_CANCEL), user.getId(), user.getBelongs());
	    	permap.put(PermissionType.BESPOKE_FINISH_ORDER, false);
	    }
    }
    return ResultData.build().put("aadata", order).put("permissionInfo", permap).put("print",printModule);
  }
  
  /**
   * 作废定制订单
   * @param session
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "bespoke/cancel", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.BESPOKE_ORDER_CANCEL})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.BESPOKE_ORDER})
  public ResultData bespokeCancel(HttpSession session, Integer id, String cancelReason) {
    if (cancelReason == null || cancelReason.length()>50){ // 作废原因必填且不超过50字
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(id);
    if(order == null || order.getMinimalFlag()!=OrderType.BESPOKE 
    		|| order.getOrderStatus() != OrderStatusConstant.orderStatusPre){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    order.setCancelReason(cancelReason);
    return ResultData.build().setStatus(orderInfoService.cancel(order));
  }
  
  /**
   * 定制订单确认完成
   * @param session
   * @param id
   * @param signatureImg
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "bespoke/finish", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.BESPOKE_FINISH_ORDER})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.BESPOKE_ORDER},setuser=true)
  public ResultData bespokeFinish(HttpSession session, Integer id, String signatureImg) {
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(id);
    if(order == null || order.getMinimalFlag()!=OrderType.BESPOKE 
    		|| order.getOrderStatus() != OrderStatusConstant.orderStatusNO){
    	return ResultData.build().parameterError();
    }
    if (order.getSignatureFlag()==1) {
    	if (StringUtils.isBlank(signatureImg)){
    		return ResultData.build().parameterError();
    	}
    	order.setSignatureImg(signatureImg);
    }
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    return ResultData.build().setStatus(orderInfoService.bespokeFinish(order,user));
  }
  
  @ResponseBody
  @RequestMapping(value = "/micromall/detail", method = RequestMethod.GET)
  @PermissionPassport(permissionids = {PermissionType.VIEW_ALL})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO},setuser=true)
  public ResultData orderMicromallDetail(HttpSession session, Integer id) {
    User user = getLoginUser(session);
    OrderInfoBean order = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.ORDER_DETAIL.url, user).excute(OrderInfoBean.class, id);
//    OrderInfoBean order = new OrderInfoBean();
    order.setId(id);
//    order.setNumber("SH20170517171100123456");
//    order.setCustomer("lhr");
//    order.setPhone("13345678980");
//    order.setContactWith("天津市xx区xx街xxxxx");
//    order.setCreatetime(new Date(1494999951279l));
//    order.setReceiceAmoun(99.99);
//    order.setDescription("这里是备注");
//    order.setPayNumber("6254567864154123");
//    order.setPayType("微信支付");
//    OrderInfoDetailBean detailA = new OrderInfoDetailBean();
//    detailA.setName("666号商品");
//    detailA.setNumber(6);
//    detailA.setPrice(6.66);
//    detailA.setProductId(60);
//    detailA.setImg("https://oss-testcdn.qianfan365.com/jcstore/talkart/1479378575083_40414991.jpg");
//    OrderInfoDetailBean detailB = new OrderInfoDetailBean();
//    detailB.setName("另一个商品");
//    detailB.setNumber(3);
//    detailB.setPrice(19.68);
//    detailB.setProductId(65);
//    detailB.setImg("https://oss-testcdn.qianfan365.com/jcstore/talkart/1474163184934_41779952.jpg");
//    List<OrderInfoDetailBean> list = new ArrayList<OrderInfoDetailBean>();
//    list.add(detailA);
//    list.add(detailB);
//    order.setDetail(list);
    order.setOrderNumber(order.getNumber().substring(12));
    return ResultData.build().put("detail", order);
  }
  
  @ResponseBody
  @RequestMapping(value = "/micromall/list", method = RequestMethod.GET)
  @PermissionPassport(permissionids = {PermissionType.VIEW_ALL})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO},setuser=true)
  public ResultData orderMicromallList(HttpSession session,
       @RequestParam(defaultValue = "1") Integer currentPage,
       @RequestParam(defaultValue = "20") Integer limit,
       @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
       @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, 
       String customer, String phone, Long querytime) {
    User user = getLoginUser(session);
    WeiYuRequst wyr = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.ORDER_LIST.url, user);
    Page<ShopOrderListBean> page = wyr.excutePage(ShopOrderListBean.class, currentPage, limit, TimeUtils.formatAsString(begin, "yyyy-MM-dd"), TimeUtils.formatAsString(end, "yyyy-MM-dd"), customer, phone, querytime);
//    Page<ShopOrderListBean> page = new Page<ShopOrderListBean>();
//    ShopOrderListBean solb = new ShopOrderListBean();
//    solb.setId(666);
//    solb.setType(0);
//    solb.setCreatetime(new Date(1494999951279l));
//    solb.setUpdatetime(new Date(1495099000079l));
//    solb.setImg("https://oss-testcdn.qianfan365.com/jcstore/talkart/1479378575083_40414991.jpg");
//    solb.setOrderNumber("SH20170517171100123456");
//    solb.setCustomer("客户A");
//    solb.setProductKind(1);
//    solb.setProductName("麦德林咖啡豆250g");
//    solb.setReceiceAmoun(66.66);
//    ShopOrderListBean solb0 = new ShopOrderListBean();
//    solb0.setId(777);
//    solb0.setType(1);
//    solb0.setCreatetime(new Date(1494988881279l));
//    solb0.setUpdatetime(new Date(1495009000079l));
//    solb0.setImg("https://oss-testcdn.qianfan365.com/jcstore/talkart/1479378575083_40414991.jpg,https://oss-testcdn.qianfan365.com/jcstore/talkart/1482312430093_5726147.jpg");
//    solb0.setOrderNumber("SH20170517171700123456");
//    solb0.setCustomer("客户B");
//    solb0.setProductKind(2);
//    solb0.setReceiceAmoun(77.77);
//    page.add(solb);
//    page.add(solb0);
    
    String time = "";
    List<ShopOrderListBean> listBeans = new ArrayList<ShopOrderListBean>();
    Page<OrderDateBean> datePage = new Page<OrderDateBean>();
    for (ShopOrderListBean bean : page) {
      bean.setOrderNumber(bean.getOrderNumber().substring(12));
      bean.setTime(TimeUtils.formatAsString(bean.getUpdatetime(), "yyyy-MM-dd"));
      if(time.equals(bean.getTime())){
        listBeans.add(bean);
      }else{
        time = bean.getTime();
        OrderDateBean dateBean = new OrderDateBean();
        dateBean.setTime(time);
        listBeans = new ArrayList<ShopOrderListBean>();
        listBeans.add(bean);
        dateBean.setList(listBeans);
        datePage.add(dateBean);
      }
    }
    datePage.setPageSize(page.getPageSize());
    datePage.setTotal(page.getTotal());
    datePage.setPageNum(currentPage);
    return ResultData.build().parsePageBean(datePage).put("querytime", wyr.resultData.get("querytime"));
  }

}
