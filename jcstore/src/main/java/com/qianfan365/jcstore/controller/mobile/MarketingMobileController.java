package com.qianfan365.jcstore.controller.mobile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.json.JSON;
import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.ADMarketingBean;
import com.qianfan365.jcstore.common.bean.ProductBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.MarketingConstant.MarketingType;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleData;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.constant.SoftTypeConstant;
import com.qianfan365.jcstore.common.pojo.Marketing;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.MarketingService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ProductGroupService;
import com.qianfan365.jcstore.service.ProductService;
import com.qianfan365.jcstore.service.ShopService;
import com.qianfan365.jcstore.service.SoftTypeService;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping("/mobile/marketing")
public class MarketingMobileController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private MarketingService marketingService;
    @Autowired
    private PermissionInfoService permissionService;
    @Autowired
    private SoftTypeService softTypeService;

    @Value("${common.domain-url}/")
    String path;
    private static String shopPath = "mobile/marketing/previewShop?shopId=";
    private static String productPath = "mobile/marketing/previewProduct?pid=";
    private static String adPath = "mobile/marketing/AD?id=";
    private static String defaultLogPath = "static/mobile/images/share_default_logo.png";

    /**
     * 新增广告营销
     *
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(value = "share", method = RequestMethod.POST)
    @ModulePassport(moduleids = {ModuleType.MARKETING, ModuleType.ADVERTIS_MARKETING})
    @PermissionPassport(permissionids = {PermissionType.ADVERTIS_MARKETING})
    public ResultData basicShare(HttpSession session, HttpServletRequest request,
                                 ADMarketingBean adMarketingBean, String url) throws IOException {
        ResultData data = ResultData.build();
        final String[] shareImgUrl = {""};
        // 参数校验,url必须以http或者https开头
        if (StringUtils.isNotEmpty(url) && marketingService.check(url, adMarketingBean)) {
            // 初始化数据
            Marketing marketing = new Marketing();
            // 场景营销
            marketing.setType(MarketingType.ADVERTISEMENT);
            // 获取店铺信息用以添加至数据表
            User user = getLoginUser(session);
            Shop shop = shopService.findShop(user);
            marketing.setParameter(url);
            // 为图片添加营销店铺地址
            adMarketingBean.setPath(path + "mobile/marketing/shop?shopId=" + shop.getId());
            // 将广告信息转为json存储
            marketing.setAdvInfo(JSON.json(adMarketingBean));
            // 校验url是否可用读取,并获取头图
            try {
//        String text = Jsoup.connect(url).timeout(50000).ignoreContentType(true).execute().parse().select("div#img-content").text();
                Document document = Jsoup.connect(url).timeout(50000).get();
                Elements elements = document.select("div#img-content");
                elements.forEach(element -> {
                    Elements imgs = element.getElementsByTag("img");
                    imgs.forEach(element1 -> {
                        if (element1.hasAttr("data-src") && StringUtils.isBlank(shareImgUrl[0])) {
                            shareImgUrl[0] = element1.attr("data-src");
                        }
                    });
                });
                if (StringUtils.isEmpty(elements.text())) return data.setStatus(Status.LINK_FAILURE);
            } catch (IOException e) {
                // 返回链接已失效
                return data.setStatus(Status.LINK_TIMEOUT);
            }
            if (StringUtils.isBlank(shareImgUrl[0])) {
                if (StringUtils.isNotBlank(adMarketingBean.getHeadAdvImg())) {
                    shareImgUrl[0] = adMarketingBean.getHeadAdvImg();
                } else {
                    shareImgUrl[0] = path + defaultLogPath;
                }
            }
            data.put("shareImgUrl", shareImgUrl[0]);
            // 插入数据
            Integer id = marketingService.insert(marketing, shop);
            if (id < 0) {
                id = -id;
                return data.setStatus(Status.ARTICLE_PUBLISHED).put("linkedURL", path + adPath + id)
                        .put("title", marketing.getName());
            }
            return data.put("linkedURL", path + adPath + id)
                    .put("title", marketing.getName());
        }
        return data.parameterError();
    }

    /**
     * 预览店铺营销信息
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(path = {"/previewShop"})
    @ModulePassport(moduleids = {ModuleType.MARKETING, ModuleType.PRODUCT}, setuser = true)
    public String previewShop(HttpSession session, Model model) {
        User user = this.getLoginUser(session);
        Shop shop = shopService.findShop(user);
        if (user.getIsTrialAccount()) {
            if (StringUtils.isEmpty(shop.getAddress())) shop.setAddress("此处展示您店铺的位置");
            if (StringUtils.isEmpty(shop.getIntroduction()))
                shop.setIntroduction("当前为体验账户内容，此处可以展示您店铺的基本信息");
        }
        boolean contains =
                Arrays.asList(softTypeService.getById(user.getSoftId()).getModuleId().split(",")).contains(ModuleType.PRODUCT_GROUP);
        marketingService.addShopMarketInfo(model, shop, contains);
        model.addAttribute("linkedURL", productPath);
        return "mobile/shop-detail";
    }

    /**
     * 预览店铺营销信息
     *
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(path = {"/previewProduct"})
    @ModulePassport(moduleids = {ModuleType.MARKETING, ModuleType.PRODUCT})
    public String previewProduct(Integer pid, HttpSession session, Model model) {
        User user = this.getLoginUser(session);
        Shop shop = shopService.findShop(user);
        model.addAttribute("shop", shop);
        model.addAttribute("product", new ProductBean(productService.findById(pid)));
        model.addAttribute("linkedURL", shopPath + shop.getId());
        return "mobile/shop-product-detail";
    }

    /**
     * 预览广告营销
     *
     * @param url
     * @param model
     * @return
     */
    @RequestMapping(value = "/previewAD")
    @ModulePassport(moduleids = {ModuleType.MARKETING, ModuleType.ADVERTIS_MARKETING})
    @PermissionPassport(permissionids = {PermissionType.ADVERTIS_MARKETING})
    public String previewAD(String url, Model model, ADMarketingBean adMarketingBean,
                            HttpSession session) {
        String html = null;
        try {
            User user = this.getLoginUser(session);
            Shop shop = shopService.findShop(user);
            model.addAttribute("adv_info", adMarketingBean);
            adMarketingBean.setPath(path + shopPath + shop.getId() + "&cookie=true");
            // 抓取内容部分
            Document document = Jsoup.connect(url).timeout(50000).get();
            Elements elements = document.select("div#img-content");
            elements.forEach(element -> {
                Elements imgs = element.getElementsByTag("img");
                imgs.forEach(element1 -> {
                    if (element1.hasAttr("data-src")) {
                        String attr = element1.attr("data-src");
                        if (StringUtils.isNotBlank(attr)) ;
                        element1.attr("src", attr);
                        element1.removeAttr("data-src");
                    }
                });
            });
//      html = elements.html().replaceAll("<img.*?>", "");
            html = elements.html();
            model.addAttribute("html", html);
        } catch (Exception e) {
        }
        return "mobile/preview-adv";
    }


    /**
     * 一键营销列表
     *
     * @param session
     * @return
     */
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.MARKETING}, setuser = true)
    public ResultData list(HttpSession session) {
        User user = getLoginUser(session);
        List<String> modules = SoftTypeConstant.softMap().get(user.getSoftId());
        List<String> marketing = new ArrayList<String>();
        Map<Integer, Boolean> permission =
                permissionService.findMapByUidPids(
                        Arrays.asList(PermissionType.SCENE_MARKETING, PermissionType.ADVERTIS_MARKETING),
                        user.getId(), user.getBelongs());
        if (modules.contains(ModuleType.PRODUCT)) {
            marketing.add("店铺预览");
        }
        if (modules.contains(ModuleType.SCENE_MARKETING)
                && permission.get(PermissionType.SCENE_MARKETING)) {
            marketing.add(ModuleData.getModulesMap().get(ModuleType.SCENE_MARKETING).getName());
        }
        if (modules.contains(ModuleType.ADVERTIS_MARKETING)
                && permission.get(PermissionType.ADVERTIS_MARKETING)) {
            marketing.add(ModuleData.getModulesMap().get(ModuleType.ADVERTIS_MARKETING).getName());
        }
        if (marketing.size() == 0) {
            return ResultData.build().setStatus(Status.MODULE_CHANGED);
        }
        return ResultData.build().put("marketing", marketing);
    }

    /**
     * 查看历史记录
     *
     * @param session
     * @param currentPage
     * @param limit
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    @ModulePassport(moduleids = {ModuleType.MARKETING, ModuleType.ADVERTIS_MARKETING})
    @PermissionPassport(permissionids = {PermissionType.ADVERTIS_MARKETING})
    public ResultData marketingHistory(HttpSession session,
                                       @RequestParam(defaultValue = "1") Integer currentPage,
                                       @RequestParam(defaultValue = "20") Integer limit) {
        User user = getLoginUser(session);
        Page<Marketing> findMarketingHistory =
                marketingService.findMarketingHistory(user, currentPage, limit);
        return ResultData.build().parsePageBean(findMarketingHistory);
    }

    /**
     * 移除历史详情
     *
     * @param session
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteHistory", method = RequestMethod.POST)
    @ModulePassport(moduleids = {ModuleType.MARKETING, ModuleType.ADVERTIS_MARKETING})
    @PermissionPassport(permissionids = {PermissionType.ADVERTIS_MARKETING})
    public ResultData deleteHistory(HttpSession session, Integer id) {
        if (id != null) {
            User user = getLoginUser(session);
            Shop shop = shopService.findShop(user);
            marketingService.deleteHistory(shop.getId(), id);
            return ResultData.build().success();
        }
        return ResultData.build().parameterError();
    }

    /**
     * 分享店铺或商品
     *
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "basicShare", method = RequestMethod.POST)
    public ResultData share(HttpSession session, Marketing marketing) {
        if (marketing != null
                && marketing.getType() != null
                && (marketing.getType() == 1 || (marketing.getType() == 2 && marketing.getParameter() != null))) {
            // 若为商品分享,校验是否是本店铺商品
            User user = this.getLoginUser(session);
            Shop shop = shopService.findShop(user);
            if (marketing.getType() == 2) {
                Product product = productService.findById(Integer.valueOf(marketing.getParameter()));
                if (product == null || !product.getShopId().equals(shop.getId()))
                    return ResultData.build().parameterError();
            }
            marketingService.insert(marketing, shop);
            return ResultData.build().success();
        }
        return ResultData.build().parameterError();
    }
    
	/**
	 * 获取营销活动列表
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/activityList", method = RequestMethod.GET)
	@ModulePassport(moduleids = {ModuleType.ACTIVITY_MARKETING})
    @PermissionPassport(permissionids = {PermissionType.ACTIVITY_MARKETING})
	public ResultData activityList(HttpSession session, HttpServletRequest request) {
		User user = this.getLoginUser(session);
		Shop shop = shopService.findShop(user);
		return null;
	}

}
