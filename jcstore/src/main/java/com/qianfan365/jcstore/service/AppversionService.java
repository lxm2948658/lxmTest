package com.qianfan365.jcstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.AppversionConstant.AppversionId;
import com.qianfan365.jcstore.common.constant.AppversionConstant.UpdateOption;
import com.qianfan365.jcstore.common.pojo.Appversion;
import com.qianfan365.jcstore.dao.inter.AppversionMapper;

/**
 * @author szz
 */

@Service
public class AppversionService {
  
  @Autowired
  private AppversionMapper appversionMapper;
  
  /**
   * 更新信息
   * @param key
   * @param info
   */
  public void setInfo(String key,String info){
    switch (key) {
      case UpdateOption.ANDROID_VERSION:
        this.appversionMapper.updateAndroidVersion(info);
        break;
      case UpdateOption.IOS_VERSION:
        this.appversionMapper.updateIOSVersion(info);
        break;
      case UpdateOption.IOS_SWITCH:
        this.appversionMapper.updateIOSSwitch(info);
        break;
      case UpdateOption.PAD_VERSION:
        this.appversionMapper.updateAndroidPad(info);
        break;
      default:
        break;
    }
  }

  /**
   * 用于给管理后台返回数据
   * @return
   */
  public ResultData getAllVersionInfo() {
    ResultData data = new ResultData();
    //获取安卓版本信息
    Appversion androidVersion = this.getAndroidVersionInfo();
    // 封装安卓信息
    data.put("androidVersion", androidVersion.getVersion());
    
    // 获取IOS版本信息
    Appversion iosVersion = this.getIOSVersion();
    // 封装IOS信息
    data.put("checkStatus", Integer.parseInt(iosVersion.getExtraInfo()));
    data.put("iosVersion", iosVersion.getVersion());
    
    //获取PAD版本信息
    Appversion androidPad = this.getAndroidPad();
    //封装PAD信息
    data.put("androidPad", androidPad.getVersion());
    return data;
  }
  
  /**
   * 获取PAD的版本信息
   * @return
   */
  public Appversion getAndroidPad() {
    return this.appversionMapper.selectByPrimaryKey(AppversionId.PAD);
  }

  /**
   * 获取IOS的版本信息
   * @return
   */
  public Appversion getIOSVersion() {
    return this.appversionMapper.selectByPrimaryKey(AppversionId.IOS);
  }

  /**
   * 获取安卓的版本信息
   * @return
   */
  public Appversion getAndroidVersionInfo() {
    return this.appversionMapper.selectByPrimaryKey(AppversionId.ANDROID);
  }
  

}
