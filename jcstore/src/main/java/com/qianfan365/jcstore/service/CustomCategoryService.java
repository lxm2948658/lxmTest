package com.qianfan365.jcstore.service;

import com.qianfan365.jcstore.common.pojo.CustomCategory;
import com.qianfan365.jcstore.common.pojo.CustomCategoryExample;
import com.qianfan365.jcstore.common.pojo.CustomProduct;
import com.qianfan365.jcstore.common.pojo.CustomProductExample;
import com.qianfan365.jcstore.dao.inter.CustomCategoryMapper;
import com.qianfan365.jcstore.dao.inter.CustomProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SZZ on 2017/3/7.
 * 定制商品类别相关
 */
@Service
public class CustomCategoryService {

    @Autowired
    private CustomCategoryMapper customCategoryMapper;
    @Autowired
    private CustomProductMapper customProductMapper;

    /**
     * 保存或更新类别
     *
     * @param customCategory ...
     */
    public void save(CustomCategory customCategory) {
        Date nowTime = new Date();
        customCategory.setUpdatetime(nowTime);
        if (customCategory.getId() == null) {
            // 新增逻辑
            customCategory.setCreatetime(nowTime);
            customCategoryMapper.insertSelective(customCategory);
        } else {
            // 更新逻辑
            CustomCategoryExample example = new CustomCategoryExample();
            CustomCategoryExample.Criteria criteria =
                example.createCriteria().andIdEqualTo(customCategory.getId());
            if (customCategory.getPid() != null)
                criteria.andPidEqualTo(customCategory.getPid());
            customCategoryMapper.updateByExampleSelective(customCategory, example);
        }
    }

    /**
     * 列表查询类目信息
     *
     * @param belongs 所属店长ID
     * @param pid     父类目ID
     * @return 标签列表
     */
    public List<CustomCategory> listAll(Integer belongs, Integer pid) {
        CustomCategoryExample example = new CustomCategoryExample();
        example.setOrderByClause(" createtime desc");
        example.createCriteria().andBelongsEqualTo(belongs).andPidEqualTo(pid);
        return customCategoryMapper.selectByExample(example);
    }

    /**
     * 批量删除
     *
     * @param belongs 所属店长ID
     * @param id      要删除的id数组
     */
    public void deleteByIds(Integer belongs, Integer id) {
        CustomCategory customCategory = customCategoryMapper.selectByPrimaryKey(id);
        if (customCategory == null)
            return;
        CustomProductExample customProductExample = new CustomProductExample();
        CustomProduct customProduct = new CustomProduct();
        customProduct.setStatus(false);
        if (customCategory.getPid() != 0) {
            // 二级类目删除其下的图片信息
            customProductExample.createCriteria().andCategoryIdEqualTo(id);
            customProductMapper.updateByExampleSelective(customProduct, customProductExample);
        } else {
            // 一级类目删除其下所有的二级类目
            CustomCategoryExample example = new CustomCategoryExample();
            example.createCriteria().andPidEqualTo(id).andBelongsEqualTo(belongs);
            // 移除所有的定制商品信息
            ArrayList<Integer> ids = new ArrayList<>();
            customCategoryMapper.selectByExample(example).forEach(customCategory1 -> ids.add(customCategory1.getId()));
            if (!ids.isEmpty()) {
                customProductExample.createCriteria().andCategoryIdIn(ids);
                customProductMapper.updateByExampleSelective(customProduct, customProductExample);
            }
            // 移除二级类目
            customCategoryMapper.deleteByExample(example);
        }
        // 移除类目
        customCategoryMapper.deleteByPrimaryKey(id);
    }

}
