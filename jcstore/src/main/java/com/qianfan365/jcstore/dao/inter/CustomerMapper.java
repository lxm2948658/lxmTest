package com.qianfan365.jcstore.dao.inter;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.qianfan365.jcstore.common.pojo.Customer;
import com.qianfan365.jcstore.common.pojo.CustomerExample;

public interface CustomerMapper {
  int countByExample(CustomerExample example);

  int deleteByExample(CustomerExample example);

  int deleteByPrimaryKey(Integer id);

  int insert(Customer record);

  int insertSelective(Customer record);

  List<Customer> selectByExample(CustomerExample example);

  Customer selectByPrimaryKey(Integer id);

  int updateByExampleSelective(@Param("record") Customer record,
      @Param("example") CustomerExample example);

  int updateByExample(@Param("record") Customer record, @Param("example") CustomerExample example);

  int updateByPrimaryKeySelective(Customer record);

  int updateByPrimaryKey(Customer record);

  @Update("update customer c set c.group_id=NULL where c.id=#{id}")
  int updateGroupIdToNull(@Param("id") Integer id);

  @Update("UPDATE customer c SET c.lately_account = (SELECT o.receice_amoun - o.refunded_fee - o.refunding_fee FROM order_info o WHERE customer_id = #{id} AND order_status != 3 ORDER BY createtime LIMIT 1)WHERE c.id = #{id}")
  int updateLatelyAccount(Integer id);

  Customer getByBelongs(@Param("cid") Integer cid, @Param("belongs") Integer belongs);
  
  Customer getByUid(@Param("cid") Integer cid, @Param("uid") Integer uid);
}
