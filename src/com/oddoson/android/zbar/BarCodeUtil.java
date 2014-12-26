package com.oddoson.android.zbar;

import java.util.Hashtable;
import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class BarCodeUtil {
	/**
	 * 生成二维码
	 * 
	 * @param 字符串
	 *            ,宽度,高度
	 * @return Bitmap
	 * @throws WriterException
	 */
	public static Bitmap cretaeBarcodeBitmap(String str, int width, int height) throws WriterException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		// BitMatrix matrix= new MultiFormatWriter().encode(str,
		// BarcodeFormat.QR_CODE, 300, 300);
		BitMatrix matrix = null;
		if (width != 0 && height != 0) {
			matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, width, height, hints);
		} else {
			matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300, hints);
		}
		int mWidth = matrix.getWidth();
		int mHeight = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[mWidth * mHeight];
		for (int y = 0; y < mHeight; y++) {
			for (int x = 0; x < mWidth; x++) {
				if (matrix.get(x, y)) {
					pixels[y * mWidth + x] = 0xff000000;
				} else { // 无信息设置像素点为白色
					pixels[y * mWidth + x] = 0xffffffff;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap
		bitmap.setPixels(pixels, 0, mWidth, 0, 0, mWidth, mHeight);
		return bitmap;
	}
}
