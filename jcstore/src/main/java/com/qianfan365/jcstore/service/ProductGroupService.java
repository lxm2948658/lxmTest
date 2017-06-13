package com.qianfan365.jcstore.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qianfan365.jcstore.common.pojo.ProductGroup;
import com.qianfan365.jcstore.common.pojo.ProductGroupExample;
import com.qianfan365.jcstore.common.pojo.Shop;
import com.qianfan365.jcstore.dao.inter.ProductGroupMapper;

@Service("productGroupService")
public class ProductGroupService {

  @Autowired
  private ProductGroupMapper productGroupMapper;
  
  @Autowired
  private ProductService productService;

  /**
   * 添加分组
   * 
   * @param productGroup
   * @param uid
   * @param shopId
   * @return
   */
  @Transactional
  public int addProductGroup(ProductGroup productGroup, Integer uid, Integer shopId) {
    Date date = new Date();
    if(productGroup.getId()==null){
      productGroup.setShopId(shopId);
      productGroup.setUserId(uid);
      productGroup.setCreatetime(date);
      productGroup.setUpdatetime(date);
      return productGroupMapper.insertSelective(productGroup);
    }else{//修改
      productGroup.setUserId(uid);
      productGroup.setUpdatetime(date);
      return updateProductGroup(productGroup);
    }
   
  }

  /**
   * 删除分组
   * 
   * @param productGroupId
   * @param uid
   * @return
   */
  public int delProductGroup(Integer productGroupId, List<Integer> uid) {
    productService.updateByGroup(productGroupId);//如果有分组则改变商品分组为空
    ProductGroupExample productGroupExample = new ProductGroupExample();
    productGroupExample.createCriteria().andUserIdIn(uid).andIdEqualTo(productGroupId);
    return productGroupMapper.deleteByExample(productGroupExample);
  }

  /**
   * 修改分组
   * 
   * @param productGroup
   * @param uid
   * @return
   */
  @Transactional
  public int updateProductGroup(ProductGroup productGroup) {
    ProductGroupExample productGroupExample = new ProductGroupExample();
    productGroupExample.createCriteria().andIdEqualTo(productGroup.getId()).andUserIdEqualTo(productGroup.getUserId());
    return productGroupMapper.updateByExampleSelective(productGroup, productGroupExample);
  }

  /**
   * 根据用户查询所有分组
   * @param uid
   * @return
   */
  public List<ProductGroup> findProductGroup(List<Integer> uid) {
    ProductGroupExample productGroupExample = new ProductGroupExample();
    productGroupExample.createCriteria().andUserIdIn(uid);
    productGroupExample.setOrderByClause("updatetime desc");
    return productGroupMapper.selectByExample(productGroupExample);
  }
  
  /**
   * 根据用户id和id判断是否存在分组
   * @param id
   * @param uid
   * @return
   */
  public Boolean findByIdAndUid(Integer id,Integer uid){
    ProductGroupExample productGroupExample = new ProductGroupExample();
    productGroupExample.createCriteria().andUserIdEqualTo(uid).andIdEqualTo(id);
    if(productGroupMapper.selectByExample(productGroupExample).isEmpty()){
      return true;
    }
    return false;
    
  }
  
  public ProductGroup findById(Integer id){
    return productGroupMapper.selectByPrimaryKey(id);
  }
  
  /**
   * 根据店铺信息删除所有的店铺分组
   * @param shop
   */
  public void deleteAllProductGroup(Shop shop) {
    ProductGroupExample productGroupExample = new ProductGroupExample();
    productGroupExample.createCriteria().andShopIdEqualTo(shop.getId());
    this.productGroupMapper.deleteByExample(productGroupExample);
  }
  
  /**
   * 根据店铺查询所有的商品分组信息
   * @param shopId
   * @return
   */
  public List<ProductGroup> findAllByShopId(Integer shopId){
    ProductGroupExample example = new ProductGroupExample();
    example.setOrderByClause("updatetime desc");
    example.createCriteria().andShopIdEqualTo(shopId);
    return productGroupMapper.selectByExample(example);
  }
}
