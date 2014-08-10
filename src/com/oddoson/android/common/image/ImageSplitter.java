package com.oddoson.android.common.image;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

/**
 * 图片分割器
 * 
 * @author suzhugen
 * 
 */
public class ImageSplitter {
	
	public static class ImagePiece {
		public int index = 0;
		public Bitmap bitmap = null;
	}

	/**
	 * 
	 * @param bitmap 要分割的图片
	 * @param xPiece  X 几片
	 * @param yPiece  Y  几片
	 * @return
	 */
	public static List<ImagePiece> split(Bitmap bitmap, int xPiece, int yPiece) {
		List<ImagePiece> pieces = new ArrayList<ImagePiece>(xPiece * yPiece);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int pieceWidth = width / xPiece;
		int pieceHeight = height / yPiece;
		for (int i = 0; i < yPiece; i++) {
			for (int j = 0; j < xPiece; j++) {
				ImagePiece piece = new ImagePiece();
				piece.index = j + i * xPiece;
				int xValue = j * pieceWidth;
				int yValue = i * pieceHeight;
				piece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue,
						pieceWidth, pieceHeight);
				pieces.add(piece);
			}
		}

		return pieces;
	}
}
