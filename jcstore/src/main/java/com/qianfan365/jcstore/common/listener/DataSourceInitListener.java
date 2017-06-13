package com.qianfan365.jcstore.common.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.ModuleExample;
import com.qianfan365.jcstore.common.pojo.SoftTypeExample;
import com.qianfan365.jcstore.dao.inter.ModuleMapper;
import com.qianfan365.jcstore.dao.inter.SoftTypeMapper;

@Component
public class DataSourceInitListener implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private ModuleMapper moduleMapper;
  @Autowired
  private SoftTypeMapper softTypeMapper;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent ev) {
    // 防止重复执行
    if (ev.getApplicationContext().getParent() == null) {
      ModuleData.setModules(moduleMapper.selectByExample(new ModuleExample()));
      SoftTypeConstant.setSoftList(softTypeMapper.selectByExample(new SoftTypeExample()));
    }
  }

}
