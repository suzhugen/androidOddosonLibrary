package com.oddoson.android.common.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 屏幕工具
 * @author Administrator
 *
 */
public class ScreenUtil {

	/**
	 * 返回屏幕尺寸
	 * 
	 * @param context
	 * @return DisplayMetrics
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		return context.getResources().getDisplayMetrics();
	}
	
	/**
	 * 返回宽
	 * 
	 * @param context
	 * @return int
	 */
	public static int getScreenWidth(Context context) {
		return getDisplayMetrics(context).widthPixels;
	}

	/**
	 * 返回高
	 * 
	 * @param context
	 * @return int
	 */
	public static int getScreenHeight(Context context) {
		return getDisplayMetrics(context).heightPixels;
	}
	
	/**
	 * 判断是否横屏
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isOrientationLandscape(Context context) {
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是否竖屏
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isOrientationPortrait(Context context) {
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			return true;
		}
		return false;
	}
	
}
