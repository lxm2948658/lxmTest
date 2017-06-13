package com.qianfan365.jcstore.controller.manage;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ModuleBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.pojo.Module;
import com.qianfan365.jcstore.common.pojo.SoftType;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ModuleService;
import com.qianfan365.jcstore.service.SoftTypeService;

@Controller
@RequestMapping("/manage/softType")
public class SoftTypeManageController extends BaseController {

  @Autowired
  private ModuleService moduleService;
  @Autowired
  private SoftTypeService softTypeService;

  /**
   * 获取模块信息
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "moduleInfo", method = RequestMethod.GET)
  public ResultData getModuleInfo() {
    List<ModuleBean> moduleInfo = moduleService.getModuleInfo();
    return ResultData.build().put("aaData", moduleInfo);
  }

  /**
   * 保存版本信息
   * @param softType
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "save", method = RequestMethod.POST)
  public ResultData save(SoftType softType) {
    // 参数校验
    // 1 版本名称在8个字符之内,不可为空
    // 2 至少选择一个模块
    if (softType != null && StringUtils.isNotEmpty(softType.getName())
        && StringUtils.isNotEmpty(softType.getModuleId()) && softType.getName().length() <= 8
        && softType.getModuleId().split(",").length > 0) {
      // 获取所选功能模块数组
      List<String> asList = Arrays.asList(softType.getModuleId().split(","));
      StringBuilder sb = new StringBuilder().append(softType.getModuleId());
      // 子模块被选择,父模块默认勾选
      HashMap<String, Module> all = moduleService.getAll();
      for (String string : asList) {
        Integer pid = all.get(string).getPid();
        if (pid != 0 && !asList.contains(pid.toString()) && sb.indexOf(","+pid) == -1) {
          sb.append(","+pid);
        }
      }
      // 勾选订单管理的情况下，常规管理和极简开单至少勾选一个
      if (asList.contains(ModuleType.ORDER_INFO)) {
        if (!asList.contains(ModuleType.GENERAL_ORDER) && !asList.contains(ModuleType.MINIMAL_ORDER)) {
          return ResultData.build().setStatus(Status.AT_LEAST_ONE_WAY_TO_ORDER);
        }
        // 勾选了常规开单那么自动会勾选商品管理
        if (asList.contains(ModuleType.GENERAL_ORDER) && !asList.contains(ModuleType.PRODUCT)) {
          sb.append(","+ModuleType.PRODUCT);
        }
      }
      // 勾选了定制商品那么自动会勾选订单管理
      if (asList.contains(ModuleType.BESPOKE_ORDER) && !asList.contains(ModuleType.ORDER_INFO)) {
        sb.append(","+ModuleType.ORDER_INFO);
      }
      // 勾选了工具则至少选择一种计算器
      if (asList.contains(ModuleType.TOOL)) {
        if (!(asList.contains(ModuleType.FLOOR_TILE_COUNTRER)
            || asList.contains(ModuleType.WALL_TILE_COUNTRER)
            || asList.contains(ModuleType.FLOOR_COUNTRER)
            || asList.contains(ModuleType.CURTAIN_COUNTRER)
            || asList.contains(ModuleType.COATING_COUNTRER) || asList
              .contains(ModuleType.WALLPAPER_COUNTRER)))
          return ResultData.build().setStatus(Status.AT_LEAST_A_CALCULATOR);
      }
      // 保存数据
      softType.setModuleId(sb.toString());
      softType.setUpdatetime(new Date());
      softTypeService.save(softType);
      return ResultData.build().success();
    }
    return ResultData.build().parameterError();
  }
  
  /**
   * 获取版本信息列表
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/findsoft",method = RequestMethod.GET)
  public ResultData findSoft(
         @RequestParam(defaultValue = "1") Integer currentPage,
         @RequestParam(defaultValue = "20") Integer limit){
    return ResultData.build().parsePageBean(softTypeService.findSoft(currentPage, limit));
  }
  
  /**
   * 更改版本状态
   * 
   * @param id
   * @param status
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "change", method = RequestMethod.POST)
  public ResultData changeStatus(SoftType softType) {
    // 参数校验
    if (softType != null && softType.getId() != null && softType.getStatus() != null
        && (softType.getStatus() == 1 || softType.getStatus() == 0)) {
      // 更新版本
      softTypeService.save(softType);
      return ResultData.build().success();
    }
    return ResultData.build().parameterError();
  }
  
  /**
   * 根据ID获取软件信息
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "get",method = RequestMethod.GET)
  public ResultData getById(Integer id){
    if(id == null) return ResultData.build().parameterError();
    return ResultData.build().put("aaData",this.softTypeService.getById(id));
  }

}
