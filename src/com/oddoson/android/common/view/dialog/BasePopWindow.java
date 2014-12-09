package com.oddoson.android.common.view.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class BasePopWindow extends PopupWindow
{
    public BasePopWindow(Context context){
        init();
    }
    public BasePopWindow(Context context,int width,int height){
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
