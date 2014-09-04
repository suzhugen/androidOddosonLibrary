package com.oddoson.android.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oddoson.android.common.security.MD5;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.util.DisplayMetrics;

public class AppsUtils
{
    
    /**
     * 获取当前APP 版本号
     * 
     * @param context
     * @return 1001
     */
    public static int getVersionCode(Context context)
    {
        int verCode = 1;
        try
        {
            String packgeName = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(packgeName, 0).versionCode;
        }
        catch (NameNotFoundException e)
        {
        }
        return verCode;
    }
    
    /**
     * 获取当前APP 版本名称
     * 
     * @param context
     * @return 1.0.1.36
     */
    public static String getVersionName(Context context)
    {
        String verName = "";
        try
        {
            String packgeName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(packgeName, 0).versionName;
        }
        catch (NameNotFoundException e)
        {
        }
        return verName;
    }
    
    /**
     * 获取本程序信息
     * 
     * @param context
     * @return if the package is not found in the list of installed applications
     *         ,return null
     */
    public static PackageInfo getMyApkInfo(Context context)
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo apkInfo = null;
        try
        {
            apkInfo = pm.getPackageInfo(context.getPackageName(), 0);
        }
        catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return apkInfo;
    }
    
    /**
     * 获取未安装的APK安装包信息
     * 
     * @param context
     * @param archiveFilePath
     *            APK文件的路径。如：/sdcard/download/XX.apk
     */
    public static PackageInfo getApkInfo(Context context, String archiveFilePath)
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo apkInfo = pm.getPackageArchiveInfo(archiveFilePath,
                PackageManager.GET_META_DATA);
        return apkInfo;
    }
    
    /**
     * 获取所有已安装程序的图标信息 读取图标
     * imageview.setImageDrawable(info.activityInfo.loadIcon(
     * getPackageManager()));
     * 
     * @param context
     * @return
     */
    public static List<ResolveInfo> loadApps(Context context)
    {
        List<ResolveInfo> mApps;
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = context.getPackageManager()
                .queryIntentActivities(mainIntent, 0);
        return mApps;
    }
    
    /**
     * 获取所有已安装app信息：字段：ico,appName,packageName
     * 
     * @param context
     * @return
     */
    public ArrayList<HashMap<String, Object>> getAllApp(Context context)
    {
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        PackageManager pm = context.getPackageManager();
        // 得到系统安装的所有程序包的PackageInfo对象
        List<PackageInfo> packs = pm.getInstalledPackages(0);
        for (PackageInfo pi : packs)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("icon", pi.applicationInfo.loadIcon(pm));// 图标
            map.put("appName", pi.applicationInfo.loadLabel(pm));// 应用程序名称
            map.put("packageName", pi.applicationInfo.packageName);// 应用程序包名
            // 循环读取并存到HashMap中，再增加到ArrayList上，一个HashMap就是一项
            items.add(map);
        }
        return items;
    }
    
    /**
     * 获取所有已安装app
     * 
     * @param context
     * @return
     */
    public List<PackageInfo> getAllInstalledApp(Context context)
    {
        PackageManager pckMan = context.getPackageManager();
        return pckMan.getInstalledPackages(0);
    }
    
    /**
     * 判断是否已安装某一个程序
     * 
     * @param context
     * @param appPackageName
     *            包名
     * @return
     */
    public static Boolean isInstalled(Context context, String appPackageName)
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;
        try
        {
            pInfo = pm.getPackageInfo(appPackageName, 0);
        }
        catch (NameNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (pInfo != null)
        {
            return true;
        }
        return false;
    }
    
    /**
     * 开启其他程序
     * 
     * @param context
     * @param appPackageName
     * @return
     */
    public static Boolean startApp(Context context, String appPackageName)
    {
        Intent intent = new Intent();
        intent = context.getPackageManager().getLaunchIntentForPackage(
                appPackageName);
        if (intent == null)
        {
            return false;
        }
        context.startActivity(intent);
        return true;
    }
    
    /***
     * 根据包名获取已安装的 apk 签名值。
     * 
     * @param context
     * @param packageName 已安装的包名
     * @return
     */
    public static byte[] getApkSign(Context context, String packageName)
    {
        byte[] arrayOfByte = null;
        try
        {
            PackageInfo localPackageInfo = context.getPackageManager()
                    .getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            arrayOfByte = localPackageInfo.signatures[0].toByteArray();
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
            localNameNotFoundException.printStackTrace();
        }
        return arrayOfByte;
    }
    
    /**
     * 根据包名获取已安装的 apk 签名值的MD5值。
     * @param context
     * @param packageName 已安装的包名
     * @return  MD5值
     */
    public static String getApkSign_Md5(Context context, String packageName){
        byte[] arrayOfByte=AppsUtils.getApkSign(context, packageName);
        if (arrayOfByte==null)
        {
            return null;
        }
        return MD5.getMD5(arrayOfByte,false);
    }
    
    
}
