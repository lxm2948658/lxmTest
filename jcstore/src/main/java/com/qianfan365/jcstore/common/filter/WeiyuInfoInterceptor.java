package com.qianfan365.jcstore.common.filter;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.GsonBuilder;
import com.qianfan365.jcstore.common.bean.PageView;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.service.UserService;

public class WeiyuInfoInterceptor extends HandlerInterceptorAdapter {
  
  public static UserService userService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String username = request.getHeader("clientUsername");
    User user = userService.findByUsername(username);
    PageView.userThreadLocal.set(user);
    boolean flag = true;
    //登录的账号超出服务期限
    if (user==null||user.getStatus() == false || user.getExpiredTime().before(new Date())) {
      ResultData r = ResultData.build();
      r.setStatus(ResultData.Status.USER_STATUS_EXPIRED);
      PrintWriter w = response.getWriter();
      w.print( new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(r).toString());
      flag = false;
    }
    return flag;
  }
  
}
