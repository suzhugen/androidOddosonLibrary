package com.oddoson.android.common.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

/**
 * 亮度调节工具
 */
public class LightHelper {
	/**
	 * 判断是否开启了自动亮度调节
	 * 
	 * @param aContext
	 * @return
	 */
	public static boolean isAutoBrightness(ContentResolver aContentResolver) {
		boolean automicBrightness = false;
		try {
			automicBrightness = Settings.System.getInt(aContentResolver,
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return automicBrightness;
	}

	/**
	 * 获取屏幕的亮度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenBrightness(Activity activity) {
		int nowBrightnessValue = 0;
		ContentResolver resolver = activity.getContentResolver();
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	/**
	 * 设置亮度
	 * 
	 * @param activity
	 * @param brightness
	 */
	public static void setBrightness(Activity activity, int brightness) {
		// Settings.System.putInt(activity.getContentResolver(),
		// Settings.System.SCREEN_BRIGHTNESS_MODE,
		// Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		if (brightness == 0) {
			brightness = 1;
		}
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
		activity.getWindow().setAttributes(lp);

	}

	/****
	 * 设置亮度(用于手指滑动调节亮度)
	 * 
	 * @param activity
	 * @param brightness
	 * */
	public static void setBrightness(Activity activity, float brightness) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
		if (lp.screenBrightness > 1) {
			lp.screenBrightness = 1;

		} else if (lp.screenBrightness < 0.2) {
			lp.screenBrightness = (float) 0.2;
		}

		activity.getWindow().setAttributes(lp);
	}

	/**
	 * 停止自动亮度调节
	 * 
	 * @param activity
	 */
	public static void stopAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 * 开启亮度自动调节
	 * 
	 * @param activity
	 */
	public static void startAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}

	/**
	 * 保存亮度设置状态
	 * 
	 * @param resolver
	 * @param brightness
	 */
	public static void saveBrightness(ContentResolver resolver, int brightness) {
		Uri uri = android.provider.Settings.System
				.getUriFor("screen_brightness");
		android.provider.Settings.System.putInt(resolver, "screen_brightness",
				brightness);
		// resolver.registerContentObserver(uri, true, myContentObserver);
		resolver.notifyChange(uri, null);
	}
	

	public static int getCartoonPlayerBrightness(Activity activity) {
		int brightness = 0;
		String brightnessStr ="";// F.loadByKey("brightness", "LightHelper");
		boolean hasSetValue = false;
		try {
			if (brightnessStr != null && !"".equals(brightnessStr)) {
				brightness = Integer.parseInt(brightnessStr);
				hasSetValue = true;
			}
		} catch (Exception e) {
			
		}
		if (!hasSetValue) {
			brightness = getScreenBrightness(activity);
		}
		return brightness;
	}
	
	public static int getVideoPlayerBrightness(Activity activity) {
		int brightness = 0;
/*		//String brightnessStr = F.loadByKey("videoBrightness", "LightHelper");
		boolean hasSetValue = false;
		try {
			if (brightnessStr != null && !"".equals(brightnessStr)) {
				brightness = Integer.parseInt(brightnessStr);
				hasSetValue = true;
			}
		} catch (Exception e) {
		}
		if (!hasSetValue) {
			brightness = getScreenBrightness(activity);
		}*/
		return brightness;
	}

}
