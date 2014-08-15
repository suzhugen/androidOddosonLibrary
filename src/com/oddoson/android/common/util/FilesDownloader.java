package com.oddoson.android.common.util;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.oddoson.android.common.security.MD5;

/**
 * 单个文件下载器，支持断点续传
 * 
 * @author su
 * 
 */
public class FilesDownloader {

	private String fileUrl;
	private String dirPath;
	private String localfilePath;
	private String tmpFile;
	private int compeleteSize;
	private int fileSize = -1;
	private Boolean isRunning = true;
	private Boolean pause=false;
	private MyHandler mHandler;
	private FileDownloadCallbabk callbabk;

	public FilesDownloader(String fileUrl, String dirPath, String filename,FileDownloadCallbabk callbabk) {
		this.fileUrl = fileUrl;
		this.dirPath = dirPath;
		this.tmpFile = dirPath + MD5.md5(fileUrl) + ".tmp";
		this.localfilePath = dirPath+filename;
		this.mHandler = new MyHandler();
		this.callbabk=callbabk;
		FileUtils.makeDirs(this.dirPath);
	}

	public void start(){
		if (TextUtils.isEmpty(fileUrl)) {
			Message message = Message.obtain();
			message.what = 0;
			message.obj="url is null or empty";
			mHandler.sendMessage(message);
			return;
		}
		if (callbabk != null) {
			callbabk.onStart(fileUrl, localfilePath);
		}
		pause = false;
		isRunning=true;
		new FileDownloaderThread().start();
	}

	public void stop() {
		isRunning = false;
		Message message = Message.obtain();
		message.what = 2;
		mHandler.sendMessage(message);
	}
	public void pause() {
		pause = true;
		Message message = Message.obtain();
		message.what = 4;
		mHandler.sendMessage(message);
	}
	public Boolean isRun(){
		return isRunning&&!pause;
	}
	public Boolean isPause(){
		return pause;
	}
	
	public void setCallbabk(FileDownloadCallbabk callbabk){
		this.callbabk=callbabk;
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				//fail
				isRunning=false;
				if (callbabk != null) {
					String mess="";
					if (msg.obj!=null) {
						mess=msg.obj.toString();
					}
					callbabk.onFail(fileUrl, localfilePath,mess);
				}
				break;
			case 1:
				//progress
				if (msg.arg1 == 100) {
					File file = new File(tmpFile);
					file.renameTo(new File(localfilePath));
					LogUtil.d("apk file remame.... ");
					isRunning=false;
				}
				if (callbabk != null) {
					callbabk.onProgress(fileUrl, localfilePath, msg.arg1);
				}
				break;
			case 2:
				isRunning=false;
				// progress oncancell
				if (callbabk != null) {
					callbabk.onCancelled(fileUrl, localfilePath);
				}
				break;
			case 3:
				isRunning=false;
				LogUtil.d("apk is exist .....");
				if (callbabk != null) {
					callbabk.onProgress(fileUrl, localfilePath, 100);
				}
				break;
			case 4:
				// progress pause
				if (callbabk != null) {
					callbabk.onPause(fileUrl, localfilePath);
				}
				break;
			default:
				break;
			}
		}
	}

	public class FileDownloaderThread extends Thread {

		public FileDownloaderThread() {
			compeleteSize = completeSize();
		}

		/**
		 * 获取已完成数据大小
		 * @return
		 */
		private int completeSize() {
			File file = new File(tmpFile);
			if (file.exists()) {
				return (int) file.length();
			} else {
				return 0;
			}
		}

		/**
		 * 获取网络文件大小
		 */
		private int getFileLength() {
			int fileSize = -1;
			try {
				URL url = new URL(fileUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				fileSize = connection.getContentLength();
				LogUtil.d("net fileSize=  " + fileSize);
				connection.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileSize;
		}

		/**
		 * 检测是否已下载完成
		 */
		private Boolean check() {
			fileSize = getFileLength();
			LogUtil.e("compeleteSize= " + compeleteSize);
			File apkFile = new File(localfilePath);
			if (apkFile.exists()) {
				int size = (int) apkFile.length();
				if (size == fileSize) {
					Message message = Message.obtain();
					message.what = 3;
					message.arg1 = 100;
					mHandler.sendMessage(message);
					return true;
				}else {
					apkFile.delete();
					return false;
				}
			} else {
				 if (fileSize == -1 || fileSize < compeleteSize) {
					Message message = Message.obtain();
					message.what = 0;
					mHandler.sendMessage(message);
					return true;
				} else if (fileSize == compeleteSize) {
					Message message = Message.obtain();
					message.what = 1;
					message.arg1 = 100;
					mHandler.sendMessage(message);
					return true;
				}
			}
			return false;
		}

		@Override
		public void run() {
			if (check()) {
				return;
			}

			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream is = null;
			try {
				URL url = new URL(fileUrl);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				// 设置范围，格式为Range：bytes x-y;
				connection.setRequestProperty("Range", "bytes=" + compeleteSize + "-" + fileSize);

				randomAccessFile = new RandomAccessFile(tmpFile, "rwd");
				randomAccessFile.seek(compeleteSize);
				is = connection.getInputStream();
				byte[] buffer = new byte[4096];
				int length = -1;
				while ((length = is.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					Message message = Message.obtain();
					message.what = 1;
					message.arg1 = compeleteSize * 100 / fileSize;
					mHandler.sendMessage(message);
					
					Thread.sleep(100);
					
					if (!isRunning) {
						break;
					}
					if (message.arg1==100) {
						break;
					}
					if (pause) {
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message message = Message.obtain();
				message.what = 0;
				mHandler.sendMessage(message);
			} finally {
				try {
					is.close();
					randomAccessFile.close();
					connection.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	public interface FileDownloadCallbabk {
		void onStart(String url, String filePath);

		void onProgress(String url, String filePath, int progress);

		void onFail(String url, String filePath,String msg);

		void onCancelled(String url, String filePath);
		
		void onPause(String url, String filePath);
	}

}
