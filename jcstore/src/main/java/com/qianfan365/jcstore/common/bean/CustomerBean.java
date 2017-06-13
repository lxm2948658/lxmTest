package com.qianfan365.jcstore.common.bean;

import com.qianfan365.jcstore.common.pojo.Customer;

public class CustomerBean extends Customer{

  /**
   * 
   */
  private static final long serialVersionUID = 7528342451826854872L;
  
  private String GroupName;     // 分组名称
  private Integer discount;      // 折扣
  private boolean groupFlag;
  
  public String getGroupName() {
    return GroupName;
  }

  public void setGroupName(String groupName) {
    GroupName = groupName;
  }

  public Integer getDiscount() {
    return discount;
  }

  public void setDiscount(Integer discount) {
    this.discount = discount;
  }

  public boolean isGroupFlag() {
    return groupFlag;
  }

  public void setGroupFlag(boolean groupFlag) {
    this.groupFlag = groupFlag;
  }

}
