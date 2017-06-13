package com.qianfan365.jcstore.common.filter;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.service.UserService;

@Component
public class ModuleInterceptor extends HttpServlet implements HandlerInterceptor {

  private static final long serialVersionUID = 1L;
  public static UserService userService;
  
  @SuppressWarnings("unchecked")
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setContentType("text/html; charset=UTF-8");

    User user = (User) req.getSession().getAttribute(SessionConstant.FRONT_USER_SESSION);
    boolean flag = true;
    String[] moduleids = null;
    if (null != user) {
      HandlerMethod handler2=(HandlerMethod) handler;
      ModulePassport fireAuthority = handler2.getMethodAnnotation(ModulePassport.class);
      if(fireAuthority != null){
        user = userService.findUser(user.getId());
        if(fireAuthority.moduleids()!=null){
          moduleids = fireAuthority.moduleids();
          List<String> userModuleids = SoftTypeConstant.softMap().get(user.getSoftId());
          flag = userModuleids.containsAll(Lists.newArrayList(moduleids));
        }
        if(fireAuthority.setuser()){
          req.getSession().setAttribute(SessionConstant.FRONT_USER_SESSION, user);
        }
      }
    }
    if(!flag){
      PrintWriter w = response.getWriter();
      ResultData r;
      r = ResultData.build().setStatus(Status.MODULE_CHANGED);
      req.getSession().removeAttribute(SessionConstant.FRONT_USER_SESSION);
      req.getSession().removeAttribute("token");
      req.getSession().removeAttribute("permission");
      if (req.getAttribute("wap")==null || !req.getAttribute("wap").equals("true")) {
      	Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
      	w.print(gson.toJson(r).toString());
      } else {
      	// 请求重定向
      	String domainUrl = request.getServletContext().getAttribute("domainUrl").toString();
        resp.sendRedirect(domainUrl + "/mobile/abnormal?status="+r.get("status"));
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
