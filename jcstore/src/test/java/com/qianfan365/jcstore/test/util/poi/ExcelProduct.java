package com.qianfan365.jcstore.test.util.poi;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;

public class ExcelProduct {
  private String name;

  private String code;

  private String barCode;

  private String groupName;
  
  private Integer groupId;

  private String standard;

  private Double costPrice;

  private Double salePrice;

  private Integer inventory;

  private Integer shopId;

  private Integer userId;

//  private String customDescription;

  private Date createtime;

  private Date updatetime;

  public ExcelProduct(List<String> values) {
    setName(values.get(0));
    setCode(values.get(1));
    setBarCode(values.get(2));
    setGroupName(values.get(3));
    setStandard(values.get(4));
    setCostPrice(NumberUtils.toDouble(values.get(5), 0.0));
    setSalePrice(NumberUtils.toDouble(values.get(6), 0.0));
    setInventory(NumberUtils.toInt(values.get(7), 0));
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getBarCode() {
    return barCode;
  }

  public void setBarCode(String barCode) {
    this.barCode = barCode;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public String getStandard() {
    return standard;
  }

  public void setStandard(String standard) {
    this.standard = standard;
  }

  public Double getCostPrice() {
    return costPrice;
  }

  public void setCostPrice(Double costPrice) {
    this.costPrice = costPrice;
  }

  public Double getSalePrice() {
    return salePrice;
  }

  public void setSalePrice(Double salePrice) {
    this.salePrice = salePrice;
  }

  public Integer getInventory() {
    return inventory;
  }

  public void setInventory(Integer inventory) {
    this.inventory = inventory;
  }

  public Integer getShopId() {
    return shopId;
  }

  public void setShopId(Integer shopId) {
    this.shopId = shopId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
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

  @Override
  public String toString() {
    return "ExcelProduct [name=" + name + ", code=" + code + ", barCode=" + barCode
        + ", groupName=" + groupName + ", groupId=" + groupId + ", standard=" + standard
        + ", costPrice=" + costPrice + ", salePrice=" + salePrice + ", inventory=" + inventory
        + ", shopId=" + shopId + ", userId=" + userId + ", createtime=" + createtime
        + ", updatetime=" + updatetime + "]";
  }
  
}
