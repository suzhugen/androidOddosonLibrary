package com.oddoson.android.common.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.oddoson.android.common.R;
import com.oddoson.android.common.util.LogUtil;
import com.oddoson.android.common.util.SystemInfoUtils;

/**
 * 加载dialog
 * 
 * @author oddoson
 * 
 */
public class LoadingLogoDialog extends BaseDialog
{
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '.';
    
    private Context mContext;
    private ImageView iv_route;
    private TextView tv;
    private TextView tv_point;
    private RotateAnimation mAnim;
    private boolean cancelable = true;
    
    private Handler handler = new Handler()
    {
        private int num = 0;
        
        public void handleMessage(android.os.Message msg)
        {
            if (msg.what == CHANGE_TITLE_WHAT)
            {
                StringBuilder builder = new StringBuilder();
                if (num >= MAX_SUFFIX_NUMBER)
                {
                    num = 0;
                }
                num++;
                for (int i = 0; i < num; i++)
                {
                    builder.append(SUFFIX);
                }
                tv_point.setText(builder.toString());
                if (isShowing())
                {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT,
                            CHNAGE_TITLE_DELAYMILLIS);
                }
                else
                {
                    num = 0;
                }
            }
        };
    };
    
    public LoadingLogoDialog(Context context)
    {
        super(context, com.oddoson.android.common.R.style.Dialog_bocop);
        mContext = context;
        init();
    }
    public LoadingLogoDialog(Context context,boolean isCancelable)
    {
        super(context, com.oddoson.android.common.R.style.Dialog_bocop);
        setCancelable(isCancelable);
        mContext = context;
        init();
    }
    public LoadingLogoDialog(Context context, String title)
    {
        super(context, com.oddoson.android.common.R.style.Dialog_bocop);
        mContext = context;
        init();
        setTitle(title);
    }
    
    public LoadingLogoDialog(Context context, String title,boolean isCancelable)
    {
        super(context, com.oddoson.android.common.R.style.Dialog_bocop);
        mContext = context;
        init();
        setTitle(title);
        setCancelable(isCancelable);
    }
    
    private void init()
    {
        View contentView = View.inflate(getContext(),
                R.layout.activity_custom_loding_dialog_layout, null);
        setContentView(contentView);
        
        contentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (cancelable)
                {
                    dismiss();
                }
            }
        });
        iv_route = (ImageView) findViewById(R.id.iv_route);
        tv = (TextView) findViewById(R.id.tv);
        tv_point = (TextView) findViewById(R.id.tv_point);
        initAnim();
        getWindow().setWindowAnimations(R.anim.alpha_in);
    }
    
    /**
     * 设置中间logo
     * 
     * @param resid
     */
    public void setLogo(int resid)
    {
        iv_route.setImageResource(resid);
    }
    
    public void setLogo(Drawable drawable)
    {
        iv_route.setImageDrawable(drawable);
    }
    
    private void initAnim()
    {
        mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f,
                Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
    }
    
    @Override
    public void show()
    {
        if (((Activity) mContext).isFinishing())
        {
            return;
        }
        iv_route.startAnimation(mAnim);
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
        super.show();
    }
    
    @SuppressLint("NewApi")
    @Override
    public void dismiss()
    {
        if (SystemInfoUtils.getAndroidSDKVersion() > 7)
        {
            mAnim.cancel();
        }
        else
        {
            iv_route.clearAnimation();
        }
        super.dismiss();
    }
    
    @Override
    public void setCancelable(boolean flag)
    {
        cancelable = flag;
        super.setCancelable(flag);
    }
    
    @Override
    public void setTitle(CharSequence title)
    {
        tv.setText(title);
    }
    
    @Override
    public void setTitle(int titleId)
    {
        setTitle(getContext().getString(titleId));
    }
}
