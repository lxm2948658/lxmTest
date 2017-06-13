package com.qianfan365.jcstore.test.service;

import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.util.CommonUtils;
import com.qianfan365.jcstore.main.StartUp;
import com.qianfan365.jcstore.service.ShopService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.stream.IntStream;

/**
 * Created by qianfanyanfa on 16/8/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(StartUp.class)
public class ShopServiceTest {

    @Autowired
    ShopService shopService;

    @Test
    public void testAddShop() {
        int id = 100002;
        IntStream.range(0, 10).forEach(index -> {
            Shop shop = shopService.addShop(id + index);
            Assert.assertTrue(shop.getId() > 0);
            System.out.println(shop.getName());
        });
    }

    @Test
    public void testUpdateShop() {
        String tel = CommonUtils.generateNum(11).orElse("18612352157");
        Shop shop = shopService.addShop(Integer.parseInt(CommonUtils.generateNum(7).get()));
        Shop shopUpdate = new Shop();
        shopUpdate.setId(shop.getId());
        shopUpdate.setTel(tel);
        boolean b = shopService.updateShop(shopUpdate);
        Assert.assertTrue(b);

        Shop shopUser = shopService.findShopUser(shop.getUserId());
        Assert.assertTrue(shopUser.getTel().equals(tel));

        shopUser = shopService.findById(shop.getId());
        Assert.assertTrue(shopUser.getTel().equals(tel));
    }


}
