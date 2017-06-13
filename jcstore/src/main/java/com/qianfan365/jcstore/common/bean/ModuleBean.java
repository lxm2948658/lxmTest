package com.qianfan365.jcstore.common.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.qianfan365.jcstore.common.pojo.Module;

public class ModuleBean extends Module {
  
  private static final long serialVersionUID = 1L;

  public ModuleBean(Module moduleInfo){
    try {
      BeanUtils.copyProperties(this, moduleInfo);
    } catch (Exception e) {
    }
  }
  
  private List<ModuleBean> submodules = new ArrayList<ModuleBean>();
  
  public void add(ModuleBean moduleInfoBean){
    submodules.add(moduleInfoBean);
  }

  public List<ModuleBean> getSubmodules() {
    return submodules;
  }
  
}
