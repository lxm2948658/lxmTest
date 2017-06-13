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
import com.qianfan365.jcstore.common.bean.PageView;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.service.UserService;

@Component
public class ModuleWeiyuInterceptor extends HttpServlet implements HandlerInterceptor {

  private static final long serialVersionUID = 1L;
  public static UserService userService;
  
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setContentType("text/html; charset=UTF-8");

    boolean flag = true;
    User user = PageView.userThreadLocal.get();
    List<String> userModuleids = SoftTypeConstant.softMap().get(user.getSoftId());
    //先判断是否有【营销平台+微商城】模块
    flag = userModuleids.contains(ModuleType.MARKETING_PLATFORM_MICROMALL);
    //如果有再判断其他所需要的模块
    if(flag){
      String[] moduleids = null;
      HandlerMethod handler2=(HandlerMethod) handler;
      ModulePassport fireAuthority = handler2.getMethodAnnotation(ModulePassport.class);
      if(fireAuthority != null){
        if(fireAuthority.moduleids()!=null){
          moduleids = fireAuthority.moduleids();
          flag = userModuleids.containsAll(Lists.newArrayList(moduleids));
        }
      }
    }
    if(!flag){
      PrintWriter w = response.getWriter();
      ResultData r;
      r = ResultData.build().setStatus(Status.MODULE_CHANGED);
      Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
      w.print(gson.toJson(r).toString());
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
