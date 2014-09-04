package com.oddoson.android.common.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * 圆型图片
 * 
 * @author oddoson
 * 
 *         <pre>
 * setRadius(100f);
 * setShowBorder(true);
 * setBorderSize(30f);
 * </pre>
 * 
 */
public class CircleImageView extends BaseImageView
{
    
    private float radius = 200f;
    
    public CircleImageView(Context context)
    {
        super(context);
    }
    
    public CircleImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    /**
     * 设置半径
     * 
     * @param radius
     */
    public void setRadius(float radius)
    {
        this.radius = radius;
        invalidate();
    }
    
    public Bitmap getBitmap(int width, int height)
    {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        if (getShowBorder())
        {
            canvas.drawCircle(width / 2, height / 2, radius - border_size,
                    paint);
        }
        else
        {
            canvas.drawCircle(width / 2, height / 2, radius, paint);
        }
        return bitmap;
    }
    
    @Override
    public void drawBorder(Canvas canvas)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mContext.getResources().getColor(border_color));
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, paint);
    }
    
    public Bitmap getBitmap()
    {
        return getBitmap(getWidth(), getHeight());
    }
    
}
