package com.qianfan365.jcstore.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.ClientBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.ClientConstant.SoftStatus;
import com.qianfan365.jcstore.common.constant.ClientConstant.TrialAccount;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageInform;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.MessageConstant.PushInform;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.UserConstant;
import com.qianfan365.jcstore.common.pojo.Client;
import com.qianfan365.jcstore.common.pojo.ClientExample;
import com.qianfan365.jcstore.common.pojo.ClientExample.Criteria;
import com.qianfan365.jcstore.common.pojo.CustomerExample;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.OrderInfoDetailExample;
import com.qianfan365.jcstore.common.pojo.OrderInfoExample;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.ShopExample;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserExample;
import com.qianfan365.jcstore.common.pojo.UserInfoExample;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.dao.inter.ClientMapper;
import com.qianfan365.jcstore.dao.inter.CustomerMapper;
import com.qianfan365.jcstore.dao.inter.OrderInfoDetailMapper;
import com.qianfan365.jcstore.dao.inter.OrderInfoMapper;
import com.qianfan365.jcstore.dao.inter.ShopMapper;
import com.qianfan365.jcstore.dao.inter.UserInfoMapper;
import com.qianfan365.jcstore.dao.inter.UserMapper;

/**
 * 后台留存账户操作相关
 * 
 * @author szz
 *
 */
@Service
public class ClientService {

  @Autowired
  private ClientMapper clientMapper;
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserService userService;
  @Autowired
  private ShopService shopService;
  @Autowired
  private UserInfoMapper userInfoMapper;
  @Autowired
  private CustomerMapper customerMapper;
  @Autowired
  private ShopMapper shopMapper;
  @Autowired
  private ProductService productService;
  @Autowired
  private OrderInfoMapper orderInfoMapper;
  @Autowired
  private ProductGroupService productGroupService;
  @Autowired
  private OrderInfoDetailMapper orderInfoDetailMapper;
  @Autowired
  private UserLoginService userLoginService;
  @Autowired
  private MessageService messageService;
  @Autowired
  private SoftTypeService softTypeService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private CustomerGroupService customerGroupService;

  // 试用时长,单位月
  @Value("${trial.mouths}")
  Integer trialMouths;
  // 试用客户名
  @Value("${trial.username}")
  String trialClientName;
  // 临时账号默认使用人数
  @Value("${trial.userNumber}")
  Integer trialUserNumber;

  /**
   * 分页查询
   * 
   * @param pageNum
   * @return
   */
  public Page<ClientBean> list(Integer pageNum, boolean isTrialAccount) {
    ClientExample example = new ClientExample();
    // 创建排序规则
    if (isTrialAccount) {
      example.setOrderByClause(" createtime desc, id asc");
    } else {
      example.setOrderByClause(" expired_time asc, updatetime desc");
    }
    // 非测试账号
    example.createCriteria().andIsTrialAccountEqualTo(isTrialAccount);
    // 开始分页
    PageHelper.startPage(pageNum, 20);
    Page<Client> page = (Page<Client>) clientMapper.selectByExample(example);
    // 结果转化为bean
    return convertToBean(page);
  }

  /**
   * 转换成bean
   * 
   * @param clients
   * @return
   */
  private Page<ClientBean> convertToBean(Page<Client> clients) {
    Page<ClientBean> page = new Page<ClientBean>();
    page.setPageSize(20);
    page.setTotal(clients.getTotal());
    page.setPageNum(clients.getPageNum());
    HashMap<Integer, String> map = softTypeService.getSoftTypeInfo();
    // 转换为bean
    ArrayList<ClientBean> list = new ArrayList<ClientBean>();
    for (Client client : clients) {
      ClientBean clientBean = new ClientBean();
      try {
        BeanUtils.copyProperties(clientBean, client);
      } catch (Exception e) {}
      if (client.getExpiredTime() != null)
        clientBean.setStatus(client.getExpiredTime().after(new Date()) ? "正常" : "到期");
      clientBean.setUserNumber(clientBean.getUserNumber() + "人");
      clientBean.setType(map.get(client.getType()));
      list.add(clientBean);
    }
    page.addAll(list);
    return page;
  }

  /**
   * 保存新账户
   * 
   * @param client
   * @param months
   * @return
   */
  public Client save(Client client, Integer months) {
    // 计算到期时间
    Date nowTime = new Date();
    Date expiredTime = createClient(client, nowTime, months);
    // 增添前台用户
    User user = userService.createBasicUserBean(client, nowTime, expiredTime);
    user.setIsTrialAccount(UserConstant.TrialAccount.NO);
    user.setPassword(DigestUtils.md5Hex("yikaidan"));
    user.setSoftId(client.getType());
    // 保存账户信息
    boolean flag = userService.insertUser(user);
    if (!flag) {
      return null;
    }
    // 生成用户附加信息
    userService.generateUserInfo(user);
    // 为用户生成店铺
    shopService.addShop(user.getId());
    // 保存完前台用户后将ID设置给后台账户
    client.setUid(user.getId());
    client.setIsTrialAccount(TrialAccount.NO);
    this.clientMapper.insert(client);
    if(SoftTypeConstant.softMap().get(client.getType()).contains(ModuleType.MESSAGE)){
      String stype = softTypeService.getSoftName(client.getType());
      String sopen = "开通时间：";
      String smonths = months + "个月。";
      List<UserPermission> uplist =
          permissionInfoService.findMessageNeed(client.getUid(), PermissionType.SERVICE_CHANGE_REMIND);
      messageService.insertBatch(String.format(Locale.JAPAN, MessageInform.SERVICE_CHANGE, stype,
          client.getUserNumber(), sopen, smonths, expiredTime), PushInform.SERVICE_CHANGE, uplist,
          PermissionType.SERVICE_CHANGE_REMIND, null, MessageType.SERVICE_CHANGE, 0, false);
    }
    return client;
  }

  private Date createClient(Client client, Date nowTime, Integer months) {
    Date expiredTime = DateUtils.addMonths(nowTime, months);
    client.setExpiredTime(expiredTime);
    client.setCreatetime(nowTime);
    client.setUpdatetime(nowTime);
    client.setIsTrialAccount(TrialAccount.NO);
    return expiredTime;
  }

  /**
   * 获取账户信息
   * 
   * @param clientId
   * @return
   */
  public ClientBean selectById(Integer clientId) {
    Client client = this.clientMapper.selectByPrimaryKey(clientId);
    ClientBean bean = new ClientBean();
    try {
      BeanUtils.copyProperties(bean, client);
      bean.setSoftTypeName(this.softTypeService.getSoftName(client.getType()));
    } catch (Exception e) {
    }
    return bean;
  }

  /**
   * 更新账户信息
   * 
   * @param client
   */
  public void updateClient(Client client) {
    Client record = new Client();
    record.setCompanyName(client.getCompanyName());
    record.setContactWay(client.getContactWay());
    record.setUpdatetime(new Date());
    record.setId(client.getId());
    this.clientMapper.updateByPrimaryKeySelective(record);
  }

  /**
   * 续费升级
   * 
   * @param type
   * @param userNumber
   * @param months
   * @param clientId
   */
  public ResultData renew(Integer type, Integer userNumber, Integer months, Integer clientId) {
    Client client = this.clientMapper.selectByPrimaryKey(clientId);
    // 1.4期 试用账户不可续费升级
    if (client == null || client.getIsTrialAccount()) {
      return ResultData.build().error().put("statusMsg", "找不到对应账户");
    }
    if (userNumber < client.getUserNumber()) {
      return ResultData.build().error().put("statusMsg", "请输入不小于原使用人数的整数");
    }
    String stype = softTypeService.getSoftName(type);
    String sopen = "", smonths = "";
    Date expiredTime = client.getExpiredTime();
    // 更新数据
    client.setUserNumber(userNumber);
    client.setType(type);
    if (months > 0) {
      sopen = "续费时间：";
      smonths = months + "个月。";
    }
    // 更新到期时间
    expiredTime = DateUtils.addMonths(client.getExpiredTime(), months);
    client.setExpiredTime(expiredTime);
    // 更新前台用户到期时间
    UserExample example = new UserExample();
    example.createCriteria().andIdEqualTo(client.getUid());
    example.or(example.createCriteria().andBelongsEqualTo(client.getUid()));
    List<User> list = this.userMapper.selectByExample(example);
    ArrayList<Integer> ids = new ArrayList<Integer>();
    Date nowTime = new Date();
    for (User user : list) {
      user.setExpiredTime(expiredTime);
      user.setUpdatetime(nowTime);
      user.setSoftId(type);
      userMapper.updateByPrimaryKeySelective(user);
      ids.add(user.getId());
    }
    // 更新前台用户登录信息
    userLoginService.updateLoginInfo(expiredTime, ids, nowTime);
    client.setUpdatetime(new Date());
    this.clientMapper.updateByPrimaryKeySelective(client);
    if(SoftTypeConstant.softMap().get(list.get(0).getSoftId()).contains(ModuleType.MESSAGE)){
      List<UserPermission> uplist =
          permissionInfoService.findMessageNeed(client.getUid(), PermissionType.SERVICE_CHANGE_REMIND);
      messageService.insertBatch(String.format(Locale.JAPAN, MessageInform.SERVICE_CHANGE, stype,
          userNumber, sopen, smonths, expiredTime), PushInform.SERVICE_CHANGE, uplist,
          PermissionType.SERVICE_CHANGE_REMIND, null, MessageType.SERVICE_CHANGE, 0, false);
    }
    return ResultData.build().success();
  }

  /**
   * 删除账户信息
   * 
   * @param clientId
   * @return
   */
  public ResultData delete(Integer clientId) {
    // 判断账户是否存在
    Client client = this.clientMapper.selectByPrimaryKey(clientId);
    if (client == null) {
      return ResultData.build().error().put("statusMsg", "找不到对应账户");
    }
    // 此时删除为彻底清空数据,不做逻辑删除
    // 获取对应店铺
    ShopExample shopExample = new ShopExample();
    shopExample.createCriteria().andUserIdEqualTo(client.getUid());
    List<Shop> list = this.shopMapper.selectByExample(shopExample);
    if (!list.isEmpty()) {
      Shop shop = list.get(0);
      // 删除其下所有商品及订单
      productService.deleteAllProduct(shop);
      // 删除全部商品分组
      productGroupService.deleteAllProductGroup(shop);
      // 删除全部订单详情
      OrderInfoExample orderInfoExample = new OrderInfoExample();
      orderInfoExample.createCriteria().andShopIdEqualTo(shop.getId());
      List<OrderInfo> list2 = this.orderInfoMapper.selectByExample(orderInfoExample);
      ArrayList<Integer> ids = new ArrayList<Integer>();
      for (OrderInfo orderInfo : list2) {
        ids.add(orderInfo.getId());
      }
      if (!ids.isEmpty()) {
        OrderInfoDetailExample orderInfoDetailExample = new OrderInfoDetailExample();
        orderInfoDetailExample.createCriteria().andOrderInfoIdIn(ids);
        // 删除全部订单详情
        this.orderInfoDetailMapper.deleteByExample(orderInfoDetailExample);
      }
      // 删除全部订单
      this.orderInfoMapper.deleteByExample(orderInfoExample);
      // 删除店铺
      this.shopMapper.deleteByPrimaryKey(shop.getId());
    }
    // 删除全部客户分组信息
    this.customerGroupService.deleleByBelongs(client.getUid());
    // 删除其全部客户信息
    CustomerExample customerExample = new CustomerExample();
    customerExample.createCriteria().andBelongsEqualTo(client.getUid());
    customerMapper.deleteByExample(customerExample);
    // 删除前台账户
    UserExample example = new UserExample();
    example.createCriteria().andIdEqualTo(client.getUid());
    example.or(example.createCriteria().andBelongsEqualTo(client.getUid()));
    List<User> users = this.userMapper.selectByExample(example);
    ArrayList<Integer> ids = new ArrayList<Integer>();
    for (User user : users) {
      ids.add(user.getId());
    }
    // 更新全部登录信息
    userLoginService.updateLoginInfoWhenDelete(ids);
    // 删除全部附加信息
    UserInfoExample infoExample = new UserInfoExample();
    infoExample.createCriteria().andUidIn(ids);
    this.userInfoMapper.deleteByExample(infoExample);
    // 删除全部信息及权限
    this.messageService.delByBelongs(client.getUid());
    this.permissionInfoService.delByBelongs(client.getUid());
    // 删除用户信息
    this.userMapper.deleteByExample(example);
    // 删除账户
    this.clientMapper.deleteByPrimaryKey(clientId);
    return ResultData.build().success();
  }

  public ResultData resetPassword(Integer clientId) {
    Client client = this.clientMapper.selectByPrimaryKey(clientId);
    if (client == null) {
      return ResultData.build().error().put("statusMsg", "找不到对应账户");
    }
    UserExample example = new UserExample();
    example.createCriteria().andIdEqualTo(client.getUid());
    List<User> list = this.userMapper.selectByExample(example);
    if (!list.isEmpty()) {
      User user = list.get(0);
      user.setPassword(DigestUtils.md5Hex("yikaidan"));
      user.setUpdatetime(new Date());
      this.userMapper.updateByPrimaryKeySelective(user);
      return ResultData.build().success();
    }
    return ResultData.build().error().put("statusMsg", "找不到对应前台账户");
  }

  /**
   * 条件查询
   * 
   * @param type
   * @param status
   * @param keys
   * @param values
   * @return
   */
  public Page<ClientBean> searchByCondition(Integer type, Integer status, String[] keys,
      String[] values, Integer pageNum) {
    PageHelper.startPage(pageNum, 20);
    ClientExample example = new ClientExample();
    // 排序规则
    example.setOrderByClause(" expired_time asc, updatetime desc");
    Criteria criteria = example.createCriteria();
    // 软件类型
    if (type != null && type != 0) {
      criteria.andTypeEqualTo(type);
    }
    if (status != null) {
      // 到期
      if (status == SoftStatus.EXPIRE) {
        criteria.andExpiredTimeLessThanOrEqualTo(new Date());
      }
      // 正常
      if (status == SoftStatus.NORMAL) {
        criteria.andExpiredTimeGreaterThan(new Date());
      }
    }
    if (keys != null) {
      for (int i = 0; i < keys.length; i++) {
        if (keys[i].equals("username")) {
          criteria.andUsernameLike("%" + values[i] + "%");
        }
        if (keys[i].equals("clientName")) {
          criteria.andClientNameLike("%" + values[i] + "%");
        }
        if (keys[i].equals("companyName")) {
          criteria.andCompanyNameLike("%" + values[i] + "%");
        }
        if (keys[i].equals("contactWay")) {
          criteria.andContactWayLike("%" + values[i] + "%");
        }
      }
    }
    Page<Client> selectByExample = (Page<Client>) this.clientMapper.selectByExample(example);
    return convertToBean(selectByExample);
  }

  /**
   * 根据用户ID获取账户对象
   * 
   * @param uid
   * @return
   */
  public Client getByUID(Integer uid) {
    ClientExample example = new ClientExample();
    example.createCriteria().andUidEqualTo(uid);
    List<Client> list = this.clientMapper.selectByExample(example);
    return list.isEmpty() ? null : list.get(0);
  }

  /**
   * 体验账号,后台生成基础信息
   */
  public User tryOut() throws Exception {
    // 生成一个后台账户对象
    Client client = new Client();
    client.setClientName(trialClientName);
    Date nowTime = new Date();
    Date expiredTime = createClient(client, nowTime, trialMouths);
    // 生成一个用户对象
    User user = userService.createBasicUserBean(client, nowTime, expiredTime);
    // 密码为随机生成
    user.setPassword(UUID.randomUUID().toString().replaceAll("-", ""));
    // 设置为试用账号
    user.setIsTrialAccount(UserConstant.TrialAccount.YES);
    // 生成账户
    boolean insertUser = false;
    do {
      // 生成用户名
      user.setUsername(userService.generateTryOutUserName());
      insertUser = userService.insertUser(user);
    } while (!insertUser);
    // 生成用户附加信息
    userService.generateUserInfo(user);
    // 创建店铺
    shopService.addTrialAccountShop(user.getId());
    // 保存完前台用户后将ID设置给后台账户
    client.setUid(user.getId());
    client.setType(1); // 体验账号默认版本
    client.setUserNumber(trialUserNumber);
    client.setUsername(user.getUsername());
    client.setIsTrialAccount(TrialAccount.YES);
    client.setCompanyName("体验账号");
    this.clientMapper.insert(client);
    return user;
  }

  /**
   * 体验账号额外删除user_login信息
   * 
   * @param cid
   */
  public void deleteTrial(Integer cid) {
    Integer uid = this.clientMapper.selectByPrimaryKey(cid).getUid();
    this.delete(cid);
    userLoginService.deleteByUid(uid);
  }

}
