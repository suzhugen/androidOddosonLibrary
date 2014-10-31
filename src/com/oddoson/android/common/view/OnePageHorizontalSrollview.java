package com.oddoson.android.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 一次滑动一屏的HorizontalScrollView
 * @author oddoson
 *
 */
public class OnePageHorizontalSrollview extends HorizontalScrollView
{
    
    private int index = 0;
    private int pageCount = 0;
    
    private PageChangeListener listener;
    
    public OnePageHorizontalSrollview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public void setPageCount(int count)
    {
        pageCount = count;
    }
    
    public int getCurrentPosition()
    {
        return index;
    }
    
    public void scrollFirstPage()
    {
        fullScroll(View.FOCUS_RIGHT);
    }
    
    public void scrollLastPage()
    {
        fullScroll(View.FOCUS_LEFT);
    }
    
    private boolean canScroll()
    {
        View child = getChildAt(0);
        if (child != null)
        {
            int childWidth = child.getWidth();
            return getWidth() < childWidth;
        }
        return false;
    }
    
    int mLastX;
    int mFirstX;
    int mLastY;
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        final int action = ev.getAction();
        
        switch (action & MotionEvent.ACTION_MASK)
        {
        case MotionEvent.ACTION_DOWN:
        {
            if (getChildCount() == 0)
            {
                return false;
            }
            mFirstX = (int) ev.getX();
            mLastX = (int) ev.getX();
            mLastY = (int) ev.getY();
            break;
        }
        case MotionEvent.ACTION_MOVE:
            
            int deltaX = (int) (mLastX - ev.getX());
            int deltaY = (int) (mLastY - ev.getY());
            if (deltaY > 10||deltaY < -10)
            {
                getParent().requestDisallowInterceptTouchEvent(false);
                movePage((int)ev.getX());
            }
            else if (canScroll())
            {
                getParent().requestDisallowInterceptTouchEvent(true);
                smoothScrollBy(deltaX, 0);
            }
            mLastX = (int) ev.getX();
            mLastY = (int) ev.getY();
            break;
        case MotionEvent.ACTION_UP:
            movePage((int)ev.getX());
            break;
        case MotionEvent.ACTION_CANCEL:
            break;
        }
        return true;
    }
    
    void movePage(int cX){
        int dX = (int) (mFirstX - cX);
        if (dX > 20 && index < pageCount - 1)
        {
            index++;
        }
        else if (dX < -20 && index > 0)
        {
            index--;
        }
        smoothScrollTo(getWidth() * index, 0);
        if (listener!=null)
        {
            listener.onPageSelected(index);
        }
    }
    public void setListener(PageChangeListener listener){
        this.listener=listener;
    }
    public interface PageChangeListener{
        public void onPageSelected(int position);
    }
    
}
