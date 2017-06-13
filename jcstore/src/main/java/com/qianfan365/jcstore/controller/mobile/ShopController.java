package com.qianfan365.jcstore.controller.mobile;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.qianfan365.jcstore.common.util.WeiYuRequst;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ShopService;
import com.qianfan365.jcstore.service.UserService;

/**
 * Created by qianfanyanfa on 16/8/12.
 */
@Controller
@RequestMapping(path = "/mobile/shop")
public class ShopController extends BaseController {

  @Autowired
  ShopService shopService;
  @Autowired
  UserService userService;
  @Value("${common.domain-url}/")
  String path;

  @ResponseBody
  @RequestMapping(value = "/info", method = RequestMethod.GET)
  public ResultData getShopInfo(HttpSession session,Integer productId) {
    User user = getLoginUser(session);
    // 不是店铺所有者,则无权限查看  ps：1.5期因为营销分享 所以店员也能查看店铺信息
//    if (user.getBelongs() != 0) {
//      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
//    }
    Shop shop = shopService.findShop(user);
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean generalModule = moduleids.contains(ModuleType.GENERAL_ORDER);
    boolean productModule = moduleids.contains(ModuleType.PRODUCT);
    boolean messageModule = moduleids.contains(ModuleType.MESSAGE);
    boolean wyModule = moduleids.contains(ModuleType.MARKETING_PLATFORM_MICROMALL);
    if(!generalModule){
      shop.setAnnouncements(null);
    }
    boolean warning = productModule&messageModule;
    if(!warning){
      shop.setInventoryWarning(null);
    }
    String shopurl=path + "mobile/marketing/shop?shopId=" + shop.getId();
    String productUrl="";
    if(productId!=null&&productId>0){
       productUrl=path + "mobile/marketing/product?pid=" + productId;
      if(wyModule){
        productUrl=WeiYuRequst.getInstance(WeiYuRequst.WYURLS.PRODUCT_URL.url,user).excute(String.class,productId);
      }
    }
    if(wyModule){
      shopurl= WeiYuRequst.getInstance(WeiYuRequst.WYURLS.SHOP_URL.url,user).excute(String.class);
    }
    return ResultData.build().put("shop", shop)
            .put("general", generalModule)
            .put("product", warning)
            .put("shopurl",shopurl).put("producturl",productUrl);

  }


  /**
   * 保存或更新店铺信息
   *
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/info/save", method = RequestMethod.POST)
  public ResultData saveLog(Shop shop, HttpSession session) {

    shop.setUserId(null);
    shop.setStatus(null);
    shop.setCreatetime(null);
    User user = getLoginUser(session);
    // 不是店铺所有者,则无权限查看
    if (user.getBelongs() != 0) {
      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
    }
    Shop old = shopService.findShop(user);

    // 取行目前一对一的shop(建议)app端传过shop的id,方便后期兼容多店铺时的信息保存
    shop.setId(old.getId());

    if (StringUtils.isBlank(shop.getName())) {
      shop.setName(null);
    } else {
      // 店铺名称不能超过15个字
      Assert.isTrue(shop.getName().length() <= 15);
      boolean shopExist = shopService.findShopExist(shop.getName());
      if (shopExist) {
        return ResultData.build().setStatus(ResultData.Status.SHOP_NAME_EXISTED)
            .put(ResultData.STATUS_MSG, "您输入的店名已存在，请修改");
      }
    }
    if (shop.getOwner() != null) {
      // 店长的名称中能为中英文
      String reg = "^[A-Za-z\\u4e00-\\u9fa5]*$";
      Assert.isTrue(shop.getOwner().length() <= 10);
      Assert.isTrue(shop.getOwner().matches(reg));
    }
    if (shop.getAddress() != null) {
      Assert.isTrue(shop.getAddress().length() <= 50);
    }
    if (shop.getTel() != null) {
      // 只能输入阿拉伯数字和符号“—”，长度不超过15个字
      String regNum = "^[0-9\\-]*$";
      Assert.isTrue(shop.getTel().length() <= 15);
      Assert.isTrue(shop.getTel().matches(regNum));
    }
    if(shop.getIntroduction() != null){
      //中英文符号都可，长度不超过30个字
      String intNum = "^[\\s\u4E00-\u9FA5A-Za-z0-9,\\.~!@#$%^&\\*()_\\+\\-=|\\\\:;/?\\[\\]{}‘’“”—，。！￥……（）、：；？【】]*$";
      Assert.isTrue(shop.getIntroduction().length() <= 30);
      Assert.isTrue(shop.getIntroduction().matches(intNum));
    }
    if (shop.getAddress() != null) {
      Assert.isTrue(shop.getAddress().length() <= 50);
    }
    if(shop.getInventoryWarning() != null){
      user = userService.findUser(user.getId());
      List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
      if(!moduleids.contains(ModuleType.PRODUCT) || !moduleids.contains(ModuleType.MESSAGE)){
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      Assert.isTrue(shop.getInventoryWarning()>=0 
          && shop.getInventoryWarning().toString().length()<=6);
    }
    if (shop.getAnnouncements() != null) {
      user = userService.findUser(user.getId());
      if(!SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.GENERAL_ORDER)){
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      if("".equals(shop.getAnnouncements().trim())){
        shop.setAnnouncements("");
      }else{
        String regNum = "^[\\s\u4E00-\u9FA5A-Za-z0-9,\\.~!@#$%^&\\*()_\\+\\-=|\\\\:;/?\\[\\]{}‘’“”—，。！￥……（）、：；？【】]*$";
        Assert.isTrue(shop.getAnnouncements().length() <= 300);
        Assert.isTrue(shop.getAnnouncements().matches(regNum));
      }
    }
    shopService.updateShop(shop);
    return ResultData.build();
  }
  
  /**
   * 保存库存预警值
   *
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/save/prewarning", method = RequestMethod.POST)
  @Deprecated
  public ResultData saveInventoryWarning(Integer prewarning, HttpSession session) {
    if(prewarning==null || prewarning.toString().length()>6){
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
    // 不是店铺所有者,则无权限查看
    if (user.getBelongs() != 0) {
      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
    }
    Shop shop = shopService.findShop(user);
    shop.setInventoryWarning(prewarning);
    if(shopService.updateShop(shop)){
      return ResultData.build().success();
    } else {
      return ResultData.build().failure();
    }
    
  }

}
