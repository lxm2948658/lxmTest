package com.qianfan365.jcstore.common.constant;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.qianfan365.jcstore.common.bean.PermissionBean;
import com.qianfan365.jcstore.common.filter.PermissionInterceptor;
import com.qianfan365.jcstore.common.pojo.PermissionInfo;
//import com.qianfan365.jcstore.service.PermissionInfoService;

/**
 * 
 * @author liuhaoran
 *
 */
public class PermissionConstant {
  
//  public static PermissionInfoService permissionInfoService;
  public final static List<PermissionInfo> infos = PermissionInterceptor.permissionInfoService.findAllPermission();
  public final static List<PermissionBean> beans = infos.stream().map(PermissionBean::new).collect(Collectors.toList());
  public final static List<Integer> ids = infos.stream().map(pi -> pi.getId()).collect(Collectors.toList());
  public final static Map<Integer,Boolean> mapids = infos.stream().collect(Collectors.toMap(PermissionInfo::getId, i->false));
  
  /**
   * 权限类型
   */
  public interface PermissionType {
    /** 1-查看个人内容 */
    int VIEW_PERSON = 1;
    /** 2-查看全员内容 */
    int VIEW_ALL = 2;
    /** 3-商品发布/编辑 */
    int PRODUCE_EDIT = 3;
    /** 4-商品删除 */
    int PRODUCE_DEL = 4;
    /** 5-查看成本价 */
    int VIEW_CORPUS = 5;
    /** 6-开单 */
    int PLACE_ORDER = 6;
    /** 7-订单确认完成 */
    int FINISH_ORDER = 7;
    /** 8-客户新增/编辑 */
    int CUSTOMER_EDIT = 8;
    /** 9-客户删除 */
    int CUSTOMER_DEL = 9;
    /** 10-下单提醒 */
    int PLACE_ORDER_REMIND = 10;
    /** 11-订单完成提醒 */
    int ORDER_FINISH_REMIND = 11;
    /** 12-上月经营概况提醒 */
    int MONTHLY_STATISTICS_REMIND = 12;
    /** 13-服务内容变更提醒 */
    int SERVICE_CHANGE_REMIND = 13;
    /** 14-到期提醒 */
    int SERVICE_DUE_REMIND = 14;
    /** 15-退货提醒 */
    int REFUND_REMID = 15;
    /** 16-订单作废 */
    int ORDER_CANCEL = 16;
    /** 17-退货 */
    int REFUND = 17;
    /** 18-库存预警 */
    int INVENTORY_WARNING_REMIND = 18;
    /** 19-场景营销 */
    int SCENE_MARKETING = 19;
    /** 20-广告营销 */ 
    int ADVERTIS_MARKETING = 20;
    /** 21-定制开单 */
    int BESPOKE_PLACE_ORDER = 21;
    /** 22-定制编辑商品 */
    int BESPOKE_PRODUCE_EDIT = 22;
    /** 23-定制订单作废 */
    int BESPOKE_ORDER_CANCEL = 23;
    /** 24-定制订单确认完成 */
    int BESPOKE_FINISH_ORDER = 24;
    /** 25-互动营销 */
    int ACTIVITY_MARKETING = 25;
  }
  
  /**
   * 权限列表map(给移动端传数据用)
   */
  public final static Map<Integer, PermissionBean> permissionMap = new LinkedHashMap<Integer, PermissionBean>() {
    private static final long serialVersionUID = 1L;
    {
      for (PermissionInfo info : infos) {
        put(info.getId(), new PermissionBean(info));
      }
    }
  };
  
  /**
   * 权限所属类别map(给移动端传数据用)
   */
  public final static Map<String, List<List<PermissionBean>>> permissionTypeMap = new LinkedHashMap<String, List<List<PermissionBean>>>() {
    private static final long serialVersionUID = 1L;
    {
      for (PermissionInfo info : infos) {
        put(info.getPermissionType(), null);
      }
    }
  };
  
  public final static Map<String, Integer> typeIdMap = new LinkedHashMap<String, Integer>() {
    private static final long serialVersionUID = 1L;
    {
      int i = 1;
      Set<String> set = new HashSet<String>();
      for (PermissionInfo info : infos) {
        if(set.add(info.getPermissionType())){
          put(info.getPermissionType(), i);
          i++;
        }
      }
    }
  };
  
//  /**
//   * 权限所属类别bean(给移动端传数据用)
//   */
//  public final static List<PermissionTypeBean> permissionTypeBean = new ArrayList<PermissionTypeBean>() {
//    private static final long serialVersionUID = 1L;
//    {
//      for (String str : permissionTypeMap.keySet()) {
//        add(new PermissionTypeBean(str));
//      }
//    }
//  };
  
}
