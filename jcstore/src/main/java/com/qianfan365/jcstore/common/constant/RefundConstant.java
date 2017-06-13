package com.qianfan365.jcstore.common.constant;

public class RefundConstant {
  
  public interface RefundStatus {
    /** 1-待完成 */
    int PENDING = 1;
    /** 2-已完成 */
    int FINISH = 2;
    /** 3-已作废 */
    int CANCEL = 3;
  }
}
