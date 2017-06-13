package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.OrderInfoDetail;
import com.qianfan365.jcstore.common.pojo.OrderInfoDetailExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface OrderInfoDetailMapper {
    int countByExample(OrderInfoDetailExample example);

    int deleteByExample(OrderInfoDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfoDetail record);

    int insertSelective(OrderInfoDetail record);

    List<OrderInfoDetail> selectByExample(OrderInfoDetailExample example);

    OrderInfoDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderInfoDetail record, @Param("example") OrderInfoDetailExample example);

    int updateByExample(@Param("record") OrderInfoDetail record, @Param("example") OrderInfoDetailExample example);

    int updateByPrimaryKeySelective(OrderInfoDetail record);

    int updateByPrimaryKey(OrderInfoDetail record);
    
    /**
     * 更改退款的商品数量累计sql lhr
     */
    @Update("update order_info_detail set refund_num=refund_num+#{num}, updatetime=now() where id=#{id}")
    int updateOrderRefundNum(@Param("num") int num, @Param("id") int id);
}