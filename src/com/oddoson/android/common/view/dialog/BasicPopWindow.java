package com.oddoson.android.common.view.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class BasicPopWindow extends PopupWindow
{
    public BasicPopWindow(Context context){
        init();
    }
    public BasicPopWindow(Context context,int width,int height){
        init();
        setWidth(width);
        setHeight(height);
    }
    private void init(){
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);//获取焦点，很重要
    }
}
