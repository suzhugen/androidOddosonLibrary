package com.oddoson.android.common.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * 圆角图片
 * 
 * @author oddoson
 * 
 */
public class RoundImageView extends BaseImageView
{
    
    private float xRadius = 30f, yRadius = 30f;
    
    public RoundImageView(Context context)
    {
        super(context);
    }
    
    public RoundImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    
    public void setRound(float x, float y)
    {
        xRadius = x;
        yRadius = y;
        invalidate();
    }
    
    public Bitmap getBitmap(int width, int height)
    {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        RectF rectF = new RectF(0.0f+border_size/2, 0.0f+border_size/2, width-border_size/2, height-border_size/2);
        if (getShowBorder())
        {
            canvas.drawRoundRect(rectF, xRadius, yRadius, paint);
        }
        else
        {
            canvas.drawRoundRect(rectF, xRadius, yRadius, paint);
        }
        return bitmap;
    }
    
    @Override
    public void drawBorder(Canvas canvas)
    {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mContext.getResources().getColor(border_color));
        RectF rectF = new RectF(0.0f, 0.0f, getWidth(), getHeight());
        canvas.drawRoundRect(rectF, xRadius, yRadius, paint);
    }
    
    public Bitmap getBitmap()
    {
        return getBitmap(getWidth(), getHeight());
    }
    
}
