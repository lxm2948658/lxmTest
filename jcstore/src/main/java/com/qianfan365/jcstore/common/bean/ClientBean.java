package com.qianfan365.jcstore.common.bean;

import java.util.Date;

/**
 * 后台账户数据返回bean
 */
public class ClientBean{

  private String status;
  
  private Integer id;

  private String username;

  private String clientName;

  private String companyName;

  private String contactWay;

  private String type;
  
  private String softTypeName;

  private Date expiredTime;

  private String userNumber;

  private Integer uid;

  private Date createtime;

  private Date updatetime;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
  
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getContactWay() {
    return contactWay;
  }

  public void setContactWay(String contactWay) {
    this.contactWay = contactWay;
  }

  public Date getExpiredTime() {
    return expiredTime;
  }

  public void setExpiredTime(Date expiredTime) {
    this.expiredTime = expiredTime;
  }

  public String getUserNumber() {
    return userNumber;
  }

  public void setUserNumber(String userNumber) {
    this.userNumber = userNumber;
  }

  public Integer getUid() {
    return uid;
  }

  public void setUid(Integer uid) {
    this.uid = uid;
  }

  public Date getCreatetime() {
    return createtime;
  }

  public void setCreatetime(Date createtime) {
    this.createtime = createtime;
  }

  public Date getUpdatetime() {
    return updatetime;
  }

  public void setUpdatetime(Date updatetime) {
    this.updatetime = updatetime;
  }

  public String getSoftTypeName() {
    return softTypeName;
  }

  public void setSoftTypeName(String softTypeName) {
    this.softTypeName = softTypeName;
  }
  
}
