package com.qianfan365.jcstore.controller.mobile;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.UserService;

/**
 * 设置管理
 * @author liuhaoran
 *
 */
@Controller
@RequestMapping("/mobile/set")
public class SetMobileController extends BaseController{
  
  @Autowired
  private UserService userService;
  
  /**
   * 店铺管理员设置列表的模块信息
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "modules", method = RequestMethod.GET)
  public ResultData modules(HttpSession session){
    User user = userService.findUser(getLoginUser(session).getId());
    if(user.getBelongs()>0){
      return ResultData.build().parameterError();
    }
    List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
    boolean productGroupModule = moduleids.contains(ModuleType.PRODUCT_GROUP);
    boolean customerGroupModule = moduleids.contains(ModuleType.CUSTOMER_GROUP);
    boolean staffModule = moduleids.contains(ModuleType.USER);
    Map<String, Boolean> map = Maps.newHashMap();
    map.put("productGroup", productGroupModule);
    map.put("customerGroup", customerGroupModule);
    map.put("staff", staffModule);
    return ResultData.build().put("module", map);
  }

}
