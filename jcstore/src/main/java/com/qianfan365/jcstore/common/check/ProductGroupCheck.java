package com.qianfan365.jcstore.common.check;

import org.apache.commons.lang.StringUtils;

import com.qianfan365.jcstore.common.pojo.ProductGroup;

public class ProductGroupCheck {

  public static Boolean checkGroup(ProductGroup productGroup) {
    if(StringUtils.isBlank(productGroup.getName())||productGroup.getName().length()>20){
      return true;
    }
    return false;
  }

}
