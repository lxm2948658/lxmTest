package com.qianfan365.jcstore.common.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.Assert;

import com.github.pagehelper.Page;

/**
 * Created by keithfu on 15/12/3.
 *
 * Controller层通用JSON数据返回格式类
 */
public class ResultData extends HashMap<String,Object>{
    /**
   * 
   */
  private static final long serialVersionUID = 1L;
    public static  final  String STATUS_KEY="statusDes";
    private static  final  String STATUS_CODE="status";
    public static  final  String STATUS_MSG="statusMsg";
    private static  final  String NOWTIME="nowTime";

    //状态码
    public static final int GROUPTHANMAX =  10098;
    public static final int PRODUCEEXIST =  10099;

    public enum Status{
        SUCCESS(1,"OK"),
        ERROR(11111,"服务器忙"),
        FAILURE(0,"FAIL"),
        JSON_NEED_LOGIN(700,"数据接口需要登录后才能访问"),
        SHOP_SHUT_DOWN(600,"店铺已经关停"),
        NO_PERMISSION(24444,"无操作权限"),
        PERMISSION_CHANGED(24445,"权限变更，请重新登录"),
        NO_VIEW_PERMISSION(24446,"您没有相关查看权限"), //这个是给消息通知相关跳转调用使的  不用重新登录
        MODULE_CHANGED(24450,"当前版本功能发生变更,请重新登录后使用"),
        /**
         *	接口需要登录
         */
        LOGIN_NEEDED(10000,"登录验证失败"),

        LOGIN_FAIL(10001,"用户名或密码错误，请重新输入"),

        REGIST_NAME_EXISTED(10010,"用户名已被注册"),

        REGIST_MOBILE_NOT_EXISTED(100101,"该手机号码未被注册，请核实"),

        REGIST_PHONE_EXISTED(10011,"手机号已被注册"),

        PHONE_NOT_EXISTED(10013,"手机号不存在"),

        OLD_PASSWORD_ERROR(10020,"旧密码错误"),

        USER_SATUS_FROZE(10030,"用户已被冻结"),
        
        USER_STATUS_EXPIRED(10031,"您所登录的账号已超出服务期限，请联系客服续费后使用"),
        
        USER_PHONE_CHANGE(10032,"您所登录的账号已在其它设备登录，请重新输入信息登录 "),

        USER_STATUS_DELETE(10033,"您所登录的账号已被删除，请重新输入信息登录"),
        
        USER_NUMBER_FULL(10034,"员工数量已达上限，不能添加更多员工信息了"),
        
        ENSITIVE_WORDS(10040,"信息包含违规内容"),

        PASSWORD_OLD_NEW_SAME(10015,"密码与旧密码相同"),

        PASSWORD_CONFIRM_ERROR(10016,"注册俩次密码输入不一致"),

        VERIFY_CODE_OVER_NUM(10014,"验证码超过次数"),

        VERIFY_CODE_FAIL(10012,"验证码验证失败"),

        PARAMETER_ERROR(10100,"参数错误"),

        DATA404(10086,"数据不存在"),

        NULL_MOBILE_CODE(10092,"请获取手机验证码"),

        GROUP_THAN_MAX(10098,"分组超过最大值"),

        PRODUCE_EXIST(10099,"此商品已存在"),

        PRODUCE_OUT_SHELF(10087,"商品已下架"),

        PRODUCE_NOT_ENOUGH(10088,"商品库存不足"),

        //目测此常量与上PRODUCE_NOT_ENOUGH没区别
        SHOPPING_CART_PRODUCE_NOT_ENOUGH(10402,"超过商品最大数量"),

        ADDRESS_EMPTY(10089,"收货地址不能为空"),

        VERIFY_CODE_OLD_GET(10090,"获取旧手机验证码"),

        VERIFY_CODE_OLD_NEW(10091,"请获取新手机验证码"),

        PHONE_NEEDED(10093,"手机号必填"),

        CONSIGNEE_NAME_NEEDED(10097,"收货人不能为空"),

        /**
         * 商品不存在已经被删除(我必须加个注释,我已经凌乱了)
         */
        PRODUCE_NOT_EXIST(2,"商品不存在"),
        
        PRODUCE_DEL(24443,"商品已被删除"),
        
        PRODUCT_BARCODE_EXIST(20201,"条形码已存在"),
        
        PRODUCE_NOT_EXIST_NOW(20202,"找不到商品"),
        
        PRODUCE_NOT_EXIST_GROUP(20203,"所选分组不存在"),
        
        PRODUCT_FORMAT_ERROR(20204,"商品导入未通过校验"),
        
        FILE_FORMAT_ERROR(20205,"文件格式错误"),
        
        IMPORT_NUMBER_OVERAGE(20206,"导入数量超出300条"),

        WITHDRAWAL_BANK_OVER_NUM(20100 ,"收款账户超过上限"),
        WITHDRAWAL_AMOUNT_LOWER(20101,"提现金额不能低于100元人民币"),
        WITHDRAWAL_AUDIT_STATUS_ERROR(20102,"该申请状态异常，无法操作"),
        WITHDRAWAL_CODE_ERRPR(20103,"提现密码错误"),
        WITHDRAWAL_CODE_MISS(20104,"您还没有设置提现密码，提现功能暂不可用"),
        WITHDRAWAL_BANK_ISEXIST(20105 ,"收款账户已存在"),

        SHOP_NAME_EXISTED(10403,"已有相同名字的店铺,请修改"),

        IMG_CODE_FAIL(10102,"图片验证码错误"),

        PRODUCE_AUDITING_LIST_DATA_UPDATED(10103,"待审核记录已发生变更，请刷新列表"),

        ORDER_STATUS_WRONG(10110,"订单状态不符，无法操作"),

        ORDER_REFUND_NOT_TODO(10112,"订单维权未解决，无法继续当前操作"),

        ORDER_STATUS_UPDATE(10111,"订单已更新为其他状态，无法继续当前操作"),

        ORDER_STATUS_CHANGE(10113,"您的操作已过期，请先刷新页面"),

        ORDER_CANCEL_OVER_NUM(10114,"当天订单取消数量超过5个，不允许主动取消订单"),

        ORDER_NO_PAY_OVER_NUM(10115,"当前待支付订单超过5个，不允许继续下单"),

        ORDER_RECEIVE_TIME_CHANGE(10116,"收货时间已延长，无法继续当前操作"),

        ORDER_REFUND_TRADENO_REPEATED(10120,"批量退款时，相同交易号的记录不能同时提交"),

        ORDER_REFUND_REPEATED(10121,"已有记录已操作退款，不能重复退款"),

        USER_THIRD_LOGIN_TIMEOUT(10101,"三方登录信息超时，请重新登录"),

        USER_THIRD_QQ_BOUND(10123,"该账号已经被其他QQ账号绑定，请先解绑QQ或绑定至其他账号"),

        USER_THIRD_WX_BOUND_ONE(101241,"该账号已绑定过微信账号，请先解绑对应的微信账号或绑定其他账号"),
        USER_THIRD_WX_BOUND_TWO(101242,"当前微信号已绑定过贝壳湾账号，请先解绑对应的贝壳湾账号或绑定其他账号"),

        USER_THIRD_WEIBO_BOUND_ONE(101251,"该账号已绑定新浪微博账号，请先解绑对应的微博账号或绑定其他账号"),
        USER_THIRD_WEIBO_BOUND_TWO(101252,"当前微博账号已绑定过贝壳湾账号，请先解绑对应的贝壳湾账号或绑定其他账号"),
        
        SHIPPING_ADDRESS_OVER_TEN(10200,"收货地址超过10个不能添加"),
        SHIPPING_PRODUCES_OVER_PAYLIMIT(10300,"您结算的商品价值过高，请分批下单"),
        SHOPPING_CART_OVER_NUM(10400,"您的购物车已满，请先去购物车下单购买一些商品，再重新添加新商品"),
        OPERATION_EXPIRED(10401,"您的操作已过期，请先刷新页面"),
        SHOP_STATUS_EXCEPTION(10501,"该商家状态异常，无法进行该操作"),
        SHOP_NOT_PUNISH(10502,"该商家目前状态无法进行处罚操作"),
        SHOP_MARGIN(10503,"商家保证金余额不足"),
        SHOP_NAME_NOTNULL(10504,"请填写店铺名称"),
        IMAGE_VERIFY_CODE_FAIL(20401,"验证码输入有误,请重新输入"),
        CUSTOM_NOT_FIND(25000,"客户不存在"),
        INVENTORY_SHORTAGE(25100,"库存不足"),
        FAILURE_ORDER_ALL_REFUND(25110,"操作失败，该订单无可退商品"),
        FAILURE_ORDER_IS_CANCEL(25111,"操作失败，该订单已作废"),
        FAILURE_REFUNDABLE_CHANGE(25112,"操作失败，该订单有商品可退数量发生变更"),
        DELIVERY_DATE_EARLY(25120,"交付日期不得早于当前时间"),
        
        TRIAL_ACCOUNT_NO_PERMISSION(10601,"试用账号权限不足!"),
        TRIAL_ACCOUNT_PRODUCT_NUM_BEYOND(106011,"试用账号添加商品数量超出!"),
        TRIAL_ACCOUNT_CUSTOMER_NUM_BEYOND(106012,"试用账号添加客户数量超出"),
        AT_LEAST_ONE_WAY_TO_ORDER(10700,"请至少选择一种开单形式"),
        AT_LEAST_A_CALCULATOR(10701,"请至少选择一种计算器"),
        LINK_FAILURE(10801,"无效的文章链接"),
        ARTICLE_PUBLISHED (10802,"该文章已发布过，您可以选择需要分享的平台"),
        LINK_TIMEOUT(10803,"请求超时，请稍后再试"),
        IMAGE_NUM_BEYOND(10901,"图片数量超出限制");
        public final Integer code;
        public final String msg;
        private final static Map<String,Status> data=new HashMap<>();
        Status(Integer code, String msg){
            this.code=code;
            this.msg=msg;
        }
        static {
            for(Status status:Status.values()){
                data.put(status.code.toString(),status);
            }
        }
        public static Status getStatusByCode(Integer code){
            Status status = data.get(code.toString());
            if(status==null){
                throw  new IllegalArgumentException("不存存文档所定义的状态值");
            }
            return status;
        }
    }
    /**
     * 参数异常
     * @return 当前实例
     */
    public  ResultData parameterError(){
        this.setStatus(Status.PARAMETER_ERROR);
        return this;
    }

    /**
     * 服务器忙
     * @return 当前实例
     */
    public  ResultData error(){
        this.setStatus(Status.ERROR);
        return this;
    }
    /**
     * OK
     * @return 当前实例
     */
    public  ResultData success(){
        this.setStatus(Status.SUCCESS);
        return this;
    }
    /**
     * FAILURE
     * @return 当前实例
     */
    public  ResultData failure(){
        this.setStatus(Status.FAILURE);
        return this;
    }
    /**
     * 验证码超过次数
     * @return 当前实例
     */
    public  ResultData verifyCodeOverNum(){
        this.setStatus(Status.VERIFY_CODE_OVER_NUM);
        return this;
    }

    /**
     * 验证码验证失败
     * @return 当前实例
     */
    public  ResultData verifyCodeFail(){
        this.setStatus(Status.VERIFY_CODE_FAIL);
        return this;
    }
    /**
     * 数据不存在
     * @return 当前实例
     */
    public  ResultData data404(){
        this.setStatus(Status.DATA404);
        return this;
    }
    /**
     * 验证码不存在,请获取手机验证码
     * @return 当前实例
     */
    public  ResultData nullMobileCode(){
        this.setStatus(Status.NULL_MOBILE_CODE);
        return this;
    }

    public ResultData(){
        //默认设置为操作成功状态
        super.put(STATUS_KEY,Status.SUCCESS);
        super.put(STATUS_CODE,Status.SUCCESS.code);
        super.put(STATUS_MSG,Status.SUCCESS.msg);
        super.put(NOWTIME,new Date());
    }

    /**
     * 设置响应状态
     * @param status 状态对象
     * @return 当前实例
     */
    public  ResultData setStatus(Status status){
        super.put(STATUS_KEY,status.toString().toLowerCase());
        super.put(STATUS_CODE,status.code);
        super.put(STATUS_MSG,status.msg);
        return  this;
    }

    /**
     * 允许使用int类型设置响应状态码，此方法请谨慎使用。</br>
     * @param status  响应码文档中所定义的code值
     * @return  响应码对应的Status对象,如果状态码不存在，会产生IllegalArgumentException
     */
    public  ResultData setStatus(Integer status){
        this.setStatus(Status.getStatusByCode(status));
        return  this;
    }

    /**
     * 添加分页数据，调整为文档通用的解析格式
     * @param page  分页数据对象
     * @return 实例本身
     */
    public ResultData parsePageBean(Page<?> page){
        super.put("sEcho", "");
        super.put("iTotalRecords", page.getTotal());
        super.put("iTotalDisplayRecords", page.getPageSize());
        super.put("aaData",page);
        return this;
    }
    
    /**
     * 添加列表数据（兼容分页数据），调整为文档通用的解析格式
     * @param list  列表数据对象
     * @return 实例本身
     */
    @SuppressWarnings("rawtypes")
    public ResultData parseList(List<?> list){
      if(list instanceof Page){
        return parsePageBean((Page<?>)list);
      }
      super.put("sEcho", "");
      super.put("aaData", list==null?new ArrayList():list);
      return this;
    }

    /**
     * 对map数据的扩展，允许使用己有map进行数据填充
     * @param map  外部map
     * @return 当前实例
     */
    public ResultData parseMap(Map<String,Object> map){
        super.putAll(map);
        return this;
    }

    /**
     * 产生默认的构造实例，默认添加选中状态为
     * @return 构造的实例
     */
    public static ResultData build(){
        return  new ResultData();
    }

    /**
     * 添加自定义 数据
     * @param key  key
     * @param value value
     * @return 当前实例
     */
    public ResultData put(String key, Object value) {
        if(STATUS_CODE.equals(key)){
          int code = NumberUtils.toInt(value.toString());
          try{
            this.setStatus(Status.getStatusByCode(code));
          }catch(Exception exception){
            //status值不是存在时，清空原有的status_key与status_msg值
            super.put(STATUS_CODE, code);
            super.put(STATUS_KEY,"");
            super.put(STATUS_MSG,"");
          }
        }else{
          super.put(key, value);
        }
        return this;
    }

    /**
     * 不太建议使用的接口,此接口仅限于自定义code 及msg显示,key可为pw
     * @param code  状态对应的CODE:如 100000
     * @param msg  状态对应的提示,如:登录验证失败
     * @param key 状态的英文名称,如 LOGIN_NEEDED ,可以为空,但建议最好带上一个值
     * @return
     */
    public ResultData put(Integer code,String msg,String key){
        Assert.notNull(code,"状态不能为空");
        Assert.notNull(msg,"描述不能为空");
        super.put(STATUS_CODE, code);
        super.put(STATUS_KEY,key);
        super.put(STATUS_MSG,msg);
        return  this;
    }

    /**
     * 将Bean 中的所有get方法的返回值,以方法名去除get后,首字母小写做为key,返回值为value存入当前 ResultData对象中
     *
     * @param object  各种Bean 如
     * @return
     */
    public ResultData parseBean(Object object){

      Method[] methods = object.getClass().getDeclaredMethods();
      for(int i=0;i<methods.length;i++){
        Method method = methods[i];
        String name = method.getName();
        if(name.startsWith("get")){
          try {
            name = name.replaceAll("get", "");
            name = name.replaceFirst(name.substring(0, 1),name.substring(0, 1).toLowerCase()) ;
            if(STATUS_CODE.equals(name)){
              name="state";
            }
            super.put(name, method.invoke(object));
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
           throw new RuntimeException(e);
          }
        }
      }
      return this;
    }
    
}
