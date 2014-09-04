package com.oddoson.android.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 系统图片选择器帮助类。
 * @author oddoson
 *
 */
public class PhotoSelectorUtil
{
    /**
     * 打开系统图片选择器
     * 
     * @param requestCode
     *            请求码
     */
    public static void openSelector(Activity context,int requestCode) {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.addCategory(Intent.CATEGORY_OPENABLE);
        getImageIntent.setType("image/*");
        context.startActivityForResult(getImageIntent, requestCode);
    }
    
    /**
     * 根据Uri 获取路径，以兼容4.4系统
     * @param context
     * @param uri
     */
    public static String getFilePaht(Context context,Uri uri){
        return FileUtils.getUriPath(context, uri);
    }
    
    
}
