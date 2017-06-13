package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.Marketing;
import com.qianfan365.jcstore.common.pojo.MarketingExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface MarketingMapper {
    int countByExample(MarketingExample example);

    int deleteByExample(MarketingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Marketing record);

    int insertSelective(Marketing record);

    List<Marketing> selectByExample(MarketingExample example);

    Marketing selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Marketing record, @Param("example") MarketingExample example);

    int updateByExample(@Param("record") Marketing record, @Param("example") MarketingExample example);

    int updateByPrimaryKeySelective(Marketing record);

    int updateByPrimaryKey(Marketing record);
    @Update("UPDATE marketing SET page_view = page_view +1 where id = #{id}")
    void updatePageView(@Param("id")Integer id);
}