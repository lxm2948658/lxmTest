package com.qianfan365.jcstore.controller.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Appversion;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.AppversionService;

@Controller
@RequestMapping("/mobile/version")
public class AppVersionMobileController extends BaseController{
  
  @Autowired
  private AppversionService appversionService;
  
  /**
   * 获取App版本信息
   * @param osType 安卓传1,IOS传2,PAD传3
   * @return
   */
  @RequestMapping(value = "/get",method = RequestMethod.GET)
  @ResponseBody
  public ResultData getAppVersion(Integer osType){
    ResultData data = ResultData.build();
    // 参数校验
    if(osType == null || (osType != 1 && osType != 2 && osType != 3)){
      return data.parameterError(); 
    }
    // 操作系统为安卓
    if(osType.equals(1)){
      Appversion androidVersionInfo = appversionService.getAndroidVersionInfo();
      data.put("version", androidVersionInfo.getVersion());
      data.put("url", androidVersionInfo.getExtraInfo());
      // 操作系统为IOS
    }else if(osType.equals(2)){
      Appversion iosVersion = appversionService.getIOSVersion();
      data.put("version", iosVersion.getVersion());
      data.put("switch", Integer.parseInt(iosVersion.getExtraInfo()));
      //PAD版本操作
    }else if(osType.equals(3)){
      Appversion padVersionInfo = appversionService.getAndroidPad();
      data.put("version", padVersionInfo.getVersion());
      data.put("url", padVersionInfo.getExtraInfo());
    }
    return data;
  }

}
