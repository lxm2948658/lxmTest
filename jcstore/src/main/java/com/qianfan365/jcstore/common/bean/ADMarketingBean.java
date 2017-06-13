package com.qianfan365.jcstore.common.bean;


public class ADMarketingBean {

  private String fullScreenImg; // 全屏广告图片地址
  private String headAdvImg; // 头部广告图片地址
  private String headAdvUrl; // 头部广告链接地址
  private String topAdvImg; // 顶部广告图片地址
  private String topAdvUrl; // 顶部广告链接地址
  private String contactway; // 联系方式
  private String address; // 地址
  private String QRCode; // 二维码地址
  private String bottomAdvImg; // 底部广告图片地址
  private String bottomAdvUrl; // 底部广告链接地址
  private String path;

  public String getPath() {
    return path;
  }

  public String getFullScreenImg() {
    return fullScreenImg;
  }

  public void setFullScreenImg(String fullScreenImg) {
    this.fullScreenImg = fullScreenImg;
  }

  public String getHeadAdvImg() {
    return headAdvImg;
  }

  public void setHeadAdvImg(String headAdvImg) {
    this.headAdvImg = headAdvImg;
  }

  public String getHeadAdvUrl() {
    return headAdvUrl;
  }

  public void setHeadAdvUrl(String headAdvUrl) {
    this.headAdvUrl = headAdvUrl;
  }

  public String getTopAdvImg() {
    return topAdvImg;
  }

  public void setTopAdvImg(String topAdvImg) {
    this.topAdvImg = topAdvImg;
  }

  public String getTopAdvUrl() {
    return topAdvUrl;
  }

  public void setTopAdvUrl(String topAdvUrl) {
    this.topAdvUrl = topAdvUrl;
  }

  public String getContactway() {
    return contactway;
  }

  public void setContactway(String contactway) {
    this.contactway = contactway;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getQRCode() {
    return QRCode;
  }

  public void setQRCode(String qRCode) {
    QRCode = qRCode;
  }

  public String getBottomAdvImg() {
    return bottomAdvImg;
  }

  public void setBottomAdvImg(String bottomAdvImg) {
    this.bottomAdvImg = bottomAdvImg;
  }

  public String getBottomAdvUrl() {
    return bottomAdvUrl;
  }

  public void setBottomAdvUrl(String bottomAdvUrl) {
    this.bottomAdvUrl = bottomAdvUrl;
  }

  public void setPath(String path) {
    this.path = path;
    if (topAdvUrl == null) {
      topAdvUrl = path;
    }
    if (headAdvUrl == null) {
      headAdvUrl = path;
    }
    if (bottomAdvUrl == null) {
      bottomAdvUrl = path;
    }
  }

}
