package com.oddoson.android.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期类
 * @author Administrator
 *
 */
public class DataUtils
{
    /**
     * 获取GMT时间,英文日期,E, dd MMM yyyy HH:mm:ss z  , Tue, 14-Aug-2014 11:24:17 GMT;
     * @param date
     * @return   Tue, 14-Aug-2014 11:24:17 GMT;
     */
    public static String toGMTString(Date date) {  
        SimpleDateFormat df = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss z", Locale.UK);  
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));  
        return df.format(date);  
    }  
    
    /**
     * 格式化日期
     * @param date
     * @param format  日期格式："yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getDateString(Date date,String format){
        SimpleDateFormat df = new SimpleDateFormat(format); 
        return df.format(date);
    }
    
    /**
     * 获取当前日期
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDate(){
        return getCurrentDate("yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 
     * @param format  日期格式
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDate(String format){
        Date date=new Date(System.currentTimeMillis());
        return getDateString(date,format);
    }
    
}
