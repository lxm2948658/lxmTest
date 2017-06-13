package com.qianfan365.jcstore.controller.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.qianfan365.jcstore.common.bean.OrderInfoBean;
import com.qianfan365.jcstore.common.bean.ProductBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.PermissionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderStatusConstant;
import com.qianfan365.jcstore.common.constant.OrderConstant.OrderType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.RefundConstant.RefundStatus;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.Refund;
import com.qianfan365.jcstore.common.pojo.RefundDetail;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.common.util.WeiYuRequst;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.MessageService;
import com.qianfan365.jcstore.service.OrderInfoService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ProductGroupService;
import com.qianfan365.jcstore.service.ProductService;
import com.qianfan365.jcstore.service.RefundService;
import com.qianfan365.jcstore.service.UserService;

/**
 * wap版消息详情页(1.3期添加)
 * @author liuhaoran
 *
 */
@Controller
@RequestMapping("/mobile/message")
public class MessageDetailController extends BaseController {
	
  @Autowired
  private OrderInfoService orderService;

  @Autowired
  private UserService userService;

  @Autowired
  private PermissionInfoService permissionService;
  
  @Autowired
  private ProductService productService;
  
  @Autowired
  private RefundService refundService;
  
  @Autowired
  private ProductGroupService productGroupService;
  
  @Autowired
  private  MessageService messageService;

  /**
   * 查询订单明细
   * @param session
   * @param model
   * @param linkParameter
   * @param type  消息类型
   * @param notification 是否是从通知栏跳的
   * @return
   */
  @RequestMapping(value = "detail", method = RequestMethod.GET)
//  @ModulePassport(moduleids = {ModuleType.MESSAGE},setuser=true) //wap页
  public String detail(HttpSession session, Model model, Integer linkParameter, Integer type, @RequestParam(defaultValue = "0") Integer notification) {
    User user = getLoginUser(session);
    user = userService.findUser(user.getId());
    String link = "";
    ResultData result = ResultData.build();
    if(!SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.MESSAGE)){
      result.setStatus(Status.MODULE_CHANGED);
      model.addAttribute("result", result);
      return "/mobile/user_abnormal";
    }
    //消息标记已读
    if(notification==1){
      messageService.tabRead(user.getId(), type, linkParameter);
    }
    if(MessageType.PLACE_AN_ORDER.equals(type) || MessageType.ORDER_FINISH.equals(type)){
      OrderInfoBean order = orderService.orderInfoDetail(linkParameter);
      if(order==null){
          result = ResultData.build().parameterError();
      }
      if(order.getMinimalFlag()!=OrderType.BESPOKE){
          result = orderDetail(session, user, order);
          link = "mobile/order-detail";
      }else{
          result = bespokeDetail(session, user, order);
          link = "mobile/bespoke-order-detail";
      }
    }else if(MessageType.PLACE_AN_SHOPORDER.equals(type)){
      result = shopOrderDetail(session, user, linkParameter);
      link = "mobile/shop-order-detail";
    }else if(MessageType.REFUND_REMID.equals(type)){
      result = refundDetail(session, user, linkParameter);
      link = "mobile/refund-detail";
    }else if(MessageType.INVENTORY_WARNING.equals(type)){
      result = productDetail(session, user, linkParameter);
      link = "mobile/product-detail";
    }
    model.addAttribute("result", result);
    if(!result.get("status").equals(1)){
    	link = "/mobile/user_abnormal";
    }
    
    return link;
    
  }

  /**
   * 订单详情
   * @param session
   * @param user
   * @param orderId
   * @return
   */
  public ResultData orderDetail(HttpSession session, User user, OrderInfoBean order) {
    Map<String, Object> map = new HashMap<String, Object>();
    ResultData result = permissionService.orderPermissionCheckmes(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    //1.1期 增加退货单信息
    order.setRefundBean(refundService.findByOid(order.getId()));
    
    map.put("aadata", order);
    //1.1期 加了退货和订单作废的权限
    Map<Integer, Boolean> permap = Maps.newHashMap();
    Map<String, Object> objmap = Maps.newHashMap();
    if (order.getOrderStatus() == OrderStatusConstant.orderStatusCancel) {
      objmap.put("refund", 0);
      objmap.put("finish_order", 0);
      objmap.put("order_cancel", 0);
      return ResultData.build().put("order",order).parseMap(objmap).put("buttonNum", 0);
    }
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean minimalModule = moduleids.contains(ModuleType.MINIMAL_ORDER);
    boolean cancelModule = moduleids.contains(ModuleType.ORDER_CANCEL);
    boolean refundModule = moduleids.contains(ModuleType.ORDER_REFUND);
    permap = permissionService.findMapByUidPids(Arrays.asList(PermissionType.FINISH_ORDER,
      PermissionType.REFUND,PermissionType.ORDER_CANCEL),user.getId(), user.getBelongs());
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
    objmap.put("refund", permap.get(PermissionType.REFUND)?1:0);
    objmap.put("finish_order", permap.get(PermissionType.FINISH_ORDER)?1:0);
    objmap.put("order_cancel", permap.get(PermissionType.ORDER_CANCEL)?1:0);
    long buttonNum = permap.values().stream().filter(v->v).count();
    return ResultData.build().put("order",order).parseMap(objmap).put("buttonNum", buttonNum);
  }
  
  /**
   * 定制订单详情
   * @param session
   * @param user
   * @param order
   * @return
   */
  public ResultData bespokeDetail(HttpSession session, User user, OrderInfoBean order) {
    ResultData result = permissionService.orderPermissionCheckmes(session, user, order.getUserId());
    if(result!=null){
      return result;
    }
    Map<Integer, Boolean> permap = Maps.newHashMap();
    Map<String, Object> objmap = Maps.newHashMap();
//    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    
    if(StringUtils.isBlank(order.getBespokeDetail().getMeasureInfo())){
    	order.getBespokeDetail().setMeasureInfo("{\"custom\":[]}");
    }
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean bespokeModule = moduleids.contains(ModuleType.BESPOKE_ORDER);
    if (order.getOrderStatus() == OrderStatusConstant.orderStatusNO && bespokeModule) {
    	permap = permissionService.findMapByUidPid(PermissionType.BESPOKE_FINISH_ORDER, user.getId(), user.getBelongs());
    } else {
    	permap.put(PermissionType.BESPOKE_FINISH_ORDER, false);
    }
    objmap.put("finish_order", permap.get(PermissionType.BESPOKE_FINISH_ORDER)?1:0);
    return ResultData.build().put("order", order).parseMap(objmap);
  }
  
  /**
   * 商城订单详情
   * @param session
   * @param user
   * @param orderId
   * @return
   */
  public ResultData shopOrderDetail(HttpSession session, User user, Integer shopOrderId) {
    if (user.getBelongs() != 0  && permissionService
        .findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) == null) {
      return ResultData.build().setStatus(Status.NO_VIEW_PERMISSION);
    }
    OrderInfoBean order = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.ORDER_DETAIL.url, user).excute(OrderInfoBean.class, shopOrderId);
    order.setId(shopOrderId);
    order.setOrderNumber(order.getNumber().substring(12));
    return ResultData.build().put("order",order);
  }
  
  /**
   * 退货单详情
   * @param session
   * @param user
   * @param refundId
   * @return
   */
  public ResultData refundDetail(HttpSession session, User user, Integer refundId) {
    Refund refund = refundService.findById(refundId);
    if(refund==null){
      return ResultData.build().parameterError();
    }
    ResultData result = permissionService.orderPermissionCheckmes(session, user, refund.getUserId());
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
    Map<String, Object> objmap = Maps.newHashMap();
    objmap.put("finish_order", permission.get(PermissionType.FINISH_ORDER)?1:0);
    objmap.put("order_cancel", permission.get(PermissionType.ORDER_CANCEL)?1:0);
    long buttonNum = permission.values().stream().filter(v->v).count();
    return ResultData.build().put("refund", refund).put("details", details).parseMap(objmap).put("buttonNum", buttonNum);
  }
  
  /**
   * 商品详情
   * @param session
   * @param user
   * @param productId
   * @return
   */
  public ResultData productDetail(HttpSession session, User user, Integer productId) {
    Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    List<Integer> usersId = new ArrayList<Integer>();
    // 当前登录为管理员时
    if (user.getBelongs() == 0) {
      // 获取旗下所有员工的ID
      usersId = listUserId(user);
      // 封装权限信息
      map.put(PermissionType.PRODUCE_EDIT, true);
      map.put(PermissionType.VIEW_CORPUS, true);
    } else {
      // 登录状态为普通用户时
      usersId = listPermissionUserId(user);
      // 封装权限信息
      map = permissionService.findMapByUidPids(
         Arrays.asList(PermissionType.PRODUCE_EDIT, PermissionType.VIEW_CORPUS), user.getId());
    }
    // 根据商品ID及用户ID查询商品
    Product product = productService.findByPidProduct(productId, usersId);
    if(product==null){
      return ResultData.build().setStatus(Status.PRODUCE_DEL);
    }
    
    if (!map.get(PermissionType.VIEW_CORPUS)) { // 没有权限成本价置空
      product.setCostPrice(null);
    }
    ProductBean bean = new ProductBean(product);
    bean.setGroupName(null);
    if(SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT_GROUP)){
      if(product.getGroupId() != null){
        bean.setGroupName(productGroupService.findById(product.getGroupId()).getName());
      }else{
        bean.setGroupName("");
      }
    }
    if(bean.getCustomDescription()==null || bean.getCustomDescription().isEmpty()){
      bean.setCustomDescription("{\"customDescription\":[]}");
    }
    Map<String, Object> objmap = Maps.newHashMap();
    objmap.put("produce_edit", map.get(PermissionType.PRODUCE_EDIT)?1:0);
    objmap.put("view_corpus", map.get(PermissionType.VIEW_CORPUS)?1:0);
    return ResultData.build().put("product", bean).parseMap(objmap);
  }
  
  
  
  
//wangruoqiu 定制订单详情
	@RequestMapping("/bespokeOrderdetail")
	public String bespokeOrderdetail(Model model,Integer oid){
	  OrderInfo order = orderService.findById(oid);
	  model.addAttribute("result", ResultData.build().put("order",order));
	  return "mobile/bespoke-order-detail";
	}
}
