package com.oddoson.android.zbar;

import java.util.Hashtable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.zbar.lib.RGBLuminanceSource;

/**
 * 条形码，二维码扫描解码
 * 
 * @author Administrator
 * 
 */
public class ScanDecodeManager {
	
	
	
	
	/**
	 * 从内存卡的图片中读取二维码
	 * @param path
	 * @return
	 */
	public static String scanningImage(String path) {
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		Bitmap scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
