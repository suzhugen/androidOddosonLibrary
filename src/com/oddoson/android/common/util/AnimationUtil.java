package com.oddoson.android.common.util;

import android.view.animation.AlphaAnimation;

/**
 * 动画类
 * @author su
 *
 */
public class AnimationUtil extends android.view.animation.AnimationUtils {


	/**
	 *  渐变动画 
	 *  如在imageview渐变显示 中imageView.startAnimation(getAlphaAnimation(0,1,2000));
	 * @param fromAlpha
	 * @param endAlpha
	 * @param durationMillis  持续时间
	 * @return
	 */
    public static AlphaAnimation getAlphaAnimation(Float fromAlpha,Float endAlpha, long durationMillis) {
        AlphaAnimation inAlphaAnimation = new AlphaAnimation(fromAlpha, endAlpha);
        inAlphaAnimation.setDuration(durationMillis);
        return inAlphaAnimation;
    }
    
    
}
