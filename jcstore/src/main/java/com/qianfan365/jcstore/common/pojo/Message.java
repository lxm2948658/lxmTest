package com.qianfan365.jcstore.common.pojo;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private Integer id;

    private Integer userId;

    private String content;

    private Integer linkParameter;

    private String link;

    private Integer messageType;

    private Integer label;    //库里定义(0-常规 1-极简 2-定制) //接口传到列表的定义(1-极简  0-非极简)(即1时没分享没打印 0时有分享打印看print字段)

    private Integer unread;

    private Integer homeRead;

    private Integer receive;

    private Date createtime;

    private Date updatetime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getLinkParameter() {
        return linkParameter;
    }

    public void setLinkParameter(Integer linkParameter) {
        this.linkParameter = linkParameter;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public Integer getUnread() {
        return unread;
    }

    public void setUnread(Integer unread) {
        this.unread = unread;
    }

    public Integer getHomeRead() {
        return homeRead;
    }

    public void setHomeRead(Integer homeRead) {
        this.homeRead = homeRead;
    }

    public Integer getReceive() {
        return receive;
    }

    public void setReceive(Integer receive) {
        this.receive = receive;
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
        sb.append(", userId=").append(userId);
        sb.append(", content=").append(content);
        sb.append(", linkParameter=").append(linkParameter);
        sb.append(", link=").append(link);
        sb.append(", messageType=").append(messageType);
        sb.append(", label=").append(label);
        sb.append(", unread=").append(unread);
        sb.append(", homeRead=").append(homeRead);
        sb.append(", receive=").append(receive);
        sb.append(", createtime=").append(createtime);
        sb.append(", updatetime=").append(updatetime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}