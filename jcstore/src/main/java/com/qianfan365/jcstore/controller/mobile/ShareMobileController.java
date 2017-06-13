package com.qianfan365.jcstore.controller.mobile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.qianfan365.jcstore.common.bean.OrderInfoBean;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.pojo.OrderInfo;
import com.qianfan365.jcstore.common.pojo.User;
import com.qianfan365.jcstore.common.util.FileUploadUtils;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.OrderInfoService;
import com.qianfan365.jcstore.service.PermissionInfoService;
import com.qianfan365.jcstore.service.ShopService;
import com.qianfan365.jcstore.service.UserService;


@Controller
@RequestMapping("/mobile/share")
public class ShareMobileController extends BaseController {

  @Value("${common.domain-url}")
  String domainUrl;
  @Value("${common.phantomjs.path}")
  String phantomjsPath;
  @Value("${common.phantomjs.pathjs}")
  String phantomjsPathjs;
  @Value("${common.phantomjs.cmd.path}")
  String cmdPath;
  @Value("${common.phantomjs.img.path}")
  String imgPath;
  @Value("${common.phantomjs.py.url}")
  String pyUrl;
  @Value("${common.phantomjs.py.switch}")
  int pySwitch;

  @Autowired
  private OrderInfoService orderInfoService;

  @Autowired
  private ShopService shopService;

  @Autowired
  private UserService userService;
  
  @Autowired
  private PermissionInfoService permissionInfoService;

  /**
   * 分享的订单页面
   * 
   * @param oid
   * @param request
   * @param reponse
   * @return
   */
  @RequestMapping("/orderinfo")
  public String orderinfo(Integer oid, Model model, HttpServletRequest request,
      HttpServletResponse reponse) {
    // TODO 限制访问ip
    // Object order = null;
    // TODO 查询order信息，填充页面
    OrderInfoBean order = orderInfoService.orderInfoDetail(oid);
    model.addAttribute("user", userService.findByUid(order.getUserId()));
    model.addAttribute("order", order);
    model.addAttribute("shop", shopService.findById(order.getShopId()));
    return "manage/share-order";
  }
  
  @SuppressWarnings("all")
  private String captureByPy(Integer oid) {
    String path = null;
    if(pySwitch==0){
      return path;
    }
    try {
      String uri = String.format(pyUrl, oid.toString());
      CloseableHttpClient httpclient = HttpClients.createDefault();
      HttpGet httpGet = new HttpGet(uri);
      ResponseHandler<Map> responseHandler = new ResponseHandler<Map>() {

        @Override
        public Map handleResponse(final HttpResponse response) throws ClientProtocolException,
            IOException {
          int status = response.getStatusLine().getStatusCode();
          if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            String string = entity != null ? EntityUtils.toString(entity) : null;
            return new Gson().fromJson(string, Map.class);
          } else {
            throw new ClientProtocolException("Unexpected response status: " + status);
          }
        }

      };
      Map responseBody = httpclient.execute(httpGet, responseHandler);
      if(responseBody.containsKey("img_url")){
        Object pathValue = responseBody.get("img_url");
        path=pathValue==null?null:pathValue.toString();
      }
    } catch (Exception e1) {
      log.error(e1.getMessage(),e1);
    }
    return path;
  }

  /**
   * 截取订单详情页保存成图片
   * 
   * @param session
   * @param oid
   * @param phoneheight
   * @param phonewidth
   * @param request
   * @param reponse
   * @return
   */
  @RequestMapping("/capture")
  @ResponseBody
  public ResultData capture(HttpSession session, Integer oid, String phoneheight,
      @RequestParam(defaultValue = "375") String phonewidth, HttpServletRequest request,
      HttpServletResponse reponse) {
    
    User user = getLoginUser(session);
    OrderInfo order = orderInfoService.findById(oid);
    ResultData result = permissionInfoService.orderPermissionCheck(session, user, order.getUserId());
    if(result!=null){
      return result;
    }

    ResultData map = ResultData.build();
    String path = captureByPy(oid);
    
    if(StringUtils.isBlank(path)){
      StringBuilder accub = new StringBuilder();
      // 拼执行路径
      accub.append(cmdPath + " " + phantomjsPath);
      // 拼js文件
      accub.append(" " + phantomjsPathjs);
      // post 地址
      accub.append(" " + domainUrl + "/mobile/share/orderinfo?oid=" + oid);
      // 图片名字
  
      String filename =
          "mobile_share_" + System.currentTimeMillis() + "_" + RandomUtils.nextInt(100000) + ".png ";
      String tempImg = imgPath + filename;
      accub.append(" " + tempImg);
      // 宽高
      accub.append(" " + phonewidth);
      if (StringUtils.isNotBlank(phoneheight) && StringUtils.isNumeric(phoneheight)) {
        accub.append(" " + phoneheight);
      }
      String cmd = accub.toString();
  
      // log.error(cmd);
  
      Process p = null;
      try {
        p = Runtime.getRuntime().exec(cmd);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
      // 取得命令结果的输出流
      InputStream fis = p.getInputStream();
      // 用一个读输出流类去读
      InputStreamReader isr = new InputStreamReader(fis);
      // 用缓冲器读行
      BufferedReader br = new BufferedReader(isr);
      String line = null;
      // 直到读完为止
  
      try {
        while ((line = br.readLine()) != null) {
          if (line.contains("SUCCESSFUL")) {
            log.debug("SUCCESSFUL: " + line);
          }
          log.debug(line);
        }
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
      p.destroy();
  
      File file = new File(tempImg.trim());
      try {
        path = FileUploadUtils.uploadFile(file);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      } finally {
        try {
          file.delete();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }

    map.put("path", path);
    if (StringUtils.isBlank(path)) {
      map.error();
    }
    return map;
  }
  
  /**
   * 截取订单详情页保存成图片(通过单据二维码查看,不需要登录拦截)
   * 
   * @param oid
   * @param proof
   * @param phoneheight
   * @param phonewidth
   * @param request
   * @param reponse
   * @return
   */
  @RequestMapping("/capture/proof")
  @ResponseBody
  public ResultData captureProof(Integer oid, String proof, String phoneheight,
      @RequestParam(defaultValue = "375") String phonewidth, HttpServletRequest request,
      HttpServletResponse reponse) {
    
    if(orderInfoService.findByProof(oid, proof)==null){
      return ResultData.build().parameterError();
    }
    
    ResultData map = ResultData.build();
    String path = captureByPy(oid);
    
    if(StringUtils.isBlank(path)){
      StringBuilder accub = new StringBuilder();
      // 拼执行路径
      accub.append(cmdPath + " " + phantomjsPath);
      // 拼js文件
      accub.append(" " + phantomjsPathjs);
      // post 地址
      accub.append(" " + domainUrl + "/mobile/share/orderinfo?oid=" + oid);
      // 图片名字
  
      String filename =
          "mobile_share_" + System.currentTimeMillis() + "_" + RandomUtils.nextInt(100000) + ".png ";
      String tempImg = imgPath + filename;
      accub.append(" " + tempImg);
      // 宽高
      accub.append(" " + phonewidth);
      if (StringUtils.isNotBlank(phoneheight) && StringUtils.isNumeric(phoneheight)) {
        accub.append(" " + phoneheight);
      }
      String cmd = accub.toString();
  
      // log.error(cmd);
  
      Process p = null;
      try {
        p = Runtime.getRuntime().exec(cmd);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
      // 取得命令结果的输出流
      InputStream fis = p.getInputStream();
      // 用一个读输出流类去读
      InputStreamReader isr = new InputStreamReader(fis);
      // 用缓冲器读行
      BufferedReader br = new BufferedReader(isr);
      String line = null;
      // 直到读完为止
  
      try {
        while ((line = br.readLine()) != null) {
          if (line.contains("SUCCESSFUL")) {
            log.debug("SUCCESSFUL: " + line);
          }
          log.debug(line);
        }
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
      p.destroy();
  
      File file = new File(tempImg.trim());
      try {
        path = FileUploadUtils.uploadFile(file);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      } finally {
        try {
          file.delete();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
    }

    map.put("path", path);
    if (StringUtils.isBlank(path)) {
      map.error();
    }
    return map;
  }

}
