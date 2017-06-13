package com.qianfan365.jcstore.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageInform;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.MessageConstant.PushInform;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.UserConstant.UserStatus;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserLogin;
import com.qianfan365.jcstore.common.pojo.UserLoginExample;
import com.qianfan365.jcstore.dao.inter.UserLoginMapper;

/**
 * 登录信息记录操作
 * @author szz
 *
 */
@Service
public class UserLoginService {

  @Autowired
  private UserLoginMapper userLoginMapper;
  @Autowired
  private MessageService messageService;

  /**
   * 根据用户信息创建登录信息,并返回token唯一码
   * @param user
   * @return
   */
  public String insert(User user, String coding) {
    // 判断是否已经登录过
    UserLoginExample example = new UserLoginExample();
    example.createCriteria().andUidEqualTo(user.getId());
    List<UserLogin> list = this.userLoginMapper.selectByExample(example);
    UserLogin userLogin = new UserLogin();
    userLogin.setUid(user.getId());
    Date nowTime = new Date();
    userLogin.setUpdatetime(nowTime);
    userLogin.setExpiretime(user.getExpiredTime());
    userLogin.setStatus(user.getStatus());
    String token = UUID.randomUUID().toString().replaceAll("-", "");
    userLogin.setToken(token);
    userLogin.setAlias(user.getId()+"_"+coding);
    // 记录表中不存在时则新增,存在是则更新
    if(list.isEmpty()){
      userLogin.setCreatetime(nowTime);
      this.userLoginMapper.insert(userLogin);
    }else{
      if(!userLogin.getAlias().equals(list.get(0).getAlias())){ //别名不一样的 对之前的别名 发送其他设备登录的通知消息
        if(SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.MESSAGE)){
          messageService.insertMessage(MessageInform.OTHERS_LOGIN, PushInform.OTHERS_LOGIN, user.getId(), MessageType.OTHERS_LOGIN, list.get(0).getAlias());
        }
      }
      userLogin.setId(list.get(0).getId());
      this.userLoginMapper.updateByPrimaryKeySelective(userLogin);
    }
    return token;
  }

  /**
   * 根据用户id及token查询登录信息
   * @param id
   * @param token
   * @return
   */
  public UserLogin findUser(Integer id, String token) {
    UserLoginExample example = new UserLoginExample();
    example.createCriteria().andUidEqualTo(id).andTokenEqualTo(token);
    List<UserLogin> userlogins = userLoginMapper.selectByExample(example);
    return userlogins.isEmpty() ? null : userlogins.get(0);
  }

  /**
   * 根据id及token删除登录信息
   * @param id
   * @param token
   */
  public void delete(Integer id, String token) {
    UserLoginExample example = new UserLoginExample();
    example.createCriteria().andUidEqualTo(id).andTokenEqualTo(token);
    userLoginMapper.deleteByExample(example);
    
  }
  
  /**
   * 根据用户id的集合查找所需的别名
   * @param uids
   * @return
   */
  public List<String> findAliases(List<Integer> uids){
    return userLoginMapper.selectAliasByUid(uids.toString().replace("[", "(").replace("]", ")"));
  }
  
  /**
   * 根据uid删除对应的登录信息
   * @param uid
   */
  public void deleteByUid(Integer uid) {
    UserLoginExample example = new UserLoginExample();
    example.createCriteria().andUidEqualTo(uid);
    userLoginMapper.deleteByExample(example);
  }
  
  /**
   * 续费时更新登录信息
   * @param expiredTime
   * @param ids
   * @param nowTime
   */
  public void updateLoginInfo(Date expiredTime, ArrayList<Integer> ids, Date nowTime) {
    UserLoginExample userLoginExample = new UserLoginExample();
    userLoginExample.createCriteria().andUidIn(ids);
    UserLogin login = new UserLogin();
    login.setExpiretime(expiredTime);
    login.setUpdatetime(nowTime);
    userLoginMapper.updateByExampleSelective(login, userLoginExample);
  }
  
  /**
   * 后台删除时更新登录信息
   * @param ids
   */
  public void updateLoginInfoWhenDelete(ArrayList<Integer> ids) {
    UserLoginExample loginExample = new UserLoginExample();
    loginExample.createCriteria().andUidIn(ids);
    UserLogin login = new UserLogin();
    login.setStatus(false);
    this.userLoginMapper.updateByExampleSelective(login, loginExample);
  }
  
  /**
   * 前台删除时更新登录信息
   * @param uid
   * @param nowTime
   */
  public void updateLoginInfo(Integer uid, Date nowTime) {
    UserLoginExample userLoginExample = new UserLoginExample();
    userLoginExample.createCriteria().andUidEqualTo(uid);
    UserLogin login = new UserLogin();
    login.setStatus(UserStatus.DELETE);
    login.setUpdatetime(nowTime);
    userLoginMapper.updateByExampleSelective(login, userLoginExample);
  }
  
}
