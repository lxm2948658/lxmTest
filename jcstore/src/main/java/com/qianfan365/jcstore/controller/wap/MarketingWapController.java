package com.qianfan365.jcstore.controller.wap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.qianfan365.jcstore.common.constant.ModuleConstant;
import com.qianfan365.jcstore.service.*;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianfan365.jcstore.common.bean.ProductBean;
import com.qianfan365.jcstore.common.constant.MarketingConstant.MarketingType;
import com.qianfan365.jcstore.common.pojo.Marketing;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;

import java.util.Arrays;

@Controller
@RequestMapping("/mobile/marketing")
public class MarketingWapController extends BaseController {

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
  private SoftTypeService softTypeService;

  private static String shopPath = "mobile/marketing/shop?shopId=";
  private static String productPath = "mobile/marketing/product?pid=";

  /**
   * 正常查看店铺营销信息
   * 
   * @param id
   * @param session
   * @param model
   * @return
   */
  @RequestMapping(path = {"/shop"})
  public String marketingShop(Model model, Integer shopId, HttpServletRequest request) {
    //initPath(request);
    if (shopId != null) {
      // 查询数据库是否有此条营销记录,若有,则对应阅读量+1,并返回店铺信息
      Shop shop = shopService.findById(shopId);
      User user = userService.findUser(shop.getUserId());
      if (shop == null || user.getIsTrialAccount()) {
        model.addAttribute("shop", null);
        return "mobile/shop-detail";
      }
      boolean contains =
          Arrays.asList(softTypeService.getById(user.getSoftId()).getModuleId().split(",")).contains(ModuleConstant.ModuleType.PRODUCT_GROUP);
      marketingService.findAndUpdatePageView(shopId.toString(), MarketingType.SHOP, shop);
      marketingService.addShopMarketInfo(model, shop, contains);
      model.addAttribute("linkedURL", productPath);
      return "mobile/shop-detail";
    }
    model.addAttribute("shop", null);
    return "mobile/shop-detail";
  }

  /**
   * 商品营销页
   * 
   * @param pid
   * @return
   */
  @RequestMapping(value = "/product")
  public String marketingProduct(HttpSession session, Model model, Integer pid,
      HttpServletRequest request) {
    //initPath(request);
    if (pid != null) {
      Product product = productService.findById(pid);
      User user = userService.findUser(product.getUserId());
      if (product == null || user.getIsTrialAccount()) {
        model.addAttribute("product", null);
        return "mobile/shop-product-detail";
      }
      Shop shop = shopService.findById(product.getShopId());
      marketingService.findAndUpdatePageView(pid.toString(), MarketingType.PRODUCT, shop);
      model.addAttribute("product", new ProductBean(product));
      model.addAttribute("shop", shop);
      model.addAttribute("linkedURL", shopPath + shop.getId());
      return "mobile/shop-product-detail";
    }
    model.addAttribute("product", null);
    return "mobile/shop-product-detail";
  }

//  private void initPath(HttpServletRequest request) {
//    if (StringUtils.isEmpty(path)) {
//      path = marketingService.generatePath(request);
//      shopPath = path + "mobile/marketing/shop?shopId=";
//      productPath = path + "mobile/marketing/product?pid=";
//    }
//  }

  /**
   * 正常查看店铺营销信息
   * 
   * @param id
   * @param session
   * @param model
   * @return
   */
  @RequestMapping(path = {"/AD"})
  public String marketingAD(Model model, HttpServletRequest request, Integer id) {
    try {
      Marketing marketing = marketingService.findAdvMarketingInfo(id, model);
      Document document = Jsoup.connect(marketing.getParameter()).timeout(50000).get();
      Elements elements = document.select("div#img-content");
      elements.forEach(element -> {
        Elements imgs = element.getElementsByTag("img");
        imgs.forEach(element1 -> {
          if(element1.hasAttr("data-src")){
            String attr = element1.attr("data-src");
            if(StringUtils.isNotBlank(attr));
            element1.attr("src",attr);
            element1.removeAttr("data-src");
          }
        });
      });
      String html = elements.html();
      if(StringUtils.isEmpty(html)){
        throw new RuntimeException("文章内容有误");
      }
      model.addAttribute("html", html);
    } catch (Exception e) {
      // 内容已被删
//      model.addAttribute("html", "");
      return "mobile/preview-adv"; 
    }
    return "mobile/preview-adv";
  }
}
