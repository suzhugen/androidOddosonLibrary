package com.oddoson.android.common.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 获取手机 系统 信息
 * @author zkyc
 *
 */
public class SystemInfoUtils {

	public static String getIMEI(Context context){
		TelephonyManager tManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getDeviceId();
	}
	public static String getIMSI(Context context){
		TelephonyManager tManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getSubscriberId();
	}
	/**
	 * 手机型号
	 * @return
	 */
	public static String getType(){		
		return android.os.Build.MODEL;
	}
	
	/**
	 * 系统版本
	 * @return
	 */
	public static String getSystemVer(){
		return android.os.Build.VERSION.RELEASE;
	}
	
	/**
	 * 品牌
	 * @param context
	 * @return
	 */
	public static String getBrand(){		
		return android.os.Build.MANUFACTURER;
	}
	
	
}
