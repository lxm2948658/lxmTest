package com.qianfan365.jcstore.common.bean;

import com.qianfan365.jcstore.common.constant.PermissionConstant;
import com.qianfan365.jcstore.common.pojo.PermissionInfo;

public class PermissionBean {
  private Integer id;
  private String name;
  private String type;
  private boolean flag;
  
  public PermissionBean(Integer pid,boolean falg){
    this.id = pid;
    this.name = PermissionConstant.permissionMap.get(pid).getName();
    this.flag = falg;
  }
  
  public PermissionBean(PermissionInfo info) {
    this.id = info.getId();
    this.name = info.getPermissionName();
    this.type = info.getPermissionType();
    this.flag = false;
  }
  
  public PermissionBean(Integer pid,String name,boolean falg){
    this.id = pid;
    this.name = name;
    this.flag = falg;
  }
  
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public boolean isFlag() {
    return flag;
  }
  public void setFlag(boolean flag) {
    this.flag = flag;
  }
  
  
}
