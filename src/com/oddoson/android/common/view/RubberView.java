package com.oddoson.android.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

/**
 * 刮奖TextView
 * @author oddoson
 *
 */
public class RubberView extends TextView implements OnTouchListener
{
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private float x, y;
    private Bitmap mBitmap;
    
    public RubberView(Context context)
    {
        super(context);
        init();
    }
    
    public RubberView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    
    void init()
    {
        setOnTouchListener(this);
        
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(20f);
        mPaint.setStyle(Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(30);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 前圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 后圆角
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); // 擦除效果
        
        mPath = new Path();
        mPath.setFillType(FillType.WINDING);
        
        mBitmap = Bitmap.createBitmap(800, 800, Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(0xffefefef);
        
        setGravity(Gravity.CENTER);
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        mCanvas.drawPath(mPath, mPaint);
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
        case MotionEvent.ACTION_DOWN:
            TouchDown(event);
            break;
        case MotionEvent.ACTION_MOVE:
            TouchMove(event);
            break;
        case MotionEvent.ACTION_UP:
            TouchUp(event);
            break;
        default:
            break;
        }
        invalidate();
        return true;
    }
    
    void TouchDown(MotionEvent event)
    {
        x = event.getX();
        y = event.getY();
        // mPath.reset(); //重置绘制路线，隐藏之前绘制的轨迹
        mPath.moveTo(x, y);
    }
    
    void TouchMove(MotionEvent event)
    {
        final float dx = Math.abs(x - event.getX());
        final float dy = Math.abs(y - event.getY());
        // 两点之间的距离大于等于3时，连接连接两点形成直线
        if (dx >= 3 || dy >= 3)
        {
            // 设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + event.getX()) / 2;
            float cY = (y + event.getY()) / 2;
            // 二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mPath.quadTo(x, y, cX, cY);
            x = event.getX();
            y = event.getY();
        }
    }
    
    void TouchUp(MotionEvent event)
    {
        mPath.lineTo(event.getX(), event.getY());
    }
    
    private boolean mRun;
    private int[] mPixels;
    //计算刮开的密度
    private Runnable mRunnable = new Runnable()
    {
        
        @Override
        public void run()
        {
            
            while (mRun)
            {
                
                SystemClock.sleep(100);
                
                int w = 640;
                int h = 400;
                
                float wipeArea = 0;
                float totalArea = w * h;
                
                // 计算耗时100毫秒左右
                Bitmap bitmap = mBitmap;
                
                if (mPixels == null)
                {
                    mPixels = new int[w * h];
                }
                
                bitmap.getPixels(mPixels, 0, w, 0, 0, w, h);
                
                for (int i = 0; i < w; i++)
                {
                    for (int j = 0; j < h; j++)
                    {
                        int index = i + j * w;
                        if (mPixels[index] == 0)
                        {
                            wipeArea++;
                        }
                    }
                }
                
                if (wipeArea > 0 && totalArea > 0)
                {
                    // 计算刮开的区域比
                    int percent = (int) (wipeArea * 100 / totalArea);
                }
                
            }
        }
    };
    
}
