/**
 * 
 */
package com.qianfan365.jcstore.common.constant;

/**
 * @author wpy
 * @package_name com.qianfan365.jcstore.common.constant
 * @time 2017年6月5日 - 下午2:16:12
 */
public class ActivityConstant {
	/**
	 * 活动类型
	 */
	public interface Type {
		/**
		 * 刮刮卡
		 */
		int SCRATCH_CARD = 1;
		/**
		 * 摇一摇
		 */
		int FRIENDSHAKE = 2;
		/**
		 * 大转盘
		 */
		int TURNTABLE = 3;
		
	}
	
	/**
	 * 活动状态
	 */
	public interface Status{
		/**未开始*/
		int NOT_START = 1;
		/**进行中*/
		int UNDERWAY = 2;
		/**已过期*/
		int EXPIRED = 3;
	}
	
	/**
	 * 是否彻底删除
	 */
	public interface Delete{
		/**删除*/
		int DELETE = 1;
		/**不删除*/
		int NOT_DELETE = 2;
	}
}
