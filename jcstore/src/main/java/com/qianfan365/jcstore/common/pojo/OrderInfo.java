package com.qianfan365.jcstore.common.pojo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderInfo implements Serializable {
    private Integer id;

    private Integer minimalFlag;

    private String number;

    private Integer customerId;

    private String customer;

    private String phone;

    private Integer orderStatus;

    private String contactWith;

    private Double total;

    private Double receiceAmoun;

    private Double amountAdvanced;

    private Double discount;

    private Integer userId;

    private Integer shopId;

    private String salesman;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
    private Date orderDate;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date expectedDeliveryDate;

    private String description;

    private String cancelReason;

    private Double refundingFee;

    private Double refundedFee;

    private Integer signatureFlag;

    private String signatureImg;

    private String invoiceImg;

    private String adjunctImg;

    private String proof;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getContactWith() {
        return contactWith;
    }

    public void setContactWith(String contactWith) {
        this.contactWith = contactWith == null ? null : contactWith.trim();
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getReceiceAmoun() {
        return receiceAmoun;
    }

    public void setReceiceAmoun(Double receiceAmoun) {
        this.receiceAmoun = receiceAmoun;
    }

    public Double getAmountAdvanced() {
        return amountAdvanced;
    }

    public void setAmountAdvanced(Double amountAdvanced) {
        this.amountAdvanced = amountAdvanced;
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
        this.salesman = salesman == null ? null : salesman.trim();
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason == null ? null : cancelReason.trim();
    }

    public Double getRefundingFee() {
        return refundingFee;
    }

    public void setRefundingFee(Double refundingFee) {
        this.refundingFee = refundingFee;
    }

    public Double getRefundedFee() {
        return refundedFee;
    }

    public void setRefundedFee(Double refundedFee) {
        this.refundedFee = refundedFee;
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
        this.signatureImg = signatureImg == null ? null : signatureImg.trim();
    }

    public String getInvoiceImg() {
        return invoiceImg;
    }

    public void setInvoiceImg(String invoiceImg) {
        this.invoiceImg = invoiceImg == null ? null : invoiceImg.trim();
    }

    public String getAdjunctImg() {
        return adjunctImg;
    }

    public void setAdjunctImg(String adjunctImg) {
        this.adjunctImg = adjunctImg == null ? null : adjunctImg.trim();
    }

    public String getProof() {
        return proof;
    }

    public void setProof(String proof) {
        this.proof = proof == null ? null : proof.trim();
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
        sb.append(", number=").append(number);
        sb.append(", customerId=").append(customerId);
        sb.append(", customer=").append(customer);
        sb.append(", phone=").append(phone);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", contactWith=").append(contactWith);
        sb.append(", total=").append(total);
        sb.append(", receiceAmoun=").append(receiceAmoun);
        sb.append(", amountAdvanced=").append(amountAdvanced);
        sb.append(", discount=").append(discount);
        sb.append(", userId=").append(userId);
        sb.append(", shopId=").append(shopId);
        sb.append(", salesman=").append(salesman);
        sb.append(", orderDate=").append(orderDate);
        sb.append(", expectedDeliveryDate=").append(expectedDeliveryDate);
        sb.append(", description=").append(description);
        sb.append(", cancelReason=").append(cancelReason);
        sb.append(", refundingFee=").append(refundingFee);
        sb.append(", refundedFee=").append(refundedFee);
        sb.append(", signatureFlag=").append(signatureFlag);
        sb.append(", signatureImg=").append(signatureImg);
        sb.append(", invoiceImg=").append(invoiceImg);
        sb.append(", adjunctImg=").append(adjunctImg);
        sb.append(", proof=").append(proof);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}