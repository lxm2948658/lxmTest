package com.qianfan365.jcstore.common.bean;

import java.util.Date;

public class ShopOrderListBean {
  
  private Integer id;
  private String time;
  private Date createtime;
  private Date updatetime;
  private String orderNumber;
  private Integer type;
  private String img;
  private String productName;
  private Double receiceAmoun;
  private String customer;
  private Integer productKind;
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public String getTime() {
    return time;
  }
  public void setTime(String time) {
    this.time = time;
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
  public String getOrderNumber() {
    return orderNumber;
  }
  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }
  public Integer getType() {
    return type;
  }
  public void setType(Integer type) {
    this.type = type;
  }
  public String getImg() {
    return img;
  }
  public void setImg(String img) {
    this.img = img;
  }
  public String getProductName() {
    return productName;
  }
  public void setProductName(String productName) {
    this.productName = productName;
  }
  public Double getReceiceAmoun() {
    return receiceAmoun;
  }
  public void setReceiceAmoun(Double receiceAmoun) {
    this.receiceAmoun = receiceAmoun;
  }
  public String getCustomer() {
    return customer;
  }
  public void setCustomer(String customer) {
    this.customer = customer;
  }
  public Integer getProductKind() {
    return productKind;
  }
  public void setProductKind(Integer productKind) {
    this.productKind = productKind;
  }
  
}
