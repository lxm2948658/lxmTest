package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.bean.OrderRefundBean;
import com.qianfan365.jcstore.common.bean.RefundPrintBean;
import com.qianfan365.jcstore.common.pojo.Refund;
import com.qianfan365.jcstore.common.pojo.RefundExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RefundMapper {
    int countByExample(RefundExample example);

    int deleteByExample(RefundExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Refund record);

    int insertSelective(Refund record);

    List<Refund> selectByExample(RefundExample example);

    Refund selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Refund record, @Param("example") RefundExample example);

    int updateByExample(@Param("record") Refund record, @Param("example") RefundExample example);

    int updateByPrimaryKeySelective(Refund record);

    int updateByPrimaryKey(Refund record);
    
    @Select("select id from refund where order_info_id=#{oid} and status <>3")
    List<Integer> selectRidByOid(@Param("oid")int oid);
    
    @Select("select id,status,refund_num from refund where order_info_id=#{oid} order by updatetime desc,id desc")
    List<OrderRefundBean> selectByOid(@Param("oid")int oid);
    
    @Select("select id,status,refund_num,refund_fee,createtime from refund where order_info_id=#{oid} order by updatetime desc,id desc")
    List<RefundPrintBean> selectPrintByOid(@Param("oid")int oid);
}