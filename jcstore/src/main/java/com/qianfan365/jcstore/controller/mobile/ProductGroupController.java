package com.qianfan365.jcstore.controller.mobile;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.trial.TrialUnavailable;
import com.qianfan365.jcstore.common.check.ProductGroupCheck;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.pojo.ProductGroup;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ProductGroupService;
import com.qianfan365.jcstore.service.ShopService;

/**
 * 商品分组相关
 * @author guanliyu
 *
 */
@Controller
@RequestMapping("/mobile/productgroup")
public class ProductGroupController extends BaseController{
  
  @Autowired
  private ProductGroupService productGroupService;
  
  @Autowired
  private ShopService shopService;
  
  /**
   * 根据用户
   * @param session
   * @return
   */
  @RequestMapping(value="find",method=RequestMethod.GET)
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.PRODUCT,ModuleType.PRODUCT_GROUP})
  public ResultData findProductGroup(HttpSession session){
    User user = getLoginUser(session);
    Integer shopId = shopService.findShop(user).getId();
    return ResultData.build().parseList(productGroupService.findAllByShopId(shopId));
//    if(user.getBelongs()==0){
//      List<Integer> users = listUserId(user);
//      return ResultData.build().parseList(productGroupService.findProductGroup(users));
//    }
//    else{
//      List<Integer> users = listPermissionUserId(user);
//      return ResultData.build().parseList(productGroupService.findProductGroup(users));
//    }
    
  }
  
  /**
   * 添加分组
   * @param session
   * @param productGroup
   * @return
   */
  @RequestMapping(value="save",method=RequestMethod.POST)
  @ResponseBody
  @TrialUnavailable
  @ModulePassport(moduleids = {ModuleType.PRODUCT,ModuleType.PRODUCT_GROUP})
  public ResultData addProductGroup(HttpSession session,ProductGroup productGroup){
    User user = getLoginUser(session);
    if(ProductGroupCheck.checkGroup(productGroup)){
      return ResultData.build().parameterError();
    }
    if(user.getBelongs()==0){
      if(productGroupService.addProductGroup(productGroup, user.getId(), shopService.findShop(user).getId())==1){
        return ResultData.build().success();
      }
      return ResultData.build().failure();
    }
    return ResultData.build().failure();
  }
  
  /**
   * 修改分组
   * @param session
   * @param productGroup
   * @return
   */
  @RequestMapping(value="update",method=RequestMethod.POST)
  @ResponseBody
  @Deprecated
  public ResultData updateProductGroup(HttpSession session,ProductGroup productGroup){
    User user = getLoginUser(session);
    if(user.getBelongs()==0){
      productGroup.setShopId(shopService.findShop(user).getId());
      productGroup.setUpdatetime(new Date());
      if(productGroupService.updateProductGroup(productGroup)==1){
        return ResultData.build().success();
      }
      return ResultData.build().failure();
    }
    productGroup.setShopId(shopService.findShop(user).getId());
    productGroup.setUpdatetime(new Date());
    if(productGroupService.updateProductGroup(productGroup)==1){
      return ResultData.build().success();
    }
    return ResultData.build().failure();
  }
  
  /**
   * 删除分组
   * @param session
   * @param productGroupId
   * @return
   */
  @RequestMapping(value="del",method=RequestMethod.POST)
  @ResponseBody
  @TrialUnavailable
  public ResultData updateProductGroup(HttpSession session,Integer id){
    User user = getLoginUser(session);
    if(user.getBelongs()==0){
      List<Integer> users = listUserId(user);
      if(productGroupService.delProductGroup(id, users)==1){
        return ResultData.build().success();
      }else{
        return ResultData.build().failure();
      }
    }
    return ResultData.build().failure();
  } 
}
