package com.qianfan365.jcstore.common.filter;

import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.pojo.User;

public class AdminLoginInterceptor extends HttpServlet implements HandlerInterceptor {

  private static final long serialVersionUID = 1L;

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setContentType("text/html; charset=UTF-8");

    User user = (User) req.getSession().getAttribute(SessionConstant.USER_SESSION_ADMIN);
    boolean flag = false;
    String domainUrl = request.getServletContext().getAttribute("domainUrl").toString();
    if (null != user) {
      flag = true;
    } else {
      // Session中没有用户名，将请求转发到错误页面或者登录页面
      /* 判断是不是ajax请求 ajax请求 返回700 */
      if (req.getHeader("x-requested-with") != null
          && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
        PrintWriter w = response.getWriter();
        ResultData r = ResultData.build();
        r.setStatus(ResultData.Status.JSON_NEED_LOGIN);
        w.print(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(r).toString());
        return false;
      } else {
        // 请求重定向
        //  更改下面的登录页面的链接地址
        resp.sendRedirect(domainUrl + "/manage/toLogin");
      }
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
