package com.oddoson.android.common.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.oddoson.android.common.R;
import com.oddoson.android.common.util.DensityUtil;
import com.oddoson.android.common.util.LogUtil;

/**
 * 字母排序 导航，通讯录导航
 * @author Administrator
 *
 */
public class IndexSideBar extends View
{
    public static final char[] chars = new char[]
    { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };;
    private SectionIndexer sectionIndexter = null;
    private ListView list;
    private TextView mDialogText;
    private int m_ItemHeight;
    private WindowManager mWindowManager;
    
    public IndexSideBar(Context context)
    {
        super(context);
        init(context);
    }
    
    public IndexSideBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }
    
    public IndexSideBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context)
    {
        mDialogText = (TextView) LayoutInflater.from(context).inflate(
                R.layout.list_position_item, null);
        mDialogText.setVisibility(View.INVISIBLE);
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int size = DensityUtil.dipToPx(context, 80);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(size,
                size, WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
    }
    
    public void setListView(ListView _list)
    {
        list = _list;
        sectionIndexter = (SectionIndexer) list.getAdapter();
    }
    
    public boolean onTouchEvent(MotionEvent event)
    {
        super.onTouchEvent(event);
        int i = (int) event.getY();
        int idx = i / m_ItemHeight;
        if (idx >= chars.length)
        {
            idx = chars.length - 1;
        }
        else if (idx < 0)
        {
            idx = 0;
        }
        if (list == null)
        {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE)
        {
            mDialogText.setVisibility(View.VISIBLE);
            mDialogText.setText("" + chars[idx]);
            if (sectionIndexter == null)
            {
                sectionIndexter = (SectionIndexer) list.getAdapter();
            }
           // int position = sectionIndexter.getPositionForSection(idx);
            LogUtil.e("idx="+chars[idx]);
            int position=getPosition(idx);
            LogUtil.e("position="+position);
            if (position == -1)
            {
                return true;
            }
            list.setSelection(position);
        }
        else
        {
            mDialogText.setVisibility(View.INVISIBLE);
        }
        return true;
    }
    
    private int  getPosition(int idx){
        int count=list.getCount();
        for (int i = 0; i < count; i++)
        {
            if (list.getAdapter().getItem(i).equals(chars[idx]+""))
            {
                return i;
            }
        }
        return 30;
    }
    
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(getResources().getColor(R.color.bocop_dialog_bg));
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);
        
        m_ItemHeight = getMeasuredHeight() / 26;
        float widthCenter = getMeasuredWidth() / 2;
        for (int i = 0; i < chars.length; i++)
        {
            canvas.drawText(String.valueOf(chars[i]), widthCenter, m_ItemHeight
                    + (i * m_ItemHeight), paint);
        }
        super.onDraw(canvas);
    }
    
    public void onDestroy()
    {
        mWindowManager.removeView(mDialogText);
    }
    
}
