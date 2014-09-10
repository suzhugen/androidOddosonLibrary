package com.oddoson.android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.oddoson.android.common.util.LogUtil;

/**
 * 自定义ViewGroup，实现ScrollView、LinearLayout功能,回弹效果
 * 
 * @author oddoson
 * 
 */
public class MyViewGroup extends ViewGroup
{
    public Scroller mScroller;
    private int totalHeight;// 最大滚动位置
    
    public MyViewGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mScroller = new Scroller(context);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        totalHeight = 0;
        int count = getChildCount();
        if (count < 1)
        {
            return;
        }
        for (int i = 0; i < count; i++)
        {
            View child = this.getChildAt(i);
            if (child == null)
                break;
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            if (child.getVisibility() == GONE)
            {
                continue;
            }
            // 测量子控件高宽
            child.measure(MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(lp.width), MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(lp.height,
                            MeasureSpec.UNSPECIFIED));
            totalHeight += child.getMeasuredHeight();
        }
        totalHeight -= getHeight();// 最大滚动位置,减去viewgroup的高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        layoutVertical(l, t, r, b);
    }
    
    void layoutVertical(int left, int top, int right, int bottom)
    {
        final int count = getChildCount();
        int childTop = 0;
        for (int i = 0; i < count; i++)
        {
            View child = getChildAt(i);
            if (child == null)
            {
                childTop += 0;
            }
            else if (child.getVisibility() != GONE)
            {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                child.layout(0, childTop, childWidth, childHeight + childTop);// 在viewgroup中显示的位置。
                childTop += childHeight;
            }
        }
    }
    
    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            int dx = mScroller.getCurrX() - mScroller.getFinalX();
            int dy = mScroller.getCurrY() - mScroller.getFinalY();
            mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(),
                    dx, dy);
            postInvalidate();
            LogUtil.e("scroll..........");
        }
        super.computeScroll();
    }
    
    private int lastX, lastY;
    private int overHeight = 0;
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            lastX = (int) event.getX();
            lastY = (int) event.getY();
            overHeight = 0;
            return true;
        case MotionEvent.ACTION_MOVE:
            
            if (getChildCount() == 0)
            {
                break;
            }
            
            int x = (int) event.getX();
            int y = (int) event.getY();
            int dy = lastY - y;
            
            int scrollH = getScrollY();// 以滚动高度
            
//            if (scrollH > totalHeight)
//            {
//                if (dy > 0)
//                {
//                    // 拉到底了，禁止再上拉
//                    dy = 0;
//                }
//            }
//            else if (scrollH <= 0)
//            {
//                if (dy < 0)
//                {
//                    // 拉到顶了，禁止再下拉
//                    dy = 0;
//                }
//            }
            scrollBy(0, dy);// 上下滚动
            
            lastX = x;
            lastY = y;
            break;
        case MotionEvent.ACTION_UP:
            //回弹效果
            int sy=getScrollY();
            overHeight=sy-totalHeight;
            if (overHeight>0)
            {
                scrollBy(0, -overHeight);
            }else if (sy<0)
            {
               scrollBy(0, -sy);
            }
            LogUtil.d("h= " + overHeight);
            LogUtil.d("********************");
            break;
        default:
            break;
        }
        return super.onTouchEvent(event);
    }
    
}
