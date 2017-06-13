package com.qianfan365.jcstore.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.bean.PermissionBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.MessageConstant;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.constant.PermissionConstant;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.PermissionInfo;
import com.qianfan365.jcstore.common.pojo.PermissionInfoExample;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.common.pojo.UserPermissionExample;
import com.qianfan365.jcstore.common.pojo.UserPermissionExample.Criteria;
import com.qianfan365.jcstore.dao.inter.PermissionInfoMapper;
import com.qianfan365.jcstore.dao.inter.UserPermissionMapper;

/**
 * 权限相关操作
 */
@Service("permissionInfoService")
public class PermissionInfoService {
  @Autowired
  private PermissionInfoMapper permissionInfoMapper;
  
  @Autowired
  private UserPermissionMapper userPermissionMapper;
  
  @Autowired
  private UserService userService;
  
  /**
   * 编辑权限
   * @param pidlist
   * @param uid
   * @param belongs
   * @return
   */
  public int savePermission(List<Integer> pidlist,Integer uid ,Integer belongs){
    int sound = 0; // 推送的声音设置  0代表 使用自定义的声音(默认音是4)(这么设是为了兼容已有数据 已有的肯定是0)
    //若有推送相关的权限 则查出推送信息
    List<Integer> pushlist = new ArrayList<Integer>(MessageConstant.pushlist());
    pushlist.retainAll(pidlist);
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    if(!pushlist.isEmpty()){
      for (Integer pid : pushlist) {
        map.put(pid, 1);
      }
      UserPermissionExample ex = new UserPermissionExample();
      ex.createCriteria().andUidEqualTo(uid).andPidIn(pushlist);
      List<UserPermission> push = userPermissionMapper.selectByExample(ex);
      if(!push.isEmpty()){
        sound = push.get(0).getPush()&4; //获取已有的声音设置  在第3位 所以&4
      }
      for (UserPermission userPermission : push) {
        map.put(userPermission.getPid(), userPermission.getPush());
      }
    }
    
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andUidEqualTo(uid);
    userPermissionMapper.deleteByExample(example);
    int ret=0;
    for (Integer pid : pidlist) {
      UserPermission record = new UserPermission();
      record.setPid(pid);
      record.setUid(uid);
      record.setBelongs(belongs);
      record.setCreatetime(new Date());
      record.setUpdatetime(new Date());
      if(map.containsKey(pid)){ //若是推送相关的权限 则放入推送信息
        int num = pidlist.contains(PermissionType.VIEW_PERSON)?2:0; //获取 是 个人权限(2) 还是 全员权限(0) (在第2位)
        record.setPush((map.get(pid)&1)+num+sound); // 设置推送信息 第1位中1是推送0是不推  在加上2,3位
      }
      ret+=userPermissionMapper.insertSelective(record);
    }
    return ret;
  }
  
  /**
   * 添加员工时,存入初始权限
   * @param uid
   * @param belongs
   * @return
   */
  public int initPermission(Integer uid ,Integer belongs){
    UserPermission record = new UserPermission();
    record.setPid(PermissionType.VIEW_PERSON);
    record.setUid(uid);
    record.setBelongs(belongs);
    record.setCreatetime(new Date());
    record.setUpdatetime(new Date());
    return userPermissionMapper.insertSelective(record);
  }
  
  /**
   * 查询给定用户的所有权限
   * @param uid
   * @return
   */
  public List<UserPermission> findByUid(int uid){
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andUidEqualTo(uid);
    return userPermissionMapper.selectByExample(example);
  }
  
  /**
   * 查询给定用户的所有权限
   * @param uid
   * @return
   */
  public List<Integer> findPidsByUid(int uid){
    return userPermissionMapper.selectPidByUid(uid);
  }
  
  /**
   * 查询给定用户的某项权限信息
   * @param pid
   * @param uid
   * @return
   */
  public UserPermission findByUidPid(Integer pid, Integer uid){
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andUidEqualTo(uid).andPidEqualTo(pid);
    List<UserPermission> list = userPermissionMapper.selectByExample(example);
    return list.isEmpty()?null:list.get(0);
  }
  
  /**
   * 校验某用户是否拥有给定的所有权限
   * @param uid
   * @param permissionids
   * @return
   */
  public boolean checkPermission(int uid ,int[] permissionids) {
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andUidEqualTo(uid).andPidIn(Arrays.stream(permissionids).boxed().collect(Collectors.toList()));
    return permissionids.length == userPermissionMapper.countByExample(example);
  }
  
  /**
   * 查询给定用户的某项权限的map形式信息
   * @param pid
   * @param uid
   * @param belongs
   * @return
   */
  public Map<Integer, Boolean> findMapByUidPid(Integer pid, Integer uid, Integer belongs){
    Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    if(belongs == 0){
      map.put(pid, true);
      return map;
    }
    UserPermission up = findByUidPid(pid, uid);
    map.put(pid, (up != null)||(belongs == 0) );
    return map;
  }
  /**
   * 查询给定用户的某几项权限信息
   * @param pids
   * @param uid
   * @return
   */
  public Map<Integer, Boolean> findMapByUidPids(List<Integer> pids, Integer uid){
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andUidEqualTo(uid).andPidIn(pids);
    List<UserPermission> list = userPermissionMapper.selectByExample(example);
    Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    for(Integer pid : pids) {
      map.put(pid, false);
    }
    for (UserPermission up : list) {
      map.put(up.getPid(), true);
    }
    return map;
  }
  
  /**
   * 查询给定用户的某几项权限信息
   * @param pids
   * @param uid
   * @param belongs
   * @return
   */
  public Map<Integer, Boolean> findMapByUidPids(List<Integer> pids, Integer uid, Integer belongs){
    Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
    if(belongs == 0){
      for(Integer pid : pids) {
        map.put(pid, true);
      }
      return map;
    }
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andUidEqualTo(uid).andPidIn(pids);
    List<UserPermission> list = userPermissionMapper.selectByExample(example);
    for(Integer pid : pids) {
      map.put(pid, false);
    }
    for (UserPermission up : list) {
      map.put(up.getPid(), true);
    }
    return map;
  }
  
  /**
   * 根据店主id删除权限
   * @param belongs
   * @return
   */
  public int delByBelongs(Integer belongs) {
    UserPermissionExample example = new UserPermissionExample();
    Criteria criteria = example.createCriteria();
    criteria.andBelongsEqualTo(belongs);
    Criteria criteriaor = example.or();
    criteriaor.andUidEqualTo(belongs);
    return userPermissionMapper.deleteByExample(example);
  }
  
  /**
   * 封装权限列表
   * @param map
   * @return
   */
  @Deprecated
  public Map<String, List<List<PermissionBean>>> permissionInfoToMap(Map<Integer, PermissionBean> map){
    //  类别名  类别list 行      权限bean
    Map<String,  List<List<PermissionBean>>> mll = new LinkedHashMap<String, List<List<PermissionBean>>>(PermissionConstant.permissionTypeMap);
    for (PermissionBean val : map.values()) {
      List<List<PermissionBean>> ll = mll.get(val.getType());
      if(ll == null){ //该类别一行也没有，新建改类别list，再新建第一行，再放入本行第一个权限bean
        ll = new ArrayList<List<PermissionBean>>();
        List<PermissionBean> l = new ArrayList<PermissionBean>();
        l.add(val);
        ll.add(l);
        mll.put(val.getType(), ll);
        continue;
      }
      List<PermissionBean> l=ll.get(ll.size()-1);
      if(l.size() == 2){ //最后一行已经有2个了，新建一行，再放入本行第一个权限bean
        List<PermissionBean> newl = new ArrayList<PermissionBean>();
        newl.add(val);
        ll.add(newl);
        continue;
      }
      l.add(val); //最后一行只有1个，直接加
    } 
    return mll;
  }
  
  /**
   * 获取发消息时需要的用户信息
   * @param belongs
   * @param permissionId
   * @return
   */
  public List<UserPermission> findMessageNeed(Integer belongs, Integer permissionId) {
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andBelongsEqualTo(belongs).andPidEqualTo(permissionId);
    List<UserPermission> uplist = userPermissionMapper.selectByExample(example);
    
    boolean push = MessageConstant.pushlist().contains(permissionId);
    if(push){ //绑定推送的权限
      UserPermissionExample example1 = new UserPermissionExample();
      example1.createCriteria().andUidEqualTo(belongs).andPidEqualTo(permissionId);
      List<UserPermission> list = userPermissionMapper.selectByExample(example1);
      if(!list.isEmpty()){
        uplist.add(list.get(0));
        return uplist;
      }
    }
    
    UserPermission up = new UserPermission();
    up.setUid(belongs);
    up.setPid(permissionId);
    up.setPush(push?1:null); //在权限表没有数据的超级账号 默认推送
    uplist.add(up);
    return uplist;
  }
  
  /**
   * 获取发消息时需要的用户信息
   * @param belongsList
   * @param permissionId
   * @return
   */
  public List<UserPermission> findMessageNeed(List<Integer> belongsList, Integer permissionId) {
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andBelongsIn(belongsList).andPidEqualTo(permissionId);
    List<UserPermission> uplist = userPermissionMapper.selectByExample(example);
    
    boolean push = MessageConstant.pushlist().contains(permissionId);
    if(push){ //绑定推送的权限
      UserPermissionExample example1 = new UserPermissionExample();
      example1.createCriteria().andUidIn(belongsList).andPidEqualTo(permissionId);
      List<UserPermission> list = userPermissionMapper.selectByExample(example1);
      if(!list.isEmpty()){
        uplist.addAll(list);
        belongsList.removeAll(uplist.stream().map(up -> up.getUid()).collect(Collectors.toList()));
      }
    }
    for(Integer belongs:belongsList){
      UserPermission up = new UserPermission();
      up.setUid(belongs);
      up.setPid(permissionId);
      up.setPush(push?1:null); //在权限表没有数据的超级账号 默认推送
      uplist.add(up);
    }
    return uplist;
  }
  
  /**
   * 查询所有权限
   * @return
   */
  public List<PermissionInfo> findAllPermission() {
    PermissionInfoExample example = new PermissionInfoExample();
    return permissionInfoMapper.selectByExample(example);
  }
  
  /**
   * 查询给定用户的所有与推送相关的权限信息
   * @param uid
   * @return
   */
  public List<UserPermission> findPushList(Integer uid) {
    UserPermissionExample example = new UserPermissionExample();
    example.createCriteria().andUidEqualTo(uid).andPushIsNotNull();
    List<UserPermission> list = userPermissionMapper.selectByExample(example);
    return list;
  }
  
  /**
   * 保存推送设置
   * @param pid
   * @param uid
   * @param belongs
   * @return
   */
  public int savePushSettings(Integer pid, User user) {
    Integer uid = user.getId();
    UserPermission record = findByUidPid(pid, uid);
    if(record != null){
      record.setPush(record.getPush()^1);
      record.setUpdatetime(new Date());
      return userPermissionMapper.updateByPrimaryKey(record);
    }
    if(user.getBelongs() == 0){
      List<Integer> allpush = new ArrayList<Integer>(MessageConstant.pushlist());
      List<String> mids = Lists.newArrayList(SoftTypeConstant.softMap().get(user.getSoftId()));
      for (String mid : new String[]{ModuleType.ORDER_INFO,ModuleType.STATICS,ModuleType.ORDER_REFUND,ModuleType.PRODUCT}) {
        if(!mids.contains(mid)){
          allpush.removeAll(ModuleData.getModulesPermissionMap().get(mid));
        }
      }
      if(!allpush.contains(pid)){
        return -1;
      }
      
      List<UserPermission> uplist = findPushList(uid);
      UserPermission up = new UserPermission();
      up.setUid(uid);
      up.setBelongs(0);
      up.setPid(pid);
      Integer push = 0;
      if(uplist !=null & !uplist.isEmpty()){
        //取其他权限的声音设置
        push = (uplist.get(0).getPush()&4)>0?4:0;  //之前没存这个肯定是要推送的 所以是5或1
      }
      up.setPush(push); // 1.2期加了新推送 是店主且表里没有的 不一定是 没设置过语音的了
      Date date = new Date();
      up.setCreatetime(date);
      up.setUpdatetime(date);
      return userPermissionMapper.insertSelective(up);
    }
    return -1;
  }
  
  /**
   * 保存语音消息提醒设置
   * @param uid
   * @param belongs
   * @return
   */
  public int savePushSound(User user, List<Integer> allpush) {
    Integer uid = user.getId();
    int num = userPermissionMapper.updatePushSound(uid, MessageConstant.pushstr());
    if(user.getBelongs() != 0){ // 不是店长
      return num==0?-1:1;
    }
    
//    if(num == allpush.size()){ // 是店长 但更改了全部的推送设置
//      return 1;
//    }
    List<UserPermission> uplist = findPushList(uid);
    Integer push = 5;
    if(uplist !=null & !uplist.isEmpty()){
      List<Integer> list = uplist.stream().map(p -> p.getPid()).collect(Collectors.toList());
      allpush.removeAll(list);
      //取其他权限的声音设置
      push = (uplist.get(0).getPush()&4)>0?5:1;  //之前没存这个肯定是要推送的 所以是5或1
    }
    for (Integer pid : allpush) { // 保存 表里没有的推送设置
      UserPermission up = new UserPermission();
      up.setUid(uid);
      up.setBelongs(0);
      up.setPid(pid);
      // 1.2期加了新推送 是店主且表里没有的 第一次设置语音 不一定是关了
      up.setPush(push); //101(2) 
      Date date = new Date();
      up.setCreatetime(date);
      up.setUpdatetime(date);
      userPermissionMapper.insertSelective(up);
    }
    return 1;
  }
  
  /**
   * 权限变更(减少)
   * @param session
   * @param PermissionList 要查的权限集合
   * @param num 至少要有的权限个数
   * @return
   */
  @SuppressWarnings("unchecked")
  public ResultData permissionChanged(HttpSession session, List<Integer> PermissionList, Integer num){
    num = num==0?PermissionList.size():num;
    List<Integer> SessionPermission = (List<Integer>) session.getAttribute("permission");
    PermissionList.retainAll(SessionPermission);
    if(PermissionList.size() < num){
      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
    }
    //权限变更 已无相应的权限
    session.removeAttribute(SessionConstant.FRONT_USER_SESSION);
    session.removeAttribute("token");
    session.removeAttribute("permission");
    return ResultData.build().setStatus(ResultData.Status.PERMISSION_CHANGED);
  }
  
  /**
   * 订单相关的权限校验
   * @param session
   * @param user
   * @param orderUid
   * @return
   */
  public ResultData orderPermissionCheck(HttpSession session,User user,Integer orderUid){
    Integer userBelongs = (user.getBelongs() == 0)?user.getId():user.getBelongs();
    Integer orderBelongs = userService.findUser(orderUid).getBelongs();
    orderBelongs = (orderBelongs == 0)?orderUid:orderBelongs;
    if (!userBelongs.equals(orderBelongs)){ //不是自己店铺下的单 
      return ResultData.build().parameterError();
    }
    if (user.getBelongs() != 0 && !orderUid.equals(user.getId()) && 
        findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) == null) {
      return permissionChanged(session,Lists.newArrayList(PermissionConstant.PermissionType.VIEW_ALL), 1);
    }
    return null;
  }
  
  /**
   * 订单相关的权限校验(从消息通知相关处调的接口)
   * @param session
   * @param user
   * @param orderUid
   * @return
   */
  public ResultData orderPermissionCheckmes(HttpSession session,User user,Integer orderUid){
    Integer userBelongs = (user.getBelongs() == 0)?user.getId():user.getBelongs();
    Integer orderBelongs = userService.findUser(orderUid).getBelongs();
    orderBelongs = (orderBelongs == 0)?orderUid:orderBelongs;
    if (!userBelongs.equals(orderBelongs)){ //不是自己店铺下的单 
      return ResultData.build().parameterError();
    }
    if (user.getBelongs() != 0 && !orderUid.equals(user.getId()) && 
        findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId()) == null) {
      return ResultData.build().setStatus(Status.NO_VIEW_PERMISSION);
    }
    return null;
  }
}
