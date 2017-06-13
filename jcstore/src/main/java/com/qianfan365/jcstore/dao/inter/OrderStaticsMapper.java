package com.qianfan365.jcstore.dao.inter;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by K.F on 16/9/20.
 * 订单统计类,用于统计销售情况
 * //TODO 未设置缓存
 */
public interface OrderStaticsMapper {

    /**
     * 店铺统计未收款
     */
    @Select("\n" +
            "select sum(amount) as amount,sum(count) as count,1 as order_status  from (\n" +
            "\n" +
            "SELECT COALESCE(sum(receice_amoun-refunding_fee-refunded_fee-amount_advanced),0) as amount,count(*) as count\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where  shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=1 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT COALESCE(sum(refunding_fee)*(-1),0) as amount,0 from order_info\n" +
            "  \n" +
            "where   shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=2  and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count\n" +
            ") tmp ")
    List<Map<String, Object>> selectNStaticsByShop(@Param("shopId") int shopId, @Param("start") Date start, @Param("end") Date end);
    /**
     * 店铺统计已经收款
     */
    @Select("\n" +
            "select sum(amount) as amount,sum(count) as count,2 as order_status  from (\n" +
            "\n" +
            "SELECT COALESCE(sum(amount_advanced),0) as amount,0 as count\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where  shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=1 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT COALESCE(sum(receice_amoun-refunded_fee),0) as amount,count(*) as count from order_info\n" +
            "  \n" +
            "where   shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=2  and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count\n" +
            ") tmp ")
    List<Map<String, Object>> selectYStaticsByShop(@Param("shopId") int shopId, @Param("start") Date start, @Param("end") Date end);

    /**
     *个人统计sql中未收款
     */
    @Select("\n" +
            "select sum(amount) as amount,sum(count) as count,1 as order_status  from (\n" +
            "\n" +
            "SELECT COALESCE(sum(receice_amoun-refunding_fee-refunded_fee-amount_advanced),0) as amount,count(*) as count\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where   user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=1 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT COALESCE(sum(refunding_fee)*(-1),0) as amount,0 from order_info\n" +
            "  \n" +
            "where   user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=2  and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count\n" +
            ") tmp ")
    List<Map<String, Object>> selectNStaticsByUser(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);
    /*
     *个人统计sql中己收款
     */
    @Select("\n" +
            "select sum(amount) as amount,sum(count) as count,2 as order_status  from (\n" +
            "\n" +
            "SELECT COALESCE(sum(receice_amoun - refunded_fee),0) as amount,count(*) as count\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where   user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=2 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT COALESCE(sum(amount_advanced),0) as amount,0 from order_info\n" +
            "  \n" +
            "where   user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=1  and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count\n" +
            ") tmp ")
    List<Map<String, Object>> selectYStaticsByUser(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);

    /**
     * 经营统计（图表折线）中对店铺己收款的统计
     */
    @Select("select sum(amount) as amount,sum(count) as count, DATE_FORMAT(order_date,'%Y-%m-%d') as day from (\n" +
            "\n" +
            "SELECT (receice_amoun-refunded_fee) as amount,1 as count,order_date\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where   shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=2 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT amount_advanced as amount,0 as count,order_date from order_info\n" +
            "  \n" +
            "where   shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=1 and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count, #{start} as day\n" +
            " ) tmp group by day order by day asc\n")
    List<Map<String, Object>> selectYGroupDaysStaticsByShop(@Param("shopId") int shopId, @Param("start") Date start, @Param("end") Date end);
    /*
    * 经营统计（图表折线）中对店铺未收款的统计
    */
    @Select("select sum(amount) as amount,sum(count) as count, DATE_FORMAT(order_date,'%Y-%m-%d') as day from (\n" +
            "\n" +
            "SELECT (receice_amoun-refunding_fee-refunded_fee-amount_advanced) as amount,1 as count,order_date\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where   shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=1 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT (refunding_fee*(-1)) as amount,0 as count,order_date from order_info\n" +
            "  \n" +
            "where    shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and order_status=2 and refunding_fee>0 and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count, #{start} as day\n" +
            " ) tmp group by day order by day asc\n")
    List<Map<String, Object>> selectNGroupDaysStaticsByShop(@Param("shopId") int shopId, @Param("start") Date start, @Param("end") Date end);

    /**
     * 经营统计（图表折线）中对个人己收款的统计
     */
    @Select("select sum(amount) as amount,sum(count) as count, DATE_FORMAT(order_date,'%Y-%m-%d') as day from (\n" +
            "\n" +
            "SELECT (receice_amoun-refunded_fee) as amount,1 as count,order_date\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where   user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=2 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT amount_advanced as amount,0 as count,order_date from order_info\n" +
            "  \n" +
            "where  user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=1 and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count, #{start} as day\n" +
            " ) tmp group by day order by day asc\n")
    List<Map<String, Object>> selectYGroupDaysStaticsByUser(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);
    /*
     * 经营统计（图表折线）中对个人未收款的统计
     */
    @Select("select sum(amount) as amount,sum(count) as count, DATE_FORMAT(order_date,'%Y-%m-%d') as day from (\n" +
            "\n" +
            "SELECT (receice_amoun-refunding_fee-refunded_fee-amount_advanced) as amount,1 as count,order_date\n" +
            "\n" +
            "from order_info\n" +
            "     \n" +
            "where   user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=1 and (receice_amoun-refunding_fee-refunded_fee)>0\n" +
            "\n" +
            "union all\n" +
            "\n" +
            "SELECT (refunding_fee*(-1)) as amount,0 as count,order_date from order_info\n" +
            "  \n" +
            "where    user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and order_status=2 and refunding_fee>0 and (receice_amoun-refunded_fee-refunding_fee)>0\n" +
            "\n" +
            "union all\n" +
            "SELECT 0 as amount, 0 as count, #{start} as day\n" +
            " ) tmp group by day order by day asc\n")
    List<Map<String, Object>> selectNGroupDaysStaticsByUser(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);

    /**
     * 个人的月销售历史(过滤掉作废的订单)
     */
    @Select("SELECT count(*) count, \n" +
            "\n" +
            "COALESCE(sum(receice_amoun-refunding_fee-refunded_fee),0) as amount,\n" +
            " \n" +
            "DATE_FORMAT(order_date,'%Y-%m') as day from order_info\n" +
            "\n" +
            "where order_status in (1,2) and  user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and (receice_amoun-refunding_fee-refunded_fee) >0\n" +
            "      \n" +
            "group by day  order by order_date desc ")
    List<Map<String, Object>> selectSaleHistroyByUser(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);

    /**
     * 店铺的月销售历史(过滤掉作废的订单)
     */
    @Select("SELECT count(*) count, \n" +
            "\n" +
            "COALESCE(sum(receice_amoun-refunding_fee-refunded_fee),0) as amount,\n" +
            " \n" +
            "DATE_FORMAT(order_date,'%Y-%m') as day from order_info\n" +
            "\n" +
            "where  order_status in (1,2) and  shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and (receice_amoun-refunding_fee-refunded_fee) >0 \n" +
            "      \n" +
            "group by day  order by order_date desc ")
    List<Map<String, Object>> selectSaleHistroyByShop(@Param("shopId") int shopId, @Param("start") Date start, @Param("end") Date end);


    /**
     * 按下单日期统计的店铺销售金额
     */
    @Select("SELECT \n" +
            "  \n" +
            "COALESCE(sum(receice_amoun-refunding_fee-refunded_fee),0) as amount,\n" +
            "\n" +
            "DATE_FORMAT(order_date,'%Y-%m-%d') as day  from order_info\n" +
            "     \n" +
            "where order_status in (1,2) and shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and (receice_amoun-refunding_fee-refunded_fee)>0  \n" +
            "\n" +
            "group by day order by day desc ")
    List<Map<String, Object>> selectShopStaticsByDay(@Param("shopId") int shopId, @Param("start") Date start, @Param("end") Date end);
    /**
     * 按下单日期统计的个人销售金额
     */
    @Select("SELECT \n" +
            "  \n" +
            "COALESCE(sum(receice_amoun-refunding_fee-refunded_fee),0) as amount,\n" +
            "\n" +
            "DATE_FORMAT(order_date,'%Y-%m-%d') as day  from order_info\n" +
            "     \n" +
            "where  order_status in (1,2) and user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and (receice_amoun-refunding_fee-refunded_fee)>0 \n" +
            "\n" +
            "group by day order by day desc ")
    List<Map<String, Object>> selectUserStaticsByDay(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);


    /**
     * 按员工统计的店铺销售金额
     */
    @Select("SELECT \n" +
            "  \n" +
            "COALESCE(sum(receice_amoun-refunding_fee-refunded_fee),0) as amount,\n" +
            "\n" +
            "user_id,count(*) as count  from order_info\n" +
            "     \n" +
            "where  order_status in (1,2) and shop_id=#{shopId} and order_date >=#{start}  and order_date<= #{end} and (receice_amoun-refunding_fee-refunded_fee)>0  \n" +
            "\n" +
            "group by user_id order by amount desc,user_id asc ")
    List<Map<String, Object>> selectShopStaticsByStaff(@Param("shopId") int shopId,@Param("start") Date start, @Param("end") Date end);

    /**
     * 按员工统计的个人销售金额（因为员工只有一个,就不需要groupby查询了）
     */
    @Select("SELECT \n" +
            " \n" +
            "COALESCE(sum(receice_amoun-refunding_fee-refunded_fee),0) as amount,\n" +
            " \n" +
            "user_id,count(*) as count  from order_info\n" +
            " \n" +
            "where order_status in (1,2) and user_id=#{uid} and order_date >=#{start}  and order_date<= #{end} and (receice_amoun-refunding_fee-refunded_fee)>0 " )
    List<Map<String, Object>> selectUserStaticsByStaff(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);

    /**
     * 按商品统计的个人销售金额
     */
    @Select("SELECT oid.name,sum(oid.price*COALESCE(oi.discount,1.00)*(oid.number-oid.refund_num)) as amount,sum(oid.number-oid.refund_num) as count \n" +
            "\n" +
            "FROM order_info_detail oid inner join order_info oi on oid.order_info_id =oi.id\n" +
            "\n" +
            "where  oi.order_status != 3 and (oid.number-oid.refund_num)>0 and (oi.receice_amoun-oi.refunding_fee-oi.refunded_fee)>0 and oi.user_id=#{uid} and oi.order_date >=#{start}  and oi.order_date<= #{end} and  oid.price>0 " +
            "group by  oid.product_id order by count desc,oid.id asc  ")
    List<Map<String, Object>> selectUserStaticsByPrduct(@Param("uid") int uid, @Param("start") Date start, @Param("end") Date end);

    /**
     * 按商品统计的店铺销售金额
     */
    @Select("SELECT oid.name,sum(oid.price*COALESCE(oi.discount,1.00)*(oid.number-oid.refund_num)) as amount,sum(oid.number-oid.refund_num) as count \n" +
            "\n" +
            "FROM order_info_detail oid inner join order_info oi on oid.order_info_id =oi.id\n" +
            "\n" +
            "where oi.order_status != 3 and(oid.number-oid.refund_num)>0 and (oi.receice_amoun-oi.refunding_fee-oi.refunded_fee)>0 and  oi.shop_id=#{shopId} and oi.order_date >=#{start}  and oi.order_date<= #{end} and  oid.price>0 " +
            "group by  oid.product_id order by count  desc ,oid.id asc  ")
    List<Map<String, Object>> selectShopStaticsByPrduct(@Param("shopId") int shopId, @Param("start") Date start, @Param("end") Date end);

}
