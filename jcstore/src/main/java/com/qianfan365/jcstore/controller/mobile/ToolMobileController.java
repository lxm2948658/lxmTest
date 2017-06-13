package com.qianfan365.jcstore.controller.mobile;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping("/mobile/tool")
public class ToolMobileController extends BaseController{
  
  @Autowired
  private UserService userService;

  /**
   * 显示的工具模块
   * @param session
   * @return
   */
  @RequestMapping(path = "/get", method = RequestMethod.GET)
  @ResponseBody
  public ResultData find(HttpSession session) {
    User user = getLoginUser(session);
    List<String> tools = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
    tools.retainAll(ModuleData.getToolModuleids());
    return ResultData.build().put("tool", tools);
  }
}
