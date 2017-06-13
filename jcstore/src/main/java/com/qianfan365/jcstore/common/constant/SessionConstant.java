package com.qianfan365.jcstore.common.constant;

import javax.servlet.http.HttpSession;

import com.qianfan365.jcstore.common.pojo.User;

public class SessionConstant {

    /**
     * 登录后台的用户
     */
    public static final String USER_SESSION_ADMIN = "user_admin";
    public static final String FRONT_USER_SESSION = "user_front";
    public static final String FRONT_USER_PERMISSION = "user_front_permission";
    public static final String AUTH_CODE = "authCode";
    public static final String AUTH_CODE_FLAG = "authCodeFlag";

    /**
     * 获取前台Session用户
     *
     * @param session
     * @return
     */
    public static User getFrontUserSession(HttpSession session) {
        return (User) session.getAttribute(SessionConstant.FRONT_USER_SESSION);
    }

    /**
     * 获取管理员用户
     *
     * @param session
     * @return
     */
    public static User getUserSessionAdmin(HttpSession session) {
        return (User) session.getAttribute(SessionConstant.USER_SESSION_ADMIN);
    }
}
