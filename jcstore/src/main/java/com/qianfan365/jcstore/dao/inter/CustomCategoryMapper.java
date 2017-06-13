package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.CustomCategory;
import com.qianfan365.jcstore.common.pojo.CustomCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomCategoryMapper {
    int countByExample(CustomCategoryExample example);

    int deleteByExample(CustomCategoryExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CustomCategory record);

    int insertSelective(CustomCategory record);

    List<CustomCategory> selectByExample(CustomCategoryExample example);

    CustomCategory selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CustomCategory record, @Param("example") CustomCategoryExample example);

    int updateByExample(@Param("record") CustomCategory record, @Param("example") CustomCategoryExample example);

    int updateByPrimaryKeySelective(CustomCategory record);

    int updateByPrimaryKey(CustomCategory record);
}