package com.qianfan365.jcstore.common.bean;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.qianfan365.jcstore.common.pojo.OrderBespokeDetail;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.OrderInfoDetail;

public class OrderInfoBean extends OrderInfo {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  // 订单号后十位
  private String orderNumber;
  private List<OrderInfoDetail> detail;
  private Double detailNum = 0D;
  private List<OrderRefundBean> refundBean;
  private OrderBespokeDetail bespokeDetail;
  private String[] adjunctImgs = {};
  private String payNumber;
  private String payType;


  public OrderInfoBean(){
    
  }
  
  public OrderInfoBean(OrderInfo orderInfo) {
    BeanUtils.copyProperties(orderInfo, this);
    if(StringUtils.isNotBlank(orderInfo.getAdjunctImg())){
      this.adjunctImgs=orderInfo.getAdjunctImg().split(",");
    }
  }


  public Double getDetailNum() {
    return detailNum;
  }


  public void setDetailNum(Double detailNum) {
    this.detailNum = detailNum;
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

  public List<OrderRefundBean> getRefundBean() {
    return refundBean;
  }

  public void setRefundBean(List<OrderRefundBean> refundBean) {
    this.refundBean = refundBean;
  }

  public OrderBespokeDetail getBespokeDetail() {
		return bespokeDetail;
	}

	public void setBespokeDetail(OrderBespokeDetail bespokeDetail) {
		this.bespokeDetail = bespokeDetail;
	}

	public String[] getAdjunctImgs() {
    return adjunctImgs;
  }

  public void setAdjunctImgs(String[] adjunctImgs) {
    this.adjunctImgs = adjunctImgs;
  }

  public String getPayNumber() {
    return payNumber;
  }

  public void setPayNumber(String payNumber) {
    this.payNumber = payNumber;
  }

  public String getPayType() {
    return payType;
  }

  public void setPayType(String payType) {
    this.payType = payType;
  }
  
  

}
