package com.qianfan365.jcstore.controller.mobile;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Panorama;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.PanoramaService;
import com.qianfan365.jcstore.service.UserService;
@Controller
@RequestMapping("/mobile/panorama")
public class PanoramaMoblieController extends BaseController{
  @Autowired
  private PanoramaService panoramaService;
  @Autowired
  private UserService userService;
  
  /**
   * 全景查询
   * @param session
   * @param querytime
   * @param currentPage
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/findPanorama",method = RequestMethod.GET)
  public ResultData findPanorama(HttpSession session,Long querytime,
         @RequestParam(defaultValue = "1")Integer currentPage){
    User user = getLoginUser(session);
    if (1 == currentPage || null == querytime || 0 == querytime) {
      querytime = new Date().getTime();
    }
    int belongs=userService.getAdminID(user);
    List<Panorama> findPanorama = panoramaService.find(belongs,querytime);
    return ResultData.build().parseList(findPanorama).put("querytime", querytime);
  }
}
