package com.qianfan365.jcstore.service.task;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qianfan365.jcstore.common.constant.MessageConstant.MessageInform;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.MessageConstant.PushInform;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.service.MessageService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.UserService;

//@Component
public class MessageTask {
  
  @Autowired
  private MessageService messageService;
  @Autowired
  private UserService userService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  
  private static Log logger = LogFactory.getLog(MessageTask.class);
  
  /**
   * 服务即将到期定时任务
   */
  @Scheduled(cron = "${cron.service.willdue}")
  public void serviceWillDue() {
    List<Integer> belongslist = userService.findWillDueBelongs();
    if(belongslist == null || belongslist.isEmpty()){
      return;
    }
    List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongslist, PermissionType.SERVICE_DUE_REMIND);
    int num = messageService.insertBatch(MessageInform.SERVICE_DUE, PushInform.SERVICE_DUE,
      uplist, PermissionType.SERVICE_DUE_REMIND, null, MessageType.SERVICE_DUE, 0, false);
    logger.info("发送了"+num+"条服务即将到期的消息");
  }
  
  /**
   * 上月概况统计定时任务
   */
  @Scheduled(cron = "${cron.monthly.statistics}")
  public void monthlyStatistics() {
    List<Integer> belongslist = userService.findValidBelongs();
    if(belongslist == null || belongslist.isEmpty()){
      return;
    }
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DATE, -1); //日子减一 取上个月的时间
    Integer year = cal.get(Calendar.YEAR);
    Integer month = cal.get(Calendar.MONTH)+1; //一月是0，所以加一
    Integer linkParameter = year * 100 + month;
    List<UserPermission> uplist = permissionInfoService.findMessageNeed(belongslist, PermissionType.MONTHLY_STATISTICS_REMIND);
    int num = messageService.insertBatch(String.format(MessageInform.MONTHLY_STATISTICS, year, month),
      String.format(PushInform.MONTHLY_STATISTICS, year, month), uplist,
      PermissionType.MONTHLY_STATISTICS_REMIND, linkParameter, MessageType.MONTHLY_STATISTICS, 0, false);
    logger.info("发送了"+num+"条上月概况统计的消息");
  }
}
