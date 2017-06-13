package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.RefundDetail;
import com.qianfan365.jcstore.common.pojo.RefundDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RefundDetailMapper {
    int countByExample(RefundDetailExample example);

    int deleteByExample(RefundDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RefundDetail record);

    int insertSelective(RefundDetail record);

    List<RefundDetail> selectByExample(RefundDetailExample example);

    RefundDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RefundDetail record, @Param("example") RefundDetailExample example);

    int updateByExample(@Param("record") RefundDetail record, @Param("example") RefundDetailExample example);

    int updateByPrimaryKeySelective(RefundDetail record);

    int updateByPrimaryKey(RefundDetail record);
}