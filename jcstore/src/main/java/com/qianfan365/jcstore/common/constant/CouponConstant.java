/**
 * 
 */
package com.qianfan365.jcstore.common.constant;

/**
 * 优惠券常量表
 * @author wpy
 * @package_name com.qianfan365.jcstore.common.constant
 * @time 2017年6月6日 - 上午9:06:32
 */
public class CouponConstant {
	
	/**
	 * 是否无门槛
	 */
	public interface Unconditional{
		int YES = 1;
		int NO = 2;
	}
	
	/**
	 * 优惠券类型（1商家发布（母券），2用户领取（可使用的））
	 */
	public interface Type{
		int SHOP = 1;
		int USER = 2;
	}
	
	/**
	 * 活动状态（0未开始，1进行中，2已过期）'
	 */
	public interface Status{
		/**未开始*/
		int NOT_START = 0;
		/**进行中*/
		int UNDERWAY = 1;
		/**已过期*/
		int EXPIRED = 2;
	}
	
	/**1未使用，2已使用，3预先生成*/
	public interface Used_State{
		int NOT = 1;
		int USED = 2;
		int BEFOREHAND = 3;
	}
 }
