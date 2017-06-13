package com.qianfan365.jcstore.service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.StaticsBean;
import com.qianfan365.jcstore.common.constant.ClientConstant.TrialAccount;
import com.qianfan365.jcstore.common.constant.CommonConstant.UserTypeConst;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.UserConstant.UserStatus;
import com.qianfan365.jcstore.common.pojo.Client;
import com.qianfan365.jcstore.common.pojo.ClientExample;
import com.qianfan365.jcstore.common.pojo.Module;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserExample;
import com.qianfan365.jcstore.common.pojo.UserExample.Criteria;
import com.qianfan365.jcstore.common.pojo.UserInfo;
import com.qianfan365.jcstore.common.pojo.UserInfoExample;
import com.qianfan365.jcstore.common.util.CommonUtils;
import com.qianfan365.jcstore.dao.inter.ClientMapper;
import com.qianfan365.jcstore.dao.inter.UserInfoMapper;
import com.qianfan365.jcstore.dao.inter.UserMapper;

/**
 * 用户操作相关
 * 
 * @author szz
 */
@Service
public class UserService {

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserInfoMapper userInfoMapper;
  @Autowired
  private ClientMapper clientMapper;
  @Autowired
  private PermissionInfoService permissionService;
  @Autowired
  private UserLoginService userLoginService;
  @Autowired
  private ShopService shopService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private StaticsService staticsService;
  @Autowired
  private ClientService clientService;
  @Autowired
  private ProductService productService;
  @Autowired
  private SoftTypeService softTypeService;

  /**
   * 登陆方法
   * 
   * @param username
   * @param password
   * @return
   */
  public User login(String username, String password) {
    // 加密
    password = DigestUtils.md5Hex(password);
    // 添加条件
    UserExample example = new UserExample();
    example.createCriteria().andUsernameEqualTo(username).andPasswordEqualTo(password)
        .andStatusEqualTo(true);
    // 查询
    List<User> users = this.userMapper.selectByExample(example);
    // 判断用户是否存在
    if (users.isEmpty()) {
      return null;
    }
    return users.get(0);
  }

  /**
   * 校验用户名是否存在
   * 
   * @param username
   * @return true不存在 false存在
   */
  public boolean checkUsername(String username) {
    UserExample example = new UserExample();
    example.createCriteria().andUsernameEqualTo(username).andStatusEqualTo(UserStatus.NORMAL);
    List<User> list = this.userMapper.selectByExample(example);
    return list.isEmpty() ? true : false;
  }

  /**
   * 添加用户
   * 
   * @param user
   * @param userInfo
   * @param currentUser
   */
  public ResultData add(User user, UserInfo userInfo, User currentUser) {
    // 校验使用人数
    ResultData data = this.checkUserNumber(currentUser);
    if (data != null) return data;
    // 添加用户
    try {
      user.setExpiredTime(currentUser.getExpiredTime());
      user.setBelongs(currentUser.getId());
      Date nowTime = new Date();
      user.setCreatetime(nowTime);
      user.setUpdatetime(nowTime);
      user.setLogo("");
      user.setStatus(UserStatus.NORMAL);
      user.setPassword(DigestUtils.md5Hex("yikaidan"));
      user.setIsTrialAccount(TrialAccount.NO);
      user.setSoftId(currentUser.getSoftId());
      // 保存账户信息
      boolean flag = this.insertUser(user);
      if (!flag) {
        return ResultData.build().setStatus(Status.REGIST_NAME_EXISTED);
      }
      // 开始保存扩展信息
      userInfo.setUid(user.getId());
      userInfoMapper.insert(userInfo);
      permissionService.initPermission(user.getId(), user.getBelongs());
      // 返回数据
      user.setPassword(null);
      return ResultData.build().success().put("user", user).put("userInfo", userInfo);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResultData.build().error();
  }

  /**
   * 所有向用户表新增数据均使用此方法
   * 
   * @param user
   * @return
   */
  public boolean insertUser(User user) {
    synchronized (UserService.class) {
      // 用户名可以使用
      if (this.checkUsername(user.getUsername())) {
        this.userMapper.insert(user);
        return true;
      } else {
        return false;
      }
    }
  }

  /**
   * 根据ID获取用户信息
   * 
   * @param id
   * @param currentid
   * @return
   */
  public ResultData findById(Integer id, Integer currentId) {
    ResultData map = ResultData.build();
    // 查询用户
    User user = null;
    if (currentId.equals(id)) {
      user = userMapper.selectByPrimaryKey(id);
    } else {
      UserExample example = new UserExample();
      example.createCriteria().andIdEqualTo(id).andBelongsEqualTo(currentId)
          .andStatusEqualTo(UserStatus.NORMAL);
      List<User> list = userMapper.selectByExample(example);
      if (!list.isEmpty()) {
        user = list.get(0);
      }
    }
    if (user == null) {
      return ResultData.build().setStatus(Status.DATA404);
    }
    user.setPassword("");
    map.put("user", user);
    // 查询用户扩展信息
    UserInfoExample userInfoExample = new UserInfoExample();
    userInfoExample.createCriteria().andUidEqualTo(id);
    List<UserInfo> list = this.userInfoMapper.selectByExample(userInfoExample);
    if (!list.isEmpty()) {
      map.put("userInfo", list.get(0));
    }
    return map.success();
  }

  public User findUserById(Integer uid) {
    UserExample example = new UserExample();
    example.createCriteria().andIdEqualTo(uid).andStatusEqualTo(UserStatus.NORMAL);
    List<User> user = userMapper.selectByExample(example);
    return user.isEmpty() ? null : user.get(0);
  }

  /**
   * 更新
   * 
   * @param userInfo
   * @return
   */
  public ResultData update(UserInfo userInfo) {
    try {
      UserInfoExample example = new UserInfoExample();
      example.createCriteria().andUidEqualTo(userInfo.getUid());
      int i = this.userInfoMapper.updateByExampleSelective(userInfo, example);
      if (i == 1) {
        // 更新用户信息
        User user = new User();
        user.setId(userInfo.getUid());
        user.setUpdatetime(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        return ResultData.build().success();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResultData.build().error();
  }

  /**
   * 更新密码
   * 
   * @param integer
   * @param password
   * @param newPassword
   * @return
   */
  public ResultData updatePassword(Integer id, String password, String newPassword) {
    // 校验原密码是否正确
    UserExample example = new UserExample();
    example.createCriteria().andIdEqualTo(id).andPasswordEqualTo(DigestUtils.md5Hex(password));
    List<User> list = userMapper.selectByExample(example);
    if (list.isEmpty()) {
      return ResultData.build().setStatus(Status.OLD_PASSWORD_ERROR);
    }
    // 更新密码
    User user = list.get(0);
    user.setPassword(DigestUtils.md5Hex(newPassword));
    user.setUpdatetime(new Date());
    userMapper.updateByPrimaryKey(user);
    return ResultData.build().success();
  }

  /**
   * 重置密码
   * 
   * @param id
   * @param currentId
   * @return
   */
  public ResultData resetPassword(Integer id, Integer currentId) {
    // 校验是否可进行重置
    UserExample example = new UserExample();
    example.createCriteria().andIdEqualTo(id).andBelongsEqualTo(currentId);
    User user = new User();
    user.setPassword(DigestUtils.md5Hex("yikaidan"));
    user.setUpdatetime(new Date());
    int i = userMapper.updateByExampleSelective(user, example);
    if (i == 1) {
      return ResultData.build().success();
    }
    return ResultData.build().error();
  }

  /**
   * 根据belongsId查询出所有的子员工
   * 
   * @param belongsId
   * @return
   */
  public List<User> findUserBelongId(Integer belongsId) {
    UserExample userExample = new UserExample();
    userExample.createCriteria().andBelongsEqualTo(belongsId);
    List<User> user = userMapper.selectByExample(userExample);
    return user;

  }
  
  /**
   * 根据用户user返回店主user
   * 
   * @param user
   * @return
   */
  public User findBelongsByUser(User user) {
    if(user.getBelongs()==0){
      return user;
    }
    return userMapper.selectByPrimaryKey(user.getBelongs());
  }

  /**
   * 根据uid 查询user信息
   * 
   * @param uid
   * @return
   */
  public User findUser(Integer uid) {
    return userMapper.selectByPrimaryKey(uid);

  }

  /**
   * 更新用户
   * 
   * @param user
   */
  public void updateUser(User user) {
    // 设置更新时间
    user.setUpdatetime(new Date());
    this.userMapper.updateByPrimaryKeySelective(user);
  }

  /**
   * 删除用户
   * 
   * @param currentUserId 当前登陆的用户ID
   * @param uid 要删除的用户ID
   * @return
   */
  public int delete(Integer currentUserId, Integer uid) {
    UserExample example = new UserExample();
    example.createCriteria().andIdEqualTo(uid).andBelongsEqualTo(currentUserId);
    User user = new User();
    Date nowTime = new Date();
    user.setUpdatetime(nowTime);
    user.setStatus(UserStatus.DELETE);// 删除用户
    int i = userMapper.updateByExampleSelective(user, example);
    if (i == 1) {
      // 更新用户登录信息
      userLoginService.updateLoginInfo(uid, nowTime);
    }
    return i;
  }

  /**
   * 获取用户列表
   * 
   * @param id
   * @param currentPage
   * @param querytime
   * @return
   */
  public Page<User> listUser(Integer id, Integer currentPage, Long querytime) {
    UserExample example = new UserExample();
    example.setOrderByClause(" createtime asc");
    Criteria criteria = example.createCriteria();
    criteria.andBelongsEqualTo(id).andStatusEqualTo(UserStatus.NORMAL);
    if (querytime != null) {
      criteria.andCreatetimeLessThanOrEqualTo(new Date(querytime));
    }
    PageHelper.startPage(currentPage, 20);
    Page<User> users = (Page<User>) userMapper.selectByExample(example);
    for (User user : users) {
      user.setPassword(null);
    }
    return users;
  }

  /**
   * 获取即将到期的店主用户id
   * 
   * @return
   */
  public List<Integer> findWillDueBelongs() {
    // 发即将到期提醒
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DATE, 15); // 数据库中的时间只到天 不用between了
    Date d = calendar.getTime();
    // calendar.add(Calendar.DATE, 1);
    // Date d1 = calendar.getTime();
    List<Integer> SoftIds =
        SoftTypeConstant.softMap().entrySet().stream()
            .filter(e -> e.getValue().contains(ModuleType.MESSAGE)).map(e -> e.getKey())
            .collect(Collectors.toList());
    UserExample example = new UserExample();
    example.createCriteria().andBelongsEqualTo(0).andExpiredTimeEqualTo(d)
        .andIsTrialAccountEqualTo(false).andSoftIdIn(SoftIds); // 数据库中的时间只到天 不用between了
    List<User> userlist = userMapper.selectByExample(example);
    List<Integer> belongs = userlist.stream().map(u -> u.getId()).collect(Collectors.toList());
    return belongs;
  }

  /**
   * 获取未到期的店主用户id
   * 
   * @return
   */
  public List<Integer> findValidBelongs() {
    Date date = new Date();
    List<Integer> SoftIds =
        SoftTypeConstant
            .softMap()
            .entrySet()
            .stream()
            .filter(
                e -> e.getValue().containsAll(
                    Lists.newArrayList(ModuleType.MESSAGE, ModuleType.STATICS)))
            .map(e -> e.getKey()).collect(Collectors.toList());
    UserExample example = new UserExample();
    example.createCriteria().andBelongsEqualTo(0).andExpiredTimeGreaterThan(date)
        .andIsTrialAccountEqualTo(false).andSoftIdIn(SoftIds);
    List<User> userlist = userMapper.selectByExample(example);
    List<Integer> belongs = userlist.stream().map(u -> u.getId()).collect(Collectors.toList());
    return belongs;
  }

  /**
   * 获取首页所需的数据
   * 
   * @param map
   * @param user
   */
  public void getIndexResource(ResultData map, User user) {
    User key = this.userMapper.selectByPrimaryKey(user.getId());
    key.setPassword(null);
    map.put("user", key);
    // 返回店铺信息
    map.put("shop", shopService.findShop(user));
    Map<Integer, Boolean> permission =
        permissionInfoService.findMapByUidPid(PermissionType.PLACE_ORDER, user.getId(),
            user.getBelongs());
    permission.put(0, user.getBelongs() == 0);
    // 返回权限信息
    map.put("permissionInfo", permission);
    String[] moduleIds =
        softTypeService.getById(userMapper.selectByPrimaryKey(user.getId()).getSoftId())
        .getModuleId().split(",");
    List<Module> list = ModuleData.getModules();
    List<String> asList = Arrays.asList(moduleIds);
    // 返回是否具有消息
    if (user.getIsTrialAccount() || !asList.contains(ModuleType.MESSAGE)) {
      map.put("newMessageFlag", false);
      map.put("newSystemMessage", "");
    } else {
      map.put("newMessageFlag", messageService.newMessageFlag(user.getId()));
      // 系统消息
      map.put("newSystemMessage", messageService.newSystemMessage(user.getId()));
    }
    // 返回统计概况
    Date nowTime = new Date();
    Date date = DateUtils.setDays(nowTime, 1);
    Date start = DateUtils.truncate(date, Calendar.DATE);
    Date end = DateUtils.addMonths(start, 1);
    StaticsBean statics = staticsService.getStatics(user, start, end,0);
    StaticsBean todayStatics = staticsService.getStatics(user, new Date(), new Date(),0);
    map.put("statics", statics);
    map.put("todayStatics", todayStatics);
    // 返回版本信息
    HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
    for (int i = 0; i < list.size(); i++) {
      hashMap.put(list.get(i).getId().toString(), asList.contains(String.valueOf(i + 1)));
    }
    //考虑一键营销的子项的有无重新判断首页一键营销的模块显示
    hashMap.put(ModuleType.MARKETING, isMarketingModule(user));
    map.put("softType", hashMap);
  }
  
  public HashMap<String, Boolean> getModuleResource(User user){
  	String[] moduleIds =
        softTypeService.getById(userMapper.selectByPrimaryKey(user.getId()).getSoftId())
        .getModuleId().split(",");
    List<Module> list = ModuleData.getModules();
    List<String> asList = Arrays.asList(moduleIds);
    // 返回版本信息
    HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();
    for (int i = 0; i < list.size(); i++) {
      hashMap.put(list.get(i).getId().toString(), asList.contains(String.valueOf(i + 1)));
    }
    //考虑一键营销的子项的有无重新判断首页一键营销的模块显示
    hashMap.put(ModuleType.MARKETING, isMarketingModule(user));
    return hashMap;
  }
  
  /**
   * 考虑一键营销的子项的有无重新判断是否有一键营销的模块
   * @param user
   * @return
   */
  public Boolean isMarketingModule(User user){
  	List<String> mod = SoftTypeConstant.softMap().get(user.getSoftId());
  	Map<Integer, Boolean> perm = permissionService.findMapByUidPids(
    	Arrays.asList(PermissionType.SCENE_MARKETING,PermissionType.ADVERTIS_MARKETING),
    	user.getId(), user.getBelongs());
  	Boolean isMarketing = mod.contains(ModuleType.MARKETING)
  		&&((mod.contains(ModuleType.ADVERTIS_MARKETING)&&perm.get(PermissionType.ADVERTIS_MARKETING))
  			||(mod.contains(ModuleType.SCENE_MARKETING)&&perm.get(PermissionType.SCENE_MARKETING))
  			||mod.contains(ModuleType.PRODUCT));
  	return isMarketing;
  }

  /**
   * 根据用户ID查找员工信息
   * 
   * @param uId
   * @return
   */
  public UserInfo findByUid(Integer uId) {
    UserInfoExample example = new UserInfoExample();
    example.createCriteria().andUidEqualTo(uId);
    List<UserInfo> userInfo = userInfoMapper.selectByExample(example);
    if (userInfo.isEmpty()) {
      return null;
    }
    return userInfo.get(0);
  }

  /**
   * 根据用户名找id
   * 
   * @param username
   * @return
   */
  public User findByUsername(String username) {
    UserExample userExample = new UserExample();
    userExample.createCriteria().andUsernameEqualTo(username)
        .andBelongsEqualTo(UserTypeConst.ADMIN).andStatusEqualTo(UserStatus.NORMAL)
        .andIsTrialAccountEqualTo(false).andExpiredTimeGreaterThan(new Date());
    List<User> users = userMapper.selectByExample(userExample);
    return users.isEmpty() ? null : users.get(0);
  }

  /**
   * 查找所有用户的uid
   * 
   * @return
   */
  public List<Integer> findIdByAll() {
    return userMapper.selectIdByAll();
  }

  /**
   * 查找所有商家的uid
   * 
   * @return
   */
  public List<Integer> findIdByBusiness() {
    return userMapper.selectIdByBusiness();
  }

  /**
   * 校验使用人数
   * 
   * @param currentUser
   * @return
   */
  public ResultData checkUserNumber(User currentUser) {
    // 判断使用人数
    ClientExample example = new ClientExample();
    example.createCriteria().andUidEqualTo(currentUser.getId());
    List<Client> list = clientMapper.selectByExample(example);
    if (list.isEmpty()) {
      return ResultData.build().error();
    }
    Integer userNumber = list.get(0).getUserNumber();
    UserExample userExample = new UserExample();
    userExample.createCriteria().andBelongsEqualTo(currentUser.getId())
        .andStatusEqualTo(UserStatus.NORMAL);
    int i = userMapper.countByExample(userExample);
    if ((i >= (userNumber - 1)) && userNumber != -1) {
      return ResultData.build().setStatus(Status.USER_NUMBER_FULL);
    }
    return null;
  }

  public User createBasicUserBean(Client client, Date nowTime, Date expiredTime) {
    User user = new User();
    user.setBelongs(0);// 设置为0是表示为平台添加账户
    user.setCreatetime(nowTime);
    user.setUpdatetime(nowTime);
    user.setExpiredTime(expiredTime);
    user.setLogo("");
    user.setStatus(UserStatus.NORMAL);
    user.setUsername(client.getUsername());
    user.setStaffname(client.getClientName());
    user.setSoftId(1);
    return user;
  }

  public void generateUserInfo(User user) {
    // 生成店长的用户信息
    UserInfo userInfo = new UserInfo();
    userInfo.setMobile("");
    userInfo.setPosition("超级管理员");
    userInfo.setRemark("");
    userInfo.setUid(user.getId());
    userInfoMapper.insert(userInfo);
  }

  /**
   * 获取一个用户所属的管理员ID
   * 
   * @param user
   * @return
   */
  public Integer getAdminID(User user) {
    return user.getBelongs() == 0 ? user.getId() : user.getBelongs();
  }

  /**
   * 开始试用
   * 
   * @return
   */
  public User tryOut() {
    ResultData map = ResultData.build();
    try {
      // 生成体验账号基础信息
      User user = clientService.tryOut();
      // 返回前台用户ID/登录所需数据
      this.getIndexResource(map, user);
      // 生成默认数据
      productService.generateDefaultProduct(user);
      return user;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 生成用户名
   * 
   * @return
   */
  public String generateTryOutUserName() {
    SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmm");
    StringBuilder sb;
    do {
      sb = new StringBuilder();
      sb.append("ty").append(CommonUtils.generateNum(5).get()).append(format.format(new Date()));
    } while (!checkUsername(sb.toString()));
    return sb.toString();
  }
}
