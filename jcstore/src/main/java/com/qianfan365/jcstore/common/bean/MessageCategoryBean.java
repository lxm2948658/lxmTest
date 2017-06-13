package com.qianfan365.jcstore.common.bean;

public class MessageCategoryBean {
  
  private Integer category;
  private boolean newMessage;
  private String dateStr;
  private long time;
  
  public Integer getCategory() {
    return category;
  }
  public void setCategory(Integer category) {
    this.category = category;
  }
  public boolean isNewMessage() {
    return newMessage;
  }
  public void setNewMessage(boolean newMessage) {
    this.newMessage = newMessage;
  }
  public String getDateStr() {
    return dateStr;
  }
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }
  public long getTime() {
    return time;
  }
  public void setTime(long time) {
    this.time = time;
  }
  
}
