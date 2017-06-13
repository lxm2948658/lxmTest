package com.qianfan365.jcstore.common.bean;

public class OrderRefundBean {
  
  private Integer id;
  private Integer status;
  private String refundNum;
  
  public OrderRefundBean(){
    
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getRefundNum() {
    return refundNum;
  }

  public void setRefundNum(String refundNum) {
    this.refundNum = refundNum;
  }

  public void setRefund_num(String refund_num) {
    this.refundNum = "TH"+refund_num.substring(refund_num.length()-8, refund_num.length());
  }
  
}
