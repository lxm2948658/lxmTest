package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.MessageManager;
import com.qianfan365.jcstore.common.pojo.MessageManagerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MessageManagerMapper {
    int countByExample(MessageManagerExample example);

    int deleteByExample(MessageManagerExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MessageManager record);

    int insertSelective(MessageManager record);

    List<MessageManager> selectByExampleWithBLOBs(MessageManagerExample example);

    List<MessageManager> selectByExample(MessageManagerExample example);

    MessageManager selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MessageManager record, @Param("example") MessageManagerExample example);

    int updateByExampleWithBLOBs(@Param("record") MessageManager record, @Param("example") MessageManagerExample example);

    int updateByExample(@Param("record") MessageManager record, @Param("example") MessageManagerExample example);

    int updateByPrimaryKeySelective(MessageManager record);

    int updateByPrimaryKeyWithBLOBs(MessageManager record);

    int updateByPrimaryKey(MessageManager record);
}