package com.qianfan365.jcstore.controller.mobile;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.UserLoginService;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping(path = "/mobile")
public class LoginMobileController extends BaseController {

  @Autowired
  private UserService userService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private UserLoginService userLoginService;
  

  /**
   * 登录接口
   * 
   * @param username
   * @param password
   * @param session
   * @return
   */
  @RequestMapping(path = "/login", method = RequestMethod.POST)
  @ResponseBody
  public ResultData login(String username, String password, String coding, HttpSession session) {
    ResultData map = ResultData.build();
    // 参数校验
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return ResultData.build().parameterError();
    }
    // 校验用户名是否存在
    if (userService.checkUsername(username)) {
      return ResultData.build().error().put("statusMsg", "用户名不存在，请检查输入是否正确");
    }
    // 判断用户是否存在
    User user = userService.login(username, password);
    // 判断登录是否成功
    if (user == null || user.getBelongs() == -1) {
      return ResultData.build().setStatus(Status.LOGIN_FAIL);
    }
    // 判断使用期限
    if (user.getExpiredTime().before(new Date())) {
      return ResultData.build().setStatus(ResultData.Status.USER_STATUS_EXPIRED);
    }
    // 用户信息存储至session
    session.setAttribute(SessionConstant.FRONT_USER_SESSION, user);
    // 添加登录信息
    String token = userLoginService.insert(user, coding);
    session.setAttribute("token", token);
    session.setAttribute("permission", permissionInfoService.findPidsByUid(user.getId()));
//    session.setAttribute("permission", permissionInfoService.findByUid(user.getId())
//      .stream().map(up -> up.getPid()).collect(Collectors.toList()));
    // 准备返回信息至移动端
    // 制空用户密码,不返回
    userService.getIndexResource(map,user);
    return ResultData.build().parseMap(map);
  }

  /**
   * 登出
   * 
   * @param session
   * @return
   */
  @RequestMapping(path = "/logout", method = RequestMethod.POST)
  @ResponseBody
  public ResultData logout(HttpSession session) {
    try {
      // 移除用户信息
      User user = this.getLoginUser(session);
      String token = (String) session.getAttribute("token");
      this.userLoginService.delete(user.getId(),token);
      session.removeAttribute(SessionConstant.FRONT_USER_SESSION);
      session.removeAttribute("token");
      session.removeAttribute("permission");
      return ResultData.build().success();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResultData.build().error();
  }
  
  /**
   * 跳wap的登录异常页
   * @param model
   * @param status
   * @return
   */
  @RequestMapping(value = "/abnormal", method = RequestMethod.GET)
  public String ToWapAbnormal(Model model, Integer status) {
    model.addAttribute("result", ResultData.build().setStatus(status));
    return "mobile/user_abnormal";
  }

}
