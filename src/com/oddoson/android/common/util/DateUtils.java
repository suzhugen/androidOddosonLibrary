package com.oddoson.android.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日期类
 * 
 * @author oddoson
 * 
 *         <pre>
 *  {@link SimpleDateFormat SimpleDateFormat-格式说明}
 *  E/EE/EEE ,在Locale.CHINA 是中文星期几(星期五)，Locale.UK 是英文 星期几 缩写(Fri)
 *  EEEE ,在Locale.CHINA 是中文星期几(星期五)，Locale.UK 是英文 星期几(Friday)
 * <p><table BORDER="1" WIDTH="100%" CELLPADDING="3" CELLSPACING="0" SUMMARY="">
 * <tr BGCOLOR="#CCCCFF" CLASS="TableHeadingColor">
 *      <td><B>Symbol</B></td> <td><B>Meaning</B></td> <td><B>Kind</B></td> <td><B>Example</B></td> </tr>
 * <tr> <td>{@code D}</td> <td>day in year</td>             <td>(Number)</td>      <td>189</td> </tr>
 * <tr> <td>{@code E}</td> <td>day of week</td>             <td>(Text)</td>        <td>{@code E}/{@code EE}/{@code EEE}:Tue, {@code EEEE}:Tuesday, {@code EEEEE}:T</td> </tr>
 * <tr> <td>{@code F}</td> <td>day of week in month</td>    <td>(Number)</td>      <td>2 <i>(2nd Wed in July)</i></td> </tr>
 * <tr> <td>{@code G}</td> <td>era designator</td>          <td>(Text)</td>        <td>AD</td> </tr>
 * <tr> <td>{@code H}</td> <td>hour in day (0-23)</td>      <td>(Number)</td>      <td>0</td> </tr>
 * <tr> <td>{@code K}</td> <td>hour in am/pm (0-11)</td>    <td>(Number)</td>      <td>0</td> </tr>
 * <tr> <td>{@code L}</td> <td>stand-alone month</td>       <td>(Text)</td>        <td>{@code L}:1 {@code LL}:01 {@code LLL}:Jan {@code LLLL}:January {@code LLLLL}:J</td> </tr>
 * <tr> <td>{@code M}</td> <td>month in year</td>           <td>(Text)</td>        <td>{@code M}:1 {@code MM}:01 {@code MMM}:Jan {@code MMMM}:January {@code MMMMM}:J</td> </tr>
 * <tr> <td>{@code S}</td> <td>fractional seconds</td>      <td>(Number)</td>      <td>978</td> </tr>
 * <tr> <td>{@code W}</td> <td>week in month</td>           <td>(Number)</td>      <td>2</td> </tr>
 * <tr> <td>{@code Z}</td> <td>time zone (RFC 822)</td>     <td>(Time Zone)</td>   <td>{@code Z}/{@code ZZ}/{@code ZZZ}:-0800 {@code ZZZZ}:GMT-08:00 {@code ZZZZZ}:-08:00</td> </tr>
 * <tr> <td>{@code a}</td> <td>am/pm marker</td>            <td>(Text)</td>        <td>PM</td> </tr>
 * <tr> <td>{@code c}</td> <td>stand-alone day of week</td> <td>(Text)</td>        <td>{@code c}/{@code cc}/{@code ccc}:Tue, {@code cccc}:Tuesday, {@code ccccc}:T</td> </tr>
 * <tr> <td>{@code d}</td> <td>day in month</td>            <td>(Number)</td>      <td>10</td> </tr>
 * <tr> <td>{@code h}</td> <td>hour in am/pm (1-12)</td>    <td>(Number)</td>      <td>12</td> </tr>
 * <tr> <td>{@code k}</td> <td>hour in day (1-24)</td>      <td>(Number)</td>      <td>24</td> </tr>
 * <tr> <td>{@code m}</td> <td>minute in hour</td>          <td>(Number)</td>      <td>30</td> </tr>
 * <tr> <td>{@code s}</td> <td>second in minute</td>        <td>(Number)</td>      <td>55</td> </tr>
 * <tr> <td>{@code w}</td> <td>week in year</td>            <td>(Number)</td>      <td>27</td> </tr>
 * <tr> <td>{@code y}</td> <td>year</td>                    <td>(Number)</td>      <td>{@code yy}:10 {@code y}/{@code yyy}/{@code yyyy}:2010</td> </tr>
 * <tr> <td>{@code z}</td> <td>time zone</td>               <td>(Time Zone)</td>   <td>{@code z}/{@code zz}/{@code zzz}:PST {@code zzzz}:Pacific Standard Time</td> </tr>
 * <tr> <td>{@code '}</td> <td>escape for text</td>         <td>(Delimiter)</td>   <td>{@code 'Date='}:Date=</td> </tr>
 * <tr> <td>{@code ''}</td> <td>single quote</td>           <td>(Literal)</td>     <td>{@code 'o''clock'}:o'clock</td> </tr>
 * </table>
 * </pre>
 * 
 */
public class DateUtils
{
    
    /**
     * 格式化日期
     * 
     * @param date
     * @param format
     *            日期格式："yyyy-MM-dd HH:mm:ss"
     * @param date
     * @param format
     * @param locale
     *            时区
     * @return
     */
    public static String getDateString(Date date, String format, Locale locale)
    {
        SimpleDateFormat df = null;
        if (locale == null)
        {
            df = new SimpleDateFormat(format);
        }
        else
        {
            df = new SimpleDateFormat(format, locale);
        }
        return df.format(date);
    }
    
    /**
     * 格式化日期,默认系统时区
     * 
     * @param date
     * @param format
     * @return
     */
    public static String getDateString(Date date, String format)
    {
        return getDateString(date, format, null);
    }
    
    public static String toGMTString(Date date, Locale locale)
    {
        SimpleDateFormat df = null;
        if (locale == null)
        {
            df = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss z");
        }
        else
        {
            df = new SimpleDateFormat("E, dd-MMM-yyyy HH:mm:ss z", locale);
        }
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
    
    /**
     * 获取GMT时间,英文日期,E, dd MMM yyyy HH:mm:ss z , Tue, 14-Aug-2014 11:24:17 GMT;
     * 常用于cookie
     * 
     * @param date
     * @return Tue, 14-Aug-2014 11:24:17 GMT;
     */
    public static String toGMTString_UK(Date date)
    {
        return toGMTString(date, Locale.UK);
    }
    
    /**
     * 中文获取GMT时间，星期五, 29-八月-2014 11:34:03
     * 
     * @param date
     * @return 星期五, 29-八月-2014 11:34:03
     */
    public static String toGMTString_CHINA(Date date)
    {
        return toGMTString(date, Locale.CHINA);
    }
    
    /**
     * 获取星期几
     * 
     * @return
     */
    public static String getWeek(Date date)
    {
        return getDateString(date, "EEEE");
    }
    
    /**
     * 获取当前日期
     * 
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDate()
    {
        Date date = new Date(System.currentTimeMillis());
        return getDateString(date, "yyyy-MM-dd HH:mm:ss");
    }
    
    /**
     * 计算afterDate-beforDate相差的天数
     * @param beforDate
     * @param afterDate
     * @return 返回相差天数，有负数
     */
    public static int diffDays(Date beforDate, Date afterDate)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int diff_day = 0;
        try
        {
            afterDate = df.parse(df.format(afterDate));
            beforDate = df.parse(df.format(beforDate));
            diff_day = (int) ((afterDate.getTime() - beforDate.getTime()) / (1000 * 60 * 60 * 24));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return diff_day;
    }
    
}
