package com.qianfan365.jcstore.main;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.qianfan365.jcstore.common.util.WeiYuRequst;
import com.qianfan365.jcstore.service.*;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.RestController;

import com.qianfan365.jcstore.common.filter.ModuleInterceptor;
import com.qianfan365.jcstore.common.filter.PermissionInterceptor;
import com.qianfan365.jcstore.common.filter.UserLoginInterceptor;
import com.qianfan365.jcstore.common.filter.WapLoginInterceptor;
import com.qianfan365.jcstore.common.filter.WeiyuInfoInterceptor;
import com.qianfan365.jcstore.common.util.FileUploadUtils;

@SpringBootApplication
@RestController
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.qianfan365.jcstore"})
@MapperScan("com.qianfan365.jcstore")
@EnableScheduling
public class StartUp implements ServletContextInitializer {


    @Value("${common.domain-url}")
    String domainUrl;
    @Value("${common.local.domain-url}")
    String localDomainUrl;

    @Value("${oss-endpoint}")
    String endpoint;
    @Value("${oss-accessKeyId}")
    String accessKeyId;
    @Value("${oss-accessKeySecret}")
    String accessKeySecret;
    @Value("${oss-bucketName}")
    String bucketName;
    @Value("${oss-domain}")
    String domain;
    @Value("${weiyu.server-url}")
    String wyServerUrl;

    @Value("${weiyu.readTimout}")
    Integer readTimout;
    @Value("${weiyu.connectTimeout}")
    Integer connectTimeout;

//  @Bean
//  public PlatformTransactionManager txManager(DataSource dataSource) {
//      return new DataSourceTransactionManager(dataSource);
//  }

    @Autowired
    PermissionInfoService permissionInfoService;
    @Autowired
    ContentService contentService;
    @Autowired
    UserService userService;
    @Autowired
    UserLoginService userLoginService;
    @Autowired
    CustomerService customerService;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(StartUp.class, args);
    }

    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setAttribute("domainUrl", domainUrl);
        servletContext.setAttribute("localDomainUrl", localDomainUrl);

        FileUploadUtils.endpoint = endpoint;
        FileUploadUtils.accessKeyId = accessKeyId;
        FileUploadUtils.accessKeySecret = accessKeySecret;
        FileUploadUtils.bucketName = bucketName;
        FileUploadUtils.domain = domain;
        FileUploadUtils.init();

        //微信服务接口访问地址
        WeiYuRequst.setWyServerUrl(wyServerUrl);
        WeiYuRequst.userService = userService;
        WeiYuRequst.readTimout=readTimout;
        WeiYuRequst.connectTimeout=connectTimeout;

        PermissionInterceptor.permissionInfoService = permissionInfoService;
        UserLoginInterceptor.userLoginService = userLoginService;
        WapLoginInterceptor.userLoginService = userLoginService;
        ModuleInterceptor.userService = userService;
        WeiyuInfoInterceptor.userService = userService;
        // 刷客户表数据
//    customerService.initCustomerInfo();
    }
}
