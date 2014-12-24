package com.oddoson.android.zbar;

import java.util.Hashtable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.oddoson.android.common.util.BitmapUtil;
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
		Bitmap scanBitmap =BitmapUtil.getBitmap(path, -1, 1500*1500);
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
