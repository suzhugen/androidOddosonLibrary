package com.oddoson.android.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式
 * @author oddoson
 *
 */
public class Regular
{
    
    
    /**
     * 替换字符
     * @param src  源字符串
     * @param replace  替换的字符
     * @param pattern  要匹配的字符串
     * @return
     */
    public  static String matcher(String src,String replace,String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(src);
        return matcher.replaceAll(replace);
    }
    
    /**
     * 是否有匹配
     * @param src
     * @param replace
     * @param pattern
     * @return
     */
    public  static Boolean isFind(String src,String replace,String pattern){
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(src);
        return matcher.find();
    }
    
    /**
     * 去除中文字符,
     * @param src 要过滤的字符
     * @param replace 替换字符
     * @return 过滤后的字符
     */
    public  static String filterChinese(String src,String replace){
        String reg = "[\u4e00-\u9fa5]";
        return matcher(src,replace,reg);
    }
    
    /**
     * 去除中文字符
     * @param src
     * @return
     */
    public  static String filterChinese(String src){
        return filterChinese(src, "");
    }
    
  
    
    
}
