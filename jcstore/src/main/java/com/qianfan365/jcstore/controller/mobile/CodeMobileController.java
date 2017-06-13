package com.qianfan365.jcstore.controller.mobile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qianfan365.jcstore.common.bean.ResultData;
import com.qianfan365.jcstore.common.bean.ResultData.Status;
import com.qianfan365.jcstore.common.constant.SessionConstant;

@Controller
@RequestMapping("/mobile")
public class CodeMobileController {

  private int width = 164;// 定义图片的width
  private int height = 86;// 定义图片的height
  private int codeCount = 4;// 定义图片上显示验证码的个数
  private int xx = 35;
  private int fontHeight = 60;
  private int codeY = 73;
  char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R',
          'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};
  public final static Color borderColor     = new Color(96, 96, 96);
  @RequestMapping("/imagecode")
  public void getCode(String type, HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    // 定义图像buffer
    BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // Graphics2D gd = buffImg.createGraphics();
     Graphics2D gd = (Graphics2D) buffImg.getGraphics();
//    Graphics gd = buffImg.getGraphics();
    // 创建一个随机数生成器类
    Random random = new Random();
    // 将图像填充为白色
    gd.setColor(Color.WHITE);
    gd.fillRect(0, 0, width, height);

    // 创建字体，字体的大小应该根据图片的高度来定。
    Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
    // 设置字体。
    gd.setFont(font);

    // 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
    gd.setColor(Color.BLACK);
    for (int i = 0; i < 5; i++) {
      int x = random.nextInt(width);
      int y = random.nextInt(height);
      int xl = random.nextInt(24);
      int yl = random.nextInt(24);
      gd.drawLine(x, y, x + xl, y + yl);
    }

    // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
    StringBuffer randomCode = new StringBuffer();
    int red = 0, green = 0, blue = 0;

    // 随机产生codeCount数字的验证码。
    for (int i = 0; i < codeCount; i++) {
      // 得到随机产生的验证码数字。
      String code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
      // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
      red = random.nextInt(241);
      green = random.nextInt(241);
      blue = random.nextInt(241);

      // 用随机产生的颜色将验证码绘制到图像中。
      gd.setColor(new Color(red, green, blue));
      gd.drawString(code, (i) * xx+8, codeY-new Random().nextInt(15));

      // 将产生的四个随机数组合在一起。
      randomCode.append(code);
    }
    Stroke defaultStroke = gd.getStroke();
    // 画边框。
    gd.setColor(borderColor);
    gd.setStroke(new BasicStroke(10.0f));
    gd.drawRect(0, 0, width - 1, height - 1);
    gd.setStroke(defaultStroke);

    // 将四位数字的验证码保存到Session中。
    HttpSession session = req.getSession();
    session.setAttribute(SessionConstant.AUTH_CODE, randomCode.toString());

    // 禁止图像缓存。
    resp.setHeader("Pragma", "no-cache");
    resp.setHeader("Cache-Control", "no-cache");
    resp.setDateHeader("Expires", 0);

    resp.setContentType("image/jpeg");

    // 将图像输出到Servlet输出流中。
    ServletOutputStream sos = resp.getOutputStream();
    ImageIO.write(buffImg, "jpeg", sos);
    sos.close();
  }
  
  @ResponseBody
  @RequestMapping(method = RequestMethod.POST,value = "/checkCode")
  public ResultData checkCode(String code,HttpSession session){
    ResultData resultData = ResultData.build();
    if(StringUtils.isEmpty(code)){
      return resultData.parameterError();
    }
    // 验证码正确
    String authCode = (String) session.getAttribute(SessionConstant.AUTH_CODE);
    if(!code.equalsIgnoreCase(authCode)){
      return resultData.setStatus(Status.VERIFY_CODE_FAIL);
    }
    session.removeAttribute(SessionConstant.AUTH_CODE);
    session.setAttribute(SessionConstant.AUTH_CODE_FLAG, true);
    return ResultData.build().success();
  }
}
