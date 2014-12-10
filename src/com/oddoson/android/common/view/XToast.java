package com.oddoson.android.common.view;

import android.content.Context;
import android.widget.Toast;

public class XToast {
	public static void show(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT);
	}
}
