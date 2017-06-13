package com.qianfan365.jcstore.controller.manage;

import java.util.Arrays;
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
import com.qianfan365.jcstore.common.bean.ClientBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.ClientConstant.TrialAccount;
import com.qianfan365.jcstore.common.pojo.Client;
import com.qianfan365.jcstore.common.pojo.SoftType;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ClientService;
import com.qianfan365.jcstore.service.SoftTypeService;
import com.qianfan365.jcstore.service.UserService;

/**
 * 操作后台留存账户相关接口
 * 
 * @author szz
 *
 */
@Controller
@RequestMapping("/manage/client")
public class ClientController extends BaseController {

  @Autowired
  private ClientService clientService;
  @Autowired
  private UserService userService;
  @Autowired
  private SoftTypeService softTypeService;

  /**
   * 分页获取账户列表
   * 
   * @param pageNum
   * @return
   */
  @RequestMapping(path = "/list", method = RequestMethod.GET)
  @ResponseBody
  public ResultData list(@RequestParam(defaultValue = "1") Integer currentPage) {
    if (currentPage == null || currentPage <= 0) {
      return ResultData.build().parameterError();
    }
    Page<ClientBean> list = this.clientService.list(currentPage, TrialAccount.NO);
    return ResultData.build().parsePageBean(list);
  }

  /**
   * 新增账户
   * 
   * @param client
   * @return
   */
  @RequestMapping(path = "/add", method = RequestMethod.POST)
  @ResponseBody
  public ResultData add(Client client, Integer months) {
    // 用户名校验
    String usernameRule = "^[A-Za-z0-9]{4,20}$";
    // 数据校验
    // 同时校验联系方式
    if (client != null && StringUtils.isNotEmpty(client.getClientName())
        && StringUtils.isNotEmpty(client.getCompanyName())
        && StringUtils.isNotEmpty(client.getUsername())
        && StringUtils.isNotEmpty(client.getContactWay()) && client.getCompanyName().length() <= 40
        && client.getClientName().length() <= 20 && client.getContactWay() != null
        && client.getContactWay().length() <= 40 && client.getUserNumber() != null
        && client.getUserNumber() >= 5 && client.getType() != null && months != null && months > 0
        && client.getUsername().matches(usernameRule) && client.getUserNumber() < 100000
        && months < 1000) {
      // // 校验用户名是否重复
      // if (!userService.checkUsername(client.getUsername())) {
      // return ResultData.build().setStatus(Status.REGIST_NAME_EXISTED);
      // }
      Client save = this.clientService.save(client, months);
      if (save == null) {
        return ResultData.build().setStatus(Status.REGIST_NAME_EXISTED);
      }
      return ResultData.build().success();
    }
    return ResultData.build().parameterError();
  }

  /**
   * 校验用户名是否重复
   * 
   * @param username
   * @return
   */
  @RequestMapping(path = "/check", method = RequestMethod.GET)
  @ResponseBody
  public ResultData check(String username) {
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
   * 根据ID获取账户信息
   * 
   * @param clientId
   * @return
   */
  @RequestMapping(path = "/get", method = RequestMethod.GET)
  @ResponseBody
  public ResultData get(Integer clientId) {
    // 数据校验
    if (clientId != null && clientId > 0) {
      ClientBean client = this.clientService.selectById(clientId);
      // 判断账户是否存在
      if (client == null) {
        return ResultData.build().error().put("statusMsg", "找不到对应账户");
      }
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("aaData", client);
      return ResultData.build().parseMap(map);
    }
    return ResultData.build().parameterError();
  }

  /**
   * 更新账户信息
   * 
   * @param client
   * @return
   */
  @RequestMapping(value = "/update", method = RequestMethod.POST)
  @ResponseBody
  public ResultData update(Client client) {
    // 数据校验
    if (client != null && client.getId() != null && client.getId() > 0
        && StringUtils.isNotEmpty(client.getCompanyName())
        && StringUtils.isNotEmpty(client.getContactWay()) && client.getCompanyName().length() <= 40
        && client.getContactWay() != null && client.getContactWay().length() <= 40) {
      this.clientService.updateClient(client);
      return ResultData.build().success();
    }
    return ResultData.build().parameterError();
  }

  /**
   * 续费更新
   * 
   * @param type
   * @param userNumber
   * @param months
   * @return
   */
  @RequestMapping(path = "/renew", method = RequestMethod.POST)
  @ResponseBody
  public ResultData renew(Integer type, Integer userNumber, Integer months, Integer clientId) {
    if (type != null && userNumber != null && userNumber >= 5 && months != null && months >= 0
        && clientId != null && clientId > 0 && userNumber < 100000 && months < 1000) {
      return this.clientService.renew(type, userNumber, months, clientId);
    }
    return ResultData.build().parameterError();
  }

  /**
   * 删除账户
   * 
   * @param clientId
   * @return
   */
  @RequestMapping(path = "/delete", method = RequestMethod.GET)
  @ResponseBody
  public ResultData delete(Integer clientId) {
    if (clientId != null && clientId > 0) {
      return this.clientService.delete(clientId);
    }
    return ResultData.build().parameterError();
  }

  /**
   * 重置密码
   * 
   * @param clientId
   * @return
   */
  @RequestMapping(path = "/reset", method = RequestMethod.GET)
  @ResponseBody
  public ResultData resetPassword(Integer clientId) {
    if (clientId != null && clientId > 0) {
      return this.clientService.resetPassword(clientId);
    }
    return ResultData.build().parameterError();
  }

  /**
   * 条件查询
   * 
   * @param type 软件类型
   * @param status 软件状态
   * @param keys 模糊查询字段名
   * @param values 模糊查询字段值
   * @return
   */
  @RequestMapping(path = "/search", method = RequestMethod.GET)
  @ResponseBody
  public ResultData search(Integer type, Integer status, String[] keys, String[] values,
      @RequestParam(defaultValue = "1") Integer currentPage) {
    // 参数校验,任意查询条件存在即可进行条件查询
    if (type != null || status != null
        || (keys != null && values != null && keys.length == values.length)) {
      Page<ClientBean> page =
          this.clientService.searchByCondition(type, status, keys, values, currentPage);
      return ResultData.build().parsePageBean(page);
    }
    Page<ClientBean> list = this.clientService.list(currentPage, TrialAccount.NO);
    return ResultData.build().parsePageBean(list);
  }

  /**
   * 获取软件类型等资源
   * 
   * @param clientId
   * @return
   */
  @RequestMapping(path = "/resource", method = RequestMethod.GET)
  @ResponseBody
  public ResultData getResource(@RequestParam(defaultValue = "0") Integer clientId) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    // 获取全部类型
    if (clientId >= -1) {
      List<SoftType> types = softTypeService.getSoftType(clientId);
      map.put("types", types);
      return ResultData.build().parseMap(map);
    }
    return ResultData.build().parameterError();
  }

  /**
   * 分页获取账户列表
   * 
   * @param pageNum
   * @return
   */
  @RequestMapping(path = "/listTrial", method = RequestMethod.GET)
  @ResponseBody
  public ResultData listTrial(@RequestParam(defaultValue = "1") Integer currentPage) {
    if (currentPage == null || currentPage <= 0) {
      return ResultData.build().parameterError();
    }
    Page<ClientBean> list = this.clientService.list(currentPage, TrialAccount.YES);
    return ResultData.build().parsePageBean(list);
  }

  /**
   * 删除临时账户
   * 
   * @param clientId
   * @return
   */
  @RequestMapping(path = "/deleteTrial", method = RequestMethod.GET)
  @ResponseBody
  public ResultData deleteTrial(Integer[] clientIds) {
    if (clientIds != null && clientIds.length > 0) {
      Arrays.asList(clientIds).stream().forEach(cid -> clientService.deleteTrial(cid));
      return ResultData.build().success();
    }
    return ResultData.build().parameterError();
  }

  /**
   * 1.5 wangruoqiu 客户管理-场景营销列表
   * 
   */
  @RequestMapping({"toH5clientlist"})
  public String toScenemarketing(HttpSession session) {
    session.setAttribute("ma_head_nav", "client");
    session.setAttribute("ma_sider_nav", "scene");
    return "/manage/h5-client-list";
  }
  
  /**
   * 1.5 wangruoqiu 客户管理-全景列表
   * 
   */
  @RequestMapping({"toOveralllist"})
  public String toOveralllist(HttpSession session) {
    session.setAttribute("ma_head_nav", "client");
    session.setAttribute("ma_sider_nav", "overall");
    return "/manage/overall-list";
  }

}
