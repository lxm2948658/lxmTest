package com.qianfan365.jcstore.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Panorama;
import com.qianfan365.jcstore.service.PanoramaService;

@Controller
@RequestMapping("/manage/panorama")
public class PanoramaManageController {
  @Autowired
  private PanoramaService panoramaService;

  /**
   * 全景查询
   * 
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/find", method = RequestMethod.GET)
  public ResultData findPanorama(@RequestParam(defaultValue = "1") Integer currentPage,
      @RequestParam(defaultValue = "20") Integer limit, Integer belongs,Panorama panorama) {
    // 判断belongs是否为空,不为空，执行查询并返回
    if(panorama.getBelongs() != null){
      return ResultData.build().parseList(panoramaService.findPanorama(currentPage, limit, belongs));
    }// 否则返回参数错误
    return ResultData.build().parameterError();
  }

  /**
   * 新增/修改全景
   * 
   * @param scene
   * @return
   */
  @ResponseBody
  @RequestMapping(path = "/add", method = RequestMethod.POST)
  public ResultData add(Panorama panorama) {
    if(panorama.getTitle() != null && panorama.getTitle().length() <= 30 && panorama.getUrl().length() <=100 && panorama.getPic() != null){
        this.panoramaService.add(panorama);
        return ResultData.build().success();
      }
    return ResultData.build().parameterError();
  }

  /**
   * 删除全景
   * 
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/delete", method = RequestMethod.POST)
  public ResultData delectPanorama(Integer id) {
    if(id != null && id>0){
     return  this.panoramaService.deletePanorama(id);
    }
    return ResultData.build().parameterError();
  }


}
