package com.qianfan365.jcstore.common.pojo;

import java.io.Serializable;
import java.util.Date;

public class MessageManager implements Serializable {
    private Integer id;

    private String content;

    private String link;

    private Integer uid;

    private Date createtime;

    private String sendObjects;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getSendObjects() {
        return sendObjects;
    }

    public void setSendObjects(String sendObjects) {
        this.sendObjects = sendObjects == null ? null : sendObjects.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", content=").append(content);
        sb.append(", link=").append(link);
        sb.append(", uid=").append(uid);
        sb.append(", createtime=").append(createtime);
        sb.append(", sendObjects=").append(sendObjects);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}