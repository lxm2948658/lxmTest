package com.qianfan365.jcstore.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qianfan365.jcstore.common.bean.MessageBean;
import com.qianfan365.jcstore.common.bean.MessageCategoryBean;
import com.qianfan365.jcstore.common.constant.MessageConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageCategory;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.MessageConstant.ReadFlag;
import com.qianfan365.jcstore.common.constant.MessageConstant.ReceiveFlag;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.Message;
import com.qianfan365.jcstore.common.pojo.MessageExample;
import com.qianfan365.jcstore.common.pojo.MessageExample.Criteria;
import com.qianfan365.jcstore.common.pojo.MessageManager;
import com.qianfan365.jcstore.common.pojo.MessageManagerExample;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.dao.inter.MessageManagerMapper;
import com.qianfan365.jcstore.dao.inter.MessageMapper;

/**
 * 消息操作相关
 * @author liuhaoran
 *
 */
@Service("messageService")
public class MessageService {
  
  @Value("${jiguang.appkey}")
  String appkey;
  @Value("${jiguang.master.secret}")
  String masterSecret;
  @Value("${jiguang.options}")
  boolean options;
  
  @Autowired
  private MessageMapper messageMapper;
  @Autowired
  private MessageManagerMapper messageManagerMapper;
  @Autowired
  private PermissionInfoService permissionService;
  @Autowired
  private UserService userService;
  @Autowired
  private UserLoginService userLoginService;
  
  private static Log logger = LogFactory.getLog(MessageService.class);
  
  /**
   * 批量保存通知消息
   * @param content 消息内容
   * @param pushinfo 推送内容
   * @param uplist 接收消息用户的权限集合
   * @param permissionId 权限编号
   * @param linkParameter 下单提醒&订单完成提醒时传订单id 退货提醒时传退货单id 库存预警时传商品id 上月经营概况提醒 时传年月如:201608
   * @param messageType 消息类型
   * @param uid  下单提醒  订单完成提醒 时传开单人的uid 其他情况传0
   * @param printModule TODO
   * @return
   */
  public int insertBatch(String content,String pushinfo, List<UserPermission> uplist, Integer permissionId,
                         Integer linkParameter, Integer messageType, Integer uid, boolean printModule){
    
    List<Message> list = new ArrayList<Message>();
    List<Integer> pushlist = new ArrayList<Integer>();
    List<Integer> soundlist = new ArrayList<Integer>();
    int num = 0;
    Date now = new Date();
    for (UserPermission up : uplist) {
      if(uid!=0 && (up.getPush()&2) > 0 && !uid.equals(up.getUid())){  //up.getPush()!=null
        //在下单提醒 、订单完成提醒中,权限是查看个人的,且订单不是本人所下的,则没有消息
        continue;
      }
      Message message = new Message();
      message.setContent(content);
      message.setLinkParameter(linkParameter);
      message.setMessageType(Math.abs(messageType));
      message.setLabel(permissionId>20?2:(messageType<0?1:0));
//      if(messageType == 2 && (up.getPush()&2) > 0){//判断是查看个人统计  消息类型置为-2
//        message.setMessageType(-messageType);
//      }
      message.setUserId(up.getUid());
      message.setCreatetime(now);
      message.setUpdatetime(now);
      list.add(message);
      if(up.getPid() == PermissionType.SERVICE_CHANGE_REMIND ||
          up.getPid() == PermissionType.SERVICE_DUE_REMIND){ //判断是要推送的 
        pushlist.add(up.getUid()); //这两种都是默认音
      }
      if((up.getPush()!=null && (up.getPush()&1)>0)){ //判断是要推送的 
        if((up.getPush()&4)>0 || messageType==MessageType.INVENTORY_WARNING){ //关了语音消息提醒的 (默认音)(库存预警没有自定义的语音)
          pushlist.add(up.getUid());
        }else{
          soundlist.add(up.getUid()); //有 自定义的 语音消息提醒
        }
      }
      if(list.size()>=30){
        num += messageMapper.insertBatch(list);
        list.clear();
        now = new Date();
      }
    }
    if(!list.isEmpty()){
      num += messageMapper.insertBatch(list);
    }
    
    if(!pushlist.isEmpty()){
      List<String> aliases = userLoginService.findAliases(pushlist);// 获取别名
      if(!aliases.isEmpty()){
        putPlural(aliases, pushinfo, messageType, false, linkParameter, printModule);// 向极光推送
      }
    }
    if(!soundlist.isEmpty()){
      List<String> aliasesSound = userLoginService.findAliases(soundlist);// 获取别名
      if(!aliasesSound.isEmpty()){
        putPlural(aliasesSound, pushinfo, messageType , true, linkParameter, printModule);// 向极光推送
      }
    }
    
    return num;
  }

  /**
   * 推送 复数的
   * @param aliases
   * @param pushinfo
   * @param messageType
   * @param sound
   * @param linkParameter 
   * @param printModule TODO
   */
  public void putPlural(List<String> aliases, String pushinfo, Integer messageType, boolean sound,
                        Integer linkParameter, boolean printModule) {
    // 推送通知
    if(aliases.size()>1000){
      int i = 0;
      while(i<aliases.size()){  //极光一次推送最多允许1000个别名
        int end = i+1000<aliases.size()?i+1000:aliases.size();
        push(aliases.subList(i, end), pushinfo, messageType, sound, linkParameter, printModule);
        i += 1000;
      }
    }else{
      push(aliases, pushinfo, messageType, sound, linkParameter, printModule);
    }
  }
  
  /**
   * 通知消息列表
   * @param currentPage
   * @param limit
   * @param uid
   * @param querytime
   * @return
   */
  public Page<MessageBean> findList(Integer currentPage, Integer limit, User user, Long querytime, Integer category){
    MessageExample example = new MessageExample();
    Criteria criteria = example.createCriteria();
    criteria.andUserIdEqualTo(user.getId());
    if(category!=null){
      List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
      List<Integer> types = Lists.newArrayList(MessageConstant.categoryDetail().get(category));
      if(category==MessageCategory.ORDER_HELPER && !mids.contains(ModuleType.ORDER_REFUND)){
        types.remove(MessageType.REFUND_REMID);
      }
      if(category==MessageCategory.SYSTEM_CATEGORY && !mids.contains(ModuleType.STATICS)){
        types.remove(MessageType.MONTHLY_STATISTICS);
      }
      criteria.andMessageTypeIn(types);
    }
    if(querytime != null){
      criteria.andCreatetimeLessThanOrEqualTo(new Date(querytime));
    }
    example.setOrderByClause(" createtime desc , id asc");
    PageHelper.startPage(currentPage, limit);
    Page<Message> page = (Page<Message>) messageMapper.selectByExample(example);
    Page<MessageBean> pageBean = new Page<MessageBean>();
    for (Message message:page) {
      MessageBean bean = new MessageBean(message);
      pageBean.add(bean);
    }
    //标记已读
    if(currentPage == 1){
      Message record = new Message();
      record.setUnread(ReadFlag.READ);
      record.setHomeRead(ReadFlag.READ);
      record.setUpdatetime(new Date());
      criteria.andUnreadEqualTo(ReadFlag.UNREAD);
      messageMapper.updateByExampleSelective(record, example);
    }
    return pageBean;
  }
  
  /**
   * 通知消息列表
   * @param category
   * @return
   */
  public List<MessageCategoryBean> findCategory(Integer uid, List<String> mids){
    List<MessageCategoryBean> listBean = new ArrayList<MessageCategoryBean>();
    Map<Integer, List<Integer>> categoryDetail = Maps.newHashMap(MessageConstant.categoryDetail());
    if(!mids.contains(ModuleType.ORDER_INFO)){
      categoryDetail.remove(MessageCategory.ORDER_HELPER);
    }
    if(!mids.contains(ModuleType.PRODUCT)){
      categoryDetail.remove(MessageCategory.PRODUCT_CATEGORY);
    }
    for(Integer category : categoryDetail.keySet()){
//      List<Integer> list = categoryDetail.get(category);
      List<Integer> types = Lists.newArrayList(categoryDetail.get(category));
      if(category==MessageCategory.ORDER_HELPER && !mids.contains(ModuleType.ORDER_REFUND)){
        types.remove(MessageType.REFUND_REMID);
      }
      if(category==MessageCategory.SYSTEM_CATEGORY && !mids.contains(ModuleType.STATICS)){
        types.remove(MessageType.MONTHLY_STATISTICS);
      }
      MessageExample example = new MessageExample();
      Criteria criteria = example.createCriteria();
      criteria.andUserIdEqualTo(uid).andMessageTypeIn(types);
      example.setOrderByClause(" createtime desc , id desc");
      PageHelper.startPage(1, 1);
      List<Message> page = messageMapper.selectByExample(example);
      criteria.andUnreadEqualTo(ReadFlag.UNREAD);
      PageHelper.startPage(1, 1);
      List<Message> unreadPage = messageMapper.selectByExample(example);
      MessageCategoryBean categoryBean = new MessageCategoryBean();
      categoryBean.setCategory(category);
      categoryBean.setNewMessage(!unreadPage.isEmpty());
      if(page.isEmpty()){
        categoryBean.setDateStr("");
        categoryBean.setTime(0);
      }else{
        Date date = page.get(0).getCreatetime();
        String dateStr = TimeUtils.formatAsString(date, "yyyy-MM-dd");
        categoryBean.setDateStr(dateStr);
        categoryBean.setTime(date.getTime());
      }
      listBean.add(categoryBean);
    }
    listBean.sort((x,y) -> {int c = -Long.compare(x.getTime(),y.getTime());
            if(c==0)c=Integer.compare(x.getCategory(),y.getCategory()); return c;});
    //标记首页已读
    Message record = new Message();
    record.setHomeRead(ReadFlag.READ);
    record.setUpdatetime(new Date());
    MessageExample example = new MessageExample();
    example.createCriteria().andUserIdEqualTo(uid).andUnreadEqualTo(ReadFlag.UNREAD);
    messageMapper.updateByExampleSelective(record, example);
    return listBean;
  }
  
  /**
   * 删除单个通知消息
   * @param uid 用户id
   * @param id 消息id
   * @return
   */
  public int delMessage(Integer uid, Integer id) {
    MessageExample example = new MessageExample();
    example.createCriteria().andUserIdEqualTo(uid).andIdEqualTo(id);
    return messageMapper.deleteByExample(example);
  }
  
  /**
   * 按类别删除通知消息
   * @param uid 用户id
   * @param category 消息类别
   * @return
   */
  public int delCategoryMessage(Integer uid, Integer category) {
    MessageExample example = new MessageExample();
    example.createCriteria().andUserIdEqualTo(uid).andMessageTypeIn(MessageConstant.categoryDetail().get(category));
    return messageMapper.deleteByExample(example);
  }
  
  /**
   * 根据店主id删除权限
   * @param belongs
   * @return
   */
  public int delByBelongs(Integer belongs) {
    List<Integer> uidlist = userService.findUserBelongId(belongs)
        .stream().map(u -> u.getId()).collect(Collectors.toList());
    uidlist.add(belongs);
    MessageExample example = new MessageExample();
    example.createCriteria().andUserIdIn(uidlist);
    return messageMapper.deleteByExample(example);
  }

  /**
   * 有无新消息
   * @param uid 用户id
   * @return
   */
  public boolean newMessageFlag(Integer uid){
    MessageExample example = new MessageExample();
    Criteria criteria = example.createCriteria();
    criteria.andUserIdEqualTo(uid).andHomeReadEqualTo(ReadFlag.UNREAD);
    return messageMapper.countByExample(example)>0;
  }
  
  /**
   * 存入单个通知消息(目前就多设备登陆用)
   * @param content 权限内容
   * @param pushinfo 推送内容
   * @param userId 用户id
   * @param messageType 权限类型
   * @param aliases 推送别名
   * @return
   */
  public int insertMessage(String content, String pushinfo, Integer userId, Integer messageType, String aliases){
    Message message = new Message();
    message.setUserId(userId);
    message.setContent(content);
    message.setMessageType(messageType);
    Date date = new Date();
    message.setCreatetime(date);
    message.setUpdatetime(date);
    push(Lists.newArrayList(aliases), pushinfo, messageType, false, null, false);
    return messageMapper.insertSelective(message);
  }
  
  /**
   * 向极光推送消息
   * @param aliases
   * @param content
   * @param extras
   * @param sound
   * @param linkParameter
   * @param printModule TODO
   */
  public void push(List<String> aliases, String content, Integer extras, boolean sound, Integer linkParameter,
                   boolean printModule) {
    JPushClient jpushClient = new JPushClient(masterSecret, appkey);

    // For push, all you need do is to build PushPayload object.
    PushPayload payload = buildPushObject(aliases, content, extras ,linkParameter, sound, printModule);
//    sendPush(jpushClient, payload);
    boolean flag = true;
    try {
      PushResult result = jpushClient.sendPush(payload);  //正常调用
//      PushResult result = jpushClient.sendPushValidate(payload); //验证用
      logger.info("Got result - " + result);
      if(result.getResponseCode() < 300){
        flag = false;
      }
    } catch (APIConnectionException e) {
        // Connection error, should retry later
      logger.error("连接错误,应该稍后重试", e);
  
    } catch (APIRequestException e) {
        // Should review the error, and fix the request
      if(e.getErrorCode() == 1011){  //TODO 别名错误返回这个，暂时先不打印具体信息
        logger.info("Error:应该检查错误,并修复请求");
      }else{
        logger.error("应该检查错误,并修复请求", e);
      }
      logger.info("HTTP Status: " + e.getStatus());
      logger.info("Error Code: " + e.getErrorCode());
      logger.info("Error Message: " + e.getErrorMessage());
    } finally {
      if(flag){
        logger.info("推送出异常的推送内容：");
        logger.info("发送对象别名:" + aliases.toString());
        logger.info("消息内容：" + content);
        logger.info("消息类型:" + extras);
      }
    }
    
  }
  
  /**
   * 推送消息
   * @param jpushClient
   * @param payload
   */
  @Deprecated
  public void sendPush(JPushClient jpushClient, PushPayload payload){
    try {
//      PushResult result = jpushClient.sendPush(payload);  //正常调用
      PushResult result = jpushClient.sendPushValidate(payload); //验证用
      logger.info("Got result - " + result);
//      if(result.getResponseCode() == 429){ //极光限制每分钟掉600次接口 返回429是频率过高
//        // ???在上月统计和服务到期时使用？
//        Thread.sleep(result.getRateLimitReset()*1000+1000);//等待距离时间窗口重置剩余的时间
//        sendPush(jpushClient, payload);
//      }
//    } catch (InterruptedException e) {
//      //  Auto-generated catch block
//      e.printStackTrace();
    } catch (APIConnectionException e) {
        // Connection error, should retry later
      logger.error("连接错误,应该稍后重试", e);
  
    } catch (APIRequestException e) {
        // Should review the error, and fix the request
      logger.error("应该检查错误,并修复请求", e);
      logger.info("HTTP Status: " + e.getStatus());
      logger.info("Error Code: " + e.getErrorCode());
      logger.info("Error Message: " + e.getErrorMessage());
    }
  }
  
  /**
   * 构建推送对象
   * @param aliases
   * @param content
   * @param extras
   * @param printModule TODO
   * @return
   */
  public PushPayload buildPushObject(List<String> aliases, String content, Integer extras, Integer linkParameter,
                                     boolean soundFlag, boolean printModule) {
    Integer label = extras<0?1:0;
    extras = Math.abs(extras);
    String soundIos = soundFlag?MessageConstant.pushSound().get(extras):"1.caf";
    Integer soundAnd = soundFlag?extras:0;
    String detailName = MessageConstant.messageDetail().get(extras);
    return PushPayload.newBuilder()
            .setPlatform(Platform.all())
            .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.alias(aliases)).build())
            .setNotification(Notification.newBuilder()
              .addPlatformNotification(IosNotification.newBuilder()
                .setAlert(content).setCategory(extras+"").setSound(soundIos).addExtra("type", extras)
                .addExtra("detailName", detailName).addExtra("detailFalg", detailName!=null)
                .addExtra("linkParameter", linkParameter).addExtra("label", label)
                .addExtra("print", printModule).build())
              .addPlatformNotification(AndroidNotification.newBuilder()
                .setAlert(content).addExtra("type", extras).addExtra("sound", soundAnd)
                .addExtra("detailName", detailName).addExtra("detailFalg", detailName!=null)
                .addExtra("linkParameter", linkParameter).addExtra("label", label)
                .addExtra("print", printModule).build())
              .build())
            .setOptions(Options.newBuilder().setApnsProduction(options).build())
            .build();
  }
  
//  /**
//   * 构建自定义声音的推送对象
//   * @param aliases
//   * @param content
//   * @param extras
//   * @return
//   */
//  public static PushPayload buildSoundPushObject(List<String> aliases, String content, Integer extras, Integer linkParameter) {
//    String sound = MessageConstant.pushSound.get(extras);
//    return PushPayload.newBuilder()
//            .setPlatform(Platform.all())
//            .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.alias(aliases)).build())
////            .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.registrationId(aliases)).build())
//            .setNotification(Notification.newBuilder()
//              .addPlatformNotification(IosNotification.newBuilder()
//                .setAlert(content).setCategory(extras+"").setSound(sound).addExtra("type", extras).addExtra("linkParameter", linkParameter).build())
//              .addPlatformNotification(AndroidNotification.newBuilder()
//                .setAlert(content).addExtra("type", extras).addExtra("sound", extras).addExtra("linkParameter", linkParameter).build())
//              .build())
//            .setOptions(Options.newBuilder().setApnsProduction(options).build())
//            .build();
//  }
  
  /**
   * 批量保存系统发的通知消息
   * 
   * @param set
   * @param content
   * @return
   */
  public int insertBatch(Set<Integer> set,MessageManager messageManager,Integer messageType){
    List<Message> list = new ArrayList<Message>();
    int num = 0;
    Date now = new Date();
    for (Integer uid : set) {
      Message notification = new Message();
      notification.setContent(messageManager.getContent());
      notification.setLinkParameter(messageManager.getId());
      notification.setLink(messageManager.getLink());
      notification.setUserId(uid);
      notification.setMessageType(messageType);
      notification.setLabel(0);
      notification.setCreatetime(now);
      notification.setUpdatetime(now);
      list.add(notification);
      if(list.size()>=30){
        num += messageMapper.insertBatch(list);
        list.clear();
        now = new Date();
      }
    }
    if(!list.isEmpty()){
      num += messageMapper.insertBatch(list);
    }
    return num;
  }
  
  /**
   * 存入消息管理信息
   * @param message
   * @return
   */
  public int insertMessageManager(MessageManager message){
    Date date = new Date();
    message.setCreatetime(date);
    return messageManagerMapper.insertSelective(message);
  }
  
  /**
   * 消息管理列表
   * @param currentPage
   * @param limit
   * @return
   */
  public Page<MessageManager> managerList(Integer currentPage, Integer limit){
    PageHelper.startPage(currentPage, limit);
    MessageManagerExample example = new MessageManagerExample();
    example.setOrderByClause(" createtime desc , id asc");
    return (Page<MessageManager>) messageManagerMapper.selectByExampleWithBLOBs(example);
  }
  
  /**
   * 标记已接受
   * @param uid
   * @return
   */
  public int tabReceive(Integer uid){
    MessageExample example = new MessageExample();
    example.createCriteria().andUserIdEqualTo(uid).andMessageTypeEqualTo(MessageType.SYSTEM_MESSAGE).andReceiveEqualTo(ReceiveFlag.UNRECEIVE);
    Message record = new Message();
    record.setReceive(ReceiveFlag.RECEIVE);
    record.setUpdatetime(new Date());
    return messageMapper.updateByExampleSelective(record, example);
  }
  
  /**
   * 获取新系统消息
   * @param uid 用户id
   * @return
   */
  public String newSystemMessage(Integer uid){
    MessageExample example = new MessageExample();
    Criteria criteria = example.createCriteria();
    criteria.andUserIdEqualTo(uid).andReceiveEqualTo(ReceiveFlag.UNRECEIVE)
      .andUnreadEqualTo(ReadFlag.UNREAD).andMessageTypeEqualTo(MessageType.SYSTEM_MESSAGE);
    example.setOrderByClause(" createtime desc,id desc");
    List<Message> messages = messageMapper.selectByExample(example);
    if(!messages.isEmpty()){
      tabReceive(uid); //标记已接受
    }
    return messages.isEmpty()?"":messages.get(0).getContent();
  }
  
  /**
   * 标记单条消息已读 (从通知栏点的)
   * @param uid
   * @param type
   * @param linkParameter
   */
  public void tabRead(Integer uid, Integer type, Integer linkParameter){
    MessageExample example = new MessageExample();
    example.createCriteria().andUnreadEqualTo(ReadFlag.UNREAD).andUserIdEqualTo(uid)
      .andMessageTypeEqualTo(type).andLinkParameterEqualTo(linkParameter);
    example.setOrderByClause(" createtime desc , id asc");
    List<Message> list = messageMapper.selectByExample(example);
    if(list.isEmpty()){
      return;
    }
    Message record = list.get(0);//这样是为了库存提醒的只改一条
    record.setUnread(ReadFlag.READ);
    record.setHomeRead(ReadFlag.READ);
    record.setUpdatetime(new Date());
    messageMapper.updateByPrimaryKeySelective(record);
  }
  
//  
//  
//  public static void main(String[] args) {
//    JPushClient jpushClient = new JPushClient("254a5e492c90f59823558f8f", "ed3f9c75bcc85bc01788e6aa");
//
//    // For push, all you need do is to build PushPayload object.//141fe1da9ea37835e75//171976fa8a88607cfcf
//    PushPayload payload = buildPush("141fe1da9ea37835e75", "多设备登陆", 5);
////    sendPush(jpushClient, payload);
//    boolean flag = true;
//    try {
//      PushResult result = jpushClient.sendPush(payload);  //正常调用
////      PushResult result = jpushClient.sendPushValidate(payload); //验证用
//      logger.info("Got result - " + result);
//      if(result.getResponseCode() < 300){
//        flag = false;
//      }
//    } catch (APIConnectionException e) {
//        // Connection error, should retry later
//      logger.error("连接错误,应该稍后重试", e);
//  
//    } catch (APIRequestException e) {
//        // Should review the error, and fix the request
//      if(e.getErrorCode() == 1011){  // 别名错误返回这个，暂时先不打印具体信息
//        logger.info("Error:应该检查错误,并修复请求");
//      }else{
//        logger.error("应该检查错误,并修复请求", e);
//      }
//      logger.info("HTTP Status: " + e.getStatus());
//      logger.info("Error Code: " + e.getErrorCode());
//      logger.info("Error Message: " + e.getErrorMessage());
//    } finally {
//      if(flag){
//        logger.info("推送出异常的推送内容：");
////        logger.info("发送对象别名:" + aliases.toString());
////        logger.info("消息内容：" + content);
////        logger.info("消息类型:" + extras);
//      }
//    }
//  }
//  
//  public static PushPayload buildPush(String aliases, String content, Integer extras) {
//    Integer i = null;
//    return PushPayload.newBuilder()
//            .setPlatform(Platform.ios())
//            .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.registrationId(aliases)).build())
//            .setNotification(Notification.newBuilder()
//              .addPlatformNotification(IosNotification.newBuilder()
//                .setAlert(content).setCategory(extras+"").setSound("1.caf").addExtra("type", extras).addExtra("linkParameter", i).build())
//              .build())
//            .build();
////    return PushPayload.newBuilder()
////            .setPlatform(Platform.all())
////            .setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.registrationId(aliases)).build())
////            .setNotification(Notification.newBuilder()
////              .addPlatformNotification(IosNotification.newBuilder()
////              .setAlert(content).setCategory(extras+"").setSound("order_finish.caf").addExtra("type", extras).build())//.setSound("order_finish.caf")//.setSound("happy.caf")
////              .addPlatformNotification(AndroidNotification.newBuilder()
////                .setAlert(content).addExtra("type", extras).build())
////              .build())
////            .build();
//  }
}
