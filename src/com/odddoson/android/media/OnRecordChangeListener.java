package com.odddoson.android.media;

public interface OnRecordChangeListener {
	
	public void onVolumnChanged(int value);
	
	public void onTimeChanged(float recordTime,String localPath);

}
