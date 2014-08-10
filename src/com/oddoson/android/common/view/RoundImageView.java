package com.oddoson.android.common.view;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundImageView extends ImageView {

	float xRadius=20f,yRadius=20f;
	
	
	public RoundImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setRound(float x,float y){
		xRadius=x;
		yRadius=y;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		int color = 0xffffffff;
		Paint paint = new Paint();
		RectF rect = new RectF(0.0f, 0.0f, getWidth(), getHeight());
				
		if (getDrawable() instanceof ColorDrawable) {
			ColorDrawable colorDrawable=(ColorDrawable) getDrawable();	
			paint.setColor(color);
			canvas.drawRoundRect(rect, xRadius, yRadius, paint);
			return;
		}
		
/*		paint.setColor(color);
		canvas.drawRoundRect(rect, xRadius, yRadius, paint);*/
		
		BitmapShader shader;
		BitmapDrawable bitmapDrawable = (BitmapDrawable) getDrawable();
		if (bitmapDrawable==null) {
			return;
		}
		shader = new BitmapShader(bitmapDrawable.getBitmap(),
		Shader.TileMode.CLAMP, Shader.TileMode.CLAMP );
		// 设置映射否则图片显示不全
		int width = bitmapDrawable.getBitmap().getWidth();
		int height = bitmapDrawable.getBitmap().getHeight();
		RectF src = new RectF(0.0f, 0.0f, getWidth(), getHeight());
		Matrix matrix = new Matrix();
		RectF rect2 = new  RectF(0.0f, 0.0f, getWidth(), getHeight());
		matrix.setRectToRect(src, rect2, Matrix.ScaleToFit.CENTER);
		shader.setLocalMatrix(matrix);
		paint=new Paint();
		paint.setAntiAlias(true);
		paint.setShader(shader);		
		canvas.drawRoundRect(rect2, xRadius, yRadius, paint);

	}

}
