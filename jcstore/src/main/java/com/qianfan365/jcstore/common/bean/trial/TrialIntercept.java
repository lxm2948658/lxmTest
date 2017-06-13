package com.qianfan365.jcstore.common.bean.trial;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.pojo.User;

/**
 * 试用账号权限校验 
 * @author szz
 */

@Aspect
@Configuration
public class TrialIntercept {

  @Around(value = "@annotation(com.qianfan365.jcstore.common.bean.trial.TrialUnavailable)")
  public Object beforeAdvice(ProceedingJoinPoint pjp) throws Throwable {
    ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
    HttpSession session=attr.getRequest().getSession(true);
    User user = (User) session.getAttribute("user_front");
    if(user != null && user.getIsTrialAccount()){
      return ResultData.build().setStatus(Status.TRIAL_ACCOUNT_NO_PERMISSION);
    }
    return pjp.proceed();
  }
  
}
