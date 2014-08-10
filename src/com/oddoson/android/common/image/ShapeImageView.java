package com.oddoson.android.common.image;

import java.lang.ref.WeakReference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 多种形状ImageView
 * <pre>
 *    圆角
 * 	   ShapeImageView shapeImageView= (ShapeImageView) findViewById(R.id.g);
	   shapeImageView.setShape(Shape.roundRect);
	   shapeImageView.setCorners(10, 10);
 * </pre>
 */
public  class ShapeImageView extends ImageView {

	protected Context mContext;

	private static final Xfermode sXfermode = new PorterDuffXfermode(
			PorterDuff.Mode.DST_IN);
	// private BitmapShader mBitmapShader;
	private Bitmap mMaskBitmap;
	private Paint mPaint;
	private WeakReference<Bitmap> mWeakBitmap;

	int shape = 3;
	float rx=15,ry=15;

	public static class Shape {
		public  final static int circle = 1; // 圆
		public final static int ractang = 2; // 矩形
		public final static int roundRect = 3; // 圆角
	}

	public ShapeImageView(Context context) {
		super(context);
		sharedConstructor(context);
	}

	public ShapeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		sharedConstructor(context);
	}

	public ShapeImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sharedConstructor(context);
	}

	private void sharedConstructor(Context context) {
		mContext = context;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	/**
	 * 设置形状
	 * 
	 * @param type
	 *            Shape类
	 */
	public void setShape(int type) {
		shape = type;
	}
	/**
	 * 设置圆角
	 * @param cx 左
	 * @param cy 右
	 */
	public void setCorners(float rx,float ry){
		this.rx=rx;
		this.ry=ry;
	}

	public void invalidate() {
		mWeakBitmap = null;
		if (mMaskBitmap != null) {
			mMaskBitmap.recycle();
		}
		super.invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (!isInEditMode()) {
			int i = canvas.saveLayer(0.0f, 0.0f, getWidth(), getHeight(), null,
					Canvas.ALL_SAVE_FLAG);
			try {
				Bitmap bitmap = mWeakBitmap != null ? mWeakBitmap.get() : null;
				// Bitmap not loaded.
				if (bitmap == null || bitmap.isRecycled()) {
					Drawable drawable = getDrawable();
					if (drawable != null) {
						// Allocation onDraw but it's ok because it will not
						// always be called.
						bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
								Bitmap.Config.ARGB_8888);
						Canvas bitmapCanvas = new Canvas(bitmap);
						drawable.setBounds(0, 0, getWidth(), getHeight());
						drawable.draw(bitmapCanvas);

						// If mask is already set, skip and use cached mask.
						if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
							mMaskBitmap = getBitmap();
						}

						// Draw Bitmap.
						mPaint.reset();
						mPaint.setFilterBitmap(false);
						// 绘制底图叠加部分
						mPaint.setXfermode(sXfermode);
						// mBitmapShader = new BitmapShader(mMaskBitmap,
						// Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
						// mPaint.setShader(mBitmapShader);
						bitmapCanvas
								.drawBitmap(mMaskBitmap, 0.0f, 0.0f, mPaint);

						mWeakBitmap = new WeakReference<Bitmap>(bitmap);
					}
				}

				// Bitmap already loaded.
				if (bitmap != null) {
					mPaint.setXfermode(null);
					// mPaint.setShader(null);
					canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
					return;
				}
			} catch (Exception e) {
				System.gc();
			} finally {
				canvas.restoreToCount(i);
			}
		} else {
			super.onDraw(canvas);
		}

	}

	public  Bitmap getBitmap(){
		Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
		switch (shape) {
		case Shape.circle:
	        canvas.drawOval(new RectF(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight()), paint);
			break;
		case Shape.ractang:
			canvas.drawRect(new RectF(0.0f, 0.0f, getMeasuredWidth(), getMeasuredHeight()), paint);
			break;
		case Shape.roundRect:
			 RectF rectF=new RectF(0.0f, 0.0f,getMeasuredWidth(), getMeasuredHeight());
		     canvas.drawRoundRect(rectF,rx, ry,  paint);
			break;
		default:
			break;
		}
		return bitmap;
	}

}
