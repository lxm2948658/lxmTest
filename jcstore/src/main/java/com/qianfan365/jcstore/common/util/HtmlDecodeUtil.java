package com.qianfan365.jcstore.common.util;

public class HtmlDecodeUtil {
  /**
   * 把html转义过的字符串反转义
   * 
   * @param s
   * @return
   */
  public static String htmlDecode(String s) {
    if (s == null) {
      return null;
    }
    return s.replaceAll("&quot;", "\"");
  }

  public static String htmlDecode1(String s) {
    if (s == null) {
      return null;
    }
    return s.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&")
        .replaceAll("&quot;", "\"");
  }
}
