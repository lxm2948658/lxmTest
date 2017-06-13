package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.OrderBespokeDetail;
import com.qianfan365.jcstore.common.pojo.OrderBespokeDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderBespokeDetailMapper {
    int countByExample(OrderBespokeDetailExample example);

    int deleteByExample(OrderBespokeDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderBespokeDetail record);

    int insertSelective(OrderBespokeDetail record);

    List<OrderBespokeDetail> selectByExample(OrderBespokeDetailExample example);

    OrderBespokeDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderBespokeDetail record, @Param("example") OrderBespokeDetailExample example);

    int updateByExample(@Param("record") OrderBespokeDetail record, @Param("example") OrderBespokeDetailExample example);

    int updateByPrimaryKeySelective(OrderBespokeDetail record);

    int updateByPrimaryKey(OrderBespokeDetail record);
}