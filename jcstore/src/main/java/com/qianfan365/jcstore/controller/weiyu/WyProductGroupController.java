package com.qianfan365.jcstore.controller.weiyu;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.pojo.ProductGroup;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ProductGroupService;
import com.qianfan365.jcstore.service.ShopService;
import com.qianfan365.jcstore.service.SoftTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 商品分组相关
 */
@Controller
@RequestMapping("/weiyu/productgroup")
public class WyProductGroupController extends BaseController {

    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    private SoftTypeService softTypeService;
    @Autowired
    private ShopService shopService;

    /**
     * 查询分组列表
     *
     * @return
     */
    @RequestMapping(value = "find", method = RequestMethod.GET)
    @ResponseBody
    @ModulePassport(moduleids = {ModuleType.PRODUCT})
    public ResultData findProductGroup() {
        User user = getLoginUser(null);
        Shop shop = shopService.findShop(user);
        boolean contains =
                Arrays.asList(softTypeService.getById(user.getSoftId()).getModuleId().split(",")).contains(ModuleType.PRODUCT_GROUP);
        if (contains) {
            return ResultData.build().put("data", productGroupService.findAllByShopId(shop.getId()));
        } else {
            return ResultData.build().put("data", new ArrayList<ProductGroup>());
        }
    }

}
