package com.qianfan365.jcstore.controller.mobile;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ActivityConstant;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.Activity;
import com.qianfan365.jcstore.common.pojo.Prize;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ActivityService;

/**
 * @author lxm
 * @介绍:
 * @ 2017年6月7日 上午9:57:00
 */
@Controller
@RequestMapping("/activity")
public class ActivityController extends BaseController {
	@Autowired
	private ActivityService activityService;

	@RequestMapping("/add")
	@ResponseBody
	@PermissionPassport(permissionids = { PermissionType.ACTIVITY_MARKETING })
	@ModulePassport(moduleids = { ModuleType.ACTIVITY_MARKETING })
	public ResultData addActivity(HttpSession session, Activity activity, String prizes) {
		if (activity == null) {
			return ResultData.build().parameterError();
		}
		// 获取店铺id
		Shop shop = getShop( session );
		if (null == shop) {
			ResultData.build().setStatus( ResultData.Status.LOGIN_NEEDED );
		}
		// 获取奖品列表
		if (StringUtils.isEmpty( prizes )) {
			ResultData.build().parameterError();
		}

		activity.setShopId( shop.getId() );
		List<Prize> prizeList = JSON.parseArray( prizes, Prize.class );
		return activityService.save( activity, prizeList );
	}

	/**
	 * 失效活动
	 * 
	 * @param session
	 * @param activity
	 * @return
	 */
	@RequestMapping("/lose")
	@ResponseBody
	@PermissionPassport(permissionids = { PermissionType.ACTIVITY_MARKETING })
	@ModulePassport(moduleids = { ModuleType.ACTIVITY_MARKETING })
	public ResultData loseActivity(HttpSession session, Activity activity) {
		if (activity == null) {
			return ResultData.build().parameterError();
		}
		// 获取店铺id
		Shop shop = getShop( session );
		if (null == shop.getId()) {
			ResultData.build().setStatus( ResultData.Status.LOGIN_NEEDED );
		}
		if (!shop.getId().equals( activity.getShopId() )) {
			return ResultData.build().parameterError();
		}

		return activityService.deleteActivity( activity );
	}
	
	/**
	 * 删除活动
	 * 
	 * @param session
	 * @param activity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@PermissionPassport(permissionids = { PermissionType.ACTIVITY_MARKETING })
	@ModulePassport(moduleids = { ModuleType.ACTIVITY_MARKETING })
	public ResultData deleteActivity(HttpSession session, Activity activity) {
		if (null == activity
				|| !activity.getStatus().equals( ActivityConstant.Status.EXPIRED )) {
			return ResultData.build().parameterError();
		}
		// 获取店铺id
		Shop shop = getShop( session );
		if (null == shop.getId()) {
			ResultData.build().setStatus( ResultData.Status.LOGIN_NEEDED );
		}
		if (!shop.getId().equals( activity.getShopId() )) {
			return ResultData.build().parameterError();
		}
		activity.setDelete( ActivityConstant.Delete.DELETE );
		return activityService.deleteActivity( activity );
	}

	/**
	 * 修改一个未开始是的活动
	 * 
	 * @param session
	 * @param activity
	 * @return
	 */
	@RequestMapping("/modification")
	@ResponseBody
	@PermissionPassport(permissionids = { PermissionType.ACTIVITY_MARKETING })
	@ModulePassport(moduleids = { ModuleType.ACTIVITY_MARKETING })
	public ResultData modification(HttpSession session, Activity activity, String prizes) {
		if (activity == null) {
			return ResultData.build().parameterError();
		}
		// 验证店铺信息
		Shop shop = getShop( session );
		if (!shop.getId().equals( activity.getShopId() )) {
			return ResultData.build().parameterError();
		}
		// 判断这个活动是不是未开始
		if (null == activity.getStatus() && !activity.getStatus().equals( ActivityConstant.Status.NOT_START )) {
			return ResultData.build().parameterError();
		}
		// 获取奖品列表
		if (StringUtils.isEmpty( prizes )) {
			ResultData.build().parameterError();
		}
		List<Prize> prizeList = JSON.parseArray( prizes, Prize.class );
		return activityService.modification( activity ,prizeList);
	}
}
