package com.oddoson.android.common.util;

import android.content.Context;

/**
 *  分辨率转换
 * @author su
 * @date 2013-3-7
 *
 */
public class DensityUtil {
	

	
	/**
	 * 根据手机分辨率 从dp换算成px
	 * @param context 
	 * @param dpValue 要换算的dp值
	 * @return
	 */
	public static int dipToPx(Context context,float dpValue) {
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(dpValue*scale+0.5f);		
	}

	/**
	 * 根据手机分辨率 从px换算成dp
	 * @param context
	 * @param pxValue 
	 * @return
	 */
	public static int pxToDip(Context context,float pxValue) {
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(pxValue/scale+0.5f);		
	}
	
	 /** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int pxToSp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
    
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int spToPx(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }
	
}
