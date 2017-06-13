package com.qianfan365.jcstore.common.constant;

public class OrderConstant {
  
  public interface OrderStatusConstant {
  	
  	/**
     * 0-订单预下单
     */
    int orderStatusPre = 0;
    
    /**
     * 1-订单待完成
     */
    int orderStatusNO = 1;
    
    /**
     * 2-订单已完成
     */
    int orderStatusYes = 2;
    
    /**
     * 3-订单已作废
     */
    int orderStatusCancel = 3;

  }
  
  public interface OrderType {
    /** 0-常规开单 */
    int ROUTINE = 0;
    /** 1-极简开单 */
    int MINIMA = 1;
    /** 2-定制开单 */
    int BESPOKE = 2;
  }
  
  public interface OrderQRCodeUrl{
    /** 查看二维码的url*/
    String url = "%s/mobile/order/certificate?oid=%s&proof=%s";
  }

}
