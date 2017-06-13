package com.qianfan365.jcstore.controller.manage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.SceneMarketing;
import com.qianfan365.jcstore.service.ClientService;
import com.qianfan365.jcstore.service.SceneMarketingService;

@Controller
@RequestMapping("/manage/scene")
public class SceneMarketingManageController {
  @Autowired
  private SceneMarketingService sceneMarketingService;
  @Autowired
  private ClientService clientService;
  
  /**
   * 查询场景营销账户数据
   * @param sceneMarketing
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/findScene",method = RequestMethod.GET)
  public ResultData findAllScene(
         @RequestParam(defaultValue = "1") Integer currentPage,
         @RequestParam(defaultValue = "20") Integer limit,Integer belong){
    
    return ResultData.build().parseList(sceneMarketingService.findAllScene(currentPage, limit,null,0,belong));
                                                                                                //type为0的是场景，只属于个人
  }
  /**
   * 查询模板营销账户数据
   * @param sceneMarketing
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/findType",method = RequestMethod.GET)
  public ResultData findAllTpl(
         @RequestParam(defaultValue = "1") Integer currentPage,
         @RequestParam(defaultValue = "20") Integer limit,
         String SceneMarketing){
    
    return ResultData.build().parseList(sceneMarketingService.findAllScene(currentPage, limit,null,1,0));
                                                                                            //type为1的是模板，所有人
  }
  
  /**
   * 删除所有场景营销数据信息
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(path = "/delectScene",method = RequestMethod.POST)
  public ResultData delectScene(Integer id){
    if(id != null && id>0){
      return this.sceneMarketingService.delectScene(id);
    }
    return ResultData.build().parameterError();
  }
  
  /**
   * 场景列表修改新增
   * @param scene
   * @return
   */
  @ResponseBody
  @RequestMapping(path = "/addScene",method = RequestMethod.POST)
  public ResultData add(SceneMarketing scene){
    Assert.notNull(scene.getBelong());
    //表示场景
    scene.setType(0);
    this.sceneMarketingService.add(scene);
    return ResultData.build().success();
  }
  
  /**
   * 模板列表修改新增
   * @param scene
   * @return
   */
  @ResponseBody
  @RequestMapping(path = "/addType",method = RequestMethod.POST)
  public ResultData addTpl(SceneMarketing scene){
    scene.setType(1);
    this.sceneMarketingService.add(scene);
    return ResultData.build().success();
  }
  
  
}