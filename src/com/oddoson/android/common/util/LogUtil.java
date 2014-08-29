package com.oddoson.android.common.util;

import android.util.Log;

public class LogUtil {
		public static String tag="---------LogUtil---------"; 
	
		public static void e(String tag,String msg){
			Log.e(tag, msg);
		}
				
		public static void e(String msg){
		    if (OddosonConfig.isDebug())
            {
		        e(tag, msg);
            }
		}
		
		public static void e(Exception ex){
			e(tag, ex.toString());
		}
		
		public static void d(String tag,String msg){
		    if (OddosonConfig.isDebug())
            {
		        Log.d(tag, msg);
            }
		}
		public static void d(String msg){
			d(tag, msg);
		}
		
		public static void i(String tag,String msg){
		    if (OddosonConfig.isDebug())
            {
		        Log.i(tag, msg);
            }
		}
		public static void i(String msg){
			i(tag, msg);
		}
		
}
