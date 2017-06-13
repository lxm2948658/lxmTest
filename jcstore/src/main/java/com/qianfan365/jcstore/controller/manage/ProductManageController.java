package com.qianfan365.jcstore.controller.manage;

import java.io.File;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.FileSystem;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.GsonBuilder;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.controller.BaseController;
import com.qianfan365.jcstore.service.ProductService;

@RequestMapping("/manage/product")
@Controller
public class ProductManageController extends BaseController {

  @Autowired
  private ProductService productService;

  /**
   * 下载模板
   * 
   * @return
   */
  @RequestMapping(value = "/download", method = RequestMethod.GET)
  public ResponseEntity<byte[]> download(HttpServletRequest request) {
    try {
      String agent = request.getHeader("User-Agent");
      // 加载文件
      File file =
          ConfigurationUtils.fileFromURL(ConfigurationUtils.locate(
              FileSystem.getDefaultFileSystem(), null, "example.xlsx"));
      // 设置请求头
      HttpHeaders headers = new HttpHeaders();
      // 设置返回文件名
      String fileName = null;
      if (agent != null
          && (agent.indexOf("MSIE") != -1 ||  (-1 != agent.indexOf("like Gecko") && agent.indexOf("Safari") == -1))) {
        fileName = URLEncoder.encode("易开单商品导入模板.xlsx", "UTF-8");
      } else {
        fileName = new String("易开单商品导入模板.xlsx".getBytes("UTF-8"), "ISO-8859-1");
      }
      headers.set("Content-Disposition", "attachment;filename=" + fileName);
      headers.set("Content-Type", "application/octet-stream;charset=UTF-8");
      // 返回数据
      return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
  }

  /**
   * 导入商品
   * 
   * @param file
   * @param clientId
   * @return
   */
  @RequestMapping(value = "/import", method = RequestMethod.POST)
  public void importProduct(HttpServletResponse response, MultipartFile file, Integer clientId)
      throws Exception {
    ResultData data = ResultData.build();
    // 校验参数
    if (clientId != null && file != null) {
      if(file.getSize() == 0){
        data.parameterError().put("statusMsg", "找不到该文件");
      }else{
        data = productService.importProduct(file, clientId);
      }
    } else {
      data.parameterError();
    }
    response.setContentType("text/html");
    response.getWriter().print(
        new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(data).toString());
  }

}
