package com.oddoson.android.media;

public interface OnRecordChangeListener {
	
	public void onVolumnChanged(int value);
	
	public void onTimeChanged(float recordTime,String localPath);

}
