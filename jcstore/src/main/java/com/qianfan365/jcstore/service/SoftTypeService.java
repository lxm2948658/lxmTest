package com.qianfan365.jcstore.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant.SoftTypeStatus;
import com.qianfan365.jcstore.common.pojo.SoftType;
import com.qianfan365.jcstore.common.pojo.SoftTypeExample;
import com.qianfan365.jcstore.dao.inter.SoftTypeMapper;

/**
 * 软件版本操作类
 * 
 * @author szz
 */

@Service("softTypeService")
public class SoftTypeService {

  @Autowired
  private SoftTypeMapper softTypeMapper;
  @Autowired
  private ClientService clientService;

  /**
   * 获取软件类型
   * 
   * @param clientId
   * @return
   */
  public List<SoftType> getSoftType(Integer clientId) {
    // 为0是表示获取全部类型,用于新增
    SoftTypeExample example = new SoftTypeExample();
    example.setOrderByClause(" updatetime desc");
    if (clientId != 0) {
      example.createCriteria().andStatusEqualTo(SoftTypeStatus.ENABLE);
      if(clientId != -1){
        example.or().andStatusEqualTo(SoftTypeStatus.DISABLE)
        .andIdEqualTo(Integer.valueOf(clientService.selectById(clientId).getType()));
      }
    }
    List<SoftType> list = this.softTypeMapper.selectByExample(example);
    return list;
  }

  /**
   * 获取软件类型名称
   * 
   * @param type
   * @return
   */
  public String getSoftName(Integer type) {
    SoftType soft = this.softTypeMapper.selectByPrimaryKey(type);
    return soft != null ? soft.getName() : "";
  }

  /**
   * 获取软件类型集合
   * 
   * @return
   */
  public HashMap<Integer, String> getSoftTypeInfo() {
    List<SoftType> type = this.getSoftType(0);
    HashMap<Integer, String> map = new HashMap<Integer, String>();
    for (SoftType softType : type) {
      map.put(softType.getId(), softType.getName());
    }
    return map;
  }

  /**
   * 保存版本信息
   * 
   * @param softType
   */
  public void save(SoftType softType) {
    if (softType.getId() != null) {
      softTypeMapper.updateByPrimaryKeySelective(softType);
    } else {
      softType.setLevel(1); // 均为默认级别,暂时无用
      softType.setStatus(SoftTypeStatus.ENABLE);
      softType.setCreatetime(softType.getUpdatetime());
      softTypeMapper.insert(softType);
    }

    // 版本信息改变重新放版本常量
    SoftTypeConstant.setSoftList(softTypeMapper.selectByExample(new SoftTypeExample()));
  }

  /**
   * 获取版本类型列表
   * 
   * @param currentPage
   * @param limit
   * @return
   */
  public Page<SoftType> findSoft(Integer currentPage, Integer limit) {
    PageHelper.startPage(currentPage, limit);
    SoftTypeExample soft = new SoftTypeExample();
    // 按更新时间排为降序
    soft.setOrderByClause(" updatetime desc,id asc");
    return (Page<SoftType>) this.softTypeMapper.selectByExample(soft);
  }

  /**
   * 根据ID获取版本信息
   * 
   * @param id
   * @return
   */
  public SoftType getById(Integer id) {
    return this.softTypeMapper.selectByPrimaryKey(id);
  }

}
