package com.oddoson.android.common.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oddoson.android.common.R;
import com.oddoson.android.common.interfaces.LoadingMaskLayerCallback;

/**
 * 加载遮罩层，加载失败可刷新
 * 
 * @author oddoson
 * 
 */
public class LoadingMaskLayer extends FrameLayout
{
    private LoadingMaskLayerCallback callback;
    private Context mContext;
    private LinearLayout loading_lly, refresh_lly;
    private TextView loading_tv, error_msg_tv, error_title_tv;
    private Button refresh_button;
    
    public LoadingMaskLayer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        init();
    }
    
    public void setCallback(LoadingMaskLayerCallback callback)
    {
        this.callback = callback;
    }
    
    void init()
    {
        View view = ((Activity) mContext).getLayoutInflater().inflate(
                R.layout.loadingmasklayer_main, null);
        loading_lly = (LinearLayout) view.findViewById(R.id.loading_lly);
        refresh_lly = (LinearLayout) view.findViewById(R.id.refresh_lly);
        loading_tv = (TextView) view.findViewById(R.id.loading_tv);
        error_msg_tv = (TextView) view.findViewById(R.id.error_msg_tv);
        error_title_tv = (TextView) view.findViewById(R.id.error_title_tv);
        refresh_button = (Button) view.findViewById(R.id.refresh_button);
        
        refresh_button.setOnClickListener(mClickListener);
        
        addView(view);
    }
    
    private OnClickListener mClickListener = new OnClickListener()
    {
        
        @Override
        public void onClick(View v)
        {
            if (callback != null)
            {
                refresh();
                callback.onRefresh();
            }
        }
    };
    
    public void setErrorMsg(String errorMsg){
        error_msg_tv.setText(errorMsg);
    }
    
    public void refresh(){
        refresh_lly.setVisibility(View.GONE);
        loading_lly.setVisibility(View.VISIBLE);
    }
    public void loadFailed(String msg){
        refresh_lly.setVisibility(View.VISIBLE);
        error_msg_tv.setText(msg);
        loading_lly.setVisibility(View.GONE);
    }
    
    public void show(){
        setVisibility(VISIBLE);
    }
    public void hide(){
        setVisibility(GONE);
    }
    
}
