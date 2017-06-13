package com.qianfan365.jcstore.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.qianfan365.jcstore.common.pojo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.ADMarketingBean;
import com.qianfan365.jcstore.common.bean.MarketingBean;
import com.qianfan365.jcstore.common.constant.MarketingConstant.MarketingType;
import com.qianfan365.jcstore.common.pojo.MarketingExample.Criteria;
import com.qianfan365.jcstore.dao.inter.MarketingMapper;

@Service
public class MarketingService {

    @Autowired
    private MarketingMapper marketingMapper;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;

    /**
     * 新增营销
     *
     * @param marketing
     * @param shop
     * @return
     */
    public Integer insert(Marketing marketing, Shop shop) {
        // 设置基本数据
        marketing.setUpdatetime(new Date());
        marketing.setCreatetime(marketing.getUpdatetime());
        marketing.setPageView(0);
        marketing.setShopName(shop.getName());
        marketing.setShopId(shop.getId());
        // 店铺营销
        if (marketing.getType() == MarketingType.SHOP) {
            marketing.setParameter(shop.getId().toString());
            // 店铺营销,传递的参数为店铺ID
            marketing.setName(shop.getName());
            // 商品营销
        } else if (marketing.getType() == MarketingType.PRODUCT) {
            Product product = productService.findById(Integer.valueOf(marketing.getParameter()));
            marketing.setName(product.getName());
            // 广告营销
        } else if (marketing.getType() == MarketingType.ADVERTISEMENT) {
            try {
                Elements select =
                    Jsoup.connect(marketing.getParameter()).timeout(50000).get().select("h2#activity-name");
                marketing.setName(select.text());
            } catch (IOException e) {}
        }
        // 除广告营销外,需要校验是否重复数据
        if (marketing.getType() != MarketingType.ADVERTISEMENT) {
            List<Marketing> exist = this.isExist(marketing.getType(), marketing.getParameter());
            if (!exist.isEmpty()) {
                return exist.get(0).getId();
            }
        } else {
            // 防止多次点击
            MarketingExample example = new MarketingExample();
            example.createCriteria().andTypeEqualTo(MarketingType.ADVERTISEMENT).andAdvInfoEqualTo(marketing.getAdvInfo()).andParameterEqualTo(marketing.getParameter()).andShopIdEqualTo(marketing.getShopId());
            List<Marketing> list = marketingMapper.selectByExample(example);
            if (!list.isEmpty()) {//负的到controller里做错误码的判断
                return -list.get(0).getId();
            }
        }
        marketingMapper.insert(marketing);
        return marketing.getId();
    }

    /**
     * 校验对应推广是否已经存在
     *
     * @param type
     * @param parameter
     * @return
     */
    private List<Marketing> isExist(Integer type, String parameter) {
        MarketingExample example = new MarketingExample();
        example.createCriteria().andTypeEqualTo(type).andParameterEqualTo(parameter);
        return marketingMapper.selectByExample(example);
    }

    /**
     * 增加一条阅读记录,返回营销信息
     *
     * @param shop
     * @param id
     * @return
     */
    public void findAndUpdatePageView(String parameter, Integer type, Shop shop) {
        // 查找是否有对应的营销项
        MarketingExample example = new MarketingExample();
        example.createCriteria().andParameterEqualTo(parameter).andTypeEqualTo(type);
        List<Marketing> list = marketingMapper.selectByExample(example);
        Marketing marketing = null;
        if (list.isEmpty()) {
            // 没有对应的营销项,为通过连接跳转至此,添加此条推广
            marketing = new Marketing();
            marketing.setParameter(parameter);
            marketing.setType(type);
            insert(marketing, shop);
        } else {
            // 有对应营销项,点击量+1
            marketing = list.get(0);
        }
        marketingMapper.updatePageView(marketing.getId());
    }

    /**
     * 添加营销信息
     *  @param model
     * @param shop
     * @param contains
     */
    public void addShopMarketInfo(Model model, Shop shop, boolean contains) {
        model.addAttribute("shop", shop);
        model.addAttribute("products", productService.findProductForWap(shop.getId(), null, 1, 10, 0));
        if(contains)
            model.addAttribute("productGroup", productGroupService.findAllByShopId(shop.getId()));
        else
            model.addAttribute("productGroup",new ArrayList<ProductGroup>());
    }

    /**
     * 生成路径
     *
     * @param request
     */
    public String generatePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + contextPath + "/";
    }

    /**
     * 添加广告信息
     *
     * @param id
     * @param model
     * @return
     * @throws ParseException
     */
    public Marketing findAdvMarketingInfo(Integer id, Model model) throws ParseException {
        Marketing marketing = marketingMapper.selectByPrimaryKey(id);
        if (marketing != null) {
            ADMarketingBean parse = JSON.parse(marketing.getAdvInfo(), ADMarketingBean.class);
            model.addAttribute("adv_info", parse);
            // 增加点击量
            marketingMapper.updatePageView(id);
            return marketing;
        }
        return null;
    }

    public boolean check(String url, ADMarketingBean adMarketingBean) {
        // 校验链接的开头
        if ((url.startsWith("http://") || url.startsWith("https://")) && (
            StringUtils.isEmpty(adMarketingBean.getBottomAdvUrl()) || (
                adMarketingBean.getBottomAdvUrl().startsWith("http://")
                    || adMarketingBean.getBottomAdvUrl().startsWith("https://"))) && (
            StringUtils.isEmpty(adMarketingBean.getTopAdvUrl()) || (
                adMarketingBean.getTopAdvUrl().startsWith("http://")
                    || adMarketingBean.getTopAdvUrl().startsWith("https://"))) && (
            StringUtils.isEmpty(adMarketingBean.getHeadAdvUrl()) || (
                adMarketingBean.getHeadAdvUrl().startsWith("http://")
                    || adMarketingBean.getHeadAdvUrl().startsWith("https://"))) && (
            StringUtils.isEmpty(adMarketingBean.getAddress())
                || adMarketingBean.getAddress().matches("^[\u4E00-\u9FA5A-Za-z0-9,\\.~!@#$%^&\\*()_\\+\\-=|\\\\:;/?\\[\\]{}]{1,50}$"))
            && (StringUtils.isEmpty(adMarketingBean.getContactway())
            || adMarketingBean.getContactway().matches("^[0-9\\-]{1,20}$"))) {
            return true;
        }
        return false;
    }

    public Page<Marketing> findMarketingHistory(User user, Integer currentPage, Integer limit) {
        Integer shopId = shopService.findShop(user).getId();
        MarketingExample marketingExample = new MarketingExample();
        marketingExample.createCriteria().andShopIdEqualTo(shopId).andTypeEqualTo(MarketingType.ADVERTISEMENT);
        // 按照生成时间倒序排列
        marketingExample.setOrderByClause(" createtime desc");
        PageHelper.startPage(currentPage, limit);
        return (Page<Marketing>) marketingMapper.selectByExample(marketingExample);
    }

    /**
     * 列表查询
     *
     * @param currentPage
     * @param limit
     * @param type
     * @param orderType
     * @return
     */
    public Page<MarketingBean> list(Integer currentPage, Integer limit, Integer type, Integer orderType) {
        MarketingExample example = new MarketingExample();
        Criteria criteria = example.createCriteria();
        if (type != null && type != 0) {
            criteria.andTypeEqualTo(type);
        }
        if (orderType == null || orderType.equals(0)) {
            example.setOrderByClause(" createtime desc");
        } else if (orderType.equals(1)) {
            example.setOrderByClause(" page_view desc");
        } else if (orderType.equals(2)) {
            example.setOrderByClause(" page_view asc");
        }
        PageHelper.startPage(currentPage, limit);
        return convertToBean((Page<Marketing>) marketingMapper.selectByExample(example), currentPage, limit);
    }

    private Page<MarketingBean> convertToBean(Page<Marketing> page, Integer currentPage, Integer limit) {
        int i = 0;
        Page<MarketingBean> newPage = new Page<MarketingBean>();
        newPage.setPageNum(page.getPageNum());
        newPage.setPageSize(page.getPageSize());
        newPage.setTotal(page.getTotal());
        for (Marketing marketing : page) {
            MarketingBean marketingBean = new MarketingBean();
            marketingBean.setSerialNum((currentPage - 1) * limit + 1 + i);
            i++;
            try {
                BeanUtils.copyProperties(marketingBean, marketing);
            } catch (Exception e) {}
            newPage.add(marketingBean);
        }
        return newPage;
    }

    public void deleteHistory(Integer shopId, Integer id) {
        MarketingExample example = new MarketingExample();
        example.createCriteria().andShopIdEqualTo(shopId).andIdEqualTo(id);
        marketingMapper.deleteByExample(example);
    }

    /**
     * 更新店铺时更新营销表数据
     * @param shop
     */
    public void updateShopName(Shop shop) {
        MarketingExample example = new MarketingExample();
        Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shop.getId());
        Marketing marketing = new Marketing();
        marketing.setShopName(shop.getName());
        marketingMapper.updateByExampleSelective(marketing, example);
        marketing.setName(shop.getName());
        criteria.andTypeEqualTo(MarketingType.SHOP);
        marketingMapper.updateByExampleSelective(marketing, example);
    }

    /**
     * 更新商品时更新营销表数据
     * @param product
     */
    public void updateProductName(Product product) {
        MarketingExample example = new MarketingExample();
        example.createCriteria().andTypeEqualTo(MarketingType.PRODUCT).andParameterEqualTo(product.getId().toString());
        Marketing marketing = new Marketing();
        marketing.setName(product.getName());
        marketingMapper.updateByExampleSelective(marketing,example);
    }

    public Integer findShopViewByUser(Integer shopId) {
        MarketingExample marketingExample = new MarketingExample();
        marketingExample.createCriteria().andShopIdEqualTo(shopId).andTypeEqualTo(MarketingType.SHOP);
        List<Marketing> list = marketingMapper.selectByExample(marketingExample);
        return list.isEmpty()?0:list.get(0).getPageView();
    }
}
