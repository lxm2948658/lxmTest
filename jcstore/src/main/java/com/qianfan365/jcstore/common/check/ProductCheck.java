package com.qianfan365.jcstore.common.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.qianfan365.jcstore.common.pojo.Product;

/**
 * 商品校验
 * @author guanliyu
 *
 */
public class ProductCheck {

  public static Boolean saveProductCheck(Product product) {
    if (StringUtils.isBlank(product.getName()) || product.getName().length() > 60) {//长度不超过60个字未输入内容是显示提示信息
      return true;
    }
    String code="^[A-Za-z0-9,\\.~!@#$%^&\\*()_\\+\\-=|\\\\:;/?\\[\\]{}]{1,20}$";
    if(StringUtils.isNotEmpty(product.getCode()) && patter(code, product.getCode())){//必填项，只能输入英文、数字或符号，不超过20个字
      return true;
    }
    if(StringUtils.isNotEmpty(product.getStandard()) && product.getStandard().length()>20){//填项，不限字符类型，长度不超过20个字
      return true;
    }
    if(product.getCostPrice()!=null&&product.getCostPrice().toString().length()>9||(product.getCostPrice()!=null&&product.getCostPrice()<0)){//长度不超过8位数 转成String会有小数点所有大于9
      return true;
    }
    if(product.getSalePrice()==null||product.getSalePrice().toString().length()>9 ||product.getSalePrice()<0){//长度不超过8位数 
      return true;
    }
    String inventory= "^\\d+$";
    if(product.getInventory()!=null&&(patter(inventory, product.getInventory())||product.getInventory().toString().length()>6)){//库存
      return true;
    }
    // 条形码不为空时校验格式是否正确
    if(StringUtils.isNotEmpty(product.getBarCode()) && !product.getBarCode().matches("^[A-Za-z0-9]{1,20}$")){
      return true;
    }
    // 关键字不为空时校验格式是否正确^[A-Za-z\u4e00-\u9fa5]*$
    if(StringUtils.isNotEmpty(product.getKeyword()) && !product.getKeyword().matches("^[A-Za-z\\u4e00-\\u9fa5]{1,10}$")){
      return true;
    }
    //校验商品图片数量是否不超过5    
    String str = product.getImage();
    if(str != null && str.length()>0){
      String[] strArr = str.split(",");
      if(strArr.length>5){
        return true;
      }
    }
    return false;
  }
  
  private static Boolean patter(String regex ,String str){
    Pattern p = Pattern.compile(regex);
    Matcher m =p.matcher(str);
    if(!m.matches()){
      return true;
    }
    return false;
    }
  
  private static Boolean patter(String regex ,Integer str){
    Pattern p = Pattern.compile(regex);
    Matcher m =p.matcher(str.toString());
    if(!m.matches()){
      return true;
    }
    return false;
    }

}
