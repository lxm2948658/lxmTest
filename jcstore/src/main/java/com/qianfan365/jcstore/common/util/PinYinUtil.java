package com.qianfan365.jcstore.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang.StringUtils;

/**
 * Created by SZZ on 2017/4/5.
 */
public class PinYinUtil {

    /**
     * 获取拼音首字母
     * @param name
     * @return
     */
    public static String getInitial(String name) {
        String regex = "[A-Za-z]";
        if (StringUtils.isNotEmpty(name)){
            if (!String.valueOf(name.charAt(0)).matches(regex)) {
                String[] strings = PinyinHelper.toHanyuPinyinStringArray(name.charAt(0));
                Character c = '#';
                if(strings != null && StringUtils.isNotEmpty(strings[0])) c = Character.toUpperCase(strings[0].charAt(0));
                return c.toString();
            }
            return String.valueOf(name.toUpperCase().charAt(0));
        }
        return "#";
    }

}
