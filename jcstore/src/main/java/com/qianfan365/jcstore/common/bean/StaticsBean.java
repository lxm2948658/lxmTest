package com.qianfan365.jcstore.common.bean;

import com.qianfan365.jcstore.common.util.AmountUtil;
import com.qianfan365.jcstore.common.util.TimeUtils;
import org.apache.commons.lang.time.DateUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by qianfanyanfa on 16/8/15.
 */
@SuppressWarnings("all")
public class StaticsBean {

    private Long orderNum;

    private BigInteger notReceiveAmout;

    private BigInteger receivedAmout;

    private BigInteger total;

    private String dateStr;

    private String staff;

    private String productName;

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public BigInteger getNotReceiveAmout() {
        return notReceiveAmout;
    }

    public void setNotReceiveAmout(BigInteger notReceiveAmout) {
        this.notReceiveAmout = this.notReceiveAmout.add(notReceiveAmout);
        this.total = this.total.add(notReceiveAmout);
    }

    public BigInteger getReceivedAmout() {
        return receivedAmout;
    }

    public void setReceivedAmout(BigInteger receivedAmout) {
        this.receivedAmout = this.receivedAmout.add(receivedAmout);
        this.total = this.total.add(receivedAmout);
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = this.total.add(total);
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public StaticsBean() {
        this.receivedAmout = new BigInteger("0");
        this.notReceiveAmout = new BigInteger("0");
        this.total = new BigInteger("0");
        this.orderNum = 0L;
        this.staff = "";
        productName = "";
    }

    public static StaticsBean emptyBean() {
        return new StaticsBean();
    }

    @SuppressWarnings("all")
    public static Map generateStaticsData(List<Map<String, Object>> dataNo, List<Map<String, Object>> dataYes, Date start,List<StaticsBean> wyDataMapYes ) {

        AtomicInteger accub = new AtomicInteger();

        Map<String, StaticsBean> dataMapNo = dataNo.stream().collect(Collectors.toMap(item -> {
            return item.get("day").toString();
        }, item -> {
            StaticsBean staticsBean = StaticsBean.emptyBean();
            staticsBean.setNotReceiveAmout(AmountUtil.yuan2Fen((Double) item.get("amount")));
            staticsBean.setDateStr(item.get("day").toString());
            staticsBean.setOrderNum(getCountByItem(item));
            return staticsBean;
        }));

        Map<String, StaticsBean> dataMapYes = dataYes.stream().collect(Collectors.toMap(item -> {
            return item.get("day").toString();
        }, item -> {
            StaticsBean staticsBean = StaticsBean.emptyBean();
            staticsBean.setReceivedAmout(AmountUtil.yuan2Fen((Double) item.get("amount")));
            staticsBean.setDateStr(item.get("day").toString());
            staticsBean.setOrderNum(getCountByItem(item));
            return staticsBean;
        }));
        //如果存在商城数据,则与dataMapYes进行合并,一期只有待收款
        if(wyDataMapYes!=null){
            wyDataMapYes.forEach(staticsBean -> {
                StaticsBean tmp = dataMapYes.get(staticsBean.getDateStr());
                if (tmp != null) {
                    tmp.setOrderNum(tmp.getOrderNum() + staticsBean.getOrderNum());
                    tmp.setReceivedAmout(staticsBean.getReceivedAmout());
                }else{
                    dataMapYes.put(staticsBean.getDateStr(),staticsBean);
                }
            });
        }
        List<Map<String, StaticsBean>> initData7Days = Stream.generate(new Supplier<Map<String, StaticsBean>>() {
            @Override
            public Map<String, StaticsBean> get() {
                Map<String, StaticsBean> map = new HashMap<String, StaticsBean>();
                Date date = DateUtils.addDays(start, accub.getAndIncrement() + 24);
                String dayStr = TimeUtils.formatAsString(date, "yyyy-MM-dd");
                StaticsBean staticsBean = StaticsBean.emptyBean();
                staticsBean.setDateStr(TimeUtils.formatAsString(date, "yyyy.M.d"));
                if (dataMapNo.get(dayStr) != null) {
                    staticsBean.setNotReceiveAmout(dataMapNo.get(dayStr).getNotReceiveAmout());
                    staticsBean.setOrderNum(staticsBean.getOrderNum() + dataMapNo.get(dayStr).getOrderNum());
                }
                if (dataMapYes.get(dayStr) != null) {
                    staticsBean.setReceivedAmout(dataMapYes.get(dayStr).getReceivedAmout());
                    staticsBean.setOrderNum(staticsBean.getOrderNum() + dataMapYes.get(dayStr).getOrderNum());
                }
                map.put(TimeUtils.formatAsString(date, "M月d"), staticsBean);
                return map;
            }
        }).limit(7).collect(Collectors.toList());
        AtomicInteger accub2 = new AtomicInteger();
        //30统计
        List<Map<String, StaticsBean>> initData30Days = Stream.generate(new Supplier<Map<String, StaticsBean>>() {
            @Override
            public Map<String, StaticsBean> get() {
                Map<String, StaticsBean> map = new HashMap<String, StaticsBean>();
                if (accub2.get() == 30) {
                    Date date = DateUtils.addDays(start, 30);

                    String endStr = TimeUtils.formatAsString(date, "yyyy-MM-dd");
                    StaticsBean staticsBean = StaticsBean.emptyBean();
                    if (dataMapNo.get(endStr) != null) {
                        staticsBean.setNotReceiveAmout(dataMapNo.get(endStr).getNotReceiveAmout());
                        staticsBean.setOrderNum(dataMapNo.get(endStr).getOrderNum() + staticsBean.getOrderNum());
                    }
                    if (dataMapYes.get(endStr) != null) {
                        staticsBean.setReceivedAmout(dataMapYes.get(endStr).getReceivedAmout());
                        staticsBean.setOrderNum(dataMapYes.get(endStr).getOrderNum() + staticsBean.getOrderNum());
                    }
                    staticsBean.setDateStr(TimeUtils.formatAsString(date, "yyyy.M.d"));
                    map.put(TimeUtils.formatAsString(date, "M月d"), staticsBean);
                    return map;
                }
                Date date = DateUtils.addDays(start, accub2.getAndAdd(5));
                String dayStr = TimeUtils.formatAsString(date, "M月d");
                StaticsBean staticsBean = StaticsBean.emptyBean();
                staticsBean.setDateStr(TimeUtils.formatAsString(date, "yyyy.M.d"));
                IntStream.range(0, 5).forEach(index -> {
                    final Date dateTpm = DateUtils.addDays(date, index);
                    String dayStrTpm = TimeUtils.formatAsString(dateTpm, "yyyy-MM-dd");
                    if (index == 4) {
                        staticsBean.setDateStr(staticsBean.getDateStr() + "~" + TimeUtils.formatAsString(dateTpm, "M.d"));
                    }
                    if (dataMapNo.get(dayStrTpm) != null) {
                        staticsBean.setNotReceiveAmout(dataMapNo.get(dayStrTpm).getNotReceiveAmout());
                        staticsBean.setOrderNum(staticsBean.getOrderNum() + dataMapNo.get(dayStrTpm).getOrderNum());
                    }
                    if (dataMapYes.get(dayStrTpm) != null) {
                        staticsBean.setReceivedAmout(dataMapYes.get(dayStrTpm).getReceivedAmout());
                        staticsBean.setOrderNum(staticsBean.getOrderNum() + dataMapYes.get(dayStrTpm).getOrderNum());
                    }
                });
                map.put(dayStr, staticsBean);
                return map;
            }
        }).limit(7).collect(Collectors.toList());

        Map map = new HashMap<>();
        map.put("7days", initData7Days);
        map.put("30days", initData30Days);
        return map;
    }

    /**
     * 月销售历史查询
     */
    public static List<StaticsBean> generateSaleHistoryData(List<Map<String, Object>> saleStatics, Date start, Date end, List<StaticsBean> shopDataList) {
        int monthSpace = TimeUtils.getMonthSpace(start, end);

        int accub = 0;
        List<String> months = IntStream.range(0, monthSpace + 1).mapToObj(val -> {
            return TimeUtils.formatAsString(DateUtils.addMonths(end, val * -1), "yyyy-MM");
        }).collect(Collectors.toList());

        Map<String, StaticsBean> shopDatas = shopDataList.stream().collect(Collectors.toMap(StaticsBean::getDateStr, Function.identity()));

        List<StaticsBean> beans = new ArrayList<>();
        for (int i = 0; i < saleStatics.size(); i++) {
            Map<String, Object> item = saleStatics.get(i);
            String day = item.get("day").toString();
            StaticsBean staticsBean;
            for (; ; ) {
                if (months.isEmpty()) {
                    break;
                }
                String s = months.get(0);
                months.remove(0);
                if (day.equals(s)) {
                    break;
                } else {
                    staticsBean = StaticsBean.emptyBean();
                    staticsBean.setDateStr(s);
                    beans.add(staticsBean);
                    continue;
                }
            }
            staticsBean = StaticsBean.emptyBean();
            staticsBean.setTotal(AmountUtil.yuan2Fen((Double) item.get("amount")));
            staticsBean.setDateStr(item.get("day").toString());
            staticsBean.setOrderNum(getCountByItem(item));
            beans.add(staticsBean);
        }
        for (; ; ) {
            if (months.isEmpty()) {
                break;
            }
            StaticsBean staticsBean = StaticsBean.emptyBean();
            staticsBean.setDateStr(months.get(0));
            beans.add(staticsBean);
            months.remove(0);
        }
        beans.forEach(staticsBean -> {
            if (shopDatas.containsKey(staticsBean.getDateStr())) {
                StaticsBean temp = shopDatas.get(staticsBean.getDateStr());
                staticsBean.setReceivedAmout(temp.getReceivedAmout());
                staticsBean.setOrderNum(staticsBean.getOrderNum() + temp.getOrderNum());
            }
        });
        return beans;
    }


    /**
     * 获得sql统计得出来的数量（因部分sql采用sum计数,导致count可能是BigDecimal类型）
     *
     * @param item
     * @return
     */
    public static Long getCountByItem(Map item) {
        Object count = item.get("count");
        if (count instanceof BigDecimal) {
            BigDecimal tmp = (BigDecimal) count;
            return tmp.longValue();
        } else {
            return (Long) count;
        }
    }


    @Override
    public String toString() {
        return "StaticsBean{" +
                "orderNum=" + orderNum +
                ", notReceiveAmout=" + notReceiveAmout +
                ", receivedAmout=" + receivedAmout +
                ", total=" + total +
                ", staff=" + staff +
                ", productName=" + productName +
                ", dateStr='" + dateStr + '\'' +
                '}';
    }
}
