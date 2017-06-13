package com.qianfan365.jcstore.common.bean;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.qianfan365.jcstore.common.constant.OrderConstant.OrderType;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.Refund;
import com.qianfan365.jcstore.common.util.TimeUtils;

public class OrderListBean {
  
  private Integer id;          //订单id
  private String time;         //日期
  private String orderNumber;  //订单编号(订单号后十位)
  private Integer orderStatus; //订单状态
  private String productName;  //商品名称
  private Integer productNum;  //共几件
  private Double receiceAmoun; //实收金额
  private String customer;     //客户姓名
  private Boolean refundFlag;  //有无退货(退货单列表不用)
  private Integer status;      //退货单状态(订单列表不用)
  private Integer signatureFlag;//是否要签名(退货单列表不用)
  private Integer minimalFlag; //常规/极简开单
  private Boolean finish;      //有无完成按钮
  private Boolean againBuy;    //有无再次购买按钮(退货单列表不用)
  private String adjunctImg;
  private String[] adjunctImgs = {}; //附件图片(退货单列表不用,订单列表只有常规单用)

  public OrderListBean(OrderInfo orderInfo) {
    BeanUtils.copyProperties(orderInfo, this);
    this.time = TimeUtils.formatAsString(orderInfo.getUpdatetime(), "yyyy-MM-dd");
    this.orderNumber = orderInfo.getNumber().substring(10);
    if(orderInfo.getMinimalFlag() == OrderType.ROUTINE && StringUtils.isNotBlank(orderInfo.getAdjunctImg())){
    	this.adjunctImgs = orderInfo.getAdjunctImg().split(",");
    }else{
    	this.adjunctImg = null;
    }
  }
  
  public OrderListBean(Refund refund) {
    this.id = refund.getId();
    String refundNum = refund.getRefundNum();
    this.orderNumber = "TH"+refundNum.substring(refundNum.length()-8, refundNum.length());
    this.status = refund.getStatus();
    this.receiceAmoun = refund.getRefundFee();
    this.customer = refund.getCustomer();
    this.time = TimeUtils.formatAsString(refund.getUpdatetime(), "yyyy-MM-dd");
    this.minimalFlag = refund.getMinimalFlag();
  }

  
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

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Integer getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(Integer orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Integer getProductNum() {
    return productNum;
  }

  public void setProductNum(Integer productNum) {
    this.productNum = productNum;
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

  public Boolean getRefundFlag() {
    return refundFlag;
  }

  public void setRefundFlag(Boolean refundFlag) {
    this.refundFlag = refundFlag;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Integer getMinimalFlag() {
    return minimalFlag;
  }

  public void setMinimalFlag(Integer minimalFlag) {
    this.minimalFlag = minimalFlag;
  }
  
  public Integer getSignatureFlag() {
    return signatureFlag;
  }

  public void setSignatureFlag(Integer signatureFlag) {
    this.signatureFlag = signatureFlag;
  }

  public Boolean getFinish() {
    return finish;
  }

  public void setFinish(Boolean finish) {
    this.finish = finish;
  }

  public Boolean getAgainBuy() {
    return againBuy;
  }

  public void setAgainBuy(Boolean againBuy) {
    this.againBuy = againBuy;
  }

	public String getAdjunctImg() {
		return adjunctImg;
	}

	public void setAdjunctImg(String adjunctImg) {
		this.adjunctImg = adjunctImg;
	}

	public String[] getAdjunctImgs() {
		return adjunctImgs;
	}

	public void setAdjunctImgs(String[] adjunctImgs) {
		this.adjunctImgs = adjunctImgs;
	}
  
}
