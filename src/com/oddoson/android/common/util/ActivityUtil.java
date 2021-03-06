package com.oddoson.android.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
/**
 * 输入法控制
 * 启动类
 * @author Administrator
 *
 */
public class ActivityUtil
{
    /**
     * 禁止自动弹出输入法
     * 
     * @param context
     */
    public static void noAutoSoftInput(Activity context)
    {
        // 禁止自动弹出输入法
        context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    
    /**
     * 输入法键盘弹出时不自动向上挤压activity
     * 
     * @param context
     */
    public static void setSoftInputAdjustPan(Activity context)
    {
        // 禁止自动弹出输入法
        context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    
    /**
     * 根据类名启动activity 或其他程序
     * @param context
     * @param packName
     * @param className com.example.demo.MainActivity 完整的类名
     * @return
     */
    public static Boolean startActivityByClass(Context context,String packName,String className,Bundle bundle){
        Intent mIntent = new Intent();
        mIntent.setClassName(packName, className);
        if (bundle!=null) {
			mIntent.putExtras(bundle);
		}
        try
        {
            context.startActivity(mIntent);
            return true;
        }
        catch (Exception wException)
        {
            LogUtil.d(className+" ,the activity has not been found .");
            return false;
        }
    }
    /**
     * 根据类名启动activity
     * @param context
     * @param className com.example.demo.MainActivity 完整的类名
     * @return
     */
    public static Boolean startActivityByClass(Context context,String className){
        return startActivityByClass(context, context.getPackageName(), className,null);
    }   
    
}
