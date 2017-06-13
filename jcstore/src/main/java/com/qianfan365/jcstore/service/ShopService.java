package com.qianfan365.jcstore.service;

import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.common.pojo.ShopExample;
import com.qianfan365.jcstore.common.pojo.ShopExample.Criteria;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.CommonUtils;
import com.qianfan365.jcstore.dao.inter.ShopMapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by qianfanyanfa on 16/8/11.
 */
@Service("shopService")
public class ShopService {

    private Log logger= LogFactory.getLog(ShopService.class);

    @Value("${shop.name.length}")
    Integer shopNameLength;

    @Autowired
    ShopMapper shopMapper;
    @Autowired
    private MarketingService marketingService;

    /**
     * 后台添加一个客户时,初始化一个店铺
     * @param userId
     * @return 生成的店铺信息
     */
    public Shop addShop(int userId){
        Shop shop=new Shop();
        //店铺名称为shop+6位随机数(且校验重复,不得不说,随着用户上来冲突的可能性太大)
        int i=0;
        for(;;){
            String shopName="shop"+ CommonUtils.generateNum(shopNameLength).get();
            boolean shopExist = findShopExist(shopName);
            if(!shopExist){
                logger.debug("冲突:"+i);
                shop.setName(shopName);
                break;
            }
            i++;
        }
        shop.setStatus(1);
        shop.setUserId(userId);
        Date now=new Date();
        shop.setCreatetime(now);
        shop.setUpdatetime(now);
        shopMapper.insertSelective(shop);
        return shop;
    }

    /**
     * 更新店铺信息
     * @param shop 要更新的数据（包含id及需要更新的数据）
     * @return true or false
     */
    public  boolean updateShop(Shop shop){
        shop.setUpdatetime(new Date());
        if(!StringUtils.isEmpty(shop.getName())) marketingService.updateShopName(shop);
        return  shopMapper.updateByPrimaryKeySelective(shop)>0?true:false;
    }


    /**
     * 根据名字查询店铺是否存在
     * @param shopName 店铺名称
     * @return true 存存, false 不存在
     */
    public boolean findShopExist(String shopName){
        ShopExample shopExample=new ShopExample();
        shopExample.createCriteria().andNameEqualTo(shopName);
        List<Shop> shops = shopMapper.selectByExample(shopExample);
        return  shops.isEmpty()?false:true;
    }

    /**
     * 不建议使用,稍后删除此方法
     * @ses ShopService.findShop(User)
     * @param uid
     * @return
     */
    @Deprecated
    public  Shop findShopUser(int uid){
        ShopExample shopExample=new ShopExample();
        shopExample.createCriteria().andUserIdEqualTo(uid);
        List<Shop> shops = shopMapper.selectByExample(shopExample);
        return shops.isEmpty()?null:shops.get(0);
    }

    public Shop findById(int id){
        return  shopMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取用户所属店铺信息
     * @param user
     * @return
     */
    public Shop findShop(User user) {
      ShopExample shopExample=new ShopExample();
      Criteria criteria = shopExample.createCriteria();
      if(user.getBelongs() == 0){
        criteria.andUserIdEqualTo(user.getId());
      }else{
        criteria.andUserIdEqualTo(user.getBelongs());
      }
      List<Shop> shops = shopMapper.selectByExample(shopExample);
      return shops.isEmpty() ? null:shops.get(0);
    }

    public Shop addTrialAccountShop(Integer userId) {
      Shop shop=new Shop();
      //店铺名称为shop+6位随机数(且校验重复,不得不说,随着用户上来冲突的可能性太大)
      shop.setName("易开单体验商家");
      shop.setStatus(1);
      shop.setUserId(userId);
      Date now=new Date();
      shop.setCreatetime(now);
      shop.setUpdatetime(now);
      shopMapper.insertSelective(shop);
      return shop;
    }
}
