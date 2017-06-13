package com.qianfan365.jcstore.main;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * 打war包时候的启动类
 * @author pengbo
 *
 */
public class WarStartUp extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
      return application.sources(StartUp.class);
  }

}
