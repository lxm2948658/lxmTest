package com.qianfan365.jcstore.common.filter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 添加允许跨域
 */
public class CrossDomainInterceptor extends HttpServlet implements HandlerInterceptor {

	private static final long serialVersionUID = 1L;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setContentType("text/html; charset=UTF-8");
    resp.setHeader("Access-Control-Allow-Origin", "*");    
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    return;
    
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    return;
  }

	 

}
