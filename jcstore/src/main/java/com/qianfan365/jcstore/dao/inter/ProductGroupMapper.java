package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.ProductGroup;
import com.qianfan365.jcstore.common.pojo.ProductGroupExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ProductGroupMapper {
    int countByExample(ProductGroupExample example);

    int deleteByExample(ProductGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ProductGroup record);

    int insertSelective(ProductGroup record);

    List<ProductGroup> selectByExample(ProductGroupExample example);

    ProductGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ProductGroup record, @Param("example") ProductGroupExample example);

    int updateByExample(@Param("record") ProductGroup record, @Param("example") ProductGroupExample example);

    int updateByPrimaryKeySelective(ProductGroup record);

    int updateByPrimaryKey(ProductGroup record);
    
    @Select("select id FROM product_group WHERE user_id = #{uid} AND `name` = #{name} ORDER BY createtime desc limit 1")
    Integer getGroupIdByUidAndName(@Param("name")String name,@Param("uid")Integer uid);
}