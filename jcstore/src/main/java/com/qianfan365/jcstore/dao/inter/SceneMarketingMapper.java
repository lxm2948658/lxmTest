package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.SceneMarketing;
import com.qianfan365.jcstore.common.pojo.SceneMarketingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SceneMarketingMapper {
    int countByExample(SceneMarketingExample example);

    int deleteByExample(SceneMarketingExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(SceneMarketing record);

    int insertSelective(SceneMarketing record);

    List<SceneMarketing> selectByExample(SceneMarketingExample example);

    SceneMarketing selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SceneMarketing record, @Param("example") SceneMarketingExample example);

    int updateByExample(@Param("record") SceneMarketing record, @Param("example") SceneMarketingExample example);

    int updateByPrimaryKeySelective(SceneMarketing record);

    int updateByPrimaryKey(SceneMarketing record);
}