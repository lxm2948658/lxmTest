package com.qianfan365.jcstore.controller.mobile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderStatusConstant;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderType;
import com.qianfan365.jcstore.common.constant.PermissionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.RefundConstant.RefundStatus;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.OrderInfoDetail;
import com.qianfan365.jcstore.common.pojo.Refund;
import com.qianfan365.jcstore.common.pojo.RefundDetail;
import com.qianfan365.jcstore.common.pojo.RefundExample;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.PageForm;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.OrderInfoService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.RefundService;
import com.qianfan365.jcstore.service.UserService;

/**
 * 退货相关
 * @author liuhaoran
 *
 */
@Controller
@RequestMapping("/mobile/refund")
public class RefundMobileController extends BaseController {
  
  @Autowired
  private RefundService refundService;
  @Autowired
  private OrderInfoService orderService;
  @Autowired
  private UserService userService;
  @Autowired
  private PermissionInfoService permissionService;
  
  /**
   * 增加退货单
   * @param session
   * @param orderId
   * @param refundFee
   * @param description
   * @param expectedDate
   * @param param
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "add", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.REFUND})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.GENERAL_ORDER,ModuleType.ORDER_REFUND},setuser=true)
  public ResultData add(HttpSession session, Integer orderId, Double refundFee, String description,@DateTimeFormat(pattern="yyyy-MM-dd") Date expectedDate, String param) {
    if (description != null && description.length()>150){ // 备注不超过150字
      return ResultData.build().parameterError();
    }
    if (expectedDate != null && expectedDate.before(TimeUtils.formatDayBegin(new Date()))){ // 预计退货日期只能选今天或今天以后的日期
      return ResultData.build().parameterError();
    }
    if(orderId==null || refundFee==null || param==null){
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
    OrderInfo order = orderService.findById(orderId);
    if (order==null){ 
      return ResultData.build().parameterError();
    }
    ResultData result = permissionService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    //退款商品详情
    JSONObject paramJson = JSON.parseObject(param);
    String detailstr = paramJson.getString("detail");
    List<RefundDetail> details = JSON.parseArray(detailstr, RefundDetail.class);
    List<Integer> pids = new ArrayList<Integer>();
    List<Integer> odids = new ArrayList<Integer>();
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    double total = details.stream().mapToDouble(rd-> {odids.add(rd.getId()); pids.add(rd.getProductId()); map.put(rd.getProductId(), -rd.getNumber()); return rd.getPrice() * rd.getNumber();}).sum();
    //校验商品是否存在  & 商品退货数量是否大于可退数量
    List<OrderInfoDetail> odlist = orderService.findOdByOPid(orderId, pids, odids);
    odlist.forEach(od -> map.put(od.getProductId(), od.getNumber()-od.getRefundNum()+map.get(od.getProductId())));
    long count = map.values().stream().filter(v -> v<0).count(); //统计商品数量退成负的个数
    if(count>0){
      return ResultData.build().setStatus(Status.FAILURE_REFUNDABLE_CHANGE);
    }
    //退款金额大于可退金额  //为了优先提示FAILURE_REFUNDABLE_CHANGE 此校验挪到了下面
    BigDecimal b = new BigDecimal(order.getReceiceAmoun()-order.getRefundedFee()-order.getRefundingFee()); 
    if(refundFee > b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()){
      return ResultData.build().parameterError();
    }
    Map<Integer, OrderInfoDetail> odmap = odlist.stream().collect(Collectors.toMap(OrderInfoDetail::getId, Function.identity()));
    details.forEach(rd->rd.setImg(odmap.get(rd.getId()).getImg()));
    int num = refundService.add(order, user, refundFee, total, description, expectedDate, details);
    return ResultData.build().setStatus(num>0?1:0).put("rid", num);
//    List<Integer> pids = new ArrayList<Integer>();
//    double total = details.stream().mapToDouble(rd-> {pids.add(rd.getProductId()); return rd.getPrice() * rd.getNumber();}).sum();
//    //校验商品是否存在  & 商品退货数量是否大于可退数量
//    List<Integer> rids = refundService.findRidByOid(orderId);
//    List<OrderInfoDetail> odlist = orderService.findOdByOPid(orderId, pids);
//    List<RefundDetail> rdlist = refundService.findRdByOPRid(orderId, pids, rids);
//    Map<Integer, Integer> map = details.stream().collect(Collectors.toMap(RefundDetail::getProductId, d -> -d.getNumber()));
//    odlist.forEach(od -> map.put(od.getProductId(), map.get(od.getProductId()+od.getNumber())));
//    rdlist.forEach(rd -> map.put(rd.getProductId(), map.get(rd.getProductId()-rd.getNumber())));
//    long count = map.values().stream().filter(v -> v<0).count(); //统计商品数量退成负的个数
//    if(count>0){
//      return ResultData.build().parameterError();
//    }
  }
  
  /**
   * 极简订单增加退款单
   * @param session
   * @param orderId
   * @param refundFee
   * @param description
   * @param expectedDate
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "add/minimal", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.REFUND})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.MINIMAL_ORDER,ModuleType.ORDER_REFUND},setuser=true)
  public ResultData addMinimal(HttpSession session, Integer orderId, Double refundFee, String description,@DateTimeFormat(pattern="yyyy-MM-dd") Date expectedDate) {
    if (description != null && description.length()>150){ // 备注不超过150字
      return ResultData.build().parameterError();
    }
    if (expectedDate != null && expectedDate.before(TimeUtils.formatDayBegin(new Date()))){ // 预计退货日期只能选今天或今天以后的日期
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
    OrderInfo order = orderService.findById(orderId);
    if (order==null){ 
      return ResultData.build().parameterError();
    }
    ResultData result = permissionService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    //退款金额大于可退金额
    BigDecimal b = new BigDecimal(order.getReceiceAmoun()-order.getRefundedFee()-order.getRefundingFee()); 
    if(refundFee > b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()){
      return ResultData.build().parameterError();
    }
    int num = refundService.add(order, user, refundFee, null, description, expectedDate, null);
    return ResultData.build().setStatus(num>0?1:0).put("rid", num);
  }
  
  /**
   * 完成退货单
   * @param session
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "finish", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.FINISH_ORDER})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.ORDER_REFUND})
  public ResultData finish(HttpSession session, Integer id) {
    User user = getLoginUser(session);
    Refund refund = refundService.findById(id);
    if(refund == null || refund.getStatus() != RefundStatus.PENDING){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionService.orderPermissionCheck(session, user, refund.getUserId());
    if(result!=null){
      return result;
    }
    if(orderService.findById(refund.getOrderInfoId()).getOrderStatus() != OrderStatusConstant.orderStatusYes){
      return ResultData.build().parameterError();
    }
    return ResultData.build().setStatus(refundService.finish(refund));
  }
  
  /**
   * 作废退货单
   * @param session
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "cancel", method = RequestMethod.POST)
  @PermissionPassport(permissionids = {PermissionType.ORDER_CANCEL})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.ORDER_CANCEL,ModuleType.ORDER_REFUND})
  public ResultData cancel(HttpSession session, Integer id) {
    User user = getLoginUser(session);
    Refund refund = refundService.findById(id);
    if(refund == null || refund.getStatus() != RefundStatus.PENDING){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionService.orderPermissionCheck(session, user, refund.getUserId());
    if(result!=null){
      return result;
    }
    return ResultData.build().setStatus(refundService.cancel(refund));
  }
  
  /**
   * 新增退货页的数据
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "toaddinfo", method = RequestMethod.GET)
  @PermissionPassport(permissionids = {PermissionType.REFUND})
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.ORDER_REFUND},setuser=true)
  public ResultData toAddInfo(HttpSession session, Integer id) {
    User user = getLoginUser(session);
    OrderInfo order = orderService.findById(id);
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
    if(order.getMinimalFlag()==OrderType.ROUTINE?!generalModule:!minimalModule){
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    if(order==null || order.getOrderStatus()==OrderStatusConstant.orderStatusCancel){
      return ResultData.build().setStatus(Status.FAILURE_ORDER_IS_CANCEL);
    }
    ResultData result = permissionService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    List<OrderInfoDetail> details = new ArrayList<OrderInfoDetail>();
    if(order.getMinimalFlag()==OrderType.ROUTINE){
      orderService.findAllDetail(id).forEach(d -> {
        int num = d.getNumber()-d.getRefundNum();
        if(num > 0){
          d.setRefundNum(num); // 商品可退数量
          details.add(d);
        }
      });
      if(details.size() == 0){
        // 所有的商品都退到上限了 
        return ResultData.build().setStatus(Status.FAILURE_ORDER_ALL_REFUND);
      }
    }
    // 可退金额
    BigDecimal b = new BigDecimal(order.getReceiceAmoun()-order.getRefundedFee()-order.getRefundingFee()); 
    order.setRefundedFee(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
    order.setTotal(0.00);
    return ResultData.build().put("order", order).put("details", details);
  }
  
  /**
   * 退货列表
   * @param session
   * @param currentPage
   * @param limit
   * @param status
   * @param querytime
   * @return
   */
  @ResponseBody
  @RequestMapping(value = {"/list"}, method = RequestMethod.GET)
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO,ModuleType.ORDER_REFUND},setuser=true)
  public ResultData findAllOrderInfo(HttpSession session,
      @RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
      @DateTimeFormat(pattern = "yyyy-MM-dd") Date end, 
      Integer customerId, String customer, String phone, Integer status, Long querytime) {
    //有状态 还不是待完成和已完成的
    if(status!=null && status!=RefundStatus.PENDING && status!=RefundStatus.FINISH){
      return ResultData.build().parameterError();
    }
    if (begin != null && end != null && begin.after(end)) {//开始时间在结束时间之后
      return ResultData.build().parameterError();
    }
    if (1 == currentPage || null == querytime || 0 == querytime) {
      querytime = new Date().getTime();
    }
    User user = getLoginUser(session);
    boolean generalModule = SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.GENERAL_ORDER);
    if(!generalModule && (customer!=null || phone!=null)){
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    
    List<Integer> userIds = new ArrayList<Integer>();// 定义所有userID集合
    // 查询是否有查看所有订单的权限
    if (user.getBelongs() == 0 ||
        permissionService.findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) != null) {
      int belongs = (user.getBelongs() == 0)?user.getId():user.getBelongs();
      List<User> userLists = userService.findUserBelongId(belongs);
      for (User userList : userLists) {
        userIds.add(userList.getId());
      }
      userIds.add(belongs);
    }else{
      userIds.add(user.getId());
    }
    RefundExample example = refundService
        .getRefundExample(customerId, customer, phone, begin, end, userIds, status, querytime);
    PageForm page = refundService.findList(currentPage, limit, example, user);
    return ResultData.build().parseMap(page.toJson()).put("querytime", querytime).put("permissionInfo",
      permissionService.findMapByUidPid(PermissionType.REFUND,user.getId(), user.getBelongs())).put("general", generalModule);
  }
  
  /**
   * 退货详情
   * @param session
   * @param id
   * @param type 1.2期添加  1-从消息通知相关调的  0,null-正常调用
   * @return
   */
  @ResponseBody
  @RequestMapping(value = {"/detail"}, method = RequestMethod.GET)
  @ModulePassport(moduleids = {ModuleType.ORDER_INFO},setuser=true)//有订单权限就能进,但只能看
  public ResultData refundDetail(HttpSession session, Integer id, Integer type){
    User user = getLoginUser(session);
    Refund refund = refundService.findById(id);
    if(refund==null){
      return ResultData.build().parameterError();
    }
//    ResultData result = permissionService.orderPermissionCheck(session, user, refund.getUserId());
    ResultData result = null;
    if(type==null || type==0){
      result = permissionService.orderPermissionCheck(session, user, refund.getUserId());
    } else if(type==1){
      result = permissionService.orderPermissionCheckmes(session, user, refund.getUserId());
    } else {
      return ResultData.build().parameterError();
    }
    if(result!=null){
      return result;
    }
    String num = refund.getRefundNum();
    refund.setRefundNum("TH"+num.substring(num.length()-8, num.length()));
    refund.setNumber(refund.getNumber().substring(10));
    refund.setCreatetime(TimeUtils.format(refund.getCreatetime(), "yyyy-MM-dd HH:mm"));
    List<RefundDetail> details = refundService.findDetailsById(refund.getId());
    Map<Integer, Boolean> permission = Maps.newHashMap();
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
    boolean cancelModule = moduleids.contains(ModuleType.ORDER_CANCEL);
    boolean refundModule = moduleids.contains(ModuleType.ORDER_REFUND);
    if(refund.getStatus() == RefundStatus.PENDING && refundModule && (refund.getMinimalFlag()==OrderType.ROUTINE?generalModule:minimalModule)){
      permission = permissionService.findMapByUidPids(
        Arrays.asList(PermissionType.FINISH_ORDER,PermissionType.ORDER_CANCEL),user.getId(), user.getBelongs());
    }else{
      permission.put(PermissionType.FINISH_ORDER, false);
      permission.put(PermissionType.ORDER_CANCEL, false);
    }
    if(permission.get(PermissionType.FINISH_ORDER) && orderService.findById(refund.getOrderInfoId()).getOrderStatus()!=OrderStatusConstant.orderStatusYes){
      permission.put(PermissionType.FINISH_ORDER, false);
    }
    if(permission.get(PermissionType.ORDER_CANCEL) && !cancelModule){ //没有作废模块(要改成false)
      permission.put(PermissionType.ORDER_CANCEL, false);
    }
    return ResultData.build().put("refund", refund).put("details", details).put("permission", permission);
  }
}
