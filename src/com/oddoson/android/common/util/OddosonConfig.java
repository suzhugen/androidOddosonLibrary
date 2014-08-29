package com.oddoson.android.common.util;

/**
 * 类库配置
 * @author oddoson
 *
 */
public class OddosonConfig
{
    private static boolean debug=true;
    private static String sdLogPath="/oddoson/log/";//sd日志地址
    private static String sdLogFileName="log.txt";//sd日志地址
    
    
    
    
    public static boolean isDebug()
    {
        return debug;
    }

    /**
     * 调试模式开关
     * @param debug
     */
    public static void setDebug(boolean debug)
    {
        OddosonConfig.debug = debug;
    }

    public static String getSdLogPath()
    {
        return sdLogPath;
    }

    public static void setSdLogPath(String sdLogPath)
    {
        OddosonConfig.sdLogPath = sdLogPath;
    }

    public static String getSdLogFileName()
    {
        return sdLogFileName;
    }

    public static void setSdLogFileName(String sdLogFileName)
    {
        OddosonConfig.sdLogFileName = sdLogFileName;
    }
    
    
    
    
    
}
