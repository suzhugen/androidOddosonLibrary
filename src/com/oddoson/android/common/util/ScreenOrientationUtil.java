package com.oddoson.android.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

/**
 * 屏幕方向切换管理
 * 
 * @author oddoson
 * 
 *  <pre>
 *  代码切换不会导致activity重建
 *         ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
 *         ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,//强制横屏
 *         ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, //强制竖屏
 *         ActivityInfo.SCREEN_ORIENTATION_USER, //根据用户配置
 *         ActivityInfo.SCREEN_ORIENTATION_BEHIND, //
 *         ActivityInfo.SCREEN_ORIENTATION_SENSOR, //根据感应器自动转换,3个方向
 *         ActivityInfo.SCREEN_ORIENTATION_NOSENSOR,//屏蔽感应器
 *         ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, //只接受 横屏感应器
 *         ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT,  //只接受 竖屏感应器
 *         ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, //强制反方向横屏
 *         ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT,//强制竖屏
 *         ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR, //接受 4个方向感应器
 * 
 * </pre>
 * 
 * 
 */
public class ScreenOrientationUtil
{
    /**
     * 更改用户设置，打开或关闭自动旋转
     * @param context
     * @param openSensor
     */
    public static void setUserConfig(Context context,Boolean openSensor){
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, openSensor?1:0);
    }
    /**
     * 获取用户自动旋转配置
     * @param context
     * @return true=打开
     */
    public static Boolean getUserConfig(Context context){
        int r=0;
        try
        {
            r=Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        }
        catch (SettingNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r==1;
    }
    
    /**
     * 切换
     * @param context
     * @param type
     */
    public static void changeOrientation(Context context,int type)
    {
        ((Activity)context).setRequestedOrientation(type);
    }
    /**
     * 3个方向感应器自动转换
     * @param context
     */
    public static void setSensor(Context context){
        changeOrientation(context,ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }
    
    /**
     * 4个方向感应器自动转换
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void setFullSensor(Context context){
        changeOrientation(context,ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }
    
    /**
     * 强制横屏
     * @param context
     */
    public static void setLandscape(Context context){
        changeOrientation(context,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    /**
     * 强制竖屏
     * @param context
     */
    public static void setPortrait(Context context){
        changeOrientation(context,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    /**
     * 根据用户的系统配置
     * @param context
     */
    public static void setUser(Context context){
        changeOrientation(context,ActivityInfo.SCREEN_ORIENTATION_USER);
    }
    /**
     * 只接受横屏传感器
     * @param context
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static void setOnlySensorLandscape(Context context){
        changeOrientation(context,ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }
    /**
     * 屏蔽传感器
     * @param context
     */
    public static void setNoSensor(Context context){
        changeOrientation(context,ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
    }
    
}
