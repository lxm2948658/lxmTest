package com.qianfan365.jcstore.test.service;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.StaticsBean;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.dao.inter.OrderStaticsMapper;
import com.qianfan365.jcstore.main.StartUp;
import com.qianfan365.jcstore.service.StaticsService;
import com.qianfan365.jcstore.service.UserService;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by K.F on 16/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StartUp.class)
public class StaticsTest {

    @Autowired
    StaticsService staticsService;

    @Autowired
    UserService userService;

    @Autowired
    OrderStaticsMapper orderStaticsMapper;

    /**
     * 测试店铺的统计
     */
    @Test
    public void testStaticsShop() {

        Date end = TimeUtils.formatDayEnd(new Date());
        Date start = DateUtils.addDays(end, -30);
        StaticsBean staticsBean = staticsService.getShopStatics(207, start, end);
        StaticsBean staticsBean2 = staticsService.getPersonalStatic(207, start, end);
        System.out.println(staticsBean.toString());
        System.out.println(staticsBean2.toString());

    }

    /**
     * 折线图数据测试
     */
    @Test
    public void testGroupStatics() {
        ResultData byId = userService.findById(39, 39);
        Map user = staticsService.getStaticsGroupDays((User) byId.get("user"));
        System.out.println(user.toString());
    }

    @Test
    public void testWeek() {
        //获取星期几
        int value = LocalDate.now().getDayOfWeek().getValue();
        //获取周一的日期
        Date start = TimeUtils.nDaysBefore(value - 1);
        //当前日期
        Date end = new Date();

        StaticsBean staticsBean = staticsService.getShopStatics(33, start, end);

        System.out.println(staticsBean.toString());
    }

    @Test
    public void testMonth() {
        Date start= DateUtils.setDays(new Date(),1);
        StaticsBean statics = staticsService.getStatics(getUser(528), start, new Date(),0);
        System.out.println(statics.toString());
    }

    @Test
    public void testSaleHostory() {
        Date end = TimeUtils.formatDayEnd(new Date());
        Date start = DateUtils.addMonths(end, -19);
        List<StaticsBean> user = staticsService.getSaleHistory(getUser(11), start, end);
        System.out.println(user);
    }

    @Test
    public void testCustomMapper() {
        List<Map<String, Object>> maps = orderStaticsMapper.selectShopStaticsByDay(18, new Date(), new Date());

        System.out.println(maps);
    }

    /**
     * 按下单日期维度的统计
     */
    @Test
    public void testStaticsByOrderDate() {
        Date end = TimeUtils.formatDayEnd(new Date());
        Date start = DateUtils.addDays(end, -30);
        List<StaticsBean> staticsByDay = staticsService.getStaticsByDay(getUser(33), start, end,1,0);
        System.out.print(staticsByDay);
    }
    /**
     * 按下单员工维度的统计
     */
    @Test
    public void testStaticsByStaff() {
        Date end = TimeUtils.formatDayEnd(new Date());
        Date start = DateUtils.addDays(end, -30);
        List<StaticsBean> staticsByDay = staticsService.getStaticsByStaff(getUser(430), start, end,1);
        List<Map<String, Object>> maps = orderStaticsMapper.selectUserStaticsByStaff(430, start, end);
        System.out.print(staticsByDay);
    }
    /**
     * 按下单商品的维度的统计
     */
    @Test
    public void testStaticsByProduct() {
        Date end = TimeUtils.formatDayEnd(new Date());
        Date start = DateUtils.addDays(end, -30);
        List<StaticsBean> staticsByDay = staticsService.getStaticsByProduct(getUser(33), start, end,1,0);
        List<Map<String, Object>> maps = orderStaticsMapper.selectUserStaticsByPrduct(33, start, end);
        System.out.print(staticsByDay);
    }

    public User getUser(int id) {


        ResultData byId = userService.findById(id, id);

        return (User) byId.get("user");

    }


}
