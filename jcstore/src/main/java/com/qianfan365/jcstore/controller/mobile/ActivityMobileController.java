package com.qianfan365.jcstore.controller.mobile;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.Activity;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ActivityService;

/**
 * @author
 * @介绍:活动模块 2017年6月5日 上午9:38:17
 */
@Controller
@RequestMapping("/mobile/activity")
public class ActivityMobileController extends BaseController {

	@Autowired
	private ActivityService activityService;

	/**
	 * 新建优惠券
	 */
	@RequestMapping(value = "/coupon", method = RequestMethod.GET)
	@ResponseBody
	@ModulePassport(moduleids = { ModuleType.INTERACTION_MARKETING })
	@PermissionPassport(permissionids = { PermissionType.INTERACTION_MARKETING })
	public ResultData savaCoupon(HttpSession session, Activity activity) {
		User user = getLoginUser(session);
		ResultData resultData = activityService.savaCoupon(user, activity);
		return resultData;
	}

	/**
	 * 新建摇一摇
	 */
	@RequestMapping(value = "/shake", method = RequestMethod.GET)
	@ResponseBody
	@ModulePassport(moduleids = { ModuleType.INTERACTION_MARKETING })
	@PermissionPassport(permissionids = { PermissionType.INTERACTION_MARKETING })
	public ResultData savaShake(HttpSession session, Activity activity) {
		User user = getLoginUser(session);
		ResultData resultData = activityService.savaShake(user, activity);
		return resultData;
	}
}
