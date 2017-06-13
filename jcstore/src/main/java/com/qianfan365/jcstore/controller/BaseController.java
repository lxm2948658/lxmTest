package com.qianfan365.jcstore.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.PageView;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.service.ShopService;
import com.qianfan365.jcstore.service.UserService;

@ControllerAdvice
public class BaseController {
	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserService userService;
	@Autowired
	private ShopService shopService;

	public User getLoginUser(HttpSession session) {
		if (session == null) {
			return PageView.userThreadLocal.get();
		}
		return (User) session.getAttribute(SessionConstant.FRONT_USER_SESSION);
	}

	// @ResponseBody

	// @ExceptionHandler(ParamException.class)
	// public ResultData paramExceptionHandler(Exception ex) {
	// ResultData parameterError = ResultData.build().parameterError();
	// return parameterError.put(ResultData.STATUS_KEY, ex.getMessage());
	// }

	// @ResponseBody
	// @ExceptionHandler(UndeclaredThrowableException.class)
	// public ResultData
	// undeclaredThrowableExceptionHandler(UndeclaredThrowableException ex) {
	// Throwable throwable = ex.getUndeclaredThrowable();
	// if(throwable instanceof ParamException){
	// ResultData parameterError = ResultData.build().parameterError();
	// return parameterError.put(ResultData.STATUS_KEY, throwable.getMessage());
	// }
	// return ResultData.build().failure();
	// }

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public ResultData exceptionHandler(Exception ex, HttpServletRequest req) {
		// if(req.getHeader("x-requested-with") != null
		// &&
		// req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){
		// System.out.println(req.getHeader("x-requested-with"));
		// }
		log.error(ex.getMessage(), ex);
		return ResultData.build().parameterError();
	}

	// @Value("${common.init-binder.switch}")
	// private boolean initBinderSwitch;
	//
	// @InitBinder
	// public void initBinder(WebDataBinder binder) {
	// if(initBinderSwitch){
	// binder.registerCustomEditor(String.class, new StringEscapeEditor(true,
	// true));
	// binder.registerCustomEditor(String[].class, new StringEscapeEditor(true,
	// true));
	// }
	// }

	/**
	 * 如果身份是店长则查询旗下所有用户
	 * 
	 * @param user
	 * @return
	 */
	protected List<Integer> listUserId(User user) {
		List<Integer> userIds = new ArrayList<Integer>();
		List<User> userLists = userService.findUserBelongId(user.getId());
		for (User userList : userLists) {
			userIds.add(userList.getId());
		}
		userIds.add(user.getId());
		return userIds;
	}

	/**
	 * 查询所有的子用户集合
	 * 
	 * @param user
	 * @return
	 */
	protected List<Integer> listPermissionUserId(User user) {
		List<Integer> userIds = new ArrayList<Integer>();
		List<User> userLists = userService.findUserBelongId(user.getBelongs());
		for (User userList : userLists) {
			userIds.add(userList.getId());
		}
		userIds.add(user.getBelongs());
		return userIds;

	}

	/**
	 * 获取店铺信息
	 */
	protected Shop getShop(HttpSession session) {
		User user = getLoginUser(session);
		Shop shop = shopService.findShop(user);

		return shop;
	}
}
