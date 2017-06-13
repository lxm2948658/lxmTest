/**
 * 
 */
package com.qianfan365.jcstore.controller.mobile;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.Activity;
import com.qianfan365.jcstore.common.pojo.Coupon;
import com.qianfan365.jcstore.common.pojo.MallConsumer;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.CouponService;
import com.qianfan365.jcstore.service.ShopService;

/**
 * 优惠券控制器
 * 
 * @author wpy
 * @package_name com.qianfan365.jcstore.controller.mobile
 * @time 2017年6月6日 - 上午9:44:27
 */
@Controller
@RequestMapping("/coupon")
public class CouponController extends BaseController {

	@Autowired
	private CouponService	couponService;
	@Autowired
	private ShopService		shopService;

	/**
	 * 商家新增优惠卷
	 * 
	 * @param session
	 * @param activity
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	@PermissionPassport(permissionids = { PermissionType.ACTIVITY_MARKETING })
	@ModulePassport(moduleids = { ModuleType.ACTIVITY_MARKETING })
	public ResultData addCoupon(HttpSession session, Coupon coupon) {
		if (!verify( coupon )) {
			return ResultData.build().parameterError();
		}

		setShopId( session, coupon );
		return couponService.add( coupon );
	}

	/**
	 * 参数校验
	 * 
	 * @param coupon
	 * @return
	 */
	private boolean verify(Coupon coupon) {
		if (coupon == null) {
			return false;
		}
		if (StringUtils.isEmpty( coupon.getName() )) {
			return false;
		}
		if (null == coupon.getCirculation()) {
			return false;
		}
		if (null == coupon.getMoney()) {
			return false;
		}
		if (coupon.getStarton() == null || coupon.getEndon() == null) {
			return false;
		}
		if (coupon.getStarton().getTime() > coupon.getEndon().getTime()) {
			return false;
		}
		if (coupon.getMaximum() == null) {
			return false;
		}
		return true;
	}

	/**
	 * 商家删除优惠卷
	 * 
	 * @param session
	 * @param activity
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	@PermissionPassport(permissionids = { PermissionType.ACTIVITY_MARKETING })
	@ModulePassport(moduleids = { ModuleType.ACTIVITY_MARKETING })
	public ResultData deleteCoupon(HttpSession session, Coupon coupon) {
		if (coupon.getId() == null) {
			return ResultData.build().parameterError();
		}
		setShopId( session, coupon );
		return couponService.delete( coupon );
	}

	/**
	 * 商家 根据优惠卷状态查询优惠卷
	 * 
	 * @param session
	 * @param activityStatus
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	@PermissionPassport(permissionids = { PermissionType.ACTIVITY_MARKETING })
	@ModulePassport(moduleids = { ModuleType.ACTIVITY_MARKETING })
	public ResultData listCoupon(HttpSession session, Integer couponStatus) {
		if (couponStatus == null) {
			return ResultData.build().parameterError();
		}
		Shop shop = setShopId( session, null );
		return couponService.list( shop.getId(), couponStatus );
	}

	/**
	 * 设置shopId
	 * 
	 * @param session
	 * @return
	 */
	private Shop setShopId(HttpSession session, Coupon coupon) {
		User user = getLoginUser( session );
		Shop shop = shopService.findShop( user );
		if (null == shop) {
			throw new RuntimeException( "用户登录超时，请重新登录" );
		}
		if (coupon != null) {
			coupon.setShopId( shop.getId() );
		}
		return shop;
	}

	private MallConsumer getCurrentMallConsumer(HttpSession session) {
		return (MallConsumer) session.getAttribute( "mallConsumer" );
	}

	/**
	 * 用户领取优惠卷
	 * 
	 * @param session
	 * @param coupon
	 * @return
	 */
	@RequestMapping("/receive")
	@ResponseBody
	public ResultData receive(HttpSession session, Coupon coupon, Activity activity) {
		MallConsumer consumer = getCurrentMallConsumer( session );
		if (this.verify( coupon )) {
			ResultData.build().parameterError();
		}
		if (consumer == null) {
			ResultData.build().parameterError();
		}
		if (StringUtils.isEmpty( consumer.getMobile() )) {
			ResultData.build().parameterError();
		}
		if (StringUtils.isEmpty( consumer.getMobile() ) && StringUtils.isEmpty( consumer.getOpenid() )) {
			ResultData.build().parameterError();
		}
		return couponService.receive( coupon, consumer, activity );
	}

	/**
	 * 用户查询优惠券列表
	 * 
	 * @param session
	 * @param couponStatus
	 * @return
	 */
	@RequestMapping("/consumerList")
	@ResponseBody
	public ResultData consumerList(HttpSession session, Integer couponStatus) {
		MallConsumer consumer = getCurrentMallConsumer( session );
		if (consumer == null) {
			ResultData.build().parameterError();
		}
		if (couponStatus == null) {
			ResultData.build().parameterError();
		}
		return couponService.consumerList( consumer, couponStatus );
	}

	/**
	 * 消费优惠券
	 * 
	 * @param session
	 * @param coupon
	 * @return
	 */
	@RequestMapping("/consume")
	@ResponseBody
	public ResultData consume(HttpSession session, Coupon coupon) {
		MallConsumer consumer = getCurrentMallConsumer( session );
		if (consumer == null) {
			ResultData.build().error();
		}
		if (coupon == null) {
			ResultData.build().parameterError();
		}
		return couponService.consume( consumer, coupon );
	}

}
