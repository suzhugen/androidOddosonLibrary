package com.oddoson.android.common.util;

import java.text.DecimalFormat;

/**
 * 格式化 数字
 * @author su
 *
 */
public class FormatUtil
{
    public static String decimalFormat(double value,String format){
        DecimalFormat dformat=new DecimalFormat(format);
        return dformat.format(value);
    }
    /**
     * 格式 ###.0
     * @param value
     * @return
     */
    public static String decimalFormat(double value){
        DecimalFormat dformat=new DecimalFormat("###.0");
        return dformat.format(value);
    }
    
    public static String longFormat(long value,String format){
        DecimalFormat dformat=new DecimalFormat(format);
        return dformat.format(value);
    }
    /**
     * 格式 ###.0
     * @param value
     * @return
     */
    public static String longFormat(long value){
        DecimalFormat dformat=new DecimalFormat("###.0");
        return dformat.format(value);
    }
    
}
