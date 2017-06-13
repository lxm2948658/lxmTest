package com.qianfan365.jcstore.controller.mobile;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.StaticsBean;
import com.qianfan365.jcstore.common.bean.StaticsHistory;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.common.util.WeiYuRequst;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by K.F on 16/8/13.
 */
@SuppressWarnings("all")
@Controller
@RequestMapping(path = "/mobile/statics")
public class StaticsController extends BaseController {

    @Autowired
    StaticsService staticsService;
    @Autowired
    ShopService shopService;
    @Autowired
    MessageService messageService;
    @Autowired
    MarketingService marketingService;
    @Autowired
    OrderInfoService orderInfoService;

    /**
     * 按日期分组查询
     *
     * @return 分组结果
     */
    @ResponseBody
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    @ModulePassport(moduleids = {ModuleType.STATICS}, setuser = true)
    public ResultData statics(HttpSession session) {
        User user = getLoginUser(session);
        Map staticsGroupDays = staticsService.getStaticsGroupDays(user);
        //最近三个月的月销售历史数据
        List list = staticsService.checkAndInit(user);
        boolean allInfo = (boolean) list.get(1);
        Integer countId;
        //用户注册月份
        Date userRegistMonth;
        Shop shop = shopService.findShop(user);
        if (allInfo) {
            //全员信息,采用店铺时间
            userRegistMonth = DateUtils.truncate(shop.getCreatetime(), Calendar.MONTH);
            countId = shop.getId();
        } else {
            //个人信息采用个人时间
            userRegistMonth = DateUtils.truncate(user.getCreatetime(), Calendar.MONTH);
            countId = user.getId();
        }
        //根据页码产生最近三个个月份,小月份不能小于用户注册日期
        Date queryDate = DateUtils.truncate(new Date(), Calendar.MONTH);
        Date end = queryDate;
        Date start = DateUtils.addMonths(end, -2);
        if (start.before(userRegistMonth)) {
            start = userRegistMonth;
        }
        //月初日期
        List<StaticsBean> saleHistory = staticsService.getSaleHistory(user, start, new Date());
        saleHistory.stream().forEach(bean -> {
            Date date = TimeUtils.format(bean.getDateStr(), "yyyy-MM");
            bean.setDateStr(TimeUtils.formatAsString(date, "M月"));
        });
        //微商城访问量： 直接取 “运营后台-平台管理-营销效果监控” 中 “店铺分享的点击量”,当有WY商城时,取的商城返回的数量
        List<String> moduleids = SoftTypeConstant.softMap().get(user.getSoftId());
        String shopView = "——";
        if (moduleids.contains(ModuleType.MARKETING)) {
            if (moduleids.contains(ModuleType.MARKETING_PLATFORM_MICROMALL)) {
                shopView = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_SHOP_VIEW.url, user).excute(Integer.class).toString();
            } else {
                shopView = marketingService.findShopViewByUser(shop.getId()).toString();
            }
        }
        //订单完成率  = 所有历史累计已完成的订单/所有历史累计已完成+待完成的订单
        int element = orderInfoService.countOrder4Completion(allInfo, false, countId);
        int denominator = orderInfoService.countOrder4Completion(allInfo, true, countId);
        int completion = Math.round(((float) element / (float) denominator) * 100);
        return ResultData.build().put("statics", staticsGroupDays).put("saleHistory", saleHistory)
                .put("shopView", shopView).put("completion", completion);
    }

    /**
     * 查询指定日期的统计
     *
     * @param start
     * @param end
     * @param notification 是否是从通知栏跳的
     * @param flag         标识是加载全部数据（0） ,门店数据（1）商城数据（2）
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/period")
    @ModulePassport(moduleids = {ModuleType.STATICS}, setuser = true)
    public ResultData staticsByDay(HttpSession session,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                   @RequestParam(defaultValue = "0") Integer flag,
                                   @RequestParam(defaultValue = "0") Integer notification) {
        User user = getLoginUser(session);
        //消息标记已读
        if (notification == 1) {
            Calendar c = Calendar.getInstance();
            c.setTime(start);
            Integer linkParameter = c.get(Calendar.YEAR) * 100 + c.get(Calendar.MONTH) + 1; //一月是0，所以加一
            messageService.tabRead(user.getId(), MessageType.MONTHLY_STATISTICS, linkParameter);
        }
        if(flag==2&&!SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.MARKETING_PLATFORM_MICROMALL)){
            return ResultData.build().setStatus(Status.MODULE_CHANGED);
        }
        StaticsBean statics = staticsService.getStatics(user, start, end, flag);
        boolean productModule = SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT);
        return ResultData.build().put("statics", statics).put("product", productModule);
    }

    /**
     * 根据下单日期（按天）统计销售情况
     *
     * @param flag 门店数据（1）商城数据（2）
     */
    @ResponseBody
    @RequestMapping(value = "/group/dimension")
    @ModulePassport(moduleids = {ModuleType.STATICS}, setuser = true)
    public ResultData staticsGroupByOrderDate(HttpSession session, @RequestParam(defaultValue = "1") Integer currentPage,
                                              String type, @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
                                              @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
                                              @RequestParam(defaultValue = "1") Integer flag
    ) {
        User user = getLoginUser(session);
        Assert.notNull(type);
        boolean productModule = SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.PRODUCT);
        boolean wYModule = SoftTypeConstant.softMap().get(user.getSoftId()).contains(ModuleType.MARKETING_PLATFORM_MICROMALL);
        List<StaticsBean> statics = new ArrayList<>();
        if (flag == 1) {
            if (type.equals("date")) {
                statics = staticsService.getStaticsByDay(user, start, end, currentPage, flag);
            }

            if (type.equals("product")) {
                if (!productModule) {
                    return ResultData.build().setStatus(Status.MODULE_CHANGED);
                }
                statics = staticsService.getStaticsByProduct(user, start, end, currentPage, flag);
            }
            if (type.equals("staff")) {
                statics = staticsService.getStaticsByStaff(user, start, end, currentPage);
            }
        } else if (flag == 2) {
            if (!wYModule) {
                return ResultData.build().setStatus(Status.MODULE_CHANGED);
            }
            //商城统计
            if (type.equals("date")) {
                statics = staticsService.getStaticsByDay(user, start, end, currentPage, flag);
            }
            if (type.equals("product")) {
                if (!productModule) {
                    return ResultData.build().setStatus(Status.MODULE_CHANGED);
                }
                statics = staticsService.getStaticsByProduct(user, start, end, currentPage, flag);
            }
        } else {
            return ResultData.build().parameterError();
        }

        return ResultData.build().parsePageBean((Page<?>) statics).put("product", productModule);
    }

    /**
     * 查询今日的统计
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/period/today")
    @ModulePassport(moduleids = {ModuleType.STATICS})
    public ResultData staticsPeriodToDay(HttpSession session) {
        User user = getLoginUser(session);
        StaticsBean statics = staticsService.getStatics(user, new Date(), new Date(), 0);
        return ResultData.build().put("statics", statics);
    }

    /**
     * 查询昨日的统计
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/period/yesterday")
    @ModulePassport(moduleids = {ModuleType.STATICS})
    public ResultData staticsPeriodYesterday(HttpSession session) {
        User user = getLoginUser(session);
        Date start = TimeUtils.nDaysBefore(1);
        Date end = TimeUtils.nDaysBefore(1);
        StaticsBean statics = staticsService.getStatics(user, start, end, 0);
        return ResultData.build().put("statics", statics);
    }

    /**
     * 查询本周的的统计
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/period/week")
    @ModulePassport(moduleids = {ModuleType.STATICS})
    public ResultData staticsPeriodWeek(HttpSession session) {
        User user = getLoginUser(session);
        //获取星期几
        int value = LocalDate.now().getDayOfWeek().getValue();
        //获取周一的日期
        Date start = TimeUtils.nDaysBefore(value - 1);
        //当前日期
        Date end = new Date();
        StaticsBean statics = staticsService.getStatics(user, start, end, 0);
        return ResultData.build().put("statics", statics);
    }

    /**
     * 查询本周的的统计
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/period/month")
    @ModulePassport(moduleids = {ModuleType.STATICS})
    public ResultData staticsPeriodMonth(HttpSession session) {
        User user = getLoginUser(session);
        //月初日期
        Date start = DateUtils.setDays(new Date(), 1);
        StaticsBean statics = staticsService.getStatics(user, start, new Date(), 0);
        return ResultData.build().put("statics", statics);
    }

    /**
     * 按年份加载销售历史
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saleHistory")
    @ModulePassport(moduleids = {ModuleType.STATICS})
    public ResultData saleHistory(HttpSession session) {
        User user = getLoginUser(session);

        List list = staticsService.checkAndInit(user);
        boolean allInfo = (boolean) list.get(1);
        //用户注册月份
        Date userRegistMonth;
        if (allInfo) {
            //全员信息,采用店铺时间
            userRegistMonth = DateUtils.truncate(shopService.findShop(user).getCreatetime(), Calendar.MONTH);
        } else {
            //个人信息采用个人时间
            userRegistMonth = DateUtils.truncate(user.getCreatetime(), Calendar.MONTH);
        }
        //查询开始的时间
        Date start = userRegistMonth;
        //查询结束时间
        Date end = new Date();

        ResultData result = ResultData.build();

        List<StaticsBean> saleHistory = staticsService.getSaleHistory(user, start, end);
        Map<String, List<StaticsBean>> datas = saleHistory.stream().collect(Collectors.groupingBy(staticsBean -> {
            Date date = TimeUtils.format(staticsBean.getDateStr(), "yyyy-MM");
            return TimeUtils.formatAsString(date, "yyyy年");
        }, Collectors.toList()));

        saleHistory.stream().forEach(bean -> {
            Date date = TimeUtils.format(bean.getDateStr(), "yyyy-MM");
            bean.setDateStr(TimeUtils.formatAsString(date, "M月"));
        });

        Map<String, List<StaticsBean>> map = new TreeMap<String, List<StaticsBean>>(new Comparator<String>() {
            public int compare(String key1, String key2) {
                return Integer.valueOf(key2.substring(0, 4)) - Integer.valueOf(key1.substring(0, 4));
            }
        });
        datas.entrySet().forEach(entry -> {
            map.put(entry.getKey(), entry.getValue());
        });
        List<StaticsHistory> histories = new ArrayList<>();
        map.entrySet().forEach(stringListEntry -> {
            histories.add(new StaticsHistory(stringListEntry.getKey(), stringListEntry.getValue()));
        });

        result.put("saleHistory", histories);
        return result;
    }

}
