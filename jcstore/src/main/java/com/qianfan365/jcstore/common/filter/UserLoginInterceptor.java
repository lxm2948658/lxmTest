package com.qianfan365.jcstore.common.filter;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserLogin;
import com.qianfan365.jcstore.service.UserLoginService;

@Component
public class UserLoginInterceptor extends HttpServlet implements HandlerInterceptor {

  private static final long serialVersionUID = 1L;

  public static UserLoginService userLoginService;

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setContentType("text/html; charset=UTF-8");

    User user = (User) req.getSession().getAttribute(SessionConstant.FRONT_USER_SESSION);
    String token = (String) req.getSession().getAttribute("token");
    boolean flag = false;
    ResultData r = ResultData.build();
    if (null != user) {
      // 校验当前用户
      UserLogin userlogin = userLoginService.findUser(user.getId(), token);
      if (userlogin == null) {
        // 用户已在其他设备登录
        req.getSession().removeAttribute(SessionConstant.FRONT_USER_SESSION);
        req.getSession().removeAttribute("token");
        req.getSession().removeAttribute("permission");
        r.setStatus(ResultData.Status.USER_PHONE_CHANGE);
      } else if(userlogin.getStatus() == false){
        // 用户已被删除
        userLoginService.delete(user.getId(), token);
        req.getSession().removeAttribute(SessionConstant.FRONT_USER_SESSION);
        req.getSession().removeAttribute("token");
        req.getSession().removeAttribute("permission");
        r.setStatus(ResultData.Status.USER_STATUS_DELETE);
      } else if (userlogin.getStatus() == true && userlogin.getExpiretime().after(new Date())) {
        flag = true;
      } else {
        // 用户已到期,不移除用户信息
        r.setStatus(ResultData.Status.USER_STATUS_EXPIRED);
      }
    } else {
      r.setStatus(ResultData.Status.JSON_NEED_LOGIN);
    }
    if(!flag){
      PrintWriter w = response.getWriter();
      w.print( new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(r).toString());
    }
    return flag;
  }

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    return;
  }

  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    return;
  }
}
