package com.qianfan365.jcstore.controller.weiyu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.ProductStatusConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageInform;
import com.qianfan365.jcstore.common.constant.MessageConstant.PushInform;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.ShopOrder;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.MessageService;
import com.qianfan365.jcstore.service.OrderInfoService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ProductService;
import com.qianfan365.jcstore.service.ShopService;

/**
 * 微语订单相关
 * @author liuhaoran
 */
@Controller
@RequestMapping("/weiyu/order")
public class WyOrderController extends BaseController {
  
  @Autowired
  private ShopService shopService;
  @Autowired
  private OrderInfoService orderService;
  @Autowired
  private ProductService productService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private MessageService messageService;
  
  /**
   * 预支付
   * @param soList
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.POST, value = "/prepay")
  public ResultData prepay(String orderNumber, String products){
    List<ShopOrder> soList = JSON.parseArray(products, ShopOrder.class);
    User user = this.getLoginUser(null);
    Shop shop = shopService.findShop(user);
    Integer shopId = shop.getId();
    List<Integer> prewarning = new ArrayList<Integer>();
    for (ShopOrder so : soList) {
      Product product = productService.findById(so.getProductId());
      if(product!=null && !shopId.equals(product.getShopId())){
        return ResultData.build().parameterError();
      }
      if(product==null || ProductStatusConstant.No==product.getStatus()){
        return ResultData.build().setStatus(Status.PRODUCE_DEL);
      }
      if(product.getInventory()<so.getBuyNumber()){
        List<Integer> productIds 
          = soList.stream().map(ShopOrder::getProductId).collect(Collectors.toList());
        return ResultData.build().setStatus(Status.INVENTORY_SHORTAGE)
            .put("data", productService.findByIds(productIds,shopId));
      }
      if ((product.getInventory()-so.getBuyNumber())<shop.getInventoryWarning()) {
        prewarning.add(product.getId());
      }
    }
    try {
      orderService.prepay2ShopOrder(soList, shopId, orderNumber);
    } catch (RuntimeException e) {
      List<Integer> productIds 
        = soList.stream().map(ShopOrder::getProductId).collect(Collectors.toList());
      return ResultData.build().setStatus(Status.INVENTORY_SHORTAGE)
          .put("data", productService.findByIds(productIds,shopId));
    }
    //存库全部扣减成功 再考虑库存预警
    int belongs = (user.getBelongs()==0)?user.getId():user.getBelongs();
    if(!prewarning.isEmpty() && SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT)){
      List<UserPermission> uplistInve = permissionInfoService.findMessageNeed(belongs, PermissionType.INVENTORY_WARNING_REMIND);
      for (Integer pid : prewarning) {
        messageService.insertBatch(MessageInform.INVENTORY_WARNING,PushInform.INVENTORY_WARNING, uplistInve,
          PermissionType.INVENTORY_WARNING_REMIND, pid, MessageType.INVENTORY_WARNING, 0, true);
      }
    }
    return ResultData.build().success();
  }
  
  /**
   * 支付成功
   * @param orderId
   * @param orderNumber
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.POST, value = "/pay/success")
  public ResultData paySuccess(Integer orderId, String orderNumber){
    User user = this.getLoginUser(null);
    int belongs = (user.getBelongs()==0)?user.getId():user.getBelongs();
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    Integer shopId = shopService.findShop(user).getId();
    //改商城订单表状态
    orderService.paySuccess2ShopOrder(orderNumber, shopId);
    
    //发消息和通知
    if(moduleids.contains(ModuleType.MESSAGE)){
      List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongs, PermissionType.PLACE_ORDER_REMIND);
      String content = String.format(MessageInform.PLACE_AN_SHOPORDER, orderNumber.substring(12));
      String pushinfo = PushInform.PLACE_AN_ORDER;
      Integer messageType = MessageType.PLACE_AN_SHOPORDER;
      Integer permissionId = PermissionType.PLACE_ORDER;
      //uid参数:由于商城订单消息,个人权限的都收不到,所以这里传了店长的id,以便service层排除个人权限
      messageService.insertBatch(content, pushinfo, uplist, permissionId, orderId, messageType, user.getId(), false);
    }
    return ResultData.build().success();
  }
  
  /**
   * 支付失败
   * @param orderNumber
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.POST, value = "/pay/failure")
  public ResultData payFailure(String orderNumber){
    User user = this.getLoginUser(null);
    Shop shop = shopService.findShop(user);
    Integer shopId = shop.getId();
    //改商城订单表状态  且  释放库存
    orderService.payFailure2ShopOrder(orderNumber, shopId);
    return ResultData.build().success();
  }
}
