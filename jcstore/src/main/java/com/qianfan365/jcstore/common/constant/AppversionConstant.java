package com.qianfan365.jcstore.common.constant;

public class AppversionConstant {

  public interface UpdateOption {
    // 安卓版本
    String ANDROID_VERSION = "android.version";
    // 安卓下载地址
    String ANDROID_URL = "android.url";
    
    // ios版本
    String IOS_VERSION = "ios.version";
    // ios版本校验开关
    String IOS_SWITCH = "ios.checkStatus";
    
    //PAD版本
    String PAD_VERSION = "pad_version";
    //PAD下载地址
    String PAD_URL ="pad_url";
  }

  public interface AppversionId {
    Integer ANDROID = 1;
    Integer IOS = 2;
    Integer PAD = 3;
  }

}
