package com.qianfan365.jcstore.controller.manage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.constant.MessageConstant.MassCommand;
import com.qianfan365.jcstore.common.constant.MessageConstant.MessageType;
import com.qianfan365.jcstore.common.constant.SessionConstant;
import com.qianfan365.jcstore.common.pojo.MessageManager;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.HtmlDecodeUtil;
import com.qianfan365.jcstore.service.MessageService;
import com.qianfan365.jcstore.service.UserService;

@Controller
@RequestMapping("/manage/message")
public class MessageManageController {
  @Autowired
  private MessageService messageService;
  @Autowired
  private UserService userService;
  
  /**
   * 发布消息
   * @param messageManager
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/save"},method=RequestMethod.POST)
  public Map<String, Object> save(HttpSession session, MessageManager messageManager) {
    User user = SessionConstant.getUserSessionAdmin(session);
    messageManager.setUid(user.getId());
    Map<String, Object> map = ResultData.build();
    String sendObjects = messageManager.getSendObjects();
    if (StringUtils.isBlank(sendObjects) || StringUtils.isBlank(messageManager.getContent())
        || HtmlDecodeUtil.htmlDecode1(messageManager.getContent()).length() > 100
        || (StringUtils.isNotBlank(messageManager.getLink())&&!messageManager.getLink().matches("^http[s]?://.+$"))) {
      map.put("status", "10100");
      return map;
    }
    sendObjects = sendObjects.replaceAll("，", ",");//兼容中文逗号
    String sendArr[] = sendObjects.split(",");
    sendObjects = ","+sendObjects+",";
    List<String> list = Arrays.asList(sendArr);
    //处理list和sendObjects
    
    Set<Integer> set = new HashSet<Integer>();//用户id集合
    if(list.contains("@"+MassCommand.ALL_USER)){
      messageManager.setSendObjects(MassCommand.ALL_USER);
      set.addAll(userService.findIdByAll());
      
      saveAssist(set, messageManager, map, true);
      return map;
    }
    if (list.contains("@"+MassCommand.ALL_BUSINESS)){
      messageManager.setSendObjects(MassCommand.ALL_BUSINESS);
      set.addAll(userService.findIdByBusiness());
      
      saveAssist(set, messageManager, map, true);
      return map;
    }
    
    //处理不含群发命令的list
    Set<String> wrong = new LinkedHashSet<String>();//错误用户名的集合
    for (String username : list) {
      if(!username.startsWith("@")){ //用户名错误
        sendObjects = sendObjects.replaceFirst(","+username+",", ",");
        wrong.add(username);
        continue;
      }
      String noAt = username.substring(1);
      User userByname = userService.findByUsername(noAt);
      if(userByname == null){ //用户名错误
        sendObjects = sendObjects.replaceFirst(","+username+",", ",");
        wrong.add(noAt);
        continue;
      }
      int size=set.size();
      set.add(userByname.getId());
      if(size == set.size()){ //用户名重复
        sendObjects = sendObjects.replaceFirst(","+username+",", ",");
        continue;
      }
    }
    
    
    if(set.isEmpty()){
      messageManager.setSendObjects("");
      map.put("status", 0);
    }else{
      messageManager.setSendObjects(sendObjects.substring(1,sendObjects.length()-1).replace("@", ""));//去掉首尾逗号
      map.put("status", 1);
      saveAssist(set, messageManager, map, false);
    }
    map.put("wrong", StringUtils.join(wrong,","));
    return map;
  }
  
  /**
   * 发布消息辅助
   * @param set
   * @param messageManager
   * @param map
   * @param status
   */
  public void saveAssist(Set<Integer> set ,MessageManager messageManager,Map<String, Object> map,boolean status){
    messageService.insertMessageManager(messageManager);
    messageService.insertBatch(set,messageManager,MessageType.SYSTEM_MESSAGE);
    if(status){
      map.put("status", 1);
      map.put("wrong", "");
    }
  }

  /**
   * 消息管理列表
   * @param currentPage
   * @param limit
   * @return
   */
  @ResponseBody
  @RequestMapping(value={"/list"},method=RequestMethod.GET)
  public ResultData list(@RequestParam(defaultValue = "1") Integer currentPage,
    @RequestParam(defaultValue = "10") Integer limit){
    Page<MessageManager> page = messageService.managerList(currentPage, limit);
    return ResultData.build().parsePageBean(page);
  }
  
  /**
   * 跳转到消息管理列表页面
   */
  @RequestMapping("/toList")
  public String toView(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
    session.setAttribute("ma_head_nav", "platform");
    session.setAttribute("ma_sider_nav", "message");
    return "manage/message-list";
  }
  
}
