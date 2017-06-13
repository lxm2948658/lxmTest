package com.qianfan365.jcstore.common.bean;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.OrderInfoDetail;
import com.qianfan365.jcstore.common.util.TimeUtils;

public class OrderPrintBean {

  private Integer id;
  private Integer customerId;
  private String customer;
  private String phone;
  private Integer orderStatus;
  private Double receiceAmoun;
  private Double amountAdvanced;
  private Double discount;
  private Integer userId;
  private Integer shopId;
  private String description;
  private String cancelReason;
  private String orderNumber;// 订单号后十位
  private String orderDate;//订单时间
  private String expectedDeliveryDate;//预计退货时间
  private Integer signatureFlag;
  private String signatureImg;//签名图
  private String shopName;//店铺名称
  private String shopAddress;//店铺地址
  private List<OrderInfoDetail> detail;
  private List<RefundPrintBean> refundBean;
  private String announcements;//注意事项
  private String salesman;
  private String salesmanPhone;//业务员电话
  private String printTime;//打印时间


  public OrderPrintBean(OrderInfo orderInfo) {
    BeanUtils.copyProperties(orderInfo, this);
    this.orderNumber = orderInfo.getNumber().substring(10);
    this.orderDate = TimeUtils.formatAsString(orderInfo.getCreatetime(), "yyyy-MM-dd HH:mm");
    if(orderInfo.getExpectedDeliveryDate()!=null){
      this.expectedDeliveryDate = TimeUtils.formatAsString(orderInfo.getExpectedDeliveryDate(), "yyyy-MM-dd");
    }
    this.printTime = TimeUtils.formatAsString(new Date(), "yyyy-MM-dd HH:mm");
  }
  
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }
  public Double getAmountAdvanced() {
    return amountAdvanced;
  }
  public void setAmountAdvanced(Double amountAdvanced) {
    this.amountAdvanced = amountAdvanced;
  }
  public Integer getCustomerId() {
    return customerId;
  }
  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }
  public String getCustomer() {
    return customer;
  }
  public void setCustomer(String customer) {
    this.customer = customer;
  }
  public String getPhone() {
    return phone;
  }
  public void setPhone(String phone) {
    this.phone = phone;
  }
  public Integer getOrderStatus() {
    return orderStatus;
  }
  public void setOrderStatus(Integer orderStatus) {
    this.orderStatus = orderStatus;
  }
  public Double getReceiceAmoun() {
    return receiceAmoun;
  }
  public void setReceiceAmoun(Double receiceAmoun) {
    this.receiceAmoun = receiceAmoun;
  }
  public Double getDiscount() {
    return discount;
  }
  public void setDiscount(Double discount) {
    this.discount = discount;
  }
  public Integer getUserId() {
    return userId;
  }
  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  public Integer getShopId() {
    return shopId;
  }
  public void setShopId(Integer shopId) {
    this.shopId = shopId;
  }
  public String getSalesman() {
    return salesman;
  }
  public void setSalesman(String salesman) {
    this.salesman = salesman;
  }
  public String getSalesmanPhone() {
    return salesmanPhone;
  }
  public void setSalesmanPhone(String salesmanPhone) {
    this.salesmanPhone = salesmanPhone;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getCancelReason() {
    return cancelReason;
  }
  public void setCancelReason(String cancelReason) {
    this.cancelReason = cancelReason;
  }
  public Integer getSignatureFlag() {
    return signatureFlag;
  }
  public void setSignatureFlag(Integer signatureFlag) {
    this.signatureFlag = signatureFlag;
  }
  public String getSignatureImg() {
    return signatureImg;
  }
  public void setSignatureImg(String signatureImg) {
    this.signatureImg = signatureImg;
  }
  public String getOrderNumber() {
    return orderNumber;
  }
  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }
  public List<OrderInfoDetail> getDetail() {
    return detail;
  }
  public void setDetail(List<OrderInfoDetail> detail) {
    this.detail = detail;
  }
  public String getAnnouncements() {
    return announcements;
  }
  public void setAnnouncements(String announcements) {
    this.announcements = announcements;
  }
  public String getPrintTime() {
    return printTime;
  }
  public void setPrintTime(String printTime) {
    this.printTime = printTime;
  }
  public String getOrderDate() {
    return orderDate;
  }
  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }
  public String getExpectedDeliveryDate() {
    return expectedDeliveryDate;
  }
  public void setExpectedDeliveryDate(String expectedDeliveryDate) {
    this.expectedDeliveryDate = expectedDeliveryDate;
  }
  public List<RefundPrintBean> getRefundBean() {
    return refundBean;
  }
  public void setRefundBean(List<RefundPrintBean> refundBean) {
    this.refundBean = refundBean;
  }
  public String getShopName() {
    return shopName;
  }
  public void setShopName(String shopName) {
    this.shopName = shopName;
  }
  public String getShopAddress() {
    return shopAddress;
  }
  public void setShopAddress(String shopAddress) {
    this.shopAddress = shopAddress;
  }
}
