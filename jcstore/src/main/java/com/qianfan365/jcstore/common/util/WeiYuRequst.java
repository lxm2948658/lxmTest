package com.qianfan365.jcstore.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by qianfanyanfa on 2017/5/16.
 */
public class WeiYuRequst {

    private final Log log = LogFactory.getLog(WeiYuRequst.class);

    //一期不考虑重试机制
    private int tryTimes = 0;
    private String url;
    private User user;
    private HttpMethod method = HttpMethod.GET;
    private static RestTemplate restTemplate;

    public static int readTimout=0;
    public static int connectTimeout=0;

    public ResultData resultData = ResultData.build();

    private static String wyServerUrl;

    public static UserService userService;

    public static void setWyServerUrl(String url) {
        if (StringUtils.isBlank(wyServerUrl)) {
            wyServerUrl = url;
        }
    }

    public String getWyServerUrl() {
        return new String(wyServerUrl == null ? "" : wyServerUrl);
    }

    static {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(readTimout);
        requestFactory.setConnectTimeout(connectTimeout);
        restTemplate = new RestTemplate(requestFactory);
    }

    public WeiYuRequst(String url, User user) {
        this.url = url;
        if (userService == null) {
            this.user = user;
            log.warn("调试开发状态,未设置UserService");
        } else {
            this.user = userService.findBelongsByUser(user);
        }
    }

    public WeiYuRequst(String url, User user, HttpMethod method) {
        this(url, user);
        this.method = method;
    }

    public static WeiYuRequst getInstance(String url, User user) {
        return new WeiYuRequst(url, user);
    }

    public WeiYuRequst tryTimes(int tryNumer) {
        this.tryTimes = tryNumer;
        return this;
    }


    public WeiYuRequst setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public WeiYuRequst noTry() {
        this.tryTimes = 0;
        return this;
    }

    /**
     * 执行请求
     *
     * @return
     */
    public <T> T excute(String key, Class<T> responseType, Object... uriVariables) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("clientUsername", user.getUsername());
        HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity response = restTemplate.exchange(wyServerUrl + url, method, requestEntity, String.class, uriVariables);
        log.debug(wyServerUrl + url);
        response.getHeaders().forEach((k, v) -> {
            log.debug("key:" + k + ",value" + v);
        });
        String body = (String) response.getBody();
        if (StringUtils.isBlank(key)) {
            return JSON.parseObject(body, responseType);
        }
        JSONObject jsonObject = JSON.parseObject(body);

        Object o = jsonObject.getObject(key, responseType);
        return (T) o;
    }


    public <T> T excute(Class<T> responseType, Object... uriVariables) {
        return excute("data", responseType, uriVariables);
    }

    public <T> Page<T> excutePage(Class<T> responseType, Object... uriVariables) {


        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("clientUsername", user.getUsername());
        HttpEntity<?> requestEntity = new HttpEntity(requestHeaders);
        ResponseEntity response = restTemplate.exchange(wyServerUrl + url, method, requestEntity, String.class, uriVariables);
        log.debug(wyServerUrl + url);
        response.getHeaders().forEach((k, v) -> {
            log.debug("key:" + k + ",value" + v);
        });
        String body = (String) response.getBody();
        JSONObject jsonObject = JSON.parseObject(body);

        Page<T> page = new Page<>();
        page.setTotal(new Long(String.valueOf(jsonObject.get("iTotalRecords"))));
        JSONArray array = (JSONArray) jsonObject.get("aaData");
        page.getResult().addAll(JSON.parseArray(array.toJSONString(), responseType));
        page.setPageSize(new Integer(String.valueOf(jsonObject.get("iTotalDisplayRecords"))));
        if(StringUtils.isNotBlank(jsonObject.getString("querytime"))){
            resultData.put("querytime",jsonObject.getLong("querytime"));
        }
        return page;
    }

    public <T> List<T> excuteList(Class<T> responseType, Object... uriVariables) {
        String data = excute("data", String.class, uriVariables);
        return JSON.parseArray(data, responseType);
    }

    public enum WYURLS {


        STATICS_PERIOD("/index.php?g=Ykd&m=Statics&a=period&start={start}&end={end}", "指定周期内订单统计"),
        STATICS_SALEHISTORY("/index.php?g=Ykd&m=Statics&a=saleHistory&start={start}&end={end}", "指定周期内的,按月分组的订单统计数据"),
        STATICS_GROUP_DIMENSION("/index.php?g=Ykd&m=Statics&a=groupDimension&start={start}&end={end}&type={type}&limit={limit}&currentPage={currentPage}", "指定周期内的,按指定维度数据统计"),
        ORDER_DETAIL("/index.php?g=Ykd&m=Order&a=detail&id={id}", "订单详情"),
        ORDER_LIST("/index.php?g=Ykd&m=Order&a=lists&currentPage={currentPage}&limit={limit}&begin={begin}&end={end}&customer={customer}&phone={phone}&querytime={querytime}", "订单列表"),
        STATICS_30DAYS("/index.php?g=Ykd&m=Statics&a=thirtyDays", "30天的统计数据"),
        STATICS_SHOP_VIEW("/index.php?g=Ykd&m=Statics&a=shopview", "商城页面的访问量"),
        SHOP_URL("/index.php?g=Ykd&m=Shop&a=address", "获取web商城的首页地址"),
        PRODUCT_URL("/index.php?g=Ykd&m=Product&a=address&id={productId}", "获取具体商品的url");

        WYURLS(String url, String desc) {
            this.url = url;
            this.desc = desc;
        }

        public String url;
        public String desc;
    }
}
