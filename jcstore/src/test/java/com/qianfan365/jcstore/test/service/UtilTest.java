package com.qianfan365.jcstore.test.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.pagehelper.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.StaticsBean;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.AmountUtil;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.common.util.WeiYuRequst;

/**
 * Created by qianfanyanfa on 16/8/16.
 */
public class UtilTest {

    User user = new User();

    @Before
    public void before() {
        WeiYuRequst.setWyServerUrl("http://192.168.10.29:8787/weiyu");
        user.setUsername("keithfu");
    }

    @Test
    public void testReg() {
        String reg = "^[A-Za-z\\u4e00-\\u9fa5]*$";
        Assert.assertTrue("中文aABz中文春树暮云s".matches(reg));

        String regNum = "^[0-9\\-]*$";

        Assert.assertTrue("121-20-".matches(regNum));
        Assert.assertTrue("".matches(regNum));
    }

    @Test
    public void testGson() {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        ResultData build = ResultData.build();
        System.out.println(gson.toJson(build).toString());
    }

    @Test
    public void testLocalDate() {

        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

        System.out.println(dayOfWeek);

        Date date = TimeUtils.nDaysBefore(0);


        Date date1 = DateUtils.setDays(date, 1);
        System.out.println(date1);

    }

    @Test
    public void testYunToFen() {
        BigInteger bigInteger = AmountUtil.yuan2Fen(126987586987412.34);
        System.out.println(bigInteger);
        Assert.assertTrue(Float.MAX_VALUE <= Double.MAX_VALUE);
    }

    @Test
    public void caculateDateMonthSpace() {
        Date start = TimeUtils.format("2014-12-12", "yyyy-MM-dd");
        Date end = TimeUtils.format("2015-01-13", "yyyy-MM-dd");
        System.out.println(TimeUtils.getMonthSpace(start, end));

        start = TimeUtils.format("2014-12-12", "yyyy-MM-dd");
        end = TimeUtils.format("2014-12-01", "yyyy-MM-dd");
        Date format = TimeUtils.format( new Date(), "yyyy-MM-dd" );
        System.out.println( format );
        System.out.println(TimeUtils.getMonthSpace(start, end));
    }

    @Test
    public void testRestTemplate() {

        WeiYuRequst.setWyServerUrl("http://192.168.10.29:8787/weiyu");
        User user = new User();
        user.setUsername("keithfu");
        Date start = new Date();
        String startDate = TimeUtils.formatLocal(start);
        String endDate = TimeUtils.formatLocal(start);
        List<StaticsBean> list2 = new ArrayList<>();
        List<StaticsBean> excute = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_SALEHISTORY.url, user).excuteList(StaticsBean.class, startDate, endDate);

        excute.forEach(staticsBean -> {
            System.out.println(startDate);
        });

    }

    @Test
    public void testGroup() {

        Date start = new Date();
        String startDate = TimeUtils.formatLocal(start);
        String endDate = TimeUtils.formatLocal(start);
        List<StaticsBean> list2 = new ArrayList<>();
        Page<StaticsBean> excute = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_GROUP_DIMENSION.url, user).excutePage(StaticsBean.class, startDate, endDate, "date", 20, 1);

        excute.getResult().forEach(o -> {
            System.out.println(o);
        });
    }

    @Test
    public void testAddress() {
        String excute = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.SHOP_URL.url, user).excute(String.class);
        System.out.println(excute);
        String purl = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.PRODUCT_URL.url, user).excute(String.class, 23);
        System.out.println(purl);

    }
    @Test
    public void date() {
    	/*
    	 * public static void main(String[] args) { for (int i = 0; i < 10; i++) {
    	 * LotteryUtils lotteryUtils20 = new LotteryUtils( 20 ); System.out.println(
    	 * "是否已中奖："+(lotteryUtils20.isWinning()?"是":"否") );
    	 * 
    	 * } for (int i = 0; i < 10; i++) { LotteryUtils lotteryUtils10 = new
    	 * LotteryUtils( 10 ,3); if(lotteryUtils10.winningPrizeLevel().equals( 0 )){
    	 * System.out.println( "未中奖" ); }else{ System.out.println(
    	 * "中了"+lotteryUtils10.winningPrizeLevel()+"等奖" ); } } }
    	 */

    }

}
