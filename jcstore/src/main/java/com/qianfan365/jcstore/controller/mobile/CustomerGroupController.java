package com.qianfan365.jcstore.controller.mobile;

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
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.pojo.CustomerGroup;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.CustomerGroupService;
import com.qianfan365.jcstore.service.CustomerService;

/**
 * 客户类型操作接口
 * 
 * @author szz
 *
 */
@Controller
@RequestMapping("/mobile/customergroup")
public class CustomerGroupController extends BaseController {

  @Autowired
  private CustomerService customerService;
  @Autowired
  private CustomerGroupService customerGroupService;

  /**
   * 保存或更新客户类型
   * 
   * @param session
   * @param customerGroup
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.POST, value = "/save")
  @TrialUnavailable
  @ModulePassport(moduleids = {ModuleType.CUSTOMER,ModuleType.CUSTOMER_GROUP})
  public ResultData save(HttpSession session, CustomerGroup customerGroup) {
    // 权限校验,仅管理员可以保存客户类型
    User user = this.getLoginUser(session);
    if (user == null || user.getBelongs() != 0) {
      // 无权限时返回
      return ResultData.build().setStatus(Status.NO_PERMISSION);
    }
    // 参数校验,组名不为空且长度在8位之内,折扣1-100之间,组名不能为纯空格
    if (customerGroup != null && StringUtils.isNotEmpty(customerGroup.getGroupName())
        && customerGroup.getGroupName().length() <= 8 && customerGroup.getDiscount() != null
        && customerGroup.getDiscount() <= 100 && customerGroup.getDiscount() >= 1
        && customerGroup.getGroupName().replaceAll(" ", "").length() != 0) {
      // 设置客户类型归属用户ID
      customerGroup.setBelongs(user.getId());
      // 保存或更新此客户类型
      return this.customerGroupService.insertOrUpdate(customerGroup);
    }
    return ResultData.build().parameterError();
  }

  /**
   * 列表查询
   * 
   * @param currentPage
   * @param limit
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/list")
  @ModulePassport(moduleids = {ModuleType.CUSTOMER,ModuleType.CUSTOMER_GROUP})
  public ResultData list(@RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit, HttpSession session) {
    User user = this.getLoginUser(session);
    // 列表查询
    Page<CustomerGroup> page = this.customerGroupService.list(currentPage, limit, user);
    return ResultData.build().parsePageBean(page);
  }

  @ResponseBody
  @RequestMapping(method = RequestMethod.POST, value = "/delete")
  @TrialUnavailable
  @ModulePassport(moduleids = {ModuleType.CUSTOMER,ModuleType.CUSTOMER_GROUP})
  public ResultData delete(HttpSession session, Integer groupId) {
    // 权限校验
    User user = this.getLoginUser(session);
    if (user == null || user.getBelongs() != 0) {
      return ResultData.build().setStatus(Status.NO_PERMISSION);
    }
    // 参数校验
    if (groupId == null) {
      return ResultData.build().parameterError();
    }
    return this.customerGroupService.delete(groupId, user);
  }

  /**
   * 根据ID获取对应客户类型信息
   * 
   * @param session
   * @param groupId
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/get")
  @TrialUnavailable
  @ModulePassport(moduleids = {ModuleType.CUSTOMER,ModuleType.CUSTOMER_GROUP})
  public ResultData get(HttpSession session, Integer groupId) {
    User user = this.getLoginUser(session);
    // 参数校验
    if (groupId == null) {
      return ResultData.build().parameterError();
    }
    // 获取数据
    CustomerGroup group = this.customerGroupService.getById(groupId);
    // 校验数据
    if (group == null
        || (group != null && !group.getBelongs().equals(
            (user.getBelongs() == 0 ? user.getId() : user.getBelongs())))) {
      return ResultData.build().data404();
    }
    return ResultData.build().put("customerGroup", group);
  }
  
  /**
   * 获取全部客户类型列表
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/listAll")
  @ModulePassport(moduleids = {ModuleType.CUSTOMER,ModuleType.CUSTOMER_GROUP})
  public ResultData listAll(HttpSession session){
    User user = this.getLoginUser(session);
    List<CustomerGroup> page = customerGroupService.listAll(user.getBelongs() == 0 ? user.getId():user.getBelongs());
    return ResultData.build().put("aaData", page);
  }

}
