package com.oddoson.android.common.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtil {

	/**
	 * 读取图片文件高宽，不用实际加载图片，防止OOM
	 * 
	 * @param fileName
	 * @return
	 */
	public static BitmapFactory.Options getImageFileSize(String fileName) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;//true,只是读图片大小，不申请bitmap内存  
		BitmapFactory.decodeFile(fileName, opts);
		return opts;
	}
	
	/**
	 * 获取缩放图，防止图片太大 导致OOM
	 * @param fileName
	 * @param minSideLength  最小边长 ,-1 不要求
	 * @param maxNumOfPixels 最大像素 高X宽, -1 不要求
	 * @return
	 */
	public static Bitmap getBitmap(String fileName,int minSideLength,int maxNumOfPixels){
		Options opts=getImageFileSize(fileName);
		opts.inJustDecodeBounds=false;
		opts.inSampleSize=computeSampleSize(opts, minSideLength, maxNumOfPixels);
		return BitmapFactory.decodeFile(fileName, opts);
	}

	/**
	 * BitmapFactory.Options opts.inSampleSize 设置缩放比例
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength,
			int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	/**
	 * 计算缩放比例
	 * @param options
	 * @param minSideLength 最小边长 
	 * @param maxNumOfPixels 最大像素 高X宽
	 * @return
	 */
	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h
				/ maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			//原比例
			return 1;
		} else if (minSideLength == -1) {
			//按最大像素
			return lowerBound;
		} else {
			//按最小边长
			return upperBound;
		}
	}
	
	public static byte[] bitmapToByte(Bitmap bitmap){
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}
	public static byte[] bitmapToByte(String path){
		File file=new File(path);
		if (!file.exists()) {
			return null;
		}
		Bitmap bitmap=BitmapFactory.decodeFile(path);
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
		return bos.toByteArray();
	}
	public static Bitmap byteToBitmap(byte[] data){
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
	
}
