package com.qianfan365.jcstore.controller.manage;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.AppversionConstant.UpdateOption;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.AppversionService;

/**
 * 后台管理版本校验接口
 * @author szz
 */

@Controller
@RequestMapping("/manage/version")
public class AppVersionManageController extends BaseController {
  
  @Autowired
  private AppversionService appversionService;
  
  @RequestMapping("setting")
  public String toView(HttpSession session){
    session.setAttribute("ma_head_nav", "contentsetting");
    session.setAttribute("ma_sider_nav", "version");
    return "manage/version-setting";
  }
  
  /**
   * 更新版本检测信息
   * @param checkStatus
   * @return
   */
  @RequestMapping(value = "/change", method = RequestMethod.POST)
  @ResponseBody
  public ResultData change(String androidVersion,String iosVersion,String padVersion,Integer checkStatus){
    ResultData resultData = ResultData.build();
    // 正则
    String regex = "^[A-Za-z0-9\\.]{0,10}$";
    if(androidVersion != null){
      if(androidVersion.matches(regex)){
        appversionService.setInfo(UpdateOption.ANDROID_VERSION, androidVersion);
      }else{
        return resultData.parameterError();
      }
    }else if(iosVersion != null){
      if(iosVersion.matches(regex)){
        appversionService.setInfo(UpdateOption.IOS_VERSION, iosVersion);
      }else{
        return resultData.parameterError();
      }
    }else if(checkStatus !=null){
      if(checkStatus == 1 || checkStatus == 0){
        appversionService.setInfo(UpdateOption.IOS_SWITCH, checkStatus.toString());
      }else{
        return resultData.parameterError();
      }
    }else if(padVersion != null){     //pad版本校验
      if(padVersion.matches(regex)){
        appversionService.setInfo(UpdateOption.PAD_VERSION, padVersion);
      }else{
        return resultData.parameterError();
      }
    }else{
      return resultData.parameterError();
    }
    return resultData;
  }


    
  /**
   * 获取IOS版本校验状态
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/get",method = RequestMethod.GET)
  public ResultData get(){
    return appversionService.getAllVersionInfo();
  }

}
