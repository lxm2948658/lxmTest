package com.qianfan365.jcstore.controller.mobile;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.trial.TrialUnavailable;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.Module;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserInfo;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.MessageService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ShopService;
import com.qianfan365.jcstore.service.StaticsService;
import com.qianfan365.jcstore.service.UserLoginService;
import com.qianfan365.jcstore.service.UserService;

/**
 * 移动端用户操作接口
 * 
 * @author szz
 *
 */
@Controller
@RequestMapping("/mobile/user")
public class UserMobileController extends BaseController {

  @Autowired
  private UserService userService;
  @Autowired
  private ShopService shopService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private StaticsService staticsService;
  @Autowired
  private UserLoginService userLoginService;

  /**
   * 增加新员工
   * 
   * @param user
   * @param position
   * @param remark
   * @param mobile
   * @param session
   * @return
   */
  @RequestMapping(path = "/add", method = RequestMethod.POST)
  @ResponseBody
  @TrialUnavailable
  @ModulePassport(moduleids={ModuleType.USER})
  public ResultData add(User user, @RequestParam(defaultValue = "") String position,
      @RequestParam(defaultValue = "") String remark,
      @RequestParam(defaultValue = "") String mobile, HttpSession session) {
    String staffNameRegex = "^[\u4E00-\u9FA5A-Za-z]{1,10}$";
    String usernameRegex = "^[A-Za-z0-9]{4,20}$";
    String positionRegex = "^[\u4E00-\u9FA5A-Za-z]{1,8}$";
    String mobileRegex = "^[0-9]{11}$";
    // 校验数据
    if (user != null && StringUtils.isNotEmpty(user.getStaffname())
        && user.getStaffname().matches(staffNameRegex)
        && StringUtils.isNotEmpty(user.getUsername()) && user.getUsername().matches(usernameRegex)
        && (position.matches(positionRegex) || "".equals(position)) && remark.length() <= 100
        && (mobile.matches(mobileRegex) || "".equals(mobile))) {
      // 判断是不是前台管理者
      User currentUser = getLoginUser(session);
      if (currentUser.getBelongs() != 0) {
        return ResultData.build().setStatus(Status.NO_PERMISSION);
      }
      synchronized (UserMobileController.class) {
        // 校验用户名是否重复
        if (!userService.checkUsername(user.getUsername())) {
          return ResultData.build().setStatus(Status.REGIST_NAME_EXISTED);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile(mobile);
        userInfo.setPosition(position);
        userInfo.setRemark(remark);
        // 添加
        return this.userService.add(user, userInfo, currentUser);
      }
    }
    return ResultData.build().parameterError();
  }

  /**
   * 根据ID获取用户信息 只允许查看当前用户信息及所属用户信息
   * 
   * @param id
   * @return
   */
  @RequestMapping(path = "/get", method = RequestMethod.GET)
  @ResponseBody
  public ResultData findById(Integer uid, HttpSession session) {
    // id为null时默认查询当前登录用户个人信息
    User user = this.getLoginUser(session);
    if (uid == null) {
      uid = user.getId();
    }
    return this.userService.findById(uid, user.getId());
  }

  /**
   * 更新账户信息
   * 
   * @param remark
   * @param position
   * @param mobile
   * @param logo
   * @param uid
   * @param session
   * @return
   */
  @RequestMapping(path = "/update", method = RequestMethod.POST)
  @ResponseBody
  public ResultData update(String remark, String position, String mobile, String logo, Integer uid,
      HttpSession session) {
    // 判断是不是前台管理者
    User currentUser = getLoginUser(session);
    boolean isManager = (currentUser.getBelongs() == 0);
    // 数据校验
    String positionRegex = "^[\u4E00-\u9FA5A-Za-z]{1,8}$";
    String mobileRegex = "^[0-9]{11}$";
    // uid不为为null时为更新所属用户信息,需要管理员权限
    if (uid != null && isManager) {
      currentUser = userService.findUser(currentUser.getId());
      if(!SoftTypeConstant.softMap().get(currentUser.getSoftId()).contains(ModuleType.USER)){
        return ResultData.build().setStatus(Status.MODULE_CHANGED);
      }
      UserInfo info = new UserInfo();
      info.setUid(uid);
      // 分别校验 职位,备注,手机号码,任意条件满足则进行更新,均不满足则返回参数错误
      if ((StringUtils.isNotEmpty(mobile) && mobile.matches(mobileRegex)) || "".equals(mobile)) {
        info.setMobile(mobile);
      } else if ((StringUtils.isNotEmpty(position) && position.matches(positionRegex))
          || "".equals(position)) {
        info.setPosition(position);
      } else if ((StringUtils.isNotEmpty(remark) && remark.length() <= 200) || "".equals(remark)) {
        info.setRemark(remark);
      } else {
        return ResultData.build().parameterError();
      }
      return userService.update(info);
    } else if (uid == null) {
      // uid为null时则更新当前用户信息
      // 可更新头像,手机号
      if ((StringUtils.isNotEmpty(mobile) && mobile.matches(mobileRegex)) || "".equals(mobile)) {
        UserInfo info = new UserInfo();
        info.setUid(currentUser.getId());
        info.setMobile(mobile);
        return userService.update(info);
      } else if (StringUtils.isNotEmpty(logo)) {
        User user = new User();
        user.setId(currentUser.getId());
        user.setLogo(logo);
        this.userService.updateUser(user);
        return ResultData.build().success();
      } else {
        return ResultData.build().parameterError();
      }
    }
    return ResultData.build().parameterError();
  }

  /**
   * 更新密码
   * 
   * @param session
   * @param password
   * @param newPassword
   * @return
   */
  @RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
  @ResponseBody
  @TrialUnavailable
  public ResultData updatePassword(HttpSession session, String password, String newPassword) {
    // 校验参数
    String passwordRegex = "^[A-Za-z0-9]{4,20}$";
    if (StringUtils.isNotEmpty(password) && password.matches(passwordRegex)
        && StringUtils.isNotEmpty(newPassword) && newPassword.matches(passwordRegex)) {
      // 进入更新逻辑
      User user = this.getLoginUser(session);
      return userService.updatePassword(user.getId(), password, newPassword);
    }
    return ResultData.build().parameterError();
  }

  /**
   * 重置密码
   * 
   * @param id
   * @param session
   * @return
   */
  @RequestMapping(path = "/resetPassword", method = RequestMethod.POST)
  @ResponseBody
  public ResultData resetPassword(Integer uid, HttpSession session) {
    // 校验参数
    if (uid == null) {
      return ResultData.build().parameterError();
    }
    // 校验权限
    User user = this.getLoginUser(session);
    if (user.getBelongs() != 0) {
      return ResultData.build().setStatus(Status.NO_PERMISSION);
    }
    // 重置密码
    return userService.resetPassword(uid, user.getId());
  }

  /**
   * 删除用户
   * 
   * @param session
   * @param uid
   * @return
   */
  @RequestMapping(path = "/delete", method = RequestMethod.POST)
  @ResponseBody
  @TrialUnavailable
  public ResultData delete(HttpSession session, Integer uid) {
    // 判断是不是前台管理者
    User currentUser = getLoginUser(session);
    if (currentUser.getBelongs() != 0) {
      return ResultData.build().setStatus(Status.NO_PERMISSION);
    }
    int i = this.userService.delete(currentUser.getId(), uid);
    if (i == 1) {
      return ResultData.build().success();
    }
    return ResultData.build().error();
  }

  /**
   * 获取员工列表
   * 
   * @param session
   * @param currentPage
   * @param querytime
   * @return
   */
  @ResponseBody
  @RequestMapping(path = "/list", method = RequestMethod.GET)
  @ModulePassport(moduleids = {ModuleType.USER})
  @TrialUnavailable
  public ResultData list(HttpSession session,
      @RequestParam(defaultValue = "1") Integer currentPage, Long querytime) {
    if (1 == currentPage || null == querytime || 0 == querytime) {
      querytime = new Date().getTime();
    }
    // 判断是不是前台管理者
    User currentUser = getLoginUser(session);
    if (currentUser.getBelongs() != 0) {
      return ResultData.build().setStatus(Status.NO_PERMISSION);
    }
    // 获取列表
    Page<User> users = userService.listUser(currentUser.getId(), currentPage, querytime);
    return ResultData.build().parsePageBean(users).put("querytime", querytime);
  }

  /**
   * 校验用户名是否正确
   * 
   * @param username
   * @return
   */
  @ResponseBody
  @RequestMapping(path = "/check", method = RequestMethod.GET)
  public ResultData checkUsername(String username) {
    // 数据校验
    if (StringUtils.isEmpty(username)) return ResultData.build().parameterError();
    // 用户名校验
    boolean checkUsername = this.userService.checkUsername(username);
    if (checkUsername) {
      return ResultData.build().success();
    }
    return ResultData.build().setStatus(Status.REGIST_NAME_EXISTED);
  }

  /**
   * 获取移动端首页展示所需要的信息
   * 
   * @param session
   * @return
   */
  @RequestMapping(path = "/index", method = RequestMethod.GET)
  @ResponseBody
  public ResultData indexResource(HttpSession session) {
    ResultData map = ResultData.build();
    // 返回数据
    User user = this.getLoginUser(session);
    this.userService.getIndexResource(map, user);
    return ResultData.build().parseMap(map);
  }
  
  /**
   * 获取移动端所需要的模块信息
   * 
   * @param session
   * @return
   */
  @RequestMapping(path = "/module", method = RequestMethod.GET)
  @ResponseBody
  public ResultData moduleResource(HttpSession session) {
    // 返回数据
    User user = this.getLoginUser(session);
    return ResultData.build().put("softType", userService.getModuleResource(user));
  }

  /**
   * 校验使用人数是否已满
   * 
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/checkUserNum")
  @TrialUnavailable
  public ResultData checkUserNumber(HttpSession session) {
    User user = this.getLoginUser(session);
    // 校验权限
    if (user.getBelongs() != 0) {
      return ResultData.build().setStatus(Status.NO_PERMISSION);
    }
    // 校验使用人数是否已满
    ResultData data = this.userService.checkUserNumber(user);
    if (data != null) {
      return data;
    }
    // 使用人数未满
    return ResultData.build().success();
  }

  // 获取验证码
  @RequestMapping(method = RequestMethod.GET, value = "/authCode")
  public ResultData authCode() {
    return null;
  }
  
  // 获取试用,加载数据
  @RequestMapping(method = RequestMethod.GET,value = "tryOut")
  @ResponseBody
  public ResultData tryOut(HttpSession session,String coding){
    ResultData map = ResultData.build();
    // 校验验证码是否正确
    Boolean flag = (Boolean) session.getAttribute(SessionConstant.AUTH_CODE_FLAG);
    if(flag == null || !flag){
      return map.setStatus(Status.VERIFY_CODE_FAIL);
    }
    session.removeAttribute(SessionConstant.AUTH_CODE_FLAG);
    User user = userService.tryOut();
    if (user == null || user.getBelongs() == -1) {
      return map.setStatus(Status.ERROR);
    }
    // 用户信息存储至session
    session.setAttribute(SessionConstant.FRONT_USER_SESSION, user);
    // 添加登录信息
    String token = userLoginService.insert(user, coding);
    session.setAttribute("token", token);
    session.setAttribute("permission", permissionInfoService.findPidsByUid(user.getId()));
    userService.getIndexResource(map,user);
    return ResultData.build().parseMap(map);
  }

}
