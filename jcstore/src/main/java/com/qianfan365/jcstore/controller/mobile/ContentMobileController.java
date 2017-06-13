package com.qianfan365.jcstore.controller.mobile;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ContentService;

/**
 * 
 * @author liuhaoran
 *
 */
@Controller
@RequestMapping("/mobile/content")
public class ContentMobileController extends BaseController {
  @Autowired
  private ContentService contentService;
  
  /**
   * 根据id查询设置的对应内容
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/find",method=RequestMethod.GET)
  public Map<String, Object> find(int id) {
    if(id>3 || id<1){
      return ResultData.build().parameterError();
    }
    Map<String, Object> map = ResultData.build();
    map.put("content", contentService.find(id).getContent());
    return map;
  }
}
