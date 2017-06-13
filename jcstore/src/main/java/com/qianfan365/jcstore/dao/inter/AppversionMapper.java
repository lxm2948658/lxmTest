package com.qianfan365.jcstore.dao.inter;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.qianfan365.jcstore.common.pojo.Appversion;
import com.qianfan365.jcstore.common.pojo.AppversionExample;

public interface AppversionMapper {
  int countByExample(AppversionExample example);

  int deleteByExample(AppversionExample example);

  int deleteByPrimaryKey(Integer id);

  int insert(Appversion record);

  int insertSelective(Appversion record);

  List<Appversion> selectByExample(AppversionExample example);

  Appversion selectByPrimaryKey(Integer id);

  int updateByExampleSelective(@Param("record") Appversion record,
      @Param("example") AppversionExample example);

  int updateByExample(@Param("record") Appversion record,
      @Param("example") AppversionExample example);

  int updateByPrimaryKeySelective(Appversion record);

  int updateByPrimaryKey(Appversion record);

  int updateAndroidVersion(@Param("version") String version);
  
  int updateAndroidPad(@Param("version") String version);

  int updateIOSVersion(@Param("version") String version);

  int updateIOSSwitch(@Param("checkStatus") String checkStatus);
}
