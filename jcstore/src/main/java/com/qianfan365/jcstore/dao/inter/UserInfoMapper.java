package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.UserInfo;
import com.qianfan365.jcstore.common.pojo.UserInfoExample;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {
    int countByExample(UserInfoExample example);

    int deleteByExample(UserInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    List<UserInfo> selectByExample(UserInfoExample example);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByExample(@Param("record") UserInfo record, @Param("example") UserInfoExample example);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    
    @Delete(value = "DELETE FROM user_info WHERE user_info.uid IN (SELECT id FROM `user` u WHERE u.id = #{clientId} OR u.belongs = #{clientId})")
    void deleteAllByManangeId(@Param("clientId")Integer clientId);
}