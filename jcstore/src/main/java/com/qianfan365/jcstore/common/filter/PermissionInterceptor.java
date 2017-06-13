package com.qianfan365.jcstore.common.filter;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.service.PermissionInfoService;

@Component
public class PermissionInterceptor extends HttpServlet implements HandlerInterceptor {

  private static final long serialVersionUID = 1L;
  
  public static PermissionInfoService permissionInfoService;

  @SuppressWarnings("unchecked")
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setContentType("text/html; charset=UTF-8");

    User user = (User) req.getSession().getAttribute(SessionConstant.FRONT_USER_SESSION);
    boolean flag = true;
    int[] permissionids = null;
    
    if (null != user && user.getBelongs() > 0) {  // 管理员不进权限
//        List<Integer> permissionIds = (List<Integer>) req.getSession().getAttribute(SessionConstant.FRONT_USER_PERMISSION);
      
//        List<UserPermission> list = permissionInfoService.findByUid(user.getId());
      
      HandlerMethod handler2=(HandlerMethod) handler;
      PermissionPassport fireAuthority = handler2.getMethodAnnotation(PermissionPassport.class);
      if(fireAuthority != null){
        permissionids = fireAuthority.permissionids();
        
//          BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext()); 
//          setPermissionInfoService((PermissionInfoService)factory.getBean("permissionInfoService"));
        flag = permissionInfoService.checkPermission(user.getId(), permissionids);
      }
    }
    if(!flag){
      List<Integer> SessionPermission = (List<Integer>) req.getSession().getAttribute("permission");
      List<Integer> PermissionList = Arrays.stream(permissionids).boxed().collect(Collectors.toList());
      PermissionList.retainAll(SessionPermission);
      PrintWriter w = response.getWriter();
      ResultData r;
      if(PermissionList.size() != permissionids.length){
        r = ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
      }else{
        r = ResultData.build().setStatus(ResultData.Status.PERMISSION_CHANGED);
        req.getSession().removeAttribute(SessionConstant.FRONT_USER_SESSION);
        req.getSession().removeAttribute("token");
        req.getSession().removeAttribute("permission");
      }
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
