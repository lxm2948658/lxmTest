package com.qianfan365.jcstore.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.bean.StaticsBean;
import com.qianfan365.jcstore.common.constant.ModuleConstant;
import com.qianfan365.jcstore.common.constant.PermissionConstant;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.pojo.UserPermission;
import com.qianfan365.jcstore.common.util.AmountUtil;
import com.qianfan365.jcstore.common.util.TimeUtils;
import com.qianfan365.jcstore.common.util.WeiYuRequst;
import com.qianfan365.jcstore.dao.inter.OrderInfoMapper;
import com.qianfan365.jcstore.dao.inter.OrderStaticsMapper;
import com.qianfan365.jcstore.dao.inter.UserMapper;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by K.F on 16/8/13.
 * 统计服务
 */
@SuppressWarnings("all")
@Service("staticsService")
public class StaticsService {

    @Autowired
    PermissionInfoService permissionInfoService;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Autowired
    OrderStaticsMapper orderStaticsMapper;

    @Autowired
    ShopService shopService;

    @Autowired
    UserMapper userMapper;

    /**
     * 查询用户指定时间内的经营统计,会要据权限决定是否查看有,还是个人
     *
     * @param user  用信息信息
     * @param start 统计的开始日期
     * @param end   统计的结束日期
     * @param flag  用于标识,查询的是门店的（1）,商城的（2）,还是全部的（0）,
     * @return StaticsBean
     */
    public StaticsBean getStatics(User user, Date start, Date end, int flag) {
        start = DateUtils.truncate(start, Calendar.DATE);
        end = TimeUtils.formatDayEnd(end);
        List list = checkAndInit(user);
        boolean allInfo = (boolean) list.get(1);
        int uid = (int) list.get(0);
        if (allInfo) {
            //检查是否有商城的权限
            StaticsBean statics = null;
            if (flag != 1 && SoftTypeConstant.checkMoudule(user, ModuleConstant.ModuleType.MARKETING_PLATFORM_MICROMALL)) {
                String startDate = TimeUtils.formatLocal(start);
                String endDate = TimeUtils.formatLocal(end);
                statics = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_PERIOD.url, user).excute(StaticsBean.class, startDate, endDate);
            }
            if (flag == 2) {
                return statics;
            }
            StaticsBean shopStatics = getShopStatics(uid, start, end);
            if (statics != null) {
                shopStatics.setOrderNum(shopStatics.getOrderNum() + statics.getOrderNum());
                shopStatics.setReceivedAmout(statics.getReceivedAmout());
            }
            return shopStatics;
        }
        return getPersonalStatic(uid, start, end);
    }

    /**
     * 个人收入的统计
     *
     * @param start 统计的开始日期
     * @param end   统计的结束日期
     * @return
     */
    public StaticsBean getPersonalStatic(int uid, Date start, Date end) {
        List<Map<String, Object>> dataN = orderStaticsMapper.selectNStaticsByUser(uid, start, end);
        List<Map<String, Object>> dataY = orderStaticsMapper.selectYStaticsByUser(uid, start, end);
        dataN.addAll(dataY);
        return covertToSB(dataN);
    }

    /**
     * map to StaticsBean
     */
    private StaticsBean covertToSB(List<Map<String, Object>> data) {

        StaticsBean staticsBean = StaticsBean.emptyBean();
        data.forEach(map -> {
            if (map.get("order_status").toString().equals("1")) {
                staticsBean.setNotReceiveAmout(AmountUtil.yuan2Fen((Double) map.get("amount")));
            } else {
                staticsBean.setReceivedAmout(AmountUtil.yuan2Fen((Double) map.get("amount")));
            }
            Long count = (Long) StaticsBean.getCountByItem(map);
            staticsBean.setOrderNum(staticsBean.getOrderNum() + count);
        });
        return staticsBean;

    }


    /**
     * 店铺收入统计
     *
     * @param uid   用户的id
     * @param start 统计的开始日期
     * @param end   统计的结束日期
     * @return
     */
    public StaticsBean getShopStatics(int uid, Date start, Date end) {
        Shop shop = shopService.findShopUser(uid);
        List<Map<String, Object>> dataN = orderStaticsMapper.selectNStaticsByShop(shop.getId(), start, end);
        List<Map<String, Object>> dataY = orderStaticsMapper.selectYStaticsByShop(shop.getId(), start, end);
        dataN.addAll(dataY);
        return covertToSB(dataN);
    }

    public Map getStaticsGroupDays(User user) {
        //统计30天的经营概况
        Date end = new Date();
        Date start = DateUtils.addDays(end, -30);

        start = DateUtils.truncate(start, Calendar.DATE);
        end = TimeUtils.formatDayEnd(end);

        List list = checkAndInit(user);
        int uid = (int) list.get(0);
        if ((boolean) list.get(1)) {
            boolean hasWY = SoftTypeConstant.checkMoudule(user, ModuleConstant.ModuleType.MARKETING_PLATFORM_MICROMALL);
            return getShopGroupStatics(userMapper.selectByPrimaryKey(uid), start, end, hasWY);
        }
        return getUserGroupStatics(uid, start, end);
    }

    /**
     * 店铺经营统计信息
     *
     * @param uid
     * @param start
     * @param end
     * @return
     */
    public Map getShopGroupStatics(User user, Date start, Date end, boolean isNeedWY) {
        Shop shop = shopService.findShopUser(user.getId());
        List<Map<String, Object>> dataNo = orderStaticsMapper.selectNGroupDaysStaticsByShop(shop.getId(), start, end);
        List<Map<String, Object>> dataYes = orderStaticsMapper.selectYGroupDaysStaticsByShop(shop.getId(), start, end);
        List<StaticsBean> wyDataYes = null;
        if (isNeedWY) {
            wyDataYes = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_30DAYS.url, user).excuteList(StaticsBean.class);
        }
        return StaticsBean.generateStaticsData(dataNo, dataYes, start, wyDataYes);
    }

    /**
     * 个人经营统计信息
     *
     * @param uid
     * @param start
     * @param end
     * @return
     */
    public Map getUserGroupStatics(int uid, Date start, Date end) {
        List<Map<String, Object>> dataNo = orderStaticsMapper.selectNGroupDaysStaticsByUser(uid, start, end);
        List<Map<String, Object>> dataYes = orderStaticsMapper.selectYGroupDaysStaticsByUser(uid, start, end);
        return StaticsBean.generateStaticsData(dataNo, dataYes, start, null);
    }

    public List checkAndInit(User user) {

        boolean allInfo = false;
        int uid = user.getId();
        if (user.getBelongs() == 0) {
            allInfo = true;
        } else {
            user.getId();
            UserPermission permission = permissionInfoService.findByUidPid(PermissionConstant.PermissionType.VIEW_ALL, user.getId());
            if (permission == null) {
                allInfo = false;
            } else {
                allInfo = true;
                uid = user.getBelongs();
            }
        }
        return Lists.newArrayList(uid, allInfo);
    }

    /**
     * 1.1期销售历史统计（按月）,统计订单量,及销售额<br/>
     * 订单下成0或退款退成0的不参与统计
     *
     * @param user  当前登录用哀悼
     * @param start 开始月份
     * @param end   结束月份
     * @return 统计结果
     * @Autor K.F
     * @Date 2016.9.14
     */
    public List<StaticsBean> getSaleHistory(User user, Date start, Date end) {
        start = DateUtils.truncate(start, Calendar.MONTH);
        List list = checkAndInit(user);
        boolean allInfo = (boolean) list.get(1);
        int uid = (int) list.get(0);
        List<Map<String, Object>> datas = null;
        List<StaticsBean> dataList = null;
        if (allInfo) {
            if (SoftTypeConstant.checkMoudule(user, ModuleConstant.ModuleType.MARKETING_PLATFORM_MICROMALL)) {
                String startDate = TimeUtils.formatLocal(start);
                String endDate = TimeUtils.formatLocal(end);
                dataList = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_SALEHISTORY.url, user).excuteList(StaticsBean.class, startDate, endDate);
            }
            Shop shop = shopService.findShopUser(uid);
            datas = orderStaticsMapper.selectSaleHistroyByShop(shop.getId(), start, end);
        } else {
            datas = orderStaticsMapper.selectSaleHistroyByUser(uid, start, end);
        }
        return StaticsBean.generateSaleHistoryData(datas, start, end, dataList == null ? new ArrayList<StaticsBean>() : dataList);
    }

    /**
     * 在给定的周期内,按下单日期（维度天）,统计销售额
     *
     * @param user  登录用户
     * @param start 开始日期
     * @param end   结束日期
     */
    public List<StaticsBean> getStaticsByDay(User user, Date start, Date end, int currentPage, int flag) {
        start = DateUtils.truncate(start, Calendar.DATE);
        end = TimeUtils.formatDayEnd(end);
        List list = checkAndInit(user);
        boolean allInfo = (boolean) list.get(1);
        int uid = (int) list.get(0);
        List<Map<String, Object>> datas = null;
        if (allInfo) {
            String startDate = TimeUtils.formatLocal(start);
            String endDate = TimeUtils.formatLocal(end);
            if (flag == 2 && SoftTypeConstant.checkMoudule(user, ModuleConstant.ModuleType.MARKETING_PLATFORM_MICROMALL)) {
                Page<StaticsBean> page = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_GROUP_DIMENSION.url, user).
                        excutePage(StaticsBean.class, startDate, endDate, "date", 20, currentPage);
                page.setPageNum(currentPage);
                return page;
            }
            Shop shop = shopService.findShopUser(uid);
            PageHelper.startPage(currentPage, 20);
            datas = orderStaticsMapper.selectShopStaticsByDay(shop.getId(), start, end);
        } else {
            PageHelper.startPage(currentPage, 20);
            datas = orderStaticsMapper.selectUserStaticsByDay(uid, start, end);
        }
        return covertToStaticsList((Page<Map<String, Object>>) datas);
    }

    /**
     * 在给定的周期内,按员工统计销售额
     *
     * @param user  登录用户
     * @param start 开始日期
     * @param end   结束日期
     */
    public List<StaticsBean> getStaticsByStaff(User user, Date start, Date end, int currentPage) {
        start = DateUtils.truncate(start, Calendar.DATE);
        end = TimeUtils.formatDayEnd(end);
        List list = checkAndInit(user);
        boolean allInfo = (boolean) list.get(1);
        int uid = (int) list.get(0);
        Page<Map<String, Object>> datas = null;
        if (allInfo) {
            Shop shop = shopService.findShopUser(uid);
            PageHelper.startPage(currentPage, 20);
            datas = (Page<Map<String, Object>>) orderStaticsMapper.selectShopStaticsByStaff(shop.getId(), start, end);
        } else {
            PageHelper.startPage(currentPage, 20);
            datas = (Page<Map<String, Object>>) orderStaticsMapper.selectUserStaticsByStaff(uid, start, end);
            if (!datas.isEmpty()) {
                if (datas.get(0).get("user_id") == null) {
                    datas.remove(0);
                    datas.setTotal(0);
                }
            }
        }
        return covertToStaticsList((Page<Map<String, Object>>) datas);
    }

    /**
     * 在给定的周期内,按商品统计销售额
     *
     * @param user  登录用户
     * @param start 开始日期
     * @param end   结束日期
     */
    public List<StaticsBean> getStaticsByProduct(User user, Date start, Date end, int currentPage, int flag) {
        start = DateUtils.truncate(start, Calendar.DATE);
        end = TimeUtils.formatDayEnd(end);
        List list = checkAndInit(user);
        boolean allInfo = (boolean) list.get(1);
        int uid = (int) list.get(0);
        List<Map<String, Object>> datas = null;
        if (allInfo) {
            String startDate = TimeUtils.formatLocal(start);
            String endDate = TimeUtils.formatLocal(end);
            if (flag == 2 && SoftTypeConstant.checkMoudule(user, ModuleConstant.ModuleType.MARKETING_PLATFORM_MICROMALL)) {
                Page<StaticsBean> page = WeiYuRequst.getInstance(WeiYuRequst.WYURLS.STATICS_GROUP_DIMENSION.url, user).
                        excutePage(StaticsBean.class, startDate, endDate, "product", 20, currentPage);
                page.setPageNum(currentPage);
                return page;
            }
            Shop shop = shopService.findShopUser(uid);
            PageHelper.startPage(currentPage, 20);
            datas = orderStaticsMapper.selectShopStaticsByPrduct(shop.getId(), start, end);
        } else {
            PageHelper.startPage(currentPage, 20);
            datas = orderStaticsMapper.selectUserStaticsByPrduct(uid, start, end);
        }
        return covertToStaticsList((Page<Map<String, Object>>) datas);
    }

    private List<StaticsBean> covertToStaticsList(Page<Map<String, Object>> datas) {
        List<StaticsBean> collect = datas.stream().map(item -> {
            StaticsBean staticsBean = StaticsBean.emptyBean();
            staticsBean.setTotal(AmountUtil.yuan2Fen((Double) item.get("amount")));
            if (item.get("day") != null) {
                staticsBean.setDateStr(item.get("day").toString());
            }
            if (item.get("count") != null) {
                staticsBean.setOrderNum(StaticsBean.getCountByItem(item));
            }
            if (item.get("name") != null) {
                staticsBean.setProductName(item.get("name").toString());
            }
            if (item.get("user_id") != null) {
                int uid = (int) item.get("user_id");
                User user = userMapper.selectByPrimaryKey(uid);
                staticsBean.setStaff(user.getStaffname());
            }
            return staticsBean;
        }).collect(Collectors.toList());
        Page page = new Page();
        page.setTotal(datas.getTotal());
        page.setPageSize(datas.getPageSize());
        page.setPageNum(datas.getPageNum());
        page.addAll(collect);
        return page;
    }
}
