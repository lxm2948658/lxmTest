package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.MallConsumer;
import com.qianfan365.jcstore.common.pojo.MallConsumerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MallConsumerMapper {
    int countByExample(MallConsumerExample example);

    int deleteByExample(MallConsumerExample example);

    int insert(MallConsumer record);

    int insertSelective(MallConsumer record);

    List<MallConsumer> selectByExample(MallConsumerExample example);

    int updateByExampleSelective(@Param("record") MallConsumer record, @Param("example") MallConsumerExample example);

    int updateByExample(@Param("record") MallConsumer record, @Param("example") MallConsumerExample example);
}