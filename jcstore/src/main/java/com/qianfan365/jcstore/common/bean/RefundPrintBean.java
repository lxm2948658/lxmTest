package com.qianfan365.jcstore.common.bean;

import java.util.Date;
import java.util.List;

import com.qianfan365.jcstore.common.pojo.RefundDetail;
import com.qianfan365.jcstore.common.util.TimeUtils;

public class RefundPrintBean {
  
  private Integer id;
  private Integer status;
  private String refundDate;
  private String refundNum;
  private Double refundFee;
  private List<RefundDetail> refundDetail;
  
  public RefundPrintBean(){
    
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
  public String getRefundDate() {
    return refundDate;
  }
  public void setRefundDate(String refundDate) {
    this.refundDate = refundDate;
  }
  public String getRefundNum() {
    return refundNum;
  }
  public void setRefundNum(String refundNum) {
    this.refundNum = refundNum;
  }
  public Double getRefundFee() {
    return refundFee;
  }
  public void setRefundFee(Double refundFee) {
    this.refundFee = refundFee;
  }
  public List<RefundDetail> getRefundDetail() {
    return refundDetail;
  }
  public void setRefundDetail(List<RefundDetail> refundDetail) {
    this.refundDetail = refundDetail;
  }

  public void setCreatetime(Date createtime) {
    this.refundDate = TimeUtils.formatAsString(createtime, "yyyy-MM-dd");
  }
  public void setRefund_num(String refund_num) {
    this.refundNum = "TH"+refund_num.substring(refund_num.length()-8, refund_num.length());
  }
  public void setRefund_fee(Double refund_fee) {
    this.refundFee = refund_fee;
  }
  
}
