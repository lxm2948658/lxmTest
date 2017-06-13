package com.qianfan365.jcstore.controller.manage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.qianfan365.jcstore.controller.BaseController;

@Controller
@RequestMapping("/manage/platform")
public class PlatformManageController extends BaseController {
  /**
   * 跳转到消息列表页面
   * 
   */
  @RequestMapping({"toMessageList"})
  public String toMessageList(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
    session.setAttribute("ma_head_nav", "platform");
    session.setAttribute("ma_sider_nav", "message");
    return "/manage/message-list";
  }
  
  /**
   * 跳转到消息发送页面
   * 
   */
  @RequestMapping({"toMessageAdd"})
  public String toMessageAdd(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
    session.setAttribute("ma_head_nav", "platform");
    session.setAttribute("ma_sider_nav", "message");
    return "/manage/message-add";
  }
  
  /**
   * 跳转到意见反馈列表页面
   * 
   */
  @RequestMapping({"toFeedback"})
  public String toFeedback(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
    session.setAttribute("ma_head_nav", "platform");
    session.setAttribute("ma_sider_nav", "feedback");
    return "/manage/feedback";
  }
  
  /** 1.5 wangruoqiu
   * 跳转到h5模板页面
   * 
   */
  @RequestMapping({"toH5List"})
  public String toH5List(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
	  session.setAttribute("ma_head_nav", "platform");
	  session.setAttribute("ma_sider_nav", "h5manage");
	  return "/manage/h5-temp-list";
  }
  
  /** 1.5 wangruoqiu
   * 营销效果监控
   * 
   */
  @RequestMapping({"toEffectcontrol"})
  public String toEffectcontrol(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
	  session.setAttribute("ma_head_nav", "platform");
	  session.setAttribute("ma_sider_nav", "control");
	  return "/manage/effect-control";
  }
  
 
}
