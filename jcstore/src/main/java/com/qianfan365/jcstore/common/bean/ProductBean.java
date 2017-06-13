package com.qianfan365.jcstore.common.bean;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.qianfan365.jcstore.common.pojo.Product;

public class ProductBean extends Product {

  private static final long serialVersionUID = 1L;

  private String GroupName; // 分组名称
  
  private boolean groupFlag;

  private String[] images;

  public ProductBean(Product product) {
    BeanUtils.copyProperties(product, this);
    if (StringUtils.isNotEmpty(product.getImage())) {
      setImages(product.getImage().split(","));
      setImage(images[0]);
    }
  }

  public String getGroupName() {
    return GroupName;
  }

  public void setGroupName(String groupName) {
    GroupName = groupName;
  }

  public String[] getImages() {
    return images;
  }

  public void setImages(String[] images) {
    this.images = images;
  }

  public boolean isGroupFlag() {
    return groupFlag;
  }

  public void setGroupFlag(boolean groupFlag) {
    this.groupFlag = groupFlag;
  }

}
