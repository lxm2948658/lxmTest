package com.qianfan365.jcstore.common.bean.trial;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记用于 试用账号不可用的且返回ResultData的接口
 * @author admin
 */

@Target({ElementType.METHOD})  
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface TrialUnavailable {

}
