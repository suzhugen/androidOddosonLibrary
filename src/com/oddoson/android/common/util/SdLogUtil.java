package com.oddoson.android.common.util;


/**
 * sd卡日志log类
 * @author oddoson
 *
 */
public class SdLogUtil
{
    public static long logFileMaxSize=SizeUtils.MB_2_BYTE*100;//日志文件 最大100M
    
    public static void d(String file,String log){
        if (FileUtils.getFileSize(file)>logFileMaxSize)
        {
            FileUtils.deleteFile(file);
        }
        StringBuilder content=new StringBuilder();
        content.append("\n\r================ ");
        content.append(DateUtils.getCurrentDate());
        content.append(" ================\n\r");
        content.append(log);
        content.append("\n\r");
        FileUtils.makeFolders(file);
        FileUtils.writeFile(file, content.toString(), true);
    }
    
    public static void d(String log){
        d(SdCardUtils.getSdCardRoot()+OddosonConfig.getSdLogPath()+OddosonConfig.getSdLogFileName(),log);
    }
    
}
