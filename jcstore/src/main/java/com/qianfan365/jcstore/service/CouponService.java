/**
 * 
 */
package com.qianfan365.jcstore.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.ActivityConstant;
import com.qianfan365.jcstore.common.constant.ActivityRecordConstant;
import com.qianfan365.jcstore.common.constant.CouponConstant;
import com.qianfan365.jcstore.common.pojo.Activity;
import com.qianfan365.jcstore.common.pojo.ActivityRecord;
import com.qianfan365.jcstore.common.pojo.ActivityRecordExample;
import com.qianfan365.jcstore.common.pojo.Coupon;
import com.qianfan365.jcstore.common.pojo.CouponExample;
import com.qianfan365.jcstore.common.pojo.MallConsumer;
import com.qianfan365.jcstore.common.pojo.Prize;
import com.qianfan365.jcstore.common.pojo.PrizeExample;
import com.qianfan365.jcstore.dao.inter.ActivityMapper;
import com.qianfan365.jcstore.dao.inter.ActivityRecordMapper;
import com.qianfan365.jcstore.dao.inter.CouponMapper;
import com.qianfan365.jcstore.dao.inter.CustomerMapper;
import com.qianfan365.jcstore.dao.inter.PrizeMapper;

/**
 * 优惠卷Service
 * @author wpy
 * @package_name com.qianfan365.jcstore.service
 * @time 2017年6月6日 - 上午10:13:02
 */
@Service
public class CouponService {
	
	@Resource
	private CouponMapper couponMapper;
	@Resource
	private CustomerMapper customerMapper;
	@Resource
	private ActivityMapper activityMapper;
	@Resource
	private PrizeMapper prizeMapper;
	@Resource
	private ActivityRecordMapper activityRecordMapper;
	
	
	/**
	 * 商家新增一张优惠券
	 * @param coupon
	 * @return
	 */
	public ResultData add(Coupon coupon){
		coupon.setType(CouponConstant.Type.SHOP);
		coupon.setCreateTime(new Date());
		coupon.setStatus(setStatusByStartOnAndEndon(coupon));
		couponMapper.insert(coupon);
		return ResultData.build().put("coupon", coupon);
	}
	
	
	/**
	 * 商家删除一张优惠券
	 * @param coupon
	 * @return
	 */
	public ResultData delete(Coupon coupon){
		Integer id = coupon.getId();
		Coupon selectCoupon = couponMapper.selectByPrimaryKey(id);
		if(selectCoupon == null || !selectCoupon.getShopId().equals(coupon.getShopId())){
			return ResultData.build().error();
		}
		
		//还有未结束的活动正在使用这张优惠卷，不能删除
		List<Activity> activityList = haveAnyActivityUsedNow(coupon);
		if(!activityList.isEmpty()){
			return ResultData.build().parseList(activityList).error();
		}
		
		selectCoupon.setStatus(CouponConstant.Status.EXPIRED);
		selectCoupon.setUpdateTime(new Date());
		couponMapper.updateByPrimaryKey(selectCoupon);
		return ResultData.build().success();
	}


	/**
	 * 是否有正在进行中和未开始的活动在使用这张优惠券
	 * @param coupon
	 * @return
	 */
	private List<Activity> haveAnyActivityUsedNow(Coupon coupon) {
		List<Activity> activities = new ArrayList<>();
		
		PrizeExample prizeExample = new PrizeExample();
		prizeExample.createCriteria().andThingIdEqualTo(coupon.getId());
		List<Prize> prizeList = prizeMapper.selectByExample(prizeExample);
		
		for (Prize prize : prizeList) {
			long now = System.currentTimeMillis();
			Activity activity = activityMapper.selectByPrimaryKey(prize.getActivityId());
			if(!activity.getStatus().equals(ActivityConstant.Status.EXPIRED)
					&& activity.getEndon().getTime() < now){
				activities.add(activity);
			}
		}
		return activities;
	}
	
	/**
	 * 商家查询优惠券列表
	 * @return
	 */
	public ResultData list(Integer shopId,Integer couponStatus){
		CouponExample example = new CouponExample();
		example.createCriteria().andShopIdEqualTo(shopId);
		List<Coupon> selectByExample = couponMapper.selectByExample(example);
		
		if(selectByExample == null || selectByExample.isEmpty()){
			return ResultData.build().parseList(new ArrayList<Coupon>()).success();
		}
		
		List<Coupon> resultList = filtrateResultListByStatus(couponStatus, selectByExample);
		for (Coupon coupon : resultList) {
			if(!coupon.getStatus().equals(couponStatus)){
				coupon.setStatus(couponStatus);
				coupon.setUpdateTime(new Date());
				coupon.setRemark("自动设置Status为"+couponStatus);
				couponMapper.updateByPrimaryKey(coupon);
			}
		}
		return ResultData.build().parseList(resultList).success();
	}


	/**
	 * 根据需要的优惠券状态，筛选出符合要求的resultList
	 * @param couponStatus 
	 * @param selectByExample
	 * @param resultList
	 * @return 
	 */
	private List<Coupon> filtrateResultListByStatus(Integer couponStatus, List<Coupon> selectByExample) {
		List<Coupon> resultList = new ArrayList<>();
		for (Coupon coupon : selectByExample) {
			
			switch (couponStatus.intValue()) {
			case CouponConstant.Status.NOT_START: 
				//状态为未开始且起止时间符合“未开始”条件的为未开始的活动
				if(coupon.getStatus().equals(CouponConstant.Status.NOT_START)
						&& this.setStatusByStartOnAndEndon(coupon) == CouponConstant.Status.NOT_START){
//					coupon.setStatus(CouponConstant.Status.NOT_START);
					resultList.add(coupon);
				}
				break;
				
			case CouponConstant.Status.UNDERWAY:
				//状态不是已过期的，且起止时间符合“进行中”的为进行中的活动
				if(!coupon.getStatus().equals(CouponConstant.Status.EXPIRED)
						&& this.setStatusByStartOnAndEndon(coupon) == CouponConstant.Status.EXPIRED){
//					coupon.setStatus(CouponConstant.Status.UNDERWAY);
					resultList.add(coupon);
				}
				break;
				
			case CouponConstant.Status.EXPIRED:
				//状态为已过期的，且起止时间符合“已过期”的为已过期的活动
				if(coupon.getStatus().equals(CouponConstant.Status.EXPIRED)
						&& this.setStatusByStartOnAndEndon(coupon) == CouponConstant.Status.EXPIRED){
//					coupon.setStatus(CouponConstant.Status.EXPIRED);
					resultList.add(coupon);
				}
				break;
			}
		}
		return resultList;
	}

	/**
	 * 根据活动的起止时间判断活动的状态
	 * @param coupon
	 */
	private int setStatusByStartOnAndEndon(Coupon coupon){
		long now = System.currentTimeMillis();
		long startOn = coupon.getStarton().getTime();
		long endOn = coupon.getEndon().getTime();
		
		if(startOn < now){
			return CouponConstant.Status.NOT_START;
		}
		
		if(endOn <= now){
			return CouponConstant.Status.EXPIRED;
		}
		
		return CouponConstant.Status.UNDERWAY;
	}
	
	/**
	 * 用户领取一张优惠券
	 * @return
	 */
	public ResultData receive(Coupon coupon,MallConsumer consumer,Activity activity){
		if( activity != null){
			Activity selectActivity = activityMapper.selectByPrimaryKey(activity.getId());
			//校验activity，重写了equals方法
			if(!selectActivity.equals(activity)){
				return ResultData.build().parameterError();
			}
		}
		//校验coupon，重写了equals方法
		Coupon selectCoupon = couponMapper.selectByPrimaryKey(coupon.getId());
		if(!selectCoupon.equals(coupon)){
			return ResultData.build().parameterError();
		}
		//校验活动是否还有优惠券库存
		if(!(coupon.getCirculation() > coupon.getReceiveNumber())){
			return ResultData.build().put(500,"优惠券被抢空了", "have no coupon");
		}
		//校验用户领取的优惠卷数量是否达到最大
		CouponExample couponExample = new CouponExample();
		couponExample.createCriteria().andCouponIdEqualTo(coupon.getId())
		.andMobileEqualTo(consumer.getMobile());
		List<Coupon>  selectByExample= couponMapper.selectByExample(couponExample);
		if(!(selectByExample.size() < coupon.getMaximum())){
			return ResultData.build().put(500,"领取的优惠券数量已达到最大限额", "can't receive more");
		}
		Integer shopCouponId = coupon.getId();
		//新建一条用户优惠券
		Integer userCouponId = creatUserCoupon(coupon, consumer, shopCouponId);
		//领取量加1
		updateShopCoupon(coupon, shopCouponId);
		//跟新活动记录
		updateActivityRecord(coupon, consumer, activity, shopCouponId, userCouponId);
		return ResultData.build().success();
	}


	/**
	 * 跟新商品优惠卷的统计信息
	 * @param coupon
	 * @param shopCouponId
	 */
	private void updateShopCoupon(Coupon coupon, Integer shopCouponId) {
		Coupon shopCoupon = new Coupon();
		shopCoupon.setId(shopCouponId);
		shopCoupon.setReceiveNumber(coupon.getReceiveNumber() + 1);
		couponMapper.updateByPrimaryKeySelective(shopCoupon);//只跟新不为空字段
	}


	/**
	 * 创建一条用户优惠券
	 * @param coupon
	 * @param consumer
	 * @param shopCouponId
	 * @return
	 */
	private Integer creatUserCoupon(Coupon coupon, MallConsumer consumer, Integer shopCouponId) {
		coupon.setType(CouponConstant.Type.USER);
		coupon.setCreateTime(new Date());
		coupon.setUpdateTime(null);
		coupon.setUsedNumber(null);
		coupon.setReceiveNumber(null);
		coupon.setCouponId(shopCouponId);
		coupon.setMobile(consumer.getMobile());
		coupon.setId(null);
		coupon.setSerial(this.generateCouponSerial(coupon.getShopId()));
		couponMapper.insert(coupon);
		Integer userCouponId = coupon.getId();
		return userCouponId;
	}


	/**
	 * 更新活动参与记录
	 * @param coupon
	 * @param consumer
	 * @param activity
	 * @param shopCouponId
	 * @param userCouponId
	 */
	private void updateActivityRecord(Coupon coupon, MallConsumer consumer, Activity activity, Integer shopCouponId,
			Integer userCouponId) {
		if(activity != null){
			ActivityRecordExample example = new ActivityRecordExample();
			example.createCriteria().andActivityIdEqualTo(activity.getId())
			.andOpenidEqualTo(consumer.getOpenid());
			List<ActivityRecord> list = activityRecordMapper.selectByExample(example);
			list.get(0).setMobile(consumer.getMobile());
			list.get(0).setUsedTime(new Date());
			list.get(0).setPrizeId(shopCouponId);
			list.get(0).setStatus(ActivityRecordConstant.Status.YES);
			activityRecordMapper.updateByPrimaryKey(list.get(0));
			
		}else{//线下扫码绑定
			ActivityRecord activityRecord = new ActivityRecord();
			activityRecord.setCreateTime(new Date());
			activityRecord.setMobile(consumer.getMobile());
			activityRecord.setPrizeId(userCouponId);
			activityRecord.setPrizeType(ActivityRecordConstant.Prize_type.COUPON);
			activityRecord.setShopId(coupon.getShopId());
			activityRecord.setStatus(ActivityRecordConstant.Status.YES);
			activityRecord.setPrizeType(ActivityRecordConstant.Prize_type.COUPON);
		}
	}
	
	/**
	 * 生成优惠券编码  （商户id+YHJ+时分秒+4位随机数）
	 * @param shopId
	 * @return
	 */
	private String generateCouponSerial(Integer shopId){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
		return ""+shopId+"YHJ"+simpleDateFormat.format(new Date())+RandomUtils.nextInt(10000);
	}
	
	/**
	 * 用户消费优惠卷
	 * @return
	 */
	public ResultData consume(MallConsumer consumer,Coupon coupon){
		Coupon selectCoupon = couponMapper.selectByPrimaryKey(coupon.getId());
		if(selectCoupon.getMobile().equals(consumer.getMobile())){
			ResultData.build().parameterError();
		}
		selectCoupon.setUsed(CouponConstant.Used_State.USED);
		couponMapper.updateByPrimaryKey(selectCoupon);
		return ResultData.build().success();
	}
	
	
	/**
	 * 用户查询优惠卷列表
	 * @return
	 */
	public ResultData consumerList(MallConsumer consumer,Integer couponStatus){
		CouponExample example = new CouponExample();
		example.createCriteria().andMobileEqualTo(consumer.getMobile())
		.andUsedEqualTo(CouponConstant.Used_State.NOT);
		List<Coupon> list = couponMapper.selectByExample(example);
		//按状态过滤
		List<Coupon> resultList = this.filtrateResultListByStatus(couponStatus, list);
		
		//跟新那些状态不符的
		for (Coupon coupon : resultList) {
			if(!coupon.getStatus().equals(couponStatus)){
				coupon.setStatus(couponStatus);
				coupon.setUpdateTime(new Date());
				coupon.setRemark("自动设置Status为"+couponStatus);
				couponMapper.updateByPrimaryKey(coupon);
			}
		}
		return ResultData.build().parseList(resultList).success();
	}
}
