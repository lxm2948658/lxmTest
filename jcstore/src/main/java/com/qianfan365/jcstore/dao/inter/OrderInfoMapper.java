package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.OrderInfoExample;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface OrderInfoMapper {
    int countByExample(OrderInfoExample example);

    int deleteByExample(OrderInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    List<OrderInfo> selectByExample(OrderInfoExample example);

    OrderInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") OrderInfo record, @Param("example") OrderInfoExample example);

    int updateByExample(@Param("record") OrderInfo record, @Param("example") OrderInfoExample example);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
    
    /**
     * 更改退款金额累计sql lhr
     * @version 1.1
     */
    @Update("update order_info set refunding_fee=refunding_fee+#{refunding},refunded_fee=refunded_fee+#{refunded},updatetime=now() where id=#{oid}")
    int updateRefundFee(@Param("refunding") double refunding, @Param("refunded") double refunded, @Param("oid") int oid);
    
    /**
     * 查询能退货的订单列表sql lhr
     * @version 1.1
     */
    @Select("select * from order_info where order_status <> 3 and user_id in ${uids} and createtime <= #{querytime} and ${condition} order by updatetime desc,id desc")
    @ResultMap("BaseResultMap")
    List<OrderInfo> selectByRefundable(@Param("uids") String uids, @Param("querytime") Date querytime,@Param("condition")String condition);
    
    /**
     * 查询能退货的订单列表sql lhr
     * @version 1.1
     */
    @Select("select * from order_info where order_status <> 3 and customer=#{customer} and user_id in ${uids} and createtime <= #{querytime} and ${condition} order by updatetime desc,id desc")
    @ResultMap("BaseResultMap")
    List<OrderInfo> selectByCustomerRefundable(@Param("uids") String uids, @Param("customer") String customer, @Param("querytime") Date querytime,@Param("condition")String condition);

}