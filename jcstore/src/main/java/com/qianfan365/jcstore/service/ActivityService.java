/**
 * 
 */
package com.qianfan365.jcstore.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.ActivityConstant;
import com.qianfan365.jcstore.common.constant.ActivityRecordConstant;
import com.qianfan365.jcstore.common.constant.PrizeConstant;
import com.qianfan365.jcstore.common.pojo.Activity;
import com.qianfan365.jcstore.common.pojo.ActivityExample;
import com.qianfan365.jcstore.common.pojo.ActivityExample.Criteria;
import com.qianfan365.jcstore.common.pojo.ActivityRecord;
import com.qianfan365.jcstore.common.pojo.ActivityRecordExample;
import com.qianfan365.jcstore.common.pojo.Coupon;
import com.qianfan365.jcstore.common.pojo.MallConsumer;
import com.qianfan365.jcstore.common.pojo.Prize;
import com.qianfan365.jcstore.common.pojo.PrizeExample;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.util.LotteryUtils;
import com.qianfan365.jcstore.dao.inter.ActivityMapper;
import com.qianfan365.jcstore.dao.inter.ActivityRecordMapper;
import com.qianfan365.jcstore.dao.inter.CouponMapper;
import com.qianfan365.jcstore.dao.inter.PrizeMapper;

/**
 * @author wpy
 * @package_name com.qianfan365.jcstore.service
 * @time 2017年6月5日 - 上午9:14:09
 */
@Service
public class ActivityService {
	@Autowired
	private ActivityMapper			activityMapper;
	@Autowired
	private CouponMapper			couponMapper;
	@Autowired
	private PrizeMapper				prizeMapper;
	@Autowired
	private ActivityRecordMapper	activityRecordMapper;

	/**
	 * 保存一个活动
	 * 
	 * @param activity
	 * @param prizes
	 * @param isCoupon
	 * 
	 * @return
	 */
	public ResultData save(Activity activity, List<Prize> prizes) {
		ResultData result = ResultData.build();

		if (!activityVerify( activity, result )) {
			return result.parameterError();
		}
		if (!awardVerify( activity, result, prizes )) {
			return result.parameterError();
		}
		// 保存活动
		activityMapper.insert( activity );
		return result.put( "activity", activity );
	}

	/**
	 * 修改一个未开始的活动
	 * 
	 * @param activity
	 * @param prizeList
	 * @return
	 */
	public ResultData modification(Activity activity, List<Prize> prizeList) {
		ResultData result = ResultData.build();
		// 基本校验
		if (!activityVerify( activity, result )) {
			return result.parameterError();
		}
		// 先查询该活动现在的奖品列表
		PrizeExample example = new PrizeExample();
		example.createCriteria().andActivityIdEqualTo( activity.getId() );
		// 执行归还库存操作
		for (Prize prize : prizeList) {
			// 判断奖品类型
			if (prize.getType().equals( PrizeConstant.Prize.COUPON )) {
				// 优惠券
				Coupon selectByPrimaryKey = couponMapper.selectByPrimaryKey( prize.getThingId() );
				// 归还库存(奖品占用数量 ,优惠券)
				restoreCouponRepertory( prize.getNumber(), selectByPrimaryKey, result );
			}
		}
		// 再执行中奖设置
		if (!awardVerify( activity, result, prizeList )) {
			return result.parameterError();
		}
		activity.setUpdateTime( new Date());
		activityMapper.updateByPrimaryKeySelective( activity );
		return result.put( "activity", activity ).success();
	}
	
	/**
	 * 查询一个活动
	 * 
	 * @param shop
	 * @param activityId
	 * @return
	 */
	public ResultData queryById(Shop shop, Integer activityId) {
		Activity selectActivity = activityMapper.selectByPrimaryKey( activityId );
		if (!selectActivity.getShopId().equals( shop.getId() )) {
			return ResultData.build().parameterError();
		}
	
		return ResultData.build().put( "activity", selectActivity );
	}

	/**
	 * 按商店和活动状态查询活动
	 * 
	 * @param shopId
	 * @param activityStatus
	 * @return
	 */
	public ResultData list(Integer shopId, Integer activityType, Integer activityStatus) {
		ResultData resultData = ResultData.build();
	
		ActivityExample example = new ActivityExample();
		example.setOrderByClause( " update_time asc" );
		Criteria criteria = example.createCriteria();
		criteria.andShopIdEqualTo( shopId )
		.andTypeEqualTo( activityType )
		.andDeleteEqualTo( ActivityConstant.Delete.NOT_DELETE );
		List<Activity> selectByExample = activityMapper.selectByExample( example );
	
		// 过滤得到需要状态的List
		List<Activity> resultList = filtrateResultListByStatus( activityStatus, selectByExample );
	
		// 跟新那些状态不一致的（自动跟新活动状态）
		for (Activity activity : resultList) {
			if (!activity.getStatus().equals( activityStatus )) {
				activity.setStatus( activityStatus );
				activity.setUpdateTime( new Date() );
				activity.setComment( activity.getComment() + "||自动更新Status为" + activityStatus );
				activityMapper.updateByPrimaryKey( activity );
			}
		}
	
		return resultData.parseList( resultList ).success();
	}

	/**
	 * 使用参与活动
	 * 
	 * @param consumer
	 * @param activity
	 * @return
	 * @throws ParseException
	 */
	public ResultData attend(MallConsumer consumer, Activity activity) throws ParseException {
		Activity selectActivity = activityMapper.selectByPrimaryKey( activity.getId() );
		// 校验是不是伪造数据，重写了equals方法进行业务对比
		if (!selectActivity.equals( activity )) {
			ResultData.build().parameterError();
		}
		// 是不是正在进行的活动
		if (this.setStatusByStartOnAndEndon( selectActivity ) == ActivityConstant.Status.UNDERWAY) {
			ResultData.build().error();
		}
		// 用户活动次数校验
		if (!verifyAttendTimes( consumer, selectActivity )) {
			return ResultData.build().error();
		}
		// 中了几等奖
		Integer winningPrizeLevel = winningPrizeLevel( selectActivity );
		// 未中奖，跟新参与人数，新建一条用户活动记录
		if (winningPrizeLevel.equals( LotteryUtils.LOSING_LOTTERY )) {
			updateActivity( selectActivity, selectActivity.getAttendNumber() + 1, selectActivity.getWinningNumber() + 1 );
			creatActivityRecrod( consumer, selectActivity, null, ActivityRecordConstant.Result.NO, null );
			return ResultData.build().error();
		}
		// 查询中奖的奖品
		PrizeExample prizeExample = new PrizeExample();
		prizeExample.createCriteria().andActivityIdEqualTo( winningPrizeLevel ).andLevelEqualTo( winningPrizeLevel );
		Prize winningPrize = prizeMapper.selectByExample( prizeExample ).get( 0 );
		// 奖品库存校验
		if (!(winningPrize.getNumber() > winningPrize.getUsedNumber())) {
			return ResultData.build().error();
		}
		// 查询奖品对应的优惠券
		Coupon winningCoupon = couponMapper.selectByPrimaryKey( winningPrize.getId() );
	
		// 更新活动参与人数与中奖人数，新建一条用户活动记录
		updateActivity( selectActivity, selectActivity.getAttendNumber() + 1, selectActivity.getWinningNumber() + 1 );
		creatActivityRecrod( consumer, selectActivity, winningPrize, ActivityRecordConstant.Result.NO, ActivityRecordConstant.Status.NO );
		// 奖品已使用数加1
		winningPrize.setUsedNumber( winningPrize.getUsedNumber() + 1 );
	
		return ResultData.build().put( "coupon", winningCoupon ).success();
	}

	/**
	 * 删除(失效)活动
	 * 
	 * @param activity
	 * @return
	 */
	public ResultData deleteActivity(Activity activity) {
		ResultData resultData = ResultData.build();
		// 删除方法引用判断 delete
		if (activity.getDelete().equals( ActivityConstant.Delete.DELETE )) {
			int i = activityMapper.updateByPrimaryKeySelective( activity );
			if (i != 1) {
				resultData.data404();
			}
			return resultData.put( "activity", activity ).success();
		}
		// 失效方法引用
		// 查询活动中用到的奖品
		PrizeExample pexample = new PrizeExample();
		pexample.createCriteria().andActivityIdEqualTo( activity.getId() );
		List<Prize> prizeList = prizeMapper.selectByExample( pexample );
		for (Prize prize : prizeList) {
			// 判断奖品类型
			if (prize.getType().equals( PrizeConstant.Prize.COUPON )) {
				// 优惠券
				Coupon selectByPrimaryKey = couponMapper.selectByPrimaryKey( prize.getThingId() );
				// 归还库存(奖品占用数量 ,优惠券)
				restoreCouponRepertory( prize.getNumber(), selectByPrimaryKey, resultData );
			}
		}
		// 失效活动
		activity.setStatus( ActivityConstant.Status.EXPIRED );
		activity.setUpdateTime( new Date() );
		int i = activityMapper.updateByPrimaryKeySelective( activity );
		if (i != 1) {
			resultData.data404();
		}
		return resultData.put( "activity", activity ).success();
	}

	/**
	 * 归还优惠券库存
	 * 
	 * @param couponid
	 * @param coupon
	 * @param resultData
	 */
	private void restoreCouponRepertory(Integer number, Coupon coupon, ResultData resultData) {
		coupon.setReceiveNumber( coupon.getReceiveNumber() - number );
		// 更新时间
		coupon.setUpdateTime( new Date() );
		int i = couponMapper.updateByPrimaryKey( coupon );
		if (i != 1) {
			resultData.data404();
		}
	}

	/**
	 * 减去奖品优惠卷的库存
	 * 
	 * @param couponid
	 * @param number
	 */
	private void subtractCouponRepertory(Integer couponid, Integer number) {
		Coupon coupon = couponMapper.selectByPrimaryKey( couponid );
		if (coupon == null) {
			throw new RuntimeException( "优惠券不存在" );
		}
		if ((coupon.getCirculation() - coupon.getUsedNumber()) < number) {
			throw new RuntimeException( "库存不足" );
		}
		coupon.setUsedNumber( coupon.getUsedNumber() + number );
		couponMapper.updateByPrimaryKey( coupon );
	}

	/**
	 * 活动校验
	 * 
	 * @param activity
	 * @param result
	 * @return
	 */
	private boolean activityVerify(Activity activity, ResultData result) {
		// 活动名称不为空
		if (null == activity.getName()) {
			result.parameterError();
			return false;
		}
		// 起止时间不为空
		if (null == activity.getStarton() || null == activity.getEndon()) {
			result.parameterError();
			return false;
		}
		// 开始时间不能小于结束时间
		if (activity.getEndon().getTime() < activity.getStarton().getTime()) {
			result.parameterError();
			return false;
		}
		// 参与次数校验空
		if (null == activity.getTotalTimes() && null == activity.getEverydayTimes()) {
			result.parameterError();
			return false;
		}
		// 总参与次数不能小于每天参与次数
		if (activity.getTotalTimes() < activity.getEverydayTimes()) {
			result.parameterError();
			return false;
		}

		return true;
	}
	
	/**
	 * 中奖设置检验
	 * 
	 * @param prizes
	 */
	private boolean awardVerify(Activity activity, ResultData result, List<Prize> prizes) {
		// 获取活动id
		Integer activityId = activity.getId();
		// 中奖率设置不能大于
		if (null == activity.getProbalility() && activity.getProbalility() > 100 && activity.getProbalility() < 0) {
			return false;
		}
		for (Prize prize : prizes) {
			if (prize.getType().equals( PrizeConstant.Prize.COUPON ) || null != prize.getType()) {
				prize.setActivityId( activityId );
				switch (prize.getLevel()) {
				case PrizeConstant.Grade.first:
					if (null != prize.getNumber()) {
						// 减库存
						subtractCouponRepertory( prize.getThingId(), prize.getNumber() );
					}
				case PrizeConstant.Grade.second:
					if (null != prize.getNumber()) {
						subtractCouponRepertory( prize.getThingId(), prize.getNumber() );
					}
					break;
				case PrizeConstant.Grade.third:
					if (null != prize.getNumber()) {
						subtractCouponRepertory( prize.getThingId(), prize.getNumber() );
					}
					break;
				case PrizeConstant.Grade.fourth:
					if (null != prize.getNumber()) {
						subtractCouponRepertory( prize.getThingId(), prize.getNumber() );
					}
					break;
				case PrizeConstant.Grade.five:
					if (null != prize.getNumber()) {
						subtractCouponRepertory( prize.getThingId(), prize.getNumber() );
					}
					break;
				case PrizeConstant.Grade.three:
					if (null != prize.getNumber()) {
						subtractCouponRepertory( prize.getThingId(), prize.getNumber() );
					}
					break;
				}
				// 保存奖品
				int i = prizeMapper.insert( prize );
				if (i != 1) {
					return false;
				}
			}
		}
		return true;

	}

	private void updateActivity(Activity activity, Integer attendNumber, Integer winningNumber) {
		activity.setAttendNumber( attendNumber );
		activity.setWinningNumber( winningNumber );
		activityMapper.updateByPrimaryKey( activity );
	}

	/**
	 * @param consumer
	 *            参加活动的用户
	 * @param activity
	 *            参加的活动
	 * @param winningPrize
	 *            中奖的奖品
	 * @param result
	 *            是否中奖
	 * @param status
	 *            是否已经领取
	 */
	private void creatActivityRecrod(MallConsumer consumer, Activity activity, Prize winningPrize, Integer result, Integer status) {
		ActivityRecord activityRecord = new ActivityRecord();
		activityRecord.setActivityId( activity.getId() );
		activityRecord.setCreateTime( new Date() );
		activityRecord.setMobile( consumer.getMobile() );
		activityRecord.setOpenid( consumer.getOpenid() );
		activityRecord.setPrizeId( winningPrize.getId() );
		activityRecord.setPrizeType( ActivityRecordConstant.Prize_type.COUPON );
		activityRecord.setResult( result );
		activityRecord.setShopId( activity.getShopId() );
		activityRecord.setStatus( status );
		activityRecordMapper.insert( activityRecord );
	}

	/**
	 * 抽奖
	 * 
	 * @param selectActivity
	 * @return
	 */
	private Integer winningPrizeLevel(Activity selectActivity) {
		// 活动有几个奖品等奖
		PrizeExample prizeExample = new PrizeExample();
		prizeExample.createCriteria().andActivityIdEqualTo( selectActivity.getId() );
		int prizeLevels = prizeMapper.countByExample( prizeExample );
		// 抽奖
		Integer probalility = selectActivity.getProbalility();
		LotteryUtils lotteryUtils = new LotteryUtils( probalility, prizeLevels );
		Integer winningPrizeLevel = lotteryUtils.winningPrizeLevel();
		return winningPrizeLevel;
	}

	private boolean verifyAttendTimes(MallConsumer consumer, Activity activity) throws ParseException {
		ActivityRecordExample activityRecordExample = new ActivityRecordExample();
		activityRecordExample.createCriteria().andActivityIdEqualTo( activity.getId() ).andMobileEqualTo( consumer.getMobile() );
		activityRecordExample.or().andOpenidEqualTo( consumer.getOpenid() );
		// 活动总次数
		int attendTotalTimes = activityRecordMapper.countByExample( activityRecordExample );
		if (attendTotalTimes >= activity.getTotalTimes()) {
			return false;
		}

		Calendar calendar = getZeroTimestamp();
		Date today = calendar.getTime();
		calendar.add( Calendar.DATE, 1 );
		Date tomorrow = calendar.getTime();
		activityRecordExample.createCriteria().andCreateTimeBetween( today, tomorrow );
		// 每天参加次数
		int attendEveryDayTimes = activityRecordMapper.countByExample( activityRecordExample );
		if (attendEveryDayTimes >= activity.getEverydayTimes()) {
			return false;
		}
		return true;
	}

	/**
	 * 获取时分秒为0的时间戳
	 * 
	 * @return
	 * @throws ParseException
	 */
	private Calendar getZeroTimestamp() throws ParseException {
		Calendar instance = Calendar.getInstance();
		instance.set( Calendar.HOUR_OF_DAY, 0 );
		instance.set( Calendar.MINUTE, 0 );
		instance.set( Calendar.SECOND, 0 );
		return instance;
	}

	private List<Activity> filtrateResultListByStatus(Integer activityStatus, List<Activity> selectByExample) {
		List<Activity> resultList = new ArrayList<>();
		for (Activity coupon : selectByExample) {

			switch (activityStatus.intValue()) {
			case ActivityConstant.Status.NOT_START:
				// 状态为未开始且起止时间符合“未开始”条件的为未开始的活动
				if (coupon.getStatus().equals( ActivityConstant.Status.NOT_START )
						&& this.setStatusByStartOnAndEndon( coupon ) == ActivityConstant.Status.NOT_START) {
					resultList.add( coupon );
				}
				break;

			case ActivityConstant.Status.UNDERWAY:
				// 状态不是已过期的，且起止时间符合“进行中”的为进行中的活动
				if (!coupon.getStatus().equals( ActivityConstant.Status.EXPIRED )
						&& this.setStatusByStartOnAndEndon( coupon ) == ActivityConstant.Status.EXPIRED) {
					resultList.add( coupon );
				}
				break;

			case ActivityConstant.Status.EXPIRED:
				// 状态为已过期的，且起止时间符合“已过期”的为已过期的活动
				if (coupon.getStatus().equals( ActivityConstant.Status.EXPIRED )
						&& this.setStatusByStartOnAndEndon( coupon ) == ActivityConstant.Status.EXPIRED) {
					resultList.add( coupon );
				}
				break;
			}
		}
		return resultList;
	}

	private int setStatusByStartOnAndEndon(Activity activity) {
		long now = System.currentTimeMillis();
		long startOn = activity.getStarton().getTime();
		long endOn = activity.getEndon().getTime();

		if (startOn < now) {
			return ActivityConstant.Status.NOT_START;
		}

		if (endOn <= now) {
			return ActivityConstant.Status.EXPIRED;
		}

		return ActivityConstant.Status.UNDERWAY;
	}
}
