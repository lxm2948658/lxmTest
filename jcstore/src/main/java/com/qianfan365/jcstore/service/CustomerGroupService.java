package com.qianfan365.jcstore.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.CustomerGroup;
import com.qianfan365.jcstore.common.pojo.CustomerGroupExample;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.dao.inter.CustomerGroupMapper;

@Service
public class CustomerGroupService {

  @Autowired
  private CustomerService customerService;
  @Autowired
  private CustomerGroupMapper customerGroupMapper;

  /**
   * 更新或新增客户类型
   * 
   * @param customerGroup
   * @return
   */
  public ResultData insertOrUpdate(CustomerGroup customerGroup) {
    // 获取当前时间
    Date nowTime = new Date();
    // 保存或更新时均需要更新此字段
    customerGroup.setUpdatetime(nowTime);
    int i = 0;
    // 更新逻辑
    if (customerGroup.getId() != null) {
      // 仅可更新属于自己的客户类型
      CustomerGroupExample example = new CustomerGroupExample();
      example.createCriteria().andBelongsEqualTo(customerGroup.getBelongs())
          .andIdEqualTo(customerGroup.getId());
      i = customerGroupMapper.updateByExampleSelective(customerGroup, example);
      // 更新失败
      if (i != 1) {
        return ResultData.build().data404();
      }
    } else {
      // 保存逻辑
      customerGroup.setCreatetime(nowTime);
      i = customerGroupMapper.insert(customerGroup);
      if (i != 1) {
        return ResultData.build().failure();
      }
    }
    // 返回数据
    return ResultData.build().put("customerGroup", customerGroup);
  }

  /**
   * 列表查询
   * 
   * @param currentPage
   * @param limit
   * @param user
   * @return
   */
  public Page<CustomerGroup> list(Integer currentPage, Integer limit, User user) {
    PageHelper.startPage(currentPage, limit);
    CustomerGroupExample example = new CustomerGroupExample();
    // 按照更新时间倒序排列
    example.setOrderByClause(" updatetime desc,id asc");
    // 查询所有属于当前用户的分组
    example.createCriteria().andBelongsEqualTo(
        user.getBelongs() == 0 ? user.getId() : user.getBelongs());
    Page<CustomerGroup> list =
        (Page<CustomerGroup>) this.customerGroupMapper.selectByExample(example);
    return list;
  }

  /**
   * 根据ID获取客户类型对象
   * 
   * @param groupId
   * @return
   */
  public CustomerGroup getById(Integer groupId) {
    return this.customerGroupMapper.selectByPrimaryKey(groupId);
  }

  /**
   * 删除分组
   * @param groupId
   * @param user
   * @return
   */
  public ResultData delete(Integer groupId, User user) {
    CustomerGroupExample example = new CustomerGroupExample();
    example.createCriteria().andIdEqualTo(groupId)
        .andBelongsEqualTo(user.getBelongs() == 0 ? user.getId() : user.getBelongs());
    int i = this.customerGroupMapper.deleteByExample(example);
    if(i == 1){
      // 更新客户表
      this.customerService.deleteCustomerGroup(groupId,user);
      return ResultData.build().success();
    }else if(i == 0){
      return ResultData.build().data404();
    }
    return ResultData.build().failure();
  }
  
  /**
   * 根据用户ID删除全部客户类型分组
   * @param belongs
   */
  public void deleleByBelongs(Integer belongs){
    CustomerGroupExample example = new CustomerGroupExample();
    example.createCriteria().andBelongsEqualTo(belongs);
    this.customerGroupMapper.deleteByExample(example);
  }

  public List<CustomerGroup> listAll(Integer belongs) {
    CustomerGroupExample example = new CustomerGroupExample();
    // 按照更新时间倒序排列
    example.setOrderByClause(" updatetime desc,id asc");
    // 查询所有属于当前用户的分组
    example.createCriteria().andBelongsEqualTo(belongs);
    List<CustomerGroup> list = this.customerGroupMapper.selectByExample(example);
    return list;
  }
}
