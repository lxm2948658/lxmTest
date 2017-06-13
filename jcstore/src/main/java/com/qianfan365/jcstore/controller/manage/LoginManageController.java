package com.qianfan365.jcstore.controller.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping(path = "/manage")
public class LoginManageController extends BaseController{
  
  @Autowired
  private UserService userService;
  
  @RequestMapping("/toLogin")
  public String toLogin(){
    return "manage/login";
  }
  
  
  @RequestMapping("/toMLogin")
  public String toMLogin(){
    return "manage/mlogin";
  }
  
  @RequestMapping("/clientAdd")
  public String clientAdd(HttpSession session){
	session.setAttribute("ma_head_nav", "client");
	session.setAttribute("ma_sider_nav", "client");
    return "manage/client-add";
  }
  
  @RequestMapping("/clientEdit")
  public String clientEdit(HttpSession session){
	session.setAttribute("ma_head_nav", "client");
	session.setAttribute("ma_sider_nav", "client");
    return "manage/client-edit";
  }
  
  @RequestMapping("/clientUping")
  public String clientUping(HttpSession session){
	session.setAttribute("ma_head_nav", "client");
	session.setAttribute("ma_sider_nav", "client");
    return "manage/client-uping";
  }
  
  @RequestMapping("/clientList")
  public String clientList(HttpSession session){
	session.setAttribute("ma_head_nav", "client");
	session.setAttribute("ma_sider_nav", "client");
    return "manage/client-list";
  }
  
  @RequestMapping("/shareOrder")
  public String shareOrder(HttpSession session){
	session.setAttribute("ma_head_nav", "client");
    return "manage/share-order";
  }
  
  @RequestMapping("/uiDemo")
  public String uiDemo(){
    return "manage/ui-demo";
  }
  
  // 版本列表
  @RequestMapping("/editionList")
  public String editionList(HttpSession session){
    session.setAttribute("ma_head_nav", "edition");
    session.setAttribute("ma_sider_nav", "edition");
    return "manage/edition-list";
  } 
  
  // 版本编辑
  @RequestMapping("/editionEdit")
  public String editionEdit(HttpSession session){
    session.setAttribute("ma_head_nav", "edition");
    session.setAttribute("ma_sider_nav", "edition");
    return "manage/edition-edit";
  }
  
  // 体验账户列表
  @RequestMapping("/experienceList")
  public String experienceList(HttpSession session){
    session.setAttribute("ma_head_nav", "client");
    session.setAttribute("ma_sider_nav", "experience");
    return "manage/client-experience-list";
  }
  
  /**
   * 跳转到商品导入
   */
  @RequestMapping("/toGoodsUpload")
  public String toView(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
    session.setAttribute("ma_head_nav", "client");
    session.setAttribute("ma_sider_nav", "client");
    return "manage/goods-upload";
  }
  
  /**
   * 登陆
   * @param username
   * @param password
   * @return
   */
  @RequestMapping(path = "/login", method = RequestMethod.POST)
  @ResponseBody
  public ResultData login(String username, String password,HttpSession session) {
    //参数校验
    if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
      return ResultData.build().parameterError();
    }
    User user = userService.login(username,password);
    //为空时返回用户名或密码错误
    if(user == null || user.getBelongs() != -1){
      return ResultData.build().setStatus(Status.LOGIN_FAIL);
    }
    session.setAttribute(SessionConstant.USER_SESSION_ADMIN, user);
    return ResultData.build().success();
  }
  
  /**
   * 注销
   * @param session
   * @return
   */
  @RequestMapping(path = "/logout", method = RequestMethod.POST)
  @ResponseBody
  public ResultData logout(HttpSession session){
    try {
      //删除session中的用户
      session.removeAttribute(SessionConstant.USER_SESSION_ADMIN);
      return ResultData.build().success();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResultData.build().error();
  }
  
}
