package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.CustomerGroup;
import com.qianfan365.jcstore.common.pojo.CustomerGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerGroupMapper {
    int countByExample(CustomerGroupExample example);

    int deleteByExample(CustomerGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CustomerGroup record);

    int insertSelective(CustomerGroup record);

    List<CustomerGroup> selectByExample(CustomerGroupExample example);

    CustomerGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CustomerGroup record, @Param("example") CustomerGroupExample example);

    int updateByExample(@Param("record") CustomerGroup record, @Param("example") CustomerGroupExample example);

    int updateByPrimaryKeySelective(CustomerGroup record);

    int updateByPrimaryKey(CustomerGroup record);
}