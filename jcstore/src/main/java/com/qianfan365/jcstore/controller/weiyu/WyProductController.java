package com.qianfan365.jcstore.controller.weiyu;

import com.qianfan365.jcstore.common.bean.ProductBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.controller.mobile.ProductController;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ProductGroupService;
import com.qianfan365.jcstore.service.ProductService;
import com.qianfan365.jcstore.service.ShopService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 商品相关
 *
 * @author guanliyu
 */
@Controller
@RequestMapping("/weiyu/product")
public class WyProductController extends BaseController {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductController productController;
  @Autowired
  private ShopService shopService;
  @Autowired
  private PermissionInfoService permissionInfoService;
  @Autowired
  private ProductGroupService productGroupService;

  /**
   * 根据ID查询单个商品明细
   *
   * @param session
   * @param productId
   * @return
   */
  @RequestMapping(value = "detail", method = RequestMethod.GET)
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.PRODUCT}, setuser = true)
  public ResultData findDetailProduct(HttpSession session, Integer id) {
    ResultData result = productController.findDetailProduct(null, id);
    result.remove("permissionInfo");
    result.put("data",result.get("product"));
    result.remove("product");
    return result;
  }

  /**
   * 根据商品名称查询商品列表(1.1期) 条件查询商品(全部整合至这一个接口)
   *
   * @param session
   * @param currentPage
   * @param limit
   * @param name
   * @param groupId
   * @param orderType
   * @return
   */
  @RequestMapping(value = "find", method = RequestMethod.GET)
  @ResponseBody
  @ModulePassport(moduleids = {ModuleType.PRODUCT})
  public ResultData findProduct(@RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit,
      @RequestParam(defaultValue = "0") Integer orderType, String name, Integer groupId,
      Long querytime) {
    ResultData result =
        productController
            .findProduct(null, currentPage, limit, orderType, name, groupId, querytime);
    result.remove("permissionInfo");
    return result;
  }


  /**
   * 根据关键字获取对应的商品
   *
   * @param barCode
   * @param session
   * @return
   */
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, value = "/find/keyword")
  public ResultData findByKeyword(String keyword) {
    String regex = "^[A-Za-z\\u4e00-\\u9fa5]{1,10}$";
    // 参数校验
    if (StringUtils.isEmpty(keyword) || !keyword.matches(regex)) {
      return ResultData.build().parameterError();
    }
    User user = this.getLoginUser(null);
    // 查询
    Product product =
        this.productService.findByKeyword(keyword, shopService.findShop(user).getId());
    if (product != null) {
      ProductBean bean = new ProductBean(product);
      if (product.getGroupId() != null) {
        bean.setGroupName(productGroupService.findById(product.getGroupId()).getName());
      }
      bean.setCostPrice(null);
      return ResultData.build().put("data", bean);
    } else {
      return ResultData.build().data404();
    }
  }

  /**
   * 刷新库存
   *
   * @param ids
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "refresh", method = RequestMethod.GET)
  public ResultData refresh(Integer[] ids) {
      ResultData refresh = productController.refresh(ids, null);
      refresh.put("data",refresh.get("aaData"));
      refresh.remove("aaData");
      return refresh;
  }

}
