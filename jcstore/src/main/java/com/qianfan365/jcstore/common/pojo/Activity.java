package com.qianfan365.jcstore.common.pojo;

import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable {
    private Integer id;

    private String name;

    private Integer type;

    private Integer shopId;

    private Date starton;

    private Date endon;

    private Integer status;

    private Integer everydayTimes;

    private Integer totalTimes;

    private Integer probalility;

    private String comment;

    private Date createTime;

    private Date updateTime;

    private Integer attendNumber;

    private Integer winningNumber;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEverydayTimes() {
        return everydayTimes;
    }

    public void setEverydayTimes(Integer everydayTimes) {
        this.everydayTimes = everydayTimes;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getProbalility() {
        return probalility;
    }

    public void setProbalility(Integer probalility) {
        this.probalility = probalility;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
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

    public Integer getAttendNumber() {
        return attendNumber;
    }

    public void setAttendNumber(Integer attendNumber) {
        this.attendNumber = attendNumber;
    }

    public Integer getWinningNumber() {
        return winningNumber;
    }

    public void setWinningNumber(Integer winningNumber) {
        this.winningNumber = winningNumber;
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
        sb.append(", type=").append(type);
        sb.append(", shopId=").append(shopId);
        sb.append(", starton=").append(starton);
        sb.append(", endon=").append(endon);
        sb.append(", status=").append(status);
        sb.append(", everydayTimes=").append(everydayTimes);
        sb.append(", totalTimes=").append(totalTimes);
        sb.append(", probalility=").append(probalility);
        sb.append(", comment=").append(comment);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", attendNumber=").append(attendNumber);
        sb.append(", winningNumber=").append(winningNumber);
        sb.append(", delete=").append(delete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}