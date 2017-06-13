package com.qianfan365.jcstore.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qianfan365.jcstore.common.bean.OrderDateBean;
import com.qianfan365.jcstore.common.bean.OrderInfoBean;
import com.qianfan365.jcstore.common.bean.OrderListBean;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageInform;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.MessageConstant.PushInform;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderStatusConstant;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.Customer;
import com.qianfan365.jcstore.common.pojo.OrderBespokeDetail;
import com.qianfan365.jcstore.common.pojo.OrderBespokeDetailExample;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.OrderInfoDetail;
import com.qianfan365.jcstore.common.pojo.OrderInfoDetailExample;
import com.qianfan365.jcstore.common.pojo.OrderInfoExample;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.OrderInfoExample.Criteria;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.ShopOrder;
import com.qianfan365.jcstore.common.pojo.ShopOrderExample;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.common.util.PageForm;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.dao.inter.OrderBespokeDetailMapper;
import com.qianfan365.jcstore.dao.inter.OrderInfoDetailMapper;
import com.qianfan365.jcstore.dao.inter.OrderInfoMapper;
import com.qianfan365.jcstore.dao.inter.ShopOrderMapper;


@Service("orderInfoService")
public class OrderInfoService {

  @Autowired
  private OrderInfoMapper orderInfoMapper;

  @Autowired
  private OrderInfoDetailMapper orderInfoDetailMapper;
  
  @Autowired
  private OrderBespokeDetailMapper orderBespokeDetailMapper;
  
  @Autowired
  private ShopOrderMapper shopOrderMapper;

  @Autowired
  private MessageService messageService;
  
  @Autowired
  private CustomerService customerService;
  
  @Autowired
  private PermissionInfoService permissionInfoService;
  
  @Autowired
  private ProductService productService;
  
  @Autowired
  private RefundService refundService;
  
  @Autowired
  private ShopService shopService;
  
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

  /**
   * 增加订单
   * @param orderInfo
   * @param param
   * @param user
   * @param productIds
   * @param customer
   * @param printModule TODO
   * @param shop
   * @param belongs
   * @return
   */
  public int addOrderInfo(OrderInfo orderInfo, String param, User user, Shop shop, List<Integer> productIds,Customer customer) {
    Integer uid = user.getId();
    Integer belongs = user.getBelongs();
    Date date = new Date();
    orderInfo.setUpdatetime(date);
    orderInfo.setCreatetime(date);
    orderInfo.setOrderStatus(OrderStatusConstant.orderStatusNO);
    orderInfo.setShopId(shop.getId());
    orderInfo.setUserId(uid);
    Customer custom = null;
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    if(orderInfo.getCustomerId()!=null){
      custom = customerService.findById(orderInfo.getCustomerId());
    }else{
      if(user.getIsTrialAccount() && customerService.checkTrialProductNum(user.getId())){
        return 10601; 
      }
      if(moduleids.contains(ModuleType.CUSTOMER)){
        custom = customerService.insert(customer, user);
        orderInfo.setCustomerId(customer.getId());
      }
    }
    
    if(custom!=null&&!custom.getStatus()){
      return 25000;
    }
    try {
      orderInfo.setOrderDate(sdf.parse(sdf.format(date)));
    } catch (ParseException e) {
      e.printStackTrace();
      return 0;
    }
    orderInfoMapper.insertSelective(orderInfo);
    OrderInfoExample orderInfoExample = new OrderInfoExample();
    orderInfoExample.createCriteria().andNumberEqualTo(orderInfo.getNumber());// 根据订单号查询出来ID
    List<OrderInfo> orderInfoList = orderInfoMapper.selectByExample(orderInfoExample);
    
    List<Product> products = productService.findByPidProduct(productIds);
    if (products.isEmpty() || products.size()!=productIds.size()) {
      throw new IllegalArgumentException();
    }
    
    Map<Integer, Integer> map = addOrderInfoDetail(orderInfoList.get(0).getId(), param, products);
    List<Integer> prewarning = products.stream().filter(p->(p.getInventory()-map.get(p.getId()))<shop.getInventoryWarning())
        .map(p->p.getId()).collect(Collectors.toList());
    
    //更新客户信息
    this.customerService.updateLatelyAccount(orderInfo.getReceiceAmoun(),orderInfo.getCustomerId());
    
    boolean printModule = moduleids.contains(ModuleType.ORDER_PRINT);
    if(!user.getIsTrialAccount() && moduleids.contains(ModuleType.MESSAGE)){
      belongs = (belongs==0)?uid:belongs;
      List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongs, PermissionType.PLACE_ORDER_REMIND);
      messageService.insertBatch(String.format(MessageInform.PLACE_AN_ORDER, orderInfo.getNumber().substring(10)),PushInform.PLACE_AN_ORDER,
        uplist, PermissionType.PLACE_ORDER, orderInfoList.get(0).getId(), MessageType.PLACE_AN_ORDER, uid, printModule);
      if(!prewarning.isEmpty() && SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT)){
        List<UserPermission> uplistInve = permissionInfoService.findMessageNeed(belongs, PermissionType.INVENTORY_WARNING_REMIND);
        for (Integer pid : prewarning) {
          messageService.insertBatch(MessageInform.INVENTORY_WARNING,PushInform.INVENTORY_WARNING, uplistInve,
            PermissionType.INVENTORY_WARNING_REMIND, pid, MessageType.INVENTORY_WARNING, 0, printModule);//uid);
        }
      }
    }
    return 1;
  
  }

  /**
   * 增加订单明细
   * 
   * @param oId
   * @param param
   * @return
   * @throws Exception 
   */
  public Map<Integer, Integer> addOrderInfoDetail(Integer oId, String param, List<Product> products) {
    Map<Integer, String> pmap = products.stream().collect(Collectors.toMap(Product::getId, Product::getImage));
    JSONObject json = JSON.parseObject(param);
    JSONArray detailArray = json.getJSONArray("detail");
    Date date = new Date();
    OrderInfoDetail orderInfoDetail = new OrderInfoDetail();
    orderInfoDetail.setOrderInfoId(oId);
    orderInfoDetail.setUpdatetime(date);
    orderInfoDetail.setCreatetime(date);
//    List<Integer> detailIds = new ArrayList<Integer>();
//    for (int i = 0; i < detailArray.size(); i++) {
//      orderInfoDetail.setProductId(productId);
//    }
    Map<Integer, Integer> map = Maps.newHashMap();
    for (int i = 0; i < detailArray.size(); i++) {
      json = detailArray.getJSONObject(i);
      int productId = json.getIntValue("productId");
      orderInfoDetail.setProductId(productId);
      String imgs = pmap.get(productId);
      if(StringUtils.isNotBlank(imgs)){
        orderInfoDetail.setImg(imgs.split(",")[0]);
      }
//      detailIds.add(productId);
      String name = json.getString("name");
      orderInfoDetail.setName(name);
      Double price = json.getDoubleValue("price");
      orderInfoDetail.setPrice(price);
      int number = json.getIntValue("number");
      orderInfoDetail.setNumber(number);
      map.put(productId, number);
      orderInfoDetailMapper.insertSelective(orderInfoDetail);
      //更新库存
      if(productService.updateInventory(productId, number)==0){
        throw new UpdateInventoryException();
      }
    }
    
    return map;
  }
  
  public class UpdateInventoryException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public UpdateInventoryException() {
      super("更新库存失败");
    }

  }
  
  /**
   * 极简开单
   * @param orderInfo
   * @param user
   * @return
   */
  public int addOrderMinimal(OrderInfo orderInfo, User user) {
    Integer uid = user.getId();
    Shop shop = shopService.findShop(user);
    Date date = new Date();
    orderInfo.setMinimalFlag(OrderType.MINIMA);
    orderInfo.setSalesman(user.getStaffname());
    orderInfo.setNumber(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999)+100000));// 设置订单号精确到秒+随机6位数字
    orderInfo.setOrderStatus(OrderStatusConstant.orderStatusNO);
    orderInfo.setShopId(shop.getId());
    orderInfo.setUserId(uid);
    orderInfo.setUpdatetime(date);
    orderInfo.setCreatetime(date);
    try {
      orderInfo.setOrderDate(sdf.parse(sdf.format(date)));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    int num = orderInfoMapper.insertSelective(orderInfo);
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean printModule = moduleids.contains(ModuleType.ORDER_PRINT);
    if(!user.getIsTrialAccount() && moduleids.contains(ModuleType.MESSAGE)){
      Integer belongs = user.getBelongs();
      belongs = (belongs==0)?uid:belongs;
      List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongs, PermissionType.PLACE_ORDER_REMIND);
      messageService.insertBatch(String.format(MessageInform.PLACE_AN_ORDER, orderInfo.getNumber().substring(10)),PushInform.PLACE_AN_ORDER,
        uplist, PermissionType.PLACE_ORDER, orderInfo.getId(), -MessageType.PLACE_AN_ORDER, uid, printModule);
    }
    return num;
  }
  
  /**
   * 查询所有订单,这里要考虑是不是管理员，如果是管理员就要查询管理员下面的所有用户，之后在根据所有用户查询订单
   * 
   * @version 1.0
   * @param uid
   * @return
   */
  @Deprecated
  public PageForm findAllOrder(Integer currentPage, Integer limit, List<Integer> uid, Integer status) {
    OrderInfoExample orderInfoExample = new OrderInfoExample();
    if (status != null) {
      orderInfoExample.createCriteria().andUserIdIn(uid).andOrderStatusEqualTo(status);
    } else { //1.0期 无法显示已作废的订单
      orderInfoExample.createCriteria().andUserIdIn(uid).andOrderStatusNotEqualTo(OrderStatusConstant.orderStatusCancel);
    }
    orderInfoExample.setOrderByClause(" order_date desc,id desc");
    List<OrderInfoBean> infoBeans = new ArrayList<OrderInfoBean>();
    PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
    Page<OrderInfo> orderinInfoLists =
        (Page<OrderInfo>) orderInfoMapper.selectByExample(orderInfoExample);
    PageForm pageForm = PageForm.getPageForm(orderinInfoLists);
    if (!orderinInfoLists.isEmpty()) {
      for (OrderInfo orderinInfoList : orderinInfoLists) {
        OrderInfoBean orderInfoBean = new OrderInfoBean(orderinInfoList);
        orderInfoBean.setOrderNumber(orderinInfoList.getNumber().substring(10));
        orderInfoBean.setDetail(findAllDetail(orderinInfoList.getId())); // 查询出订单下的所有明细
        infoBeans.add(orderInfoBean);
      }
    }
    pageForm.setData(infoBeans);
    return pageForm;
  }

  /**
   * 订单列表  ||  订单搜索 的封装
   * @version 1.1
   * @param page
   * @return
   */
  public PageForm findAllOrder(Page<OrderInfo> page,User user,Map<Integer, Boolean> map) {
    List<OrderDateBean> dateBeans = new ArrayList<OrderDateBean>();
    PageForm pageForm = PageForm.getPageForm(page);
    String time = "";
    boolean generalModule=false,minimalModule=false,againModule=false,refundModule=false;
    if(user!=null){ //user为null的是关联退货单的  不需要按钮
      List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
      generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
      minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
      againModule = moduleids.contains(ModuleType.AGAIN_BUY);
      refundModule = moduleids.contains(ModuleType.ORDER_REFUND);
    }
    List<OrderListBean> listBeans = new ArrayList<OrderListBean>();
    if (!page.isEmpty()) {
      for (OrderInfo order : page) {
        OrderListBean orderListBean = new OrderListBean(order);
        if (order.getMinimalFlag()==OrderType.BESPOKE) {
        	orderListBean.setOrderNumber("DZ"+order.getNumber().substring(14));
        	if(user!=null){
            orderListBean.setRefundFlag(false);
            orderListBean.setFinish(order.getOrderStatus()==1 && map.get(PermissionType.BESPOKE_FINISH_ORDER));
            orderListBean.setAgainBuy(false);
          }
        	findOrderBespoke(orderListBean);
        } else {
        	if(user!=null){
        		//没退货标记true  没退货模块 || 没退货金额
        		orderListBean.setRefundFlag(!refundModule || (order.getRefundingFee()==0 && order.getRefundedFee()==0));
        		orderListBean.setFinish(order.getOrderStatus()==1 && map.get(PermissionType.FINISH_ORDER)
        				&& (order.getMinimalFlag()==OrderType.ROUTINE?generalModule:minimalModule));
        		orderListBean.setAgainBuy(map.get(PermissionType.PLACE_ORDER) && againModule && generalModule);
        	}
        	findOrderProduct(orderListBean);
        }
        if(time.equals(orderListBean.getTime())){
          listBeans.add(orderListBean);
        }else{
          time = orderListBean.getTime();
          OrderDateBean dateBean = new OrderDateBean();
          dateBean.setTime(time);
          listBeans = new ArrayList<OrderListBean>();
          listBeans.add(orderListBean);
          dateBean.setList(listBeans);
          dateBeans.add(dateBean);
        }
        
      }
    }
    pageForm.setData(dateBeans);
    return pageForm;
  }
  
  /**
   * 订单列表 || 订单搜索 的订单查询
   * @version 1.1
   * @param currentPage
   * @param limit
   * @param customerId
   * @param customer
   * @param phone
   * @param Begin
   * @param end
   * @param uid
   * @param status
   * @param querytime
   * @return
   */
  public Page<OrderInfo> getOrderPage(Integer currentPage, Integer limit, Integer customerId,
  		String customer, String phone, Date begin, Date end, List<Integer> uid, Integer status,
  		Long querytime, Boolean cancelModule, Boolean bespokeModule, String pad){
    OrderInfoExample orderInfoExample = new OrderInfoExample();
    Criteria oc = orderInfoExample.createCriteria();
    oc.andUserIdIn(uid).andCreatetimeLessThanOrEqualTo(new Date(querytime));
    if("Pad".equals(pad)||"pad".equals(pad)){
    	oc.andOrderStatusIn(Lists.newArrayList(1,2));
    	oc.andMinimalFlagEqualTo(OrderType.ROUTINE);
    }
    if (status != null) {
      oc.andOrderStatusEqualTo(status);
    }else{ //订单搜索时没有订单状态的选项
    //1.6期 经协商 以前的作废单仍能查看,但不能做任何操作,故以下代码不在使用
    //(只有同时没有定制模块和订单模块是订单列表才没有作废标签,但全部里仍能查看以前的作废单)
//      if(!cancelModule){ //没订单作废模块，不显示所有【已作废】的订单
//        oc.andOrderStatusNotEqualTo(OrderStatusConstant.orderStatusCancel);
//      }
      //经协商取消定制模块时,以前下的定制单仍能查看,但不能做任何操作,故以下代码不在使用
//      if(!bespokeModule){ //没定制商品模块，不显示所有定制订单
//        oc.andMinimalFlagNotEqualTo(OrderType.BESPOKE);
//      }
      if (customerId != null) {
        oc.andCustomerIdEqualTo(customerId);
      }
      if (customer != null) {
        oc.andCustomerEqualTo(customer);
      }
      if (phone != null) {
        oc.andPhoneEqualTo(phone);
      }
      if (begin != null && end != null) {
        oc.andUpdatetimeBetween(begin, TimeUtils.formatDayEnd1(end));
      }
    }
    orderInfoExample.setOrderByClause(" updatetime desc,id desc");
    PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
    Page<OrderInfo> page = (Page<OrderInfo>) orderInfoMapper.selectByExample(orderInfoExample);
    return page;
  }
  
  /**
   * 关联主订单列表(搜索) 的订单查询(可退货的)
   * @version 1.1
   * @param currentPage
   * @param limit
   * @param uid
   * @param querytime
   * @return
   */
  private final static String allCondition = "(minimal_flag=1 or (select count(*) as count from order_info_detail where number > refund_num and order_info_id = order_info.id) > 0)";
  private final static String minimalCondition = "minimal_flag=1";
  private final static String generalCondition = "(select count(*) as count from order_info_detail where number > refund_num and order_info_id = order_info.id) > 0";
  public Page<OrderInfo> getRefundablePage(Integer currentPage, Integer limit, String customer,
      List<Integer> uid, Long querytime, boolean general, boolean minimal){
    String uids = uid.toString().replace("[", "(").replace("]", ")");
    PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
    String condition = (general&&minimal)?allCondition:(general?generalCondition:minimalCondition);
    if(customer != null){
      return (Page<OrderInfo>) orderInfoMapper.selectByCustomerRefundable(uids, customer, new Date(querytime), condition);
    }
    Page<OrderInfo> page = (Page<OrderInfo>) orderInfoMapper.selectByRefundable(uids, new Date(querytime), condition);
    return page;
  }
  
  /**
   * 查询订单下的所有明细
   * 
   * @param orderListBean
   * @return
   */
  public void findOrderProduct(OrderListBean orderListBean) {
    OrderInfoDetailExample orderInfoDetailExample = new OrderInfoDetailExample();
    orderInfoDetailExample.createCriteria().andOrderInfoIdEqualTo(orderListBean.getId());
    orderInfoDetailExample.setOrderByClause("id desc");
    List<OrderInfoDetail> details = orderInfoDetailMapper.selectByExample(orderInfoDetailExample);
    orderListBean.setProductNum(details.size());
    orderListBean.setProductName(details.stream().map(OrderInfoDetail::getName).collect(Collectors.joining("、")));
  }
  
  /**
   * 查询定制订单下的明细
   * 
   * @param orderListBean
   * @return
   */
  public void findOrderBespoke(OrderListBean orderListBean) {
    OrderBespokeDetailExample example = new OrderBespokeDetailExample();
    example.createCriteria().andOrderInfoIdEqualTo(orderListBean.getId());
    List<OrderBespokeDetail> details = orderBespokeDetailMapper.selectByExample(example);
    orderListBean.setProductNum(1);
    orderListBean.setProductName(details.get(0).getName());
  }

  /**
   * 查询某一订单明细
   * 
   * @param id
   * @return
   */
  public OrderInfoBean orderInfoDetail(Integer id) {
    OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(id);
    if(orderInfo!=null){
      OrderInfoBean orderInfoBean = new OrderInfoBean(orderInfo);
      String orderNumber = orderInfo.getNumber().substring(10);
      if (orderInfo.getMinimalFlag()==OrderType.ROUTINE) {
      	List<OrderInfoDetail> lists = findAllDetail(orderInfo.getId());
      	orderInfoBean.setDetail(lists);
      	for(OrderInfoDetail list:lists){
      		orderInfoBean.setDetailNum(orderInfoBean.getDetailNum()+(list.getPrice()*list.getNumber()));
      	}
      } else if(orderInfo.getMinimalFlag()==OrderType.BESPOKE){
      	orderInfoBean.setBespokeDetail(findBespokeDetail(orderInfo.getId()));
      	orderNumber = "DZ"+orderInfo.getNumber().substring(14);
      }
      orderInfoBean.setOrderNumber(orderNumber);
      return orderInfoBean;
    }
    return null;

  }

  /**
   * 查询订单下的所有明细
   * 
   * @param orderInfoId
   * @return
   */
  public List<OrderInfoDetail> findAllDetail(Integer orderInfoId) {
    OrderInfoDetailExample orderInfoDetailExample = new OrderInfoDetailExample();
    orderInfoDetailExample.createCriteria().andOrderInfoIdEqualTo(orderInfoId);
    orderInfoDetailExample.setOrderByClause("id desc");
    return orderInfoDetailMapper.selectByExample(orderInfoDetailExample);

  }
  
  /**
   * 查询定制订单的明细
   * @param orderInfoId
   * @return
   */
  public OrderBespokeDetail findBespokeDetail(Integer orderInfoId){
  	OrderBespokeDetailExample example = new OrderBespokeDetailExample();
  	example.createCriteria().andOrderInfoIdEqualTo(orderInfoId);
  	List<OrderBespokeDetail> bespokeDetail = orderBespokeDetailMapper.selectByExample(example);
  	return bespokeDetail.isEmpty()?null:bespokeDetail.get(0);
  }

  /**
   * 根据订单ID和用户ID查询订单
   * 
   * @param orderInfoId
   * @param uid
   * @return
   */
  public List<OrderInfo> orderInfo(Integer orderInfoId, List<Integer> uid) {
    OrderInfoExample orderInfoExample = new OrderInfoExample();
    orderInfoExample.createCriteria().andIdEqualTo(orderInfoId).andUserIdIn(uid);
    return orderInfoMapper.selectByExample(orderInfoExample);

  }
  
  public OrderInfo findById(Integer orderInfoId) {
    return orderInfoMapper.selectByPrimaryKey(orderInfoId);
  }
  
  public List<OrderInfo> findByIds(List<Integer> oids) {
    OrderInfoExample orderInfoExample = new OrderInfoExample();
    orderInfoExample.createCriteria().andIdIn(oids);
    return orderInfoMapper.selectByExample(orderInfoExample);
  }

  /**
   * 订单完成
   * 
   * @param orderInfoId
   * @param uid
   * @return
   */
  @Transactional
  public int finish(OrderInfo order, List<Integer> uid, Integer belongs, String signatureImg,
                    boolean sendMessage, boolean printModule) {
    Integer orderInfoId = order.getId();
    if (orderInfo(orderInfoId, uid).isEmpty()) {
      return 0;
    } else {
      if(order==null || OrderStatusConstant.orderStatusNO!=order.getOrderStatus()){
        return -1;
      }
      if(order.getSignatureFlag()==1 && (signatureImg==null || signatureImg.isEmpty())){
        return -1;
      }
      OrderInfo orderInfo = new OrderInfo();
      orderInfo.setOrderStatus(OrderStatusConstant.orderStatusYes);
      orderInfo.setUpdatetime(new Date());
      orderInfo.setId(orderInfoId);
      orderInfo.setSignatureImg(signatureImg);
      OrderInfoExample orderInfoExample = new OrderInfoExample();
      orderInfoExample.createCriteria().andOrderStatusEqualTo(OrderStatusConstant.orderStatusNO).andIdEqualTo(orderInfoId);
      int num = orderInfoMapper.updateByExampleSelective(orderInfo, orderInfoExample);
      if(num == 0){
        return -1;
      }
      Integer messageType = order.getMinimalFlag()==1?-MessageType.ORDER_FINISH:MessageType.ORDER_FINISH;
      if(sendMessage){
        List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongs, PermissionType.ORDER_FINISH_REMIND);
        messageService.insertBatch(String.format(MessageInform.ORDER_FINISH, order.getNumber().substring(10)),PushInform.ORDER_FINISH,
          uplist, PermissionType.PLACE_ORDER, order.getId(), messageType, order.getUserId(), printModule);
      }
      return 1;
    }

  }

  /**
   * 搜索订单
   * 
   * @param orderInfo
   * @param uid
   * @return
   */
  public PageForm searchOrderInfo(Integer currentPage, Integer limit, OrderInfo orderInfo,
      Date Begin,Date end, List<Integer> uid) {
    OrderInfoExample orderInfoExample = new OrderInfoExample();
    List<OrderInfoBean> infoBeans = new ArrayList<OrderInfoBean>();
    Criteria oc = orderInfoExample.createCriteria();
    if (orderInfo.getCustomerId() != null) {
      oc.andCustomerIdEqualTo(orderInfo.getCustomerId());
    }
    if (orderInfo.getCustomer() != null) {
      oc.andCustomerEqualTo(orderInfo.getCustomer());
    }
    if (orderInfo.getPhone() != null) {
      oc.andPhoneEqualTo(orderInfo.getPhone());
    }
    if (Begin != null && end != null) {
      oc.andOrderDateBetween(Begin, end);
    }
    if (!uid.isEmpty()) {
      oc.andUserIdIn(uid);
    }
    orderInfoExample.setOrderByClause("order_date desc,id desc");
    PageHelper.startPage(currentPage == null ? 1 : currentPage, limit);
    Page<OrderInfo> order = (Page<OrderInfo>) orderInfoMapper.selectByExample(orderInfoExample);
    PageForm pageForm = PageForm.getPageForm(order);
    if (!order.isEmpty()) {
      for (OrderInfo orderinInfoList : order) {
        OrderInfoBean orderInfoBean = new OrderInfoBean(orderinInfoList);
        orderInfoBean.setOrderNumber(orderinInfoList.getNumber().substring(10));
        orderInfoBean.setDetail(findAllDetail(orderinInfoList.getId()));// 查询出订单下的所有明细
        infoBeans.add(orderInfoBean);
      }
    }
    pageForm.setData(infoBeans);
    return pageForm;
  }
  
  /**
   * 根据订单号查询订单
   * @param orderNumber 订单号
   * @return
   */
  public OrderInfo findByOrderNumber(String orderNumber){
    OrderInfoExample example = new OrderInfoExample();
    example.createCriteria().andNumberEqualTo(orderNumber);
    List<OrderInfo> list = orderInfoMapper.selectByExample(example);
    if(list.isEmpty()){
      return null;
    }
    return list.get(0);
    
  }
  
  /**
   * 根据订单商品id查询订单详情集合
   * @param oid
   * @param pids
   * @return
   */
  public List<OrderInfoDetail> findOdByOPid(Integer oid, List<Integer> pids, List<Integer> odids){
    OrderInfoDetailExample example = new OrderInfoDetailExample();
    example.createCriteria().andOrderInfoIdEqualTo(oid).andProductIdIn(pids).andIdIn(odids);
    return orderInfoDetailMapper.selectByExample(example);
  }
  
  /**
   * 更改退款金额累计
   * @param refunding
   * @param refunded
   * @param oid
   * @return
   */
  public int updateRefundFee(double refunding, double refunded, int oid){
    return orderInfoMapper.updateRefundFee(refunding, refunded, oid);
  }
  
  /**
   * 更改退款的商品数量累计
   * @param num
   * @param id
   * @return
   */
  public int updateOrderRefundNum(int num, int id){
    return orderInfoDetailMapper.updateOrderRefundNum(num, id);
  }
  
  /**
   * 作废订单
   * @param refund
   * @return
   */
  public int cancel(OrderInfo order){
    order.setOrderStatus(OrderStatusConstant.orderStatusCancel);
    order.setRefundingFee(0.00);
    order.setUpdatetime(new Date());
    int num = orderInfoMapper.updateByPrimaryKeySelective(order);
    //作废退货单
    if(order.getMinimalFlag()!=OrderType.BESPOKE){
    	refundService.cancelByOid(order.getId());
    }
    if(order.getMinimalFlag()==OrderType.ROUTINE){
      //订单详情退货数置0
      OrderInfoDetailExample example = new OrderInfoDetailExample();
      OrderInfoDetailExample.Criteria criteria = example.createCriteria();
      criteria.andOrderInfoIdEqualTo(order.getId());
      List<OrderInfoDetail> list = orderInfoDetailMapper.selectByExample(example);
      for (OrderInfoDetail od : list) {
        productService.updateInventory(od.getProductId(), -od.getNumber()+od.getRefundNum());
      }
      criteria.andRefundNumNotEqualTo(0);
      OrderInfoDetail detail = new OrderInfoDetail();
      detail.setRefundNum(0);
      detail.setUpdatetime(new Date());
      orderInfoDetailMapper.updateByExampleSelective(detail, example);
    }
    
    return num;
  }
  
  /**
   * 编辑订单附件图片
   * @param id
   * @param adjunctImg
   * @param user
   * @return
   */
  public int saveAdjunctImg(Integer id, String adjunctImg){
    OrderInfo order = new OrderInfo();
    order.setId(id);
    order.setAdjunctImg(adjunctImg);
    order.setUpdatetime(new Date());
    return orderInfoMapper.updateByPrimaryKeySelective(order);
  }
  
  /**
   * 保存查看凭证
   * @param id
   * @return
   */
  public String saveProof(Integer id){
    OrderInfo order = new OrderInfo();
    order.setId(id);
    String proof = UUID.randomUUID().toString().replaceAll("-", "");
    order.setProof(proof);
    order.setUpdatetime(new Date());
    int num = orderInfoMapper.updateByPrimaryKeySelective(order);
    return num==1?proof:null;
  }
  
  /**
   * 根据二维码的校验凭证查询订单
   * @param id 订单id
   * @param proof 二维码的校验凭证
   * @return
   */
  public OrderInfo findByProof(Integer id, String proof){
    OrderInfoExample example = new OrderInfoExample();
    example.createCriteria().andIdEqualTo(id).andProofEqualTo(proof);
    List<OrderInfo> list = orderInfoMapper.selectByExample(example);
    if(list.isEmpty()){
      return null;
    }
    return list.get(0);
  }
  
  /**
   * 定制订单   预下单||确认下单
   * @param orderInfo
   * @param bespoke
   * @param user
   * @return
   */
	public int addOrderBespoke(OrderInfo orderInfo, OrderBespokeDetail bespoke, User user) {//, Customer customer
		orderInfo.setCustomerId(null);//根据新需求 定制订单不关联客户id 客户信息传什么就存什么
		
    Integer uid = user.getId();
    Integer belongs = user.getBelongs();
    Shop shop = shopService.findShop(user);
    Date date = new Date();
    orderInfo.setUpdatetime(date);
    orderInfo.setSalesman(user.getStaffname());
    orderInfo.setMinimalFlag(OrderType.BESPOKE);
    orderInfo.setShopId(shop.getId());
    orderInfo.setUserId(uid);
    
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
//    if(orderInfo.getOrderStatus()==OrderStatusConstant.orderStatusNO){
//    	Customer custom = null;
//    	if(orderInfo.getCustomerId()!=null){
//    		custom = customerService.findById(orderInfo.getCustomerId());
//    	}else{
//    		if(user.getIsTrialAccount() && customerService.checkTrialProductNum(user.getId())){
//    			return 10601; 
//    		}
//    		if(moduleids.contains(ModuleType.CUSTOMER)){
//    			custom = customerService.insert(customer, user);
//    			orderInfo.setCustomerId(customer.getId());
//    		}
//    	}
//    	if(custom!=null&&!custom.getStatus()){
//    		return 25000;
//    	}
//    }
    
    try {
      orderInfo.setOrderDate(sdf.parse(sdf.format(date)));
    } catch (ParseException e) {
      e.printStackTrace();
      return 0;
    }
    
    //没有过预下单的直接保存
    if(orderInfo.getId()==null){
    	orderInfo.setNumber("DZ" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + (new Random().nextInt(899999)+100000));
    	orderInfo.setCreatetime(date);
    	orderInfoMapper.insertSelective(orderInfo);
    	bespoke.setOrderInfoId(orderInfo.getId());
      bespoke.setCreatetime(date);
      bespoke.setUpdatetime(date);
      orderBespokeDetailMapper.insert(bespoke);
    }else{ //有过预下单的更改
    	putOriginalInfo(orderInfo);//放入原信息
    	OrderInfoExample orderExample = new OrderInfoExample();
    	orderExample.createCriteria().andIdEqualTo(orderInfo.getId()).andShopIdEqualTo(orderInfo.getShopId())
    		.andOrderStatusEqualTo(OrderStatusConstant.orderStatusPre);
    	orderInfoMapper.updateByExample(orderInfo, orderExample);
    	bespoke.setOrderInfoId(orderInfo.getId());
      bespoke.setUpdatetime(date);
      OrderBespokeDetailExample bespokeExample = new OrderBespokeDetailExample();
      bespokeExample.createCriteria().andOrderInfoIdEqualTo(orderInfo.getId()).andCustomProductIdEqualTo(bespoke.getCustomProductId());
      List<OrderBespokeDetail> bespokes = orderBespokeDetailMapper.selectByExample(bespokeExample);
      OrderBespokeDetail old = bespokes.get(0);
      bespoke.setId(old.getId());
      bespoke.setCreatetime(old.getCreatetime());
      orderBespokeDetailMapper.updateByPrimaryKey(bespoke);
    }
    
    if(orderInfo.getOrderStatus()==OrderStatusConstant.orderStatusNO){
    	//更新客户信息
    	this.customerService.updateLatelyAccount(orderInfo.getReceiceAmoun(),orderInfo.getCustomerId());
    	
    	boolean printModule = moduleids.contains(ModuleType.BESPOKE_ORDER_PRINT);
    	if(!user.getIsTrialAccount() && moduleids.contains(ModuleType.MESSAGE)){
    		belongs = (belongs==0)?uid:belongs;
    		List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongs, PermissionType.PLACE_ORDER_REMIND);
    		messageService.insertBatch(String.format(MessageInform.PLACE_AN_ORDER, "DZ"+orderInfo.getNumber().substring(14)),PushInform.PLACE_AN_ORDER,
    				uplist, PermissionType.BESPOKE_PLACE_ORDER, orderInfo.getId(), MessageType.PLACE_AN_ORDER, uid, printModule);
    	}
    }
    return 1;
  }
  
  /**
   * 放入原信息
   * @param orderInfo
   */
  public void putOriginalInfo(OrderInfo orderInfo){
  	OrderInfo order = findById(orderInfo.getId());
  	orderInfo.setNumber(order.getNumber());
  	orderInfo.setCreatetime(order.getCreatetime());
  	orderInfo.setRefundedFee(0.00);
  	orderInfo.setRefundingFee(0.00);
  	orderInfo.setDiscount(1d);
  }
  
  /**
   * 定制订单完成
   * @param order
   * @param user
   * @return
   */
  public int bespokeFinish(OrderInfo order, User user){
  	order.setOrderStatus(OrderStatusConstant.orderStatusYes);
    order.setUpdatetime(new Date());
    OrderInfoExample orderInfoExample = new OrderInfoExample();
    orderInfoExample.createCriteria().andOrderStatusEqualTo(OrderStatusConstant.orderStatusNO).andIdEqualTo(order.getId());
    int num = orderInfoMapper.updateByExample(order, orderInfoExample);
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    if(num==1 && !user.getIsTrialAccount() && moduleids.contains(ModuleType.MESSAGE)){
    	Integer belongs = user.getBelongs();
    	belongs = (belongs==0)?user.getId():belongs;
    	boolean printModule = moduleids.contains(ModuleType.BESPOKE_ORDER_PRINT);
    	List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongs, PermissionType.ORDER_FINISH_REMIND);
      messageService.insertBatch(String.format(MessageInform.ORDER_FINISH, "DZ"+order.getNumber().substring(14)),PushInform.ORDER_FINISH,
        uplist, PermissionType.BESPOKE_FINISH_ORDER, order.getId(), MessageType.ORDER_FINISH, order.getUserId(), printModule);
    }
    return num;
  }
  
  /**
   * 
   * @param allInfo 是否取全员信息
   * @param denominator 是否是分母
   * @param id 全员-shopId  个人-userId
   */
  public int countOrder4Completion(boolean allInfo,boolean denominator,Integer id){
  	OrderInfoExample example = new OrderInfoExample();
  	Criteria criteria = example.createCriteria();
  	if(allInfo){
  		criteria.andShopIdEqualTo(id);
  	}else{
  		criteria.andUserIdEqualTo(id);
  	}
  	if(denominator){
  		criteria.andOrderStatusIn(Lists.newArrayList(OrderStatusConstant.orderStatusYes,OrderStatusConstant.orderStatusNO));
  	}else{
  		criteria.andOrderStatusEqualTo(OrderStatusConstant.orderStatusYes);
  	}
  	return orderInfoMapper.countByExample(example);
  }
  
  /**
   * 商城预支付
   * 
   * @param pid
   * @param inventory
   * @return
   */
  public void prepay2ShopOrder(List<ShopOrder> products, Integer shopId, String orderNumber) {
    for (ShopOrder so : products) {
      int num = productService.updateInventory(so.getProductId(), so.getBuyNumber());
      if(num==0){
        throw new RuntimeException("更新库存失败");
      }
      Date now = new Date();
      so.setShopId(shopId);
      so.setOrderNum(orderNumber);
      so.setCreatetime(now);
      so.setUpdatetime(now);
      shopOrderMapper.insertSelective(so);
    }
  }
  
  /**
   * 商城下单成功
   * @param orderNumber
   * @param shopId
   */
  public void paySuccess2ShopOrder(String orderNumber, Integer shopId){
    ShopOrderExample example = new ShopOrderExample();
    example.createCriteria().andStatusEqualTo(0).andShopIdEqualTo(shopId).andOrderNumEqualTo(orderNumber);
    List<ShopOrder> sos = shopOrderMapper.selectByExample(example);
    ShopOrder record = new ShopOrder();
    record.setStatus(1);
    record.setUpdatetime(new Date());
    shopOrderMapper.updateByExampleSelective(record, example);
    for (ShopOrder so : sos) {
      productService.updateSalesVolume(so.getProductId(), shopId, so.getBuyNumber());
    }
  }
  
  /**
   * 商城下单失败
   * @param orderNumber
   * @param shopId
   */
  public void payFailure2ShopOrder(String orderNumber, Integer shopId){
    ShopOrderExample example = new ShopOrderExample();
    example.createCriteria().andStatusEqualTo(0).andShopIdEqualTo(shopId).andOrderNumEqualTo(orderNumber);
    List<ShopOrder> sos = shopOrderMapper.selectByExample(example);
    for (ShopOrder so : sos) {
      productService.updateInventory(so.getProductId(), -so.getBuyNumber());
    }
    ShopOrder so = new ShopOrder();
    so.setStatus(2);
    so.setUpdatetime(new Date());
    shopOrderMapper.updateByExampleSelective(so, example);
  }

}
