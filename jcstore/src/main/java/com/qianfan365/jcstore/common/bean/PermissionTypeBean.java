package com.qianfan365.jcstore.common.bean;

import java.util.List;

public class PermissionTypeBean {
  private String type;
  private List<List<PermissionBean>> list;
  
  public PermissionTypeBean() {
  }
  
  public PermissionTypeBean(String str) {
    this.type = str;
  }
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public List<List<PermissionBean>> getList() {
    return list;
  }
  public void setList(List<List<PermissionBean>> list) {
    this.list = list;
  }
  
  
  
}
