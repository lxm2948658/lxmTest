/**
 * 
 */
package com.qianfan365.jcstore.test.service;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Activity;
import com.qianfan365.jcstore.dao.inter.ActivityMapper;
import com.qianfan365.jcstore.main.StartUp;
import com.qianfan365.jcstore.service.ActivityService;

/**
 * @author wpy com.qianfan365.jcstore.test.service 2017年6月5日 - 上午11:08:00
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StartUp.class)
public class ActivityTest {
	@Resource
	private ActivityService activityService;
	@Autowired
	private ActivityMapper activityMapper;
	
	@Test
	public void test(){
		System.out.println(activityMapper);
	}

	@Test
	public void testSave() {
		Calendar instance = Calendar.getInstance();
		instance.set(2017, 8, 20);
		
		String name = "摇一摇";
		Integer type = 3;
		Double money = null;
		Integer shopId = 1;
		Date starton = new Date();
		Date endon = instance.getTime();
		Integer status = 0;
		
		Integer circulation = 3;
		Integer maximum = null;
		
		Double requireMoney = null;
		Integer unconditional = null;
		
		Integer everydayTimes = 3;
		Integer totalTimes = 9;
		
		Integer firstPrize = 1;
		Integer firstPrizeNumber = 3;
		Integer secondPrize = 2;
		Integer secondPrizeNumber = 3;
		Integer thirdPrize = 3;
		Integer thirdPrizeNumber = 3;
		
		Integer probalility = 80;
		String comment = "啦啦啦啦啦啦搞活动";
		
		Date createTime = new Date();
		Date updateTime = new Date();
		
		/*Activity activity = new Activity(null, name, type, money, shopId, starton, endon, circulation, maximum,
				requireMoney, unconditional, status, everydayTimes, totalTimes, probalility, firstPrize,
				firstPrizeNumber, secondPrize, secondPrizeNumber, thirdPrize, thirdPrizeNumber, comment, createTime,
				updateTime);*/
		/*ResultData save = activityService.save(activity);
		System.out.println(save.toString());
*/	}
}
