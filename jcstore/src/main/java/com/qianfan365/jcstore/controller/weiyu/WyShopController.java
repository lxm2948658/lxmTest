package com.qianfan365.jcstore.controller.weiyu;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qianfanyanfa on 2017/5/16.
 */
@RestController
@RequestMapping("/weiyu/shop")
public class WyShopController extends BaseController {

    @Autowired
    ShopService shopService;

    @RequestMapping("/info")
    public ResultData getShopInfo() {
        User loginUser = getLoginUser(null);
        Shop shop = shopService.findShop(loginUser);
        shop.setCreatetime(null);
        shop.setUpdatetime(null);
        shop.setStatus(null);
        shop.setInventoryWarning(null);
        shop.setId(null);
        return ResultData.build().put("data", shop);
    }
}
