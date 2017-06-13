package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.Panorama;
import com.qianfan365.jcstore.common.pojo.PanoramaExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PanoramaMapper {
    int countByExample(PanoramaExample example);

    int deleteByExample(PanoramaExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Panorama record);

    int insertSelective(Panorama record);

    List<Panorama> selectByExample(PanoramaExample example);

    Panorama selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Panorama record, @Param("example") PanoramaExample example);

    int updateByExample(@Param("record") Panorama record, @Param("example") PanoramaExample example);

    int updateByPrimaryKeySelective(Panorama record);

    int updateByPrimaryKey(Panorama record);
}