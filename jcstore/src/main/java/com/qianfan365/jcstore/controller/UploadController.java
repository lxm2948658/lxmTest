package com.qianfan365.jcstore.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baidu.ueditor.ActionEnter;
import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.util.FileUploadUtils;

@Controller
public class UploadController extends BaseController{

  public static final String CONTENT_TYPE = "application/octet-stream";
  
  @Value("${common.domain-url}")
  String domainUrl;
  @Value("${common.file.maxsize}")
  long maxSize;

  /**
   * 上传
   * @param request
   * @param type
   * @return
   * @throws IOException
   */
  @RequestMapping(path = "/upload")
  @ResponseBody
  public ResultData upload(MultipartHttpServletRequest request, Integer type) throws IOException {
    ResultData success = ResultData.build();
    MultipartFile file = request.getFile("__avatar1");

    String path = "/jcstore/talkart/";

    String originalFilename = file.getOriginalFilename();
    String filetype = "";
    if(originalFilename.lastIndexOf(".")>0){
      filetype=originalFilename.substring(originalFilename.lastIndexOf("."));
    }
    originalFilename = System.currentTimeMillis()+"_"+new Random().nextInt(89999999)+filetype;
    if (originalFilename.indexOf(".") < 0) {
      if(type==1){
        originalFilename = originalFilename + ".png";
      }else{
        originalFilename = originalFilename + ".mp3";
      }
    }
    Long fileSize = file.getSize();
    
    // 上传图片
    path =
        FileUploadUtils
            .upload(file.getInputStream(), originalFilename, path, fileSize, file.getContentType());
    
    if(StringUtils.isBlank(path)){
      return success.failure();
    }
    
    if (StringUtils.isBlank(path)) {
      return success.failure();
    } else {
      return success.put("path", path);
    }
  }
  
  /**
   * 上传图片,处理图片
   * 
   * @param request
   * @param path
   * @param cut
   * @param zoomW
   * @param zoomH
   * @param x
   * @param y
   * @param width
   * @param height
   * @return
   * @throws IOException
   */
  @ResponseBody
  @RequestMapping("/uploadFile")
  public Map<String, Object> upload(MultipartHttpServletRequest request, String path,
      @RequestParam(defaultValue = "0") int cut, @RequestParam(defaultValue = "0") int isavatar,
      @RequestParam(defaultValue = "320") Integer zoomW,
      @RequestParam(defaultValue = "320") Integer zoomH, Integer x, Integer y, Integer width,
      Integer height) throws IOException {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", "0");
    MultipartFile file = null;
    String originalFilename = "defalt.jpg";
    if (StringUtils.isNotBlank(path)) {
      path = URLDecoder.decode(path, "UTF-8");
    }
    if (isavatar > 0) {
      file = request.getFile("__avatar1");
      originalFilename = file.getOriginalFilename() + ".jpg";
      path = "/jcstore/avatar/"+ System.currentTimeMillis()+"/";;
    } else {
      file = request.getFile("file");
      originalFilename = file.getOriginalFilename();
    }
    InputStream inputStream = file.getInputStream();
    BufferedImage bi = ImageIO.read(inputStream);
    if (bi == null) {
      map.put("status", "0");
      return map;
    }
    // 处理图片
    bi = FileUploadUtils.procImage(bi, cut, zoomW, zoomH, x, y, width, height);

    // 取图片类型
    Iterator<ImageReader> iterator = ImageIO.getImageReaders(bi);
    String type = null;
    if (iterator.hasNext()) {
      ImageReader reader = iterator.next();
      type = reader.getFormatName().toUpperCase();
    }
    if (StringUtils.isBlank(type)) {
      type = "JPG";
    }
    // 输出流
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageIO.write(bi, type, output);

    inputStream = new ByteArrayInputStream(output.toByteArray());
    Long fileSize = new Long(output.size());
    // 上传图片
    path =
        FileUploadUtils
            .upload(inputStream, originalFilename, path, fileSize, file.getContentType());
    if (path == null) {
      map.put("status", "0");
    } else {
      map.put("status", "1");
      map.put("path", path);
      if (path.lastIndexOf("/") > 0 && path.lastIndexOf(".") > 0) {
        String name = path.substring(path.lastIndexOf("/") + 1);
        if (StringUtils.isNotBlank(name)) {
          map.put("name", name);
        }
      }
      map.put("size", fileSize);
    }
    if (bi != null) {
      map.put("width", bi.getWidth());
      map.put("height", bi.getHeight());
    }

    return map;
  }

  /**
   * 上传图片,处理图片
   * 
   * @param request
   * @throws IOException
   */
  @ResponseBody
  @RequestMapping({"/upload/avatars","/mobile/upload/avatars"})
  public Object uploadavatars(HttpServletResponse respnse, MultipartHttpServletRequest request,
      String type, @RequestParam(defaultValue = "0") Integer withWH,
      @RequestParam(defaultValue = "0") Integer width,
      @RequestParam(defaultValue = "0") Integer height) throws IOException {

    List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
    List<MultipartFile> files = request.getFiles("__avatar1");
    String path = "/jcstore/avatar/";
    //js中取不到响应状态，所以注释掉
    //注释掉可能在ie中会下载json文件
//    respnse.setContentType("text/html;charset=UTF-8");
    for (MultipartFile file : files) {
      path = "/jcstore/avatar/"+ System.currentTimeMillis()+"/";
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("status", 0);
      InputStream inputStream = file.getInputStream();
      String originalFilename = new Random().nextInt(89999999) + "";
      if (originalFilename.indexOf(".") < 0) {
        originalFilename = originalFilename + ".png";
      }
      Long fileSize = file.getSize();
      if (fileSize > maxSize) {
        //单张图片太大不进行上传
        path = null;
      } else {
        // 上传图片
        path =
            FileUploadUtils.upload(inputStream, originalFilename, path, fileSize,
                file.getContentType());
      }
      if (path == null) {
        map.put("status", "0");
        map.put("success", false);
        map.put("statusMsg", "上传失败，单张图片请勿超过2M");
      } else {
        map.put("success", true);
        map.put("status", "1");
        map.put("path", path);
        if (path.lastIndexOf("/") > 0 && path.lastIndexOf(".") > 0) {
          String name = path.substring(path.lastIndexOf("/") + 1);
          if (StringUtils.isNotBlank(name)) {
            map.put("name", name);
          }
        }
        map.put("size", fileSize);
      }
      if (withWH == 1) {
        map.put("width", width);
        map.put("height", height);
      }
      ret.add(map);
    }
    return ret;
  }

  /**
   * 上传图片,处理图片
   * 
   * @param request
   * @throws IOException
   */
  @ResponseBody
  @RequestMapping("/upload/avatar")
  public Map<String, Object> uploadavatar(MultipartHttpServletRequest request, String type,
      @RequestParam(defaultValue = "0") Integer withWH,
      @RequestParam(defaultValue = "0") Integer width,
      @RequestParam(defaultValue = "0") Integer height) throws IOException {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", 0);
    MultipartFile file = request.getFile("__avatar1");

    String path = "/jcstore/avatar/"+ System.currentTimeMillis()+"/";

    InputStream inputStream = file.getInputStream();
    String originalFilename = new Random().nextInt(89999999) + "";
    if (originalFilename.indexOf(".") < 0) {
      originalFilename = originalFilename + ".png";
    }
    Long fileSize = file.getSize();
    // 上传图片
    path =
        FileUploadUtils
            .upload(inputStream, originalFilename, path, fileSize, file.getContentType());

    if (path == null) {
      map.put("status", "0");
      map.put("success", false);
    } else {
      map.put("success", true);
      map.put("status", "1");
      map.put("path", path);
      if (path.lastIndexOf("/") > 0 && path.lastIndexOf(".") > 0) {
        String name = path.substring(path.lastIndexOf("/") + 1);
        if (StringUtils.isNotBlank(name)) {
          map.put("name", name);
        }
      }
      map.put("size", fileSize);
    }
    if (withWH == 1) {
      map.put("width", width);
      map.put("height", height);
    }
    return map;
  }

  /**
   * 
   * @param request
   * @return
   * @throws IOException
   */
  @ResponseBody
  @RequestMapping("/uploadlocal")
  public Map<String, Object> uploadLocal(HttpSession session, MultipartHttpServletRequest request,
      int cut, @RequestParam(defaultValue = "320") Integer zoomW,
      @RequestParam(defaultValue = "320") Integer zoomH) throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("status", "0");
    MultipartFile file = request.getFile("file");

    InputStream inputStream = file.getInputStream();
    BufferedImage bi = ImageIO.read(inputStream);
    if (bi == null) {
      map.put("status", "0");
      return map;
    }
    // 取图片类型
    Iterator<ImageReader> iterator = ImageIO.getImageReaders(bi);
    String type = null;
    if (iterator.hasNext()) {
      ImageReader reader = iterator.next();
      type = reader.getFormatName().toUpperCase();
    }
    if (StringUtils.isBlank(type)) {
      type = "JPG";
    }
    // 处理图片
    bi = FileUploadUtils.procImage(bi, cut, zoomW, zoomH, null, null, null, null);

    String rootPath =
        Thread.currentThread().getContextClassLoader()
            .getResource("../../assets/show/images/temp/").getPath();
    String filename = new Date().getTime() + "-" + file.getOriginalFilename();
    File tempfile = new File(rootPath + filename);

    FileOutputStream outputStream = new FileOutputStream(tempfile);
    ImageIO.write(bi, type, outputStream);

    map.put("status", "1");
    map.put("path", domainUrl + "/assets/show/images/temp/" + filename);

    if (bi != null) {
      map.put("width", bi.getWidth());
      map.put("height", bi.getHeight());
    }

    return map;
  }


  @ResponseBody
  @RequestMapping("/uploadEditorFile")
  public Map<String, Object> uploadEditorFile(MultipartHttpServletRequest request)
      throws IOException {
    Map<String, Object> map = new HashMap<String, Object>();
    MultipartFile file = request.getFile("file");
    String path = "/jcstore/editor/image/"+ System.currentTimeMillis()+"/";
    String path2 =
        FileUploadUtils.upload(file.getInputStream(), file.getOriginalFilename(), path,
            file.getSize(), file.getContentType());

    if (path2 == null) {
      map.put("state", "FAIL");
    } else {
      String fileName = file.getOriginalFilename();
      String type = fileName.substring(fileName.lastIndexOf("."));

      map.put("state", "SUCCESS");
      map.put("url", path2);
      map.put("title", fileName);
      map.put("type", type);
      map.put("original", file.getOriginalFilename());
      map.put("size", file.getSize());
    }
    return map;
  }

  @ResponseBody
  @RequestMapping("/uedit-config")
  public  String cofig(HttpServletRequest request){
    return  new ActionEnter( request,  request.getSession().getServletContext().getRealPath("/") ).exec();
  }

}
