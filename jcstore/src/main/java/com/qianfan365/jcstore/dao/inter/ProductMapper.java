package com.qianfan365.jcstore.dao.inter;

import com.qianfan365.jcstore.common.pojo.Product;
import com.qianfan365.jcstore.common.pojo.ProductExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ProductMapper {
    int countByExample(ProductExample example);

    int deleteByExample(ProductExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    List<Product> selectByExample(ProductExample example);

    Product selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Product record, @Param("example") ProductExample example);

    int updateByExample(@Param("record") Product record, @Param("example") ProductExample example);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
    
    @Update("update product a set a.group_id=NULL where a.group_id=#{groupId}")
    int updateGroup(@Param("groupId") Integer groupId);           
    @Update("update product a set a.cost_price=NULL where a.id=#{pid}")
    int updateCostPriceToNull(@Param("pid") Integer pid);           
    @Update("update product a set a.inventory=NULL where a.id=#{pid}")
    int updateInventoryToNull(@Param("pid") Integer pid);  
    @Update("update product a set a.bar_code=NULL where a.id=#{pid}")
    int updateBarCodeToNull(@Param("pid") Integer pid); 
    @Update("update product a set a.group_id=NULL where a.id=#{pid}")
    int updateGroupToNull(@Param("pid") Integer pid); 
    @Update("update product set inventory=inventory-#{inventory} where id=#{pid} and inventory>=#{inventory}")
    int updateInventory(@Param("pid") Integer pid,@Param("inventory") Integer inventory);
    @Update("update product set sales_volume=sales_volume+#{salesVolume} where id=#{pid} and shop_id=#{shopId}")
    int updateSalesVolume(@Param("pid") Integer pid, @Param("shopId") Integer shopId, @Param("salesVolume") Integer salesVolume);
    
    //没查成本价
    @Select("select id,name,image,code,bar_code,status,group_id,standard,sale_price,inventory,shop_id,user_id from product where id in (${pids}) and shop_id=#{shopId} order by field(id,${pids})")
    @ResultMap("BaseResultMap")
    List<Product> selectByIds(@Param("pids") String pids, @Param("shopId") Integer shopId);
    
    int insertBatch(List<Product> list);
    
    @Select("select bar_code from product WHERE shop_id = #{shopId} and status = 1")
    List<String> getAllBarCode(@Param("shopId")Integer shopId);

}
