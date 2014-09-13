package com.oddoson.android.common.util;

import android.app.Activity;
import android.view.WindowManager;

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
    
}
