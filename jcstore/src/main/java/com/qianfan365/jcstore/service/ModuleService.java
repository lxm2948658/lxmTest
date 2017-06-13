package com.qianfan365.jcstore.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianfan365.jcstore.common.bean.ModuleBean;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.pojo.Module;
import com.qianfan365.jcstore.dao.inter.ModuleMapper;

@Service
public class ModuleService {

  @Autowired
  private ModuleMapper moduleMapper;

  /**
   * 生成树装结构的模块信息数据
   * 
   * @return
   */
  public List<ModuleBean> getModuleInfo() {
    List<Module> list = ModuleData.getModules();
    HashMap<Integer, ModuleBean> map = new HashMap<Integer, ModuleBean>();
    LinkedList<ModuleBean> linkedList = new LinkedList<ModuleBean>();
    for (Module moduleInfo : list) {
      ModuleBean bean = new ModuleBean(moduleInfo);
      map.put(moduleInfo.getId(), bean);
      if (moduleInfo.getPid() == 0) linkedList.addLast(bean);
    }
    for (Module moduleInfo : list) {
    	if (moduleInfo.getId()==3||moduleInfo.getId()==27) continue;//经营概况和定制商品的打印暂时不做,这里跳过好隐藏下
      if (moduleInfo.getPid() != 0) {
        ModuleBean bean = map.get(moduleInfo.getPid());
        if (bean != null) bean.add(map.get(moduleInfo.getId()));
      }
    }
    return linkedList;
  }

  /**
   * 所有模块信息的列表
   * 
   * @return
   */
  public HashMap<String, Module> getAll() {
    List<Module> list = ModuleData.getModules();
    HashMap<String, Module> hashMap = new HashMap<String, Module>();
    list.forEach(moduleInfo -> hashMap.put(String.valueOf(moduleInfo.getId()), moduleInfo));
    return hashMap;
  }

}
