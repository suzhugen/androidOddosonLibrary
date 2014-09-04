package com.oddoson.android.common.view.image;

import java.lang.ref.WeakReference;

import com.oddoson.android.common.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 各种图形的基类
 * 
 * @author oddoson
 * 
 */
public abstract class BaseImageView extends ImageView
{
    protected Context mContext;
    private static final Xfermode sXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;//形状图片
    private Paint mPaint;
    private WeakReference<Bitmap> mWeakBitmap;//要显示的图片
    protected Boolean showBorder=false;//绘制边框
    protected float border_size=10f; //边框大小
    protected int border_color = R.color.silver; //边框颜色
    
    public BaseImageView(Context context)
    {
        super(context);
        sharedConstructor(context);
    }
    
    public BaseImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        sharedConstructor(context);
    }
    
    public BaseImageView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        sharedConstructor(context);
    }
    
    private void sharedConstructor(Context context)
    {
        mContext = context;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }
    
    public void invalidate()
    {
        mWeakBitmap = null;
        if (mMaskBitmap != null)
        {
            mMaskBitmap.recycle();
        }
        super.invalidate();
    }
    
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        if (!isInEditMode())
        {
            int i = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null,
                    Canvas.ALL_SAVE_FLAG);
            try
            {
                if (showBorder)
                {
                    drawBorder(canvas); 
                }
                
                Bitmap bitmap = mWeakBitmap != null ? mWeakBitmap.get() : null;
                // Bitmap not loaded.
                if (bitmap == null || bitmap.isRecycled())
                {
                    Drawable drawable = getDrawable();
                    if (drawable != null)
                    {
                        // Allocation onDraw but it's ok because it will not
                        // always be called.
                        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                                Bitmap.Config.ARGB_8888);
                        Canvas bitmapCanvas = new Canvas(bitmap);
                        drawable.setBounds(0, 0, getWidth(), getHeight());
                        drawable.draw(bitmapCanvas);
                        // If mask is already set, skip and use cached mask.
                        if (mMaskBitmap == null || mMaskBitmap.isRecycled())
                        {
                            mMaskBitmap = getBitmap();
                        }
                        mPaint.reset();
                        mPaint.setFilterBitmap(false);
                        // 绘制底图叠加部分
                        mPaint.setXfermode(sXfermode);
                        bitmapCanvas.drawBitmap(mMaskBitmap, 0.0f, 0.0f, mPaint);
                        mWeakBitmap = new WeakReference<Bitmap>(bitmap);
                    }
                }
                // Bitmap already loaded.
                if (bitmap != null)
                {
                    mPaint.setXfermode(null);
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
                }
            }
            catch (Exception e)
            {
                System.gc();
            }
            finally
            {
                canvas.restoreToCount(i);
            }
        }
        else
        {
            super.onDraw(canvas);
        }
    }
    
    /**
     * 获取想要的形状
     * 
     * @return
     */
    public abstract Bitmap getBitmap();
    
    /**
     * 绘制边框
     */
    public void drawBorder(Canvas canvas){
    }

    public Boolean getShowBorder()
    {
        return showBorder;
    }

    /**
     * 显示边框
     * @param showBorder
     */
    public void setShowBorder(Boolean showBorder)
    {
        this.showBorder = showBorder;
        invalidate();
    }

    public float getBorderSize()
    {
        return border_size;
    }

    /**
     * 设置边框大小
     * @param border_size
     */
    public void setBorderSize(float border_size)
    {
        this.border_size = border_size;
        invalidate();
    }
    
    /**
     * 设置边框颜色
     * 
     * @param color_id
     *            颜色资源id
     */
    public void setBorderColor(int color_id)
    {
        this.border_color = color_id;
        setShowBorder(true);
        invalidate();
    }
    
    
 
}