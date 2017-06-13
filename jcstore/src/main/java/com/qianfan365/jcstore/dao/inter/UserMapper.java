package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    @Select("select id from user where belongs <> -1 and status = 1 and is_trial_account = false and expired_time > now()")
    List<Integer> selectIdByAll();
    
    @Select("select id from user where belongs = 0 and status = 1 and is_trial_account = false and expired_time > now()")
    List<Integer> selectIdByBusiness();
}