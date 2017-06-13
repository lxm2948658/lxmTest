package com.qianfan365.jcstore.common.pojo;

import java.io.Serializable;
import java.util.Date;

public class OrderBespokeDetail implements Serializable {
    private Integer id;

    private Integer orderInfoId;

    private Integer customProductId;

    private String image;

    private String name;

    private String measureInfo;

    private Double budgetAppraisal;

    private Date createtime;

    private Date updatetime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderInfoId() {
        return orderInfoId;
    }

    public void setOrderInfoId(Integer orderInfoId) {
        this.orderInfoId = orderInfoId;
    }

    public Integer getCustomProductId() {
        return customProductId;
    }

    public void setCustomProductId(Integer customProductId) {
        this.customProductId = customProductId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMeasureInfo() {
        return measureInfo;
    }

    public void setMeasureInfo(String measureInfo) {
        this.measureInfo = measureInfo == null ? null : measureInfo.trim();
    }

    public Double getBudgetAppraisal() {
        return budgetAppraisal;
    }

    public void setBudgetAppraisal(Double budgetAppraisal) {
        this.budgetAppraisal = budgetAppraisal;
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
        sb.append(", orderInfoId=").append(orderInfoId);
        sb.append(", customProductId=").append(customProductId);
        sb.append(", image=").append(image);
        sb.append(", name=").append(name);
        sb.append(", measureInfo=").append(measureInfo);
        sb.append(", budgetAppraisal=").append(budgetAppraisal);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}