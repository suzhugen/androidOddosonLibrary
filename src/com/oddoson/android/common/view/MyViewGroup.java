package com.oddoson.android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
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
    private int totalHeight;// 最大滚动位置,<0 未满一屏幕，>0超过一屏幕
    private boolean isCross = true;// 是否可越界
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaxVelocity;
    private int direction = status_down;
    public static final int status_up = 1;// 上拉
    public static final int status_down = 0;// 下拉
    
    public boolean isCross()
    {
        return isCross;
    }
    
    /**
     * 越界效果
     * 
     * @param isCross
     */
    public void setCross(boolean isCross)
    {
        this.isCross = isCross;
    }
    
    public MyViewGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mScroller = new Scroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
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
            if (getScrollY()==0)
            {
                return;
            }
            int dx = mScroller.getCurrX() - mScroller.getFinalX();
            int dy = mScroller.getCurrY() - mScroller.getFinalY();
            scrollTo(0,mScroller.getCurrY());
            //scrollBy(0, -dy);
            postInvalidate();
        }else {
            LogUtil.d("scroll complete...");
        }
        super.computeScroll();
    }
    
    private int lastX, lastY, dy;
    private int overHeight = 0;
    
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        acquireVelocityTracker(event);
        
        switch (event.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            lastX = (int) event.getX();
            lastY = (int) event.getY();
            overHeight = 0;
            if (!mScroller.isFinished())
            {
                mScroller.abortAnimation();
            }
            return true;
        case MotionEvent.ACTION_MOVE:
            
            if (getChildCount() == 0)
            {
                break;
            }
            int x = (int) event.getX();
            int y = (int) event.getY();
            checkMove(x, y);
            
            break;
        case MotionEvent.ACTION_UP:
            moveUp();
            break;
        case MotionEvent.ACTION_CANCEL:
            releaseVelocityTracker();
            break;
        default:
            break;
        }
        return super.onTouchEvent(event);
    }
    
    protected void checkMove(int x, int y)
    {
        dy = lastY - y;
        int scrollH = getScrollY();// 以滚动高度
        if (dy > 0)
        {
            direction = status_up;
        }
        else
        {
            direction = status_down;
        }
        
        if (dy > 0 && scrollH > totalHeight)
        {
            if (!isCross())
            {
                // 拉到底了，禁止再上拉
                dy = 0;
            }
        }
        else if (dy < 0 && scrollH < 0)
        {
            
            if (!isCross())
            {
                // 拉到顶了，禁止再下拉
                dy = 0;
            }
        }
        
        scrollBy(0, dy);// 上下滚动
        
        lastX = x;
        lastY = y;
    }
    
    private void moveUp()
    {
        mVelocityTracker.computeCurrentVelocity(1000,getHeight());
        
        int fy = (int) mVelocityTracker.getYVelocity();
        LogUtil.e("fy=" + fy);
        //mScroller.startScroll(getScrollX(), getScrollY(), 0,-fy, 3000);
        mScroller.fling(getScrollX(), getScrollY(), 0, -fy, 0, 0, 0, 1000);
        
        if (isCross())
        {
            int sy = getScrollY();
            overHeight = sy - totalHeight;
            int dt = 0;
            if (sy < 0)
            {
                // 下拉
                dt = -sy;// 内容未超过一屏幕，回原位
            }
            else if (sy > 0)
            {
                if (overHeight < 0)
                {
                    return;
                }
                // 上拉
                if (totalHeight <= 0)
                {
                    // 内容未超过一屏幕，回原位
                    dt = -sy;
                }
                else
                {
                    dt = -overHeight;
                }
            }
           // scrollBy(0, dt);
        }
    }
    
    /**
     * 
     * @param event
     *            向VelocityTracker添加MotionEvent
     * 
     * @see android.view.VelocityTracker#obtain()
     * @see android.view.VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(MotionEvent event)
    {
        if (null == mVelocityTracker)
        {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }
    
    /**
     * 释放VelocityTracker
     * 
     * @see android.view.VelocityTracker#clear()
     * @see android.view.VelocityTracker#recycle()
     */
    private void releaseVelocityTracker()
    {
        if (null != mVelocityTracker)
        {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            // mVelocityTracker = null;
        }
    }
    
}
