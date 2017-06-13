package com.qianfan365.jcstore.common.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Refund implements Serializable {
    private Integer id;

    private Integer minimalFlag;

    private Integer orderInfoId;

    private String number;

    private String refundNum;

    private Integer status;

    private Integer customerId;

    private String customer;

    private String phone;

    private String contactWith;

    private Double refundTotal;

    private Double receiceAmoun;

    private Double refundFee;

    private Integer userId;

    private String salesman;

    private Integer refundUid;

    private String refundSalesman;

    private String refundDescription;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expectedDate;

    private String invoiceImg;

    private Date createtime;

    private Date updatetime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinimalFlag() {
        return minimalFlag;
    }

    public void setMinimalFlag(Integer minimalFlag) {
        this.minimalFlag = minimalFlag;
    }

    public Integer getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(Integer orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum == null ? null : refundNum.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        this.customer = customer == null ? null : customer.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getContactWith() {
        return contactWith;
    }

    public void setContactWith(String contactWith) {
        this.contactWith = contactWith == null ? null : contactWith.trim();
    }

    public Double getRefundTotal() {
        return refundTotal;
    }

    public void setRefundTotal(Double refundTotal) {
        this.refundTotal = refundTotal;
    }

    public Double getReceiceAmoun() {
        return receiceAmoun;
    }

    public void setReceiceAmoun(Double receiceAmoun) {
        this.receiceAmoun = receiceAmoun;
    }

    public Double getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Double refundFee) {
        this.refundFee = refundFee;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSalesman() {
        return salesman;
    }

    public void setSalesman(String salesman) {
        this.salesman = salesman == null ? null : salesman.trim();
    }

    public Integer getRefundUid() {
        return refundUid;
    }

    public void setRefundUid(Integer refundUid) {
        this.refundUid = refundUid;
    }

    public String getRefundSalesman() {
        return refundSalesman;
    }

    public void setRefundSalesman(String refundSalesman) {
        this.refundSalesman = refundSalesman == null ? null : refundSalesman.trim();
    }

    public String getRefundDescription() {
        return refundDescription;
    }

    public void setRefundDescription(String refundDescription) {
        this.refundDescription = refundDescription == null ? null : refundDescription.trim();
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getInvoiceImg() {
        return invoiceImg;
    }

    public void setInvoiceImg(String invoiceImg) {
        this.invoiceImg = invoiceImg == null ? null : invoiceImg.trim();
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", minimalFlag=").append(minimalFlag);
        sb.append(", orderInfoId=").append(orderInfoId);
        sb.append(", number=").append(number);
        sb.append(", refundNum=").append(refundNum);
        sb.append(", status=").append(status);
        sb.append(", customerId=").append(customerId);
        sb.append(", customer=").append(customer);
        sb.append(", phone=").append(phone);
        sb.append(", contactWith=").append(contactWith);
        sb.append(", refundTotal=").append(refundTotal);
        sb.append(", receiceAmoun=").append(receiceAmoun);
        sb.append(", refundFee=").append(refundFee);
        sb.append(", userId=").append(userId);
        sb.append(", salesman=").append(salesman);
        sb.append(", refundUid=").append(refundUid);
        sb.append(", refundSalesman=").append(refundSalesman);
        sb.append(", refundDescription=").append(refundDescription);
        sb.append(", expectedDate=").append(expectedDate);
        sb.append(", invoiceImg=").append(invoiceImg);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}