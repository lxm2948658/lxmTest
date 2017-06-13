package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.SoftType;
import com.qianfan365.jcstore.common.pojo.SoftTypeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SoftTypeMapper {
    int countByExample(SoftTypeExample example);

    int deleteByExample(SoftTypeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SoftType record);

    int insertSelective(SoftType record);

    List<SoftType> selectByExample(SoftTypeExample example);

    SoftType selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SoftType record, @Param("example") SoftTypeExample example);

    int updateByExample(@Param("record") SoftType record, @Param("example") SoftTypeExample example);

    int updateByPrimaryKeySelective(SoftType record);

    int updateByPrimaryKey(SoftType record);
}