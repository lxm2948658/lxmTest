package com.qianfan365.jcstore.common.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;

/**
 * 
 * @author liuhaoran
 *
 */
public class MessageConstant {
  
  /**
   * 阅读标记的状态
   */
  public interface ReadFlag {
    /** 0-未读 */
    Integer UNREAD= 0;
    /** 1-已读 */
    Integer READ= 1;
  }
  
  /**
   * 接收标记的状态
   */
  public interface ReceiveFlag {
    /** 0-未接收 */
    Integer UNRECEIVE= 0;
    /** 1-已接收 */
    Integer RECEIVE= 1;
  }
  
  public interface MassCommand {
    String ALL_USER = "全部用户";
    String ALL_BUSINESS = "全部商家";
  }
  
  /**
   * 消息类型
   */
  public interface MessageType {
    /** 0-系统消息 */
    Integer SYSTEM_MESSAGE= 0;
    /** 1-下单成功 */
    Integer PLACE_AN_ORDER= 1;
    /** 2-上月概况统计 */
    Integer MONTHLY_STATISTICS= 2;
    /** 3-订单完成 */
    Integer ORDER_FINISH= 3;
    /** 4-服务内容变更 */
    Integer SERVICE_CHANGE= 4;
    /** 5-多设备登陆 */
    Integer OTHERS_LOGIN= 5;
    /** 6-服务将要到期 */
    Integer SERVICE_DUE= 6;
    /** 7-退货通知 */
    Integer REFUND_REMID= 7;
    /** 8-库存预警 */
    Integer INVENTORY_WARNING= 8;
    //todo 浩然不存,临时添加,用于页面开发,未加入其它相对数组等
    /** 9-商城订单下单成功*/
    Integer PLACE_AN_SHOPORDER= 9;

  }
  
  /**
   * 消息类别(是大的类别)
   */
  public interface MessageCategory {
    /** 1-订单助手 */
    Integer ORDER_HELPER = 1;
    /** 2-商品消息 */
    Integer PRODUCT_CATEGORY = 2;
    /** 3-系统消息 */
    Integer SYSTEM_CATEGORY = 3;
  }
  
  /**
   * 消息内容
   */
  public interface MessageInform {
    /** 1-下单成功 */
    String PLACE_AN_ORDER= "您有一个新的订单，订单编号为%s,请点击查看详情！";
    /** 2-上月概况统计 */
    String MONTHLY_STATISTICS= "%s年%s月的统计报表已经生成啦，请点击查看详情！";
    /** 3-订单完成 */
    String ORDER_FINISH= "您有一个订单已完成，订单编号为%s,请点击查看详情！";
    /** 4-服务内容变更 *///月分中包含tb 调用String.format时要加Locale.JAPAN参数 话说为啥只有日语环境才有不补零的月啊
    String SERVICE_CHANGE= "您的服务变更成功，软件类型：%s。使用人数：%s人。%s%s有效期截止至%tY年%<tb月%<te日。";
    /** 5-多设备登陆 */
    String OTHERS_LOGIN= "您的账号被其他设备登录，请及时处理或联系管理员！";
    /** 6-服务将要到期 */
    String SERVICE_DUE= "您开通的易开单服务还有15日到期，请及时续费，客服电话：022-58920649";
    /** 7-退货通知 */
    String REFUND_REMID= "您有一个新的退货单，退货单号为%s,请点击查看详情！";
    /** 8-库存预警 */
    String INVENTORY_WARNING = "您有一个商品库存过低，请及时补货";
    /** 9-商城订单下单成功 */
    String PLACE_AN_SHOPORDER= "您有一个新商城的订单，订单编号为%s,请点击查看详情！";
  }
  
  /**
   * 提醒内容
   */
  public interface PushInform {
    /** 1-下单成功 */
    String PLACE_AN_ORDER= "您有一个新的订单，请及时查看！";
    /** 2-上月概况统计 */
    String MONTHLY_STATISTICS= "%s年%s月的统计报表已经生成啦，请及时查看！";
    /** 3-订单完成 */
    String ORDER_FINISH= "您有一个订单已完成，请及时查看！";
    /** 4-服务内容变更 */
    String SERVICE_CHANGE= "您的服务已变更，请及时查看！";
    /** 5-多设备登陆 */
    String OTHERS_LOGIN= "您的账号被其他设备登录，请及时处理或联系管理员！";
    /** 6-服务将要到期 */
    String SERVICE_DUE= "您开通的易开单服务还有15日到期，请及时续费，客服电话：022-58920649";
    /** 7-退货通知 */
    String REFUND_REMID= "您有一个新的退货单，请及时查看！";
    /** 8-库存预警 */
    String INVENTORY_WARNING = "您有一个商品库存过低，请及时补货";
  }
  
  /**
   * 推送设置内容
   */
  private static List<Integer> pushlist = new ArrayList<Integer>() {
    private static final long serialVersionUID = 1L;
    {
      add(PermissionType.PLACE_ORDER_REMIND);
      add(PermissionType.ORDER_FINISH_REMIND);
      add(PermissionType.MONTHLY_STATISTICS_REMIND);
      add(PermissionType.REFUND_REMID);
      add(PermissionType.INVENTORY_WARNING_REMIND);
    }
  };
  public static List<Integer> pushlist(){
    return pushlist;
  }
  
  private static String pushstr = pushlist.toString().replace("[", "(").replace("]", ")");
  public static String pushstr(){
    return pushstr;
  }
  
  /**
   * 推送设置内容
   */
  private static Map<Integer, String> pushSound = new HashMap<Integer, String>() {
    private static final long serialVersionUID = 1L;
    {
      put(MessageType.PLACE_AN_ORDER, "place_an_order.caf");
      put(MessageType.ORDER_FINISH, "order_finish.caf");
      put(MessageType.MONTHLY_STATISTICS, "monthly_statistics.caf");
      put(MessageType.REFUND_REMID, "refund_remid.caf");
      put(MessageType.PLACE_AN_SHOPORDER, "place_an_order.caf");
    }
  };
  public static Map<Integer, String> pushSound(){
    return pushSound;
  }
  
  /**
   * 推送设置内容
   */
  private static Map<Integer, String> messageDetail = new HashMap<Integer, String>() {
    private static final long serialVersionUID = 1L;
    {
      put(MessageType.SYSTEM_MESSAGE, "网页");
      put(MessageType.PLACE_AN_ORDER, "订单详情");
      put(MessageType.MONTHLY_STATISTICS, null);
      put(MessageType.ORDER_FINISH, "订单详情");
      put(MessageType.SERVICE_CHANGE, null);
      put(MessageType.OTHERS_LOGIN, null);
      put(MessageType.SERVICE_DUE, null);
      put(MessageType.REFUND_REMID, "退货详情");
      put(MessageType.INVENTORY_WARNING, "商品详情");
      put(MessageType.PLACE_AN_SHOPORDER, "订单详情");
    }
  };
  public static Map<Integer, String> messageDetail(){
    return messageDetail;
  }
  
  /**
   * 消息类别包括消息类型
   */
 private static Map<Integer, List<Integer>> categoryDetail = new HashMap<Integer, List<Integer>>() {
   private static final long serialVersionUID = 1L;
   {
     put(MessageCategory.ORDER_HELPER, Lists.newArrayList(MessageType.PLACE_AN_ORDER,
                                                          MessageType.ORDER_FINISH,
                                                          MessageType.REFUND_REMID,
                                                          MessageType.PLACE_AN_SHOPORDER));
     put(MessageCategory.PRODUCT_CATEGORY, Lists.newArrayList(MessageType.INVENTORY_WARNING));
     put(MessageCategory.SYSTEM_CATEGORY, Lists.newArrayList(MessageType.SYSTEM_MESSAGE,
                                                             MessageType.MONTHLY_STATISTICS,
                                                             MessageType.SERVICE_CHANGE,
                                                             MessageType.OTHERS_LOGIN,
                                                             MessageType.SERVICE_DUE));
   }
 };
 public static Map<Integer, List<Integer>> categoryDetail(){
   return categoryDetail;
 }
  
}
