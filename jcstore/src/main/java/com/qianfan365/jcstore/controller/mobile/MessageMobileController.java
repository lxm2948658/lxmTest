package com.qianfan365.jcstore.controller.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.qianfan365.jcstore.service.MessageService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.common.bean.MessageBean;
import com.qianfan365.jcstore.common.bean.PermissionBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.trial.TrialUnavailable;
import com.qianfan365.jcstore.common.constant.MessageConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageCategory;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.controller.BaseController;

/**
 * 
 * @author liuhaoran
 *
 */
@Controller
@RequestMapping("/mobile/message")
public class MessageMobileController extends BaseController {
  @Autowired
  private MessageService messageService;
  @Autowired
  private PermissionInfoService permissionService;
  
  /**
   * 通知消息列表
   * @param session
   * @param currentPage
   * @param limit
   * @param querytime
   * @param category
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/list"},method=RequestMethod.GET)
  @TrialUnavailable
  @ModulePassport(moduleids={ModuleType.MESSAGE},setuser=true)
  public ResultData list(HttpSession session, @RequestParam(defaultValue = "1")Integer currentPage,
                         @RequestParam(defaultValue = "20") Integer limit, Long querytime, Integer category) {
    if (1 == currentPage || null == querytime || 0 == querytime) {
      querytime = new Date().getTime();
    }
    User user = getLoginUser(session);
    if(category!=null){
    	ResultData result = ResultData.build();
    	List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
    	Page<MessageBean> page = messageService.findList(currentPage, limit, user, querytime, category);
    	if(category==MessageCategory.ORDER_HELPER){
    		if(!mids.contains(ModuleType.ORDER_INFO)){
    			return ResultData.build().setStatus(Status.MODULE_CHANGED);
    		}
    		boolean print = mids.contains(ModuleType.ORDER_PRINT);
    		boolean bespokePrint = mids.contains(ModuleType.BESPOKE_ORDER_PRINT);
//    		result.put("print", print);
    		for (MessageBean bean : page) {
    			if(bean.getLabel()==0){
						bean.setPrint(print);
					}else if(bean.getLabel()==1){
						bean.setPrint(false);
					}else if(bean.getLabel()==2){//等于2的只能最后判断(因为要改label)
						bean.setPrint(bespokePrint);
						bean.setLabel(0);
					}
				}
    	}
      result.parsePageBean(page).put("querytime", querytime);
      if(category==MessageCategory.PRODUCT_CATEGORY && !mids.contains(ModuleType.PRODUCT)){
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      return result;
    }
    //下面是1.3期及以前的
    boolean b = user.getBelongs() > 0 && permissionService.findPushList(user.getId()).isEmpty();
    return ResultData.build().parsePageBean(messageService.findList(currentPage, limit, 
      user, querytime, category)).put("querytime", querytime).put("settings", !b);
  }
  
  /**
   * 通知消息类别列表
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/category"},method=RequestMethod.GET)
  @TrialUnavailable
  @ModulePassport(moduleids={ModuleType.MESSAGE},setuser=true)
  public ResultData category(HttpSession session) {
    User user = getLoginUser(session);
    //b:是否不需要设置按钮
    boolean b = user.getBelongs() > 0 && permissionService.findPushList(user.getId()).isEmpty();
    List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
    //这三模块一个也没有时,消息列表是也就没有设置了(即removeAll之后list没变)
    if(!Lists.newArrayList(ModuleType.ORDER_INFO,ModuleType.PRODUCT,ModuleType.STATICS).removeAll(mids)){
      b = true;
    }
    return ResultData.build().put("category", messageService.findCategory(user.getId(),mids)).put("settings", !b);
  }
  
  /**
   * 按类别删除通知消息
   * @param session
   * @param category 消息类别
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/del/category"},method=RequestMethod.POST)
  @ModulePassport(moduleids={ModuleType.MESSAGE})
  public ResultData delCategory(HttpSession session,Integer category){
    User user = getLoginUser(session);
//    int num = 
    messageService.delCategoryMessage(user.getId(), category);
    return ResultData.build().success();//put("status", num > 0 ? 1 : 0);
  }
  
  /**
   * 删除通知消息
   * @param session
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/del"},method=RequestMethod.POST)
  @ModulePassport(moduleids={ModuleType.MESSAGE})
  public ResultData del(HttpSession session,Integer id){
    User user = getLoginUser(session);
    int num = messageService.delMessage(user.getId(), id);
    return ResultData.build().put("status", num == 1 ? 1 : 0);
  }
  
  /**
   * 有无新消息
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/flag"},method=RequestMethod.GET)
  @ModulePassport(moduleids={ModuleType.MESSAGE})
  public ResultData newMessageFlag(HttpSession session){
    User user = getLoginUser(session);
    return ResultData.build().put("flag",messageService.newMessageFlag(user.getId()));
  }
  
  /**
   * 消息列表中是否显示【设置】按钮
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/setflag"},method=RequestMethod.GET)
  @ModulePassport(moduleids={ModuleType.MESSAGE},setuser=true)
  public ResultData settingsFlag(HttpSession session){
    User user = getLoginUser(session);
    boolean b = user.getBelongs() > 0 && permissionService.findPushList(user.getId()).isEmpty();
    List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
    //这三模块一个也没有时,消息列表是也就没有设置了(即removeAll之后list没变)
    if(!Lists.newArrayList(ModuleType.ORDER_INFO,ModuleType.PRODUCT,ModuleType.STATICS).removeAll(mids)){
      b = true;
    }
    return ResultData.build().put("settings", !b);
  }
  
  /**
   * 获取推送设置列表
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/pushlist"},method=RequestMethod.GET)
  @ModulePassport(moduleids={ModuleType.MESSAGE},setuser=true)
  public ResultData pushList(HttpSession session){
    User user = getLoginUser(session);
    List<UserPermission> pushList = permissionService.findPushList(user.getId());
    List<PermissionBean> list = new ArrayList<PermissionBean>();
    boolean sound = true;
    List<Integer> allpush = new ArrayList<Integer>(MessageConstant.pushlist());
    //1.4.2期推送消息是按模块来定了  店长也不一定是全有了 所以把没有的从allpush里去掉
    List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
    for (String mid : new String[]{ModuleType.ORDER_INFO,ModuleType.STATICS,ModuleType.ORDER_REFUND,ModuleType.PRODUCT}) {
      if(!mids.contains(mid)){
        allpush.removeAll(ModuleData.getModulesPermissionMap().get(mid));
      }
    }
    if(user.getBelongs() > 0 && pushList.isEmpty()){ //权限变更　||　无操作权限
      return permissionService.permissionChanged(session, new ArrayList<Integer>(MessageConstant.pushlist()), 1);
    }
    if(allpush.isEmpty()){ //版本变更(店主有可能是初始状态,所以要验allpush)
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    pushList = pushList.stream().filter(pl->allpush.contains(pl.getPid())).collect(Collectors.toList());
    
    if(user.getBelongs() > 0) {
      if(pushList.isEmpty()){ //版本变更(有allpush不是空但pushList是空的可能)
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      sound = (pushList.get(0).getPush() & 4) == 0;
      for (UserPermission push : pushList) {
        list.add(new PermissionBean(push.getPid(),(push.getPush() & 1) > 0));
      }
    }else{
      if(!pushList.isEmpty()){
        sound = (pushList.get(0).getPush() & 4) == 0;
        for (UserPermission push : pushList) {
          list.add(new PermissionBean(push.getPid(),(push.getPush() & 1) > 0));
          allpush.remove(push.getPid());
        }
      }
      for (Integer permissionId : allpush) {
        list.add(new PermissionBean(permissionId,true));
      }
    }
    list.sort((x,y) -> Integer.compare(x.getId(),y.getId()));
    PermissionBean soundBean = new PermissionBean(-1, "语音消息提醒", sound);
    return ResultData.build().put("pushlist", list).put("sound", soundBean);
  }
  
  /**
   * 获取推送设置列表(封装版)
   * @version 1.1
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/pushlist/pack"},method=RequestMethod.GET)
  @ModulePassport(moduleids={ModuleType.MESSAGE},setuser=true)
  public ResultData pushListPack(HttpSession session){
    User user = getLoginUser(session);
    List<UserPermission> pushList = permissionService.findPushList(user.getId());
    List<PermissionBean> list = new ArrayList<PermissionBean>();
    boolean sound = true;
    List<Integer> allpush = new ArrayList<Integer>(MessageConstant.pushlist());
    //1.4.2期推送消息是按模块来定了  店长也不一定是全有了 所以把没有的从allpush里去掉
    List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
    for (String mid : new String[]{ModuleType.ORDER_INFO,ModuleType.STATICS,ModuleType.ORDER_REFUND,ModuleType.PRODUCT}) {
      if(!mids.contains(mid)){
        allpush.removeAll(ModuleData.getModulesPermissionMap().get(mid));
      }
    }
    if(user.getBelongs() > 0 && pushList.isEmpty()){ //权限变更　||　无操作权限
      return permissionService.permissionChanged(session, new ArrayList<Integer>(allpush), 1);
    }
    if(allpush.isEmpty()){ //版本变更(店主有可能是初始状态,所以要验allpush)
      return ResultData.build().setStatus(Status.MODULE_CHANGED);
    }
    pushList = pushList.stream().filter(pl->allpush.contains(pl.getPid())).collect(Collectors.toList());
    if(user.getBelongs() > 0) {
      if(pushList.isEmpty()){ //版本变更(有allpush不是空但pushList是空的可能)
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      sound = (pushList.get(0).getPush() & 4) == 0;
      for (UserPermission push : pushList) {
        list.add(new PermissionBean(push.getPid(),(push.getPush() & 1) > 0));
      }
    }else{
      if(!pushList.isEmpty()){
        sound = (pushList.get(0).getPush() & 4) == 0;
        for (UserPermission push : pushList) {
          list.add(new PermissionBean(push.getPid(),(push.getPush() & 1) > 0));
          allpush.remove(push.getPid());
        }
      }
      for (Integer permissionId : allpush) {
        list.add(new PermissionBean(permissionId,true));
      }
    }
    list.sort((x,y) -> Integer.compare(x.getId(),y.getId()));
    PermissionBean soundBean = new PermissionBean(-1, "语音消息提醒", sound);
    List<PermissionBean> soundlist = new ArrayList<PermissionBean>();
    soundlist.add(soundBean);
    List<List<PermissionBean>> ll = new ArrayList<List<PermissionBean>>();
    ll.add(soundlist);
    ll.add(list);
    return ResultData.build().put("pushlist", ll);
  }
  
  /**
   * 保存推送设置
   * @param pid
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/setpush",method=RequestMethod.POST)
  @ModulePassport(moduleids={ModuleType.MESSAGE},setuser=true)
  public ResultData savePushSettings(Integer pid, HttpSession session) {
    List<Integer> allpush = new ArrayList<Integer>(MessageConstant.pushlist());
    if (!allpush.contains(pid) && pid != -1) {
      return ResultData.build().parameterError();
    }
    User user = getLoginUser(session);
    
    //1.4.2期推送消息是按模块来定了  店长也不一定是全有了 所以把没有的从allpush里去掉
    List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
    for (String mid : new String[]{ModuleType.ORDER_INFO,ModuleType.STATICS,ModuleType.ORDER_REFUND,ModuleType.PRODUCT}) {
      if(!mids.contains(mid)){
        allpush.removeAll(ModuleData.getModulesPermissionMap().get(mid));
      }
    }
    
    int num = 1;
    if(pid == -1){ //设置  语音消息提醒
      num = permissionService.savePushSound(user, allpush);
    }else{ //保存是否推送设置
      if(!allpush.contains(pid)){
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      num = permissionService.savePushSettings(pid, user);
    }
    if(num == -1){  //权限变更　||　无操作权限
      List<Integer> list = new ArrayList<Integer>();
      list.add(pid);
      return permissionService.permissionChanged(session, list, 1);
    }
    return ResultData.build().setStatus(num);
  }
}
