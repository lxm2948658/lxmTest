package com.qianfan365.jcstore.controller.mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qianfan365.jcstore.common.bean.PermissionBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.constant.PermissionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping(path = "/mobile/permission")
public class PermissionInfoMobileController extends BaseController{
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private UserService userService;
  
  
  /**
   * 管理员修改员工权限
   * @param pids
   * @param uid
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/save",method=RequestMethod.POST)
  @ModulePassport(moduleids={ModuleType.USER},setuser=true)
  public Map<String, Object> save(Integer[] pids, Integer uid, HttpSession session) {
//    Map<String, Object> map = ResultData.build();
    User user = getLoginUser(session);
//    user.getId();
    User staffuser = userService.findUserById(uid);
    //校验用户权限，是否管理员，uid是否归登陆用户管理
    if(user.getBelongs() != 0 || !staffuser.getBelongs().equals(user.getId())){
      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
    }
    
    //校验参数（连带权限）
    List<Integer> pidlist = new ArrayList<Integer>();
    pidlist.addAll(Arrays.asList(pids));
    //不能有重复的权限
    Set<Integer> pidset = new HashSet<Integer>();
    pidset.addAll(pidlist);
    if(pidset.size() != pidlist.size()){
      return ResultData.build().parameterError();
    }
    
    //pids中的参数必须是有效的
    Set<Integer> noPids = ModuleData.getModulesPermissionMap().entrySet().stream()
        .filter(module->!SoftTypeConstant.softMap().get(user.getSoftId()).contains(module.getKey()))
        .flatMap(module->module.getValue().stream()).collect(Collectors.toSet());
    List<Integer> ids = Lists.newArrayList(pidlist);
    ids.removeAll(PermissionConstant.ids.stream().filter(id->!noPids.contains(id)).collect(Collectors.toList()));
    if(!ids.isEmpty()){
      return ResultData.build().setStatus(ResultData.Status.MODULE_CHANGED);
    }
    //【查看个人内容】和【查看全员内容】必须且只能选一个
    List<Integer> view = Lists.newArrayList(PermissionType.VIEW_PERSON,PermissionType.VIEW_ALL);
    view.retainAll(pidlist);
    if(view.size() != 1){
      return ResultData.build().parameterError();
    }
    //如选择【商品发布/编辑】，则【查看成本价】同步勾选
    if(pidlist.contains(PermissionType.PRODUCE_EDIT) && !pidlist.contains(PermissionType.VIEW_CORPUS)){
      return ResultData.build().parameterError();
    }
    //如选择【查看个人内容】，则【开单】和【退货】同时未勾选时，【订单确认完成】为不可选状态
    if(pidlist.contains(PermissionType.VIEW_PERSON) && pidlist.contains(PermissionType.FINISH_ORDER)
        && !pidlist.contains(PermissionType.PLACE_ORDER) && !pidlist.contains(PermissionType.REFUND)){
      return ResultData.build().parameterError();
    }
    //在有客户模块时(1.4.2) ,勾选了【开单】权限，则【客户新增/编辑】权限为选择状态，不能改为未选择
    if(pidlist.contains(PermissionType.PLACE_ORDER) && !pidlist.contains(PermissionType.CUSTOMER_EDIT)){
      if(SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.CUSTOMER)){
        return ResultData.build().setStatus(ResultData.Status.MODULE_CHANGED);
      }else{ //若没有客户模块(1.4.2),且还勾了【开单】的,自动放入【客户新增/编辑】(反正没这模块也使不了),以便日后加了客户模块时逻辑正确.
        pidlist.add(PermissionType.CUSTOMER_EDIT);
      }
    }
    
    int num = permissionInfoService.savePermission(pidlist, uid ,user.getId());
    return ResultData.build().setStatus(num==pidlist.size()?1:0);
  }
  
  /**
   * 查询给定员工权限
   * @version 1.0 (只取id为1到14的数据)
   * @param uid
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/find",method=RequestMethod.GET)
  @Deprecated//1.1期废弃
  public ResultData find(Integer uid, HttpSession session){
    User user = getLoginUser(session);
    User staffuser = userService.findUserById(uid);
    //校验用户权限，是否管理员，uid是否归登陆用户管理
    if(user.getBelongs() != 0 || !staffuser.getBelongs().equals(user.getId())){
      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
    }
    List<Integer> pids =  permissionInfoService.findPidsByUid(uid);

    Map<String, List<PermissionBean>> collect = Maps.newLinkedHashMap();
    Lists.newArrayList(PermissionConstant.beans).stream().filter(b -> b.getId()<15).forEach(o->{
      o.setFlag(pids.contains(o.getId()));
      String type = o.getType();
      if(!collect.containsKey(type)){
        collect.put(type, Lists.newArrayList());
      }
      collect.get(type).add(o);
    });
  
    
    List<LinkedHashMap<Object,Object>> ret = Lists.newArrayList();
    collect.forEach((typeName,typelist)->{
      LinkedHashMap<Object,Object> map = Maps.newLinkedHashMap();
      map.put("type", typeName);
      Map<Integer, List<PermissionBean>> collect2 = typelist.stream().collect(Collectors.groupingBy(o->{return typelist.indexOf(o)/2;}));
      map.put("list", collect2.values());
      ret.add(map);
    });
    
    return ResultData.build().put("permissions", ret);
  }
  
  /**
   * 查询给定员工权限
   * @version 1.1 (Android版)
   * @param uid   要询查员工的用户id
   * @param param 所查权限(有序的)
   * @param viewparam 备用参数 目前不传 所查内容权限(有序的)
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/list/android",method=RequestMethod.GET)
  @Deprecated//1.4.2期废弃
  public ResultData listAndroid(Integer uid, String param, String viewparam, HttpSession session){
    User user = getLoginUser(session);
    User staffuser = userService.findUserById(uid);
    //校验用户权限，是否管理员，uid是否归登陆用户管理
    if(user.getBelongs() != 0 || !staffuser.getBelongs().equals(user.getId())){
      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
    }
    List<Integer> needpids = JSON.parseArray(param, Integer.class);
    List<Integer> pids =  permissionInfoService.findPidsByUid(uid);
    List<PermissionBean> view = new ArrayList<PermissionBean>();
    List<Integer> viewpids = viewparam==null?Lists.newArrayList(1,2):JSON.parseArray(viewparam, Integer.class);
    for (Integer pid : viewpids) {
      PermissionBean bean = PermissionConstant.permissionMap.get(pid);
      bean.setFlag(pids.contains(pid));
      view.add(bean);
    }
    List<PermissionBean> list = new ArrayList<PermissionBean>();
    for (Integer pid : needpids) {
      PermissionBean bean = PermissionConstant.permissionMap.get(pid);
      bean.setFlag(pids.contains(pid));
      list.add(bean);
    }
   
    return ResultData.build().put("view", view).put("permissions", list);
  }
  
  /**
   * 查询给定员工权限
   * @version 1.1(最新修改1.5期)
   * 		1.4.2期修改: 按模块显示,滤掉没模块的部分; 取消了行数的那层list;
   * 		1.5期加版本兼容参数maxPid
   * @param uid
   * @param maxPid 最大的权限id(默认18是1.4.2,1.4.3的权限数) 1.5期增加此参数
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/list",method=RequestMethod.GET)
  @ModulePassport(moduleids={ModuleType.USER},setuser=true)
  public ResultData list(Integer uid, HttpSession session, @RequestParam(defaultValue = "18") Integer maxPid){
    User user = getLoginUser(session);
    User staffuser = userService.findUserById(uid);
    //校验用户权限，是否管理员，uid是否归登陆用户管理
    if(user.getBelongs() != 0 || !staffuser.getBelongs().equals(user.getId())){
      return ResultData.build().setStatus(ResultData.Status.NO_PERMISSION);
    }
//    List<UserPermission> permissions = permissionInfoService.findByUid(uid);
//    List<Integer> pids = permissions.stream().mapToInt(UserPermission::getPid).boxed().collect(Collectors.toList());
    List<Integer> pids =  permissionInfoService.findPidsByUid(uid);
    //没有的权限set(1.4.2期加)
    Set<Integer> noPids = ModuleData.getModulesPermissionMap().entrySet().stream()
      .filter(module->!SoftTypeConstant.softMap().get(user.getSoftId()).contains(module.getKey()))
      .flatMap(module->module.getValue().stream()).collect(Collectors.toSet());
    
    Map<Integer, Boolean> mapids = Maps.newHashMap(PermissionConstant.mapids);
    //为版本兼容,滤掉版本没有的部分 (1.5期加)
    if(PermissionConstant.beans.size()>maxPid){//若相等则是最新的版本,不用过滤
    	for (int i = maxPid+1; i <= PermissionConstant.beans.size(); i++) {
    		mapids.remove(i);
			}
    }
    Map<String, List<PermissionBean>> collect = Maps.newLinkedHashMap();
    Lists.newArrayList(PermissionConstant.beans).stream()
      .filter(o->!noPids.contains(o.getId()))  //排除没有的权限set(1.4.2期加)
      .filter(o->o.getId()<=maxPid)  //为版本兼容,滤掉版本没有的部分 (1.5期加)
      .forEach(o->{
        boolean flag = pids.contains(o.getId());
        o.setFlag(flag);
        mapids.put(o.getId(), true);
        String type = o.getType();
        if(!collect.containsKey(type)){
          collect.put(type, Lists.newArrayList());
        }
        collect.get(type).add(o);
      });
  
//    ps.stream().forEach(pb->{
//      String type = pb.getType();
//      if(!collect.containsKey(type)){
//        collect.put(type, Lists.newArrayList());
//      }
//      collect.get(type).add(pb);
//    });
    
    List<LinkedHashMap<Object,Object>> ret = Lists.newArrayList();
    collect.forEach((typeName,typelist)->{
      LinkedHashMap<Object,Object> map = Maps.newLinkedHashMap();
      map.put("id", PermissionConstant.typeIdMap.get(typeName));
      map.put("type", typeName);
      //1.4.2期权限列表按模块显示  取消了行数的那层list
//      Map<Integer, List<PermissionBean>> collect2 = typelist.stream().collect(Collectors.groupingBy(o->{return typelist.indexOf(o)/2;}));
      map.put("list", typelist);//collect2.values());
      ret.add(map);
    });
    
//    Map<Integer, PermissionBean> map = new LinkedHashMap<Integer, PermissionBean>(PermissionConstant.permissionMap);
//    for (UserPermission up : permissions) {
//      PermissionBean bean = map.get(up.getPid());
//      bean.setFlag(true);
//      map.put(up.getPid(), bean);
//    }
//    
//    Map<String, List<List<PermissionBean>>> mll = permissionInfoService.permissionInfoToMap(map);
//    List<PermissionTypeBean> beans = new ArrayList<PermissionTypeBean>(PermissionConstant.permissionTypeBean);
//    for (PermissionTypeBean bean : beans) {
//      bean.setList(mll.get(bean.getType()));
//    }
    return ResultData.build().put("permissions", ret).put("mapids", mapids);
  }
  
  /**
   * 查自己的某项权限
   */
  @ResponseBody
  @RequestMapping(value="/find/self",method=RequestMethod.GET)
  public ResultData findSelf(Integer pid, HttpSession session){
    User user = getLoginUser(session);
    boolean flag = true;
    if(user.getBelongs()!=0){
      flag = permissionInfoService.findByUidPid(pid, user.getId())!=null;
    }else if(pid==PermissionType.VIEW_PERSON){
      flag = false;
    }
    return ResultData.build().put("flag", flag);
  }
}
