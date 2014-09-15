package com.oddoson.android.common.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * 广播
 * @author oddoson
 *
 */
public class BroadcastUtil
{
    /**
     * 注册广播
     * @param context
     * @param receive
     * @param action
     */
    public static void registerReceiver(Context context,BroadcastReceiver receive,String action){
        IntentFilter filter=new IntentFilter(action);
        context.registerReceiver(receive,filter);
    }
    public static void sendBroadcast(Context context,Intent intent){
        context.sendBroadcast(intent);
    }
    public static void sendBroadcast(Context context,String action){
        Intent intent=new Intent(action);
        context.sendBroadcast(intent);
    }
    public static void sendBroadcast(Context context,String action,Bundle data){
        Intent intent=new Intent(action);
        intent.putExtras(data);
        context.sendBroadcast(intent);
    }
    /**
     * 发送广播，指定包名的程序接收
     * @param context
     * @param packageName
     * @param action
     */
    public static void sendBroadcast(Context context,String packageName,String action){
        Intent intent=new Intent(action);
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }
    
    /**
     * 发送本地广播，本程序才可以接收广播
     * @param context
     * @param intent
     */
    public static void sendLocalBroadcast(Context context,Intent intent){
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.sendBroadcast(intent);
    }
    public static void sendLocalBroadcast(Context context,String action){
        Intent intent=new Intent(action);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.sendBroadcast(intent);
    }
    /**
     * 注册本地广播
     * @param context
     * @param receive
     * @param action
     */
    public static void registerLocalReceiver(Context context,BroadcastReceiver receive,String action){
        IntentFilter filter=new IntentFilter(action);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.registerReceiver(receive,filter);
    }
    /**
     * 注销本地广播
     * @param context
     * @param receive
     * @param action
     */
    public static void unregisterLocalReceiver(Context context,BroadcastReceiver receive){
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.unregisterReceiver(receive);
    }

    
}
