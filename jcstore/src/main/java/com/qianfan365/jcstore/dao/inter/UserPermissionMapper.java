package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.common.pojo.UserPermissionExample;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserPermissionMapper {
    int countByExample(UserPermissionExample example);

    int deleteByExample(UserPermissionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserPermission record);

    int insertSelective(UserPermission record);

    List<UserPermission> selectByExample(UserPermissionExample example);

    UserPermission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserPermission record, @Param("example") UserPermissionExample example);

    int updateByExample(@Param("record") UserPermission record, @Param("example") UserPermissionExample example);

    int updateByPrimaryKeySelective(UserPermission record);

    int updateByPrimaryKey(UserPermission record);
    
    @Select("select pid from user_permission where uid = #{uid}")
    List<Integer> selectPidByUid(Integer uid);
    
    /**
     * 保存语音消息提醒设置sql lhr
     * @version 1.1
     */
    @Update("update user_permission set push = push^4, updatetime = now() where pid in ${pid} and uid = #{uid}")
    int updatePushSound(@Param("uid") int uid, @Param("pid") String pid);
}