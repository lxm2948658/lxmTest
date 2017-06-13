package com.qianfan365.jcstore.service;

import com.github.pagehelper.PageHelper;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.CustomCategory;
import com.qianfan365.jcstore.common.pojo.CustomCategoryExample;
import com.qianfan365.jcstore.common.pojo.CustomProduct;
import com.qianfan365.jcstore.common.pojo.CustomProductExample;
import com.qianfan365.jcstore.dao.inter.CustomCategoryMapper;
import com.qianfan365.jcstore.dao.inter.CustomProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by SZZ on 2017/3/7.
 * 定制商品相关
 */
@Service
public class CustomProductService {

    @Autowired
    private CustomProductMapper customProductMapper;
    @Autowired
    private CustomCategoryMapper customCategoryMapper;

    /**
     * 批量添加图片
     *  @param categoryId
     * @param images
     */
    @Transactional
    public ResultData addPic(Integer categoryId, List<String> images) {
        // 校验对应类别是否存在
        CustomCategoryExample customCategoryExample = new CustomCategoryExample();
        customCategoryExample.createCriteria().andIdEqualTo(categoryId).andPidNotEqualTo(0);
        List<CustomCategory> customCategories =
            customCategoryMapper.selectByExample(customCategoryExample);
        if(customCategories.isEmpty()) return ResultData.build().parameterError();
        // 开始插入图片
        CustomProductExample example = new CustomProductExample();
        example.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo(true);
        int i = customProductMapper.countByExample(example);
        if(i+images.size() >50){
            // 图片数量超出
            return ResultData.build().setStatus(ResultData.Status.IMAGE_NUM_BEYOND);
        }
        // 生成数据
        Date nowTime = new Date();
        ArrayList<CustomProduct> customProducts = new ArrayList<>(10);
        for (String image : images) {
            CustomProduct product = new CustomProduct();
            product.setUpdatetime(nowTime);
            product.setCreatetime(nowTime);
            product.setCategoryId(categoryId);
            product.setImage(image);
            customProducts.add(product);
        }
        // 批量插入
        customProductMapper.insertBatch(customProducts);
        // 首图及数量设置
        CustomCategory customCategory = new CustomCategory();
        customCategory.setId(categoryId);
        if(i == 0 && images.size() > 0) customCategory.setImage(images.get(0));
        i = customProductMapper.countByExample(example);
        if(i > 50) throw new RuntimeException("图片数量超出");
        customCategory.setSum(i);
        customCategoryMapper.updateByPrimaryKeySelective(customCategory);
        // 返回成功
        return ResultData.build().success();
    }

    /**
     * 返回类目下全部图片
     * @param categoryId
     * @return
     */
    public List<CustomProduct> listAllByCategoryId(Integer categoryId) {
        CustomProductExample example = new CustomProductExample();
        // 创建时间倒序,后插入的在前面
        example.setOrderByClause(" createtime desc,id desc");
        example.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo(true);
        return customProductMapper.selectByExample(example);
    }

    /**
     * 为商品设置描述
     * @param id
     * @param description
     */
    public void description(Integer id, String description) {
        CustomProduct product = new CustomProduct();
        product.setUpdatetime(new Date());
        product.setId(id);
        product.setDescription(description);
        customProductMapper.updateByPrimaryKeySelective(product);
    }

    /**
     * 批量删除
     * @param ids
     * @param categoryId
     */
    public void delete(Integer[] ids, Integer categoryId) {
        // 批量删除商品
        CustomProductExample example = new CustomProductExample();
        example.createCriteria().andIdIn(Arrays.asList(ids)).andCategoryIdEqualTo(categoryId);
        CustomProduct customProduct = new CustomProduct();
        customProduct.setStatus(false);
        customProductMapper.updateByExampleSelective(customProduct,example);
        // 更新二级目录信息
        example.clear();
        example.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo(true);
        CustomCategory customCategory = new CustomCategory();
        customCategory.setId(categoryId);
        int sum = customProductMapper.countByExample(example);
        // 更新图片信息
        customCategory.setSum(sum);
        String image = "";
        // 二级目录内图片数量不为0,使用第一张上传图片
        if(sum != 0){
            example.setOrderByClause(" createtime asc,id asc");
            PageHelper.startPage(1,1);
            image = customProductMapper.selectByExample(example).get(0).getImage();
        }
        customCategory.setImage(image);
        customCategoryMapper.updateByPrimaryKeySelective(customCategory);
    }
}
