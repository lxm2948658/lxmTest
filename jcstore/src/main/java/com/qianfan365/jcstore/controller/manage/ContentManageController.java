package com.qianfan365.jcstore.controller.manage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.Content;
import com.qianfan365.jcstore.common.util.HtmlDecodeUtil;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ContentService;

/**
 * 
 * @author liuhaoran
 *
 */
@Controller
@RequestMapping("/manage/content")
public class ContentManageController extends BaseController {
  @Autowired
  private ContentService contentService;
  
  /**
   * 根据id查询内容设置页中对应的内容
   * 
   * @param id
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/find",method=RequestMethod.GET)
  public Map<String, Object> find(int id) {
    Map<String, Object> map = ResultData.build();
    map.put("content", contentService.find(id).getContent());
    return map;
  }
  
  /**
   * 保存内容设置
   * 
   * @param content
   * @return
   */
  @ResponseBody
  @RequestMapping(value="/save",method=RequestMethod.POST)
  public Map<String, Object> save(Content content) {
    Map<String, Object> map = ResultData.build();
    content.setContent(HtmlDecodeUtil.htmlDecode1(content.getContent()).replaceAll("<script", "<!--script--").replaceAll("/script>", "!--/script-->"));
    map.put("status", contentService.saveContent(content));
    return map;
  }

  /**
   * 跳转到内容设置页面
   * 
   * @param response
   * @param request
   * @param session
   * @return
   */
  @RequestMapping({"toContent"})
  public String toList(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
    session.setAttribute("ma_head_nav", "contentsetting");
    session.setAttribute("ma_sider_nav", "contents");
    return "/manage/content-setting";
  }
}
