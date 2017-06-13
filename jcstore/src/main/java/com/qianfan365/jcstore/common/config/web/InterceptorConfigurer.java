package com.qianfan365.jcstore.common.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.qianfan365.jcstore.common.filter.AdminLoginInterceptor;
import com.qianfan365.jcstore.common.filter.CrossDomainInterceptor;
import com.qianfan365.jcstore.common.filter.ModuleInterceptor;
import com.qianfan365.jcstore.common.filter.ModuleWeiyuInterceptor;
import com.qianfan365.jcstore.common.filter.PermissionInterceptor;
import com.qianfan365.jcstore.common.filter.UserLoginInterceptor;
import com.qianfan365.jcstore.common.filter.WapLoginInterceptor;
import com.qianfan365.jcstore.common.filter.WeiyuInfoInterceptor;

@Configuration
public class InterceptorConfigurer extends WebMvcConfigurerAdapter {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new CrossDomainInterceptor()).addPathPatterns("/**");
    registry
        .addInterceptor(new AdminLoginInterceptor())
        .addPathPatterns("/manage/**")
        .excludePathPatterns("/manage/toLogin", "/manage/login", "/manage/logout",
            "/manage/toMLogin");
    registry
        .addInterceptor(new UserLoginInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/weiyu/**","/manage/**", "/upload/**", "/mobile/login", "/mobile/register",
            "/mobile/abnormal", "/toLogin", "/login", "/UI/**", "/error", "/uedit-config",
            "/uploadEditorFile", "/mobile/share/orderinfo", "/mobile/version/get",
            "/mobile/message/detail", "/mobile/user/tryOut", "/mobile/imagecode",
            "/mobile/checkCode", "/mobile/order/certificate", "/mobile/share/capture/proof",
            "/wap/**", "/mobile/marketing/shop", "/mobile/marketing/product",
            "/mobile/marketing/AD","/mobile/marketing/preview*");
    registry.addInterceptor(new WapLoginInterceptor()).addPathPatterns("/mobile/message/detail",
        "/mobile/marketing/preview*");
    registry.addInterceptor(new WeiyuInfoInterceptor()).addPathPatterns("/weiyu/**");
    registry.addInterceptor(new ModuleWeiyuInterceptor()).addPathPatterns("/weiyu/**");
    registry.addInterceptor(new ModuleInterceptor()).addPathPatterns("/mobile/**");
    registry
        .addInterceptor(new PermissionInterceptor())
        .addPathPatterns("/mobile/**")
        .excludePathPatterns("/mobile/login", "/mobile/register", "/mobile/abnormal",
            "/mobile/share/orderinfo", "/mobile/version/get", "/mobile/message/detail",
            "/mobile/user/tryOut", "/mobile/imagecode", "/mobile/checkCode");
    super.addInterceptors(registry);
  }
}
