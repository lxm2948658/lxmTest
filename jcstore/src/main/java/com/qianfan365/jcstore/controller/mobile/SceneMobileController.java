package com.qianfan365.jcstore.controller.mobile;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.permission.ModulePassport;
import com.qianfan365.jcstore.common.bean.permission.PermissionPassport;
import com.qianfan365.jcstore.common.constant.ModuleConstant.ModuleType;
import com.qianfan365.jcstore.common.constant.PermissionConstant.PermissionType;
import com.qianfan365.jcstore.common.pojo.SceneMarketing;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.SceneMarketingService;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping("/mobile/scene")
public class SceneMobileController extends BaseController {
  @Autowired
  private SceneMarketingService sceneMarketingService;
  @Autowired
  private UserService userService;

  
  /**
   * 我的场景查询列表，场景是0，模板是1
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/inquiryScene",method = RequestMethod.GET)
  @ModulePassport(moduleids = {ModuleType.MARKETING,ModuleType.SCENE_MARKETING})
  @PermissionPassport(permissionids = {PermissionType.SCENE_MARKETING})
  public ResultData inquiryScene(HttpSession session,
         @RequestParam(defaultValue = "1") Integer currentPage,
         @RequestParam(defaultValue = "20") Integer limit,Long querytime){
     User user = getLoginUser(session);
    if (1 == currentPage || null == querytime || 0 == querytime) {
      querytime = new Date().getTime();
    }
    int belong=userService.getAdminID(user);
    Page<SceneMarketing> findAllScene = sceneMarketingService.findAllScene(currentPage, limit,querytime,0,belong);
    return ResultData.build().parseList(findAllScene).put("querytime", querytime);
  }
  
  /**
   * 模板查询列表,场景是0，模板是1
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/inquiryType",method = RequestMethod.GET)
  @ModulePassport(moduleids = {ModuleType.MARKETING,ModuleType.SCENE_MARKETING})
  @PermissionPassport(permissionids = {PermissionType.SCENE_MARKETING})
  public ResultData inquiryScene(
         @RequestParam(defaultValue = "1") Integer currentPage,
         @RequestParam(defaultValue = "20") Integer limit,Long querytime,Integer belongs){
    if (1 == currentPage || null == querytime || 0 == querytime) {
      querytime = new Date().getTime();
    }
    return ResultData.build().parseList(sceneMarketingService.findAllScene(currentPage, limit,querytime,1,0));
  }
  
  /**
   * 删除场景列表
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/delectMob",method = RequestMethod.POST)
  @ModulePassport(moduleids = {ModuleType.MARKETING,ModuleType.SCENE_MARKETING})
  @PermissionPassport(permissionids = {PermissionType.SCENE_MARKETING})
  public ResultData delctMob(Integer id,
         @RequestParam(defaultValue = "1") Integer currentPage,
         @RequestParam(defaultValue = "20") Integer limit){
    this.sceneMarketingService.delectScene(id);
    return ResultData.build().success();
  }
  
  
}