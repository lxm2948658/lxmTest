package com.qianfan365.jcstore.common.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;

/**
 * 文件上传工具类
 * @author pengbo
 *
 */
public class FileUploadUtils {

  private static Log logger = LogFactory.getLog(FileUploadUtils.class);

  public static String endpoint;
  public static String accessKeyId;
  public static String accessKeySecret;
  public static String bucketName;
  public static String domain;

  
  static OSSClient client = null;
  
  public static void init(){
    
    ClientConfiguration conf = new ClientConfiguration();

    // 设置HTTP最大连接数为10
    conf.setMaxConnections(10);

    // 设置TCP连接超时为5000毫秒
    conf.setConnectionTimeout(200000);

    // 设置最大的重试次数为3
    conf.setMaxErrorRetry(3);

    // 设置Socket传输数据超时的时间为2000毫秒
    conf.setSocketTimeout(200000);

    client = new OSSClient(endpoint, accessKeyId, accessKeySecret, conf);
  }
  
//  public static void main(String[] args) {
//    init();
//    String dir = "E:\\user\\yy";
//    File d = new File(dir);
//    if(d.isDirectory()){
//      File[] files = d.listFiles();
//      for (File file : files) {
//        if(file.isDirectory()){
//          File[] fs = file.listFiles();
//          for (File f : fs) {
//            if(f.isFile()){
//              try {
//                uploadFile(f);
//              } catch (FileNotFoundException e) {
//                System.out.println("ERROR: "+f.getName());
//              }
//            }
//          }
//        }else if(file.isFile()){
//          try {
//            uploadFile(file);
//          } catch (FileNotFoundException e) {
//            System.out.println("ERROR: "+file.getName());
//          }
//        }
//      }
//    }
//  }
  
  public static String uploadFile(File file) throws FileNotFoundException {
    String fileName = file.getName();
    String contentType = getContentTypeByFileType(fileName);
    
    return upload(new FileInputStream(file), fileName, null, file.length(), contentType);
  }

  public static String getContentTypeByFileType(String string) {
    if(string.endsWith(".gif")){
      return "image/gif";
    }else if(string.endsWith(".png")){
      return "image/png";
    }else{
      return "image/jpeg";
    }
    
  }

  /**
   * 文件上传工具方法
   * @param in
   * @param fileName
   * @param path
   * @param fileSize
   * @return
   */
  public static String upload(InputStream in, String fileName, String path, Long fileSize,
      String contentType) {
    // 处理name
    String realName = fileName.substring(0, fileName.lastIndexOf("."));// 最后一个.的位置
    String type = fileName.substring(fileName.lastIndexOf("."));
//    fileName = realName + "_" + new Date().getTime() + type;
    fileName = realName + type;
    if(StringUtils.isBlank(path)){
      path = "jcstore/avatar/";
    }
    
    // 处理path
    if (path.startsWith("/")) {
      // 去掉斜杠
      path = path.substring(1, path.length());
    }
    path = path + fileName;

    // 创建上传Object的Metadata
    ObjectMetadata meta = new ObjectMetadata();
    // 必须设置ContentLength
    meta.setContentLength(fileSize);
    meta.setContentType(contentType);
    // 上传Object
    try {
      client.putObject(bucketName, path, in, meta);
      return domain + path;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return null;
    }

  }
  
  

  public static Map<String, Object> getImageData(MultipartFile file) throws IOException {
    BufferedImage bi = ImageIO.read(file.getInputStream());
    if (bi == null) {
      return null;
    } else {
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("width", bi.getWidth());
      data.put("height", bi.getHeight());
      return data;
    }
  }

  /**
   * 处理图片，重置大小cut=2, 剪裁cut=1, 重置+剪裁cut=3
   * 
   * @param bi
   * @param cut
   * @param zoomW
   * @param zoomH
   * @param x
   * @param y
   * @param width
   * @param height
   * @return
   */
  public static BufferedImage procImage(BufferedImage bi, int cut, Integer zoomW, Integer zoomH,
      Integer x, Integer y, Integer width, Integer height) {
    if ((cut & 2) > 0) {
      if (bi.getWidth() > zoomW || bi.getHeight() > zoomH) {
        int type = bi.getColorModel().getTransparency();
        // 计算尺寸
        float r = (float) ((bi.getWidth() * 1.0) / (bi.getHeight() * 1.0));
        if (r > 1) {
          zoomH = Math.round(zoomH / r);
        } else {
          zoomW = Math.round(zoomW * r);
        }

        BufferedImage image = new BufferedImage(zoomW, zoomH, type);

        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        graphics2D.drawImage(bi, 0, 0, zoomW, zoomH, 0, 0, bi.getWidth(), bi.getHeight(), null);
        bi = image;
      }
    }
    if ((cut & 1) > 0) {
      bi = bi.getSubimage(x, y, width, height);
    }
    return bi;
  }
}
