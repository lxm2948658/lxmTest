package com.qianfan365.jcstore.common.pojo;

import java.io.Serializable;
import java.util.Date;

public class Coupon implements Serializable {
    private Integer id;

    private String name;

    private Integer unconditional;

    private Integer type;

    private Integer shopId;

    private Date starton;

    private Date endon;

    private Integer maximum;

    private Integer status;

    private String remark;

    private Date createTime;

    private Date updateTime;

    private Integer usedNumber;

    private Integer receiveNumber;

    private Integer couponId;

    private String mobile;

    private Integer circulation;

    private Double money;

    private Double requiredMoney;

    private Integer shareable;

    private String serial;

    private Integer used;

    private Integer delete;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getUnconditional() {
        return unconditional;
    }

    public void setUnconditional(Integer unconditional) {
        this.unconditional = unconditional;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Date getStarton() {
        return starton;
    }

    public void setStarton(Date starton) {
        this.starton = starton;
    }

    public Date getEndon() {
        return endon;
    }

    public void setEndon(Date endon) {
        this.endon = endon;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUsedNumber() {
        return usedNumber;
    }

    public void setUsedNumber(Integer usedNumber) {
        this.usedNumber = usedNumber;
    }

    public Integer getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(Integer receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public Integer getCouponId() {
        return couponId;
    }

    public void setCouponId(Integer couponId) {
        this.couponId = couponId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Integer getCirculation() {
        return circulation;
    }

    public void setCirculation(Integer circulation) {
        this.circulation = circulation;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getRequiredMoney() {
        return requiredMoney;
    }

    public void setRequiredMoney(Double requiredMoney) {
        this.requiredMoney = requiredMoney;
    }

    public Integer getShareable() {
        return shareable;
    }

    public void setShareable(Integer shareable) {
        this.shareable = shareable;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial == null ? null : serial.trim();
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", unconditional=").append(unconditional);
        sb.append(", type=").append(type);
        sb.append(", shopId=").append(shopId);
        sb.append(", starton=").append(starton);
        sb.append(", endon=").append(endon);
        sb.append(", maximum=").append(maximum);
        sb.append(", status=").append(status);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", usedNumber=").append(usedNumber);
        sb.append(", receiveNumber=").append(receiveNumber);
        sb.append(", couponId=").append(couponId);
        sb.append(", mobile=").append(mobile);
        sb.append(", circulation=").append(circulation);
        sb.append(", money=").append(money);
        sb.append(", requiredMoney=").append(requiredMoney);
        sb.append(", shareable=").append(shareable);
        sb.append(", serial=").append(serial);
        sb.append(", used=").append(used);
        sb.append(", delete=").append(delete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}