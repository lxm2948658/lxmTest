package com.qianfan365.jcstore.controller.wap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.ProductBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.service.ProductService;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping("/wap/product")
public class ProductWapController {

  @Autowired
  private ProductService productService;
  @Autowired
  private UserService userService;

  /**
   * 
   * wap版 加载商品列表
   * 
   * @param shopId
   * @param currentPage
   * @param limit
   * @param groupId
   * @param orderType
   * @return
   */
  @RequestMapping(value = "find", method = RequestMethod.GET)
  @ResponseBody
  public ResultData findProduct(Integer shopId, Integer groupId,
      @RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit,
      @RequestParam(defaultValue = "0") Integer orderType) {
    Page<ProductBean> page =
        productService.findProductForWap(shopId, groupId, currentPage, limit, orderType);
    return ResultData.build().parsePageBean(page);
  }



}
