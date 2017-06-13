package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.CustomProduct;
import com.qianfan365.jcstore.common.pojo.CustomProductExample;

import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomProductMapper {
    int countByExample(CustomProductExample example);

    int deleteByExample(CustomProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CustomProduct record);

    int insertSelective(CustomProduct record);

    List<CustomProduct> selectByExample(CustomProductExample example);

    CustomProduct selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CustomProduct record, @Param("example") CustomProductExample example);

    int updateByExample(@Param("record") CustomProduct record, @Param("example") CustomProductExample example);

    int updateByPrimaryKeySelective(CustomProduct record);

    int updateByPrimaryKey(CustomProduct record);

    void insertBatch(ArrayList<CustomProduct> customProducts);
}
