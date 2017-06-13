package com.qianfan365.jcstore.common.constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.qianfan365.jcstore.common.pojo.Module;

public class ModuleConstant {

  public interface ModuleType {
    // 消息
    String MESSAGE = "1";
    // 经营概况
    String STATICS = "2";
    // 打印
    String PRINT = "3";
    // 订单管理
    String ORDER_INFO = "4";
    // 极简开单
    String MINIMAL_ORDER = "5";
    // 常规开单
    String GENERAL_ORDER = "6";
    // 订单打印
    String ORDER_PRINT = "7";
    // 再次购买
    String AGAIN_BUY = "8";
    // 订单退货
    String ORDER_REFUND = "9";
    // 订单作废
    String ORDER_CANCEL = "10";
    // 商品管理
    String PRODUCT = "11";
    // 商品分组
    String PRODUCT_GROUP = "12";
    // 客户管理
    String CUSTOMER = "13";
    // 客户类型
    String CUSTOMER_GROUP = "14";
    // 工具
    String TOOL = "15";
    // 地砖计算器
    String FLOOR_TILE_COUNTRER = "16";
    // 墙砖计算器
    String WALL_TILE_COUNTRER = "17";
    // 地板计算器
    String FLOOR_COUNTRER = "18";
    // 窗帘计算器
    String CURTAIN_COUNTRER = "19";
    // 涂料计算器
    String COATING_COUNTRER = "20";
    // 壁纸计算器
    String WALLPAPER_COUNTRER = "21";
    // 员工管理
    String USER = "22";
    // 一键营销
    String MARKETING = "23";
    // 场景营销
    String SCENE_MARKETING = "24";
    // 广告营销
    String ADVERTIS_MARKETING = "25";
    // 定制商品
    String BESPOKE_ORDER = "26";
    // 定制订单打印
    String BESPOKE_ORDER_PRINT = "27";
    // 营销平台+微商城
    String MARKETING_PLATFORM_MICROMALL = "28";
    //互动营销
    String ACTIVITY_MARKETING = "29";
    
  }

  /**
   * 所有的模块信息
   * @author admin
   *
   */
  public static class ModuleData {
    
    private static List<Module> modules;
    private static List<String> ToolModuleids;
    private static Map<String, List<Integer>> modulesPermissionMap = new HashMap<String, List<Integer>>();
    private static Map<String, Module> modulesMap = new HashMap<String, Module>();

    public static List<Module> getModules() {
      return modules;
    }

    public static void setModules(List<Module> modules) {
      ModuleData.modules = modules;
      ModuleData.ToolModuleids = modules.stream().filter(tm->ModuleType.TOOL.equals(tm.getPid()+""))
          .map(tm->tm.getId()+"").collect(Collectors.toList());
      modules.forEach(m->{
            if(StringUtils.isNotBlank(m.getPermId())){
              List<Integer> l = Lists.newArrayList();
              for(String id : m.getPermId().split(",")){
                l.add(Integer.valueOf(id));
              }
              modulesPermissionMap.put(m.getId()+"", l);
            }
            modulesMap.put(m.getId()+"", m);
          });
    }

    public static List<String> getToolModuleids() {
      return ToolModuleids;
    }

    public static Map<String, List<Integer>> getModulesPermissionMap() {
      return modulesPermissionMap;
    }
    
    public static Map<String, Module> getModulesMap() {
      return modulesMap;
    }
    
  }

}
