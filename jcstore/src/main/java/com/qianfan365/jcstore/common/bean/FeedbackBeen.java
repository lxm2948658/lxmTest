package com.qianfan365.jcstore.common.bean;

/**
 * 用于给后台返回反馈数据的bean
 * @author szz
 */
public class FeedbackBeen {

  private Integer id;

  private String username;

  private String systemVersion;

  private String phoneModel;

  private String networkEnvironment;

  private String phoneNumber;

  private String companyName;

  private String feedbackContent;

  private String createtime;
  
  private Integer serialNumber;

  public Integer getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(Integer serialNumber) {
    this.serialNumber = serialNumber;
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

  public String getSystemVersion() {
    return systemVersion;
  }

  public void setSystemVersion(String systemVersion) {
    this.systemVersion = systemVersion;
  }

  public String getPhoneModel() {
    return phoneModel;
  }

  public void setPhoneModel(String phoneModel) {
    this.phoneModel = phoneModel;
  }

  public String getNetworkEnvironment() {
    return networkEnvironment;
  }

  public void setNetworkEnvironment(String networkEnvironment) {
    this.networkEnvironment = networkEnvironment;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getFeedbackContent() {
    return feedbackContent;
  }

  public void setFeedbackContent(String feedbackContent) {
    this.feedbackContent = feedbackContent;
  }

  public String getCreatetime() {
    return createtime;
  }

  public void setCreatetime(String createtime) {
    this.createtime = createtime;
  }
  
}
