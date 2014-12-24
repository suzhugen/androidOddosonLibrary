/*
 * Basic no frills app which integrates the ZBar barcode scanner with
 * the camera.
 * 
 * Created by lisah0 on 2012-02-24
 */
package com.oddoson.android.zbar;

import java.io.IOException;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.oddoson.android.camera.CameraConfigurationManager;
import com.oddoson.android.common.R;
import com.oddoson.android.common.ui.QrCodePreviewActivity;
import com.oddoson.android.common.util.AnimationUtil;
import com.oddoson.android.common.util.FileUtils;
import com.oddoson.android.common.util.LogUtil;
import com.oddoson.android.common.view.XToast;

/**
 * 条形码，二维码 扫描界面
 * 
 * @author Administrator
 * 
 */
public class CameraScanActivity extends Activity implements OnClickListener {
	
	 static
	    {
	        System.loadLibrary("iconv");
	    }

	public static final int status_decode_success = 1001;// 解码成功

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private CommonHandler commonHandler;
	private Thread decodeThread;// 解码线程

	private static int autoFocusTime = 1500;// 间隔1s
	private ImageScanner scanner;
	private Image barcode;
	private boolean barcodeScanned = false;// 扫描成功
	private boolean previewing = false; // 预览中
	private boolean decodeing = false; // 解码中
	private boolean flashLighting = false;

	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.50f;
	private boolean vibrate;
	private static final long VIBRATE_DURATION = 200L;

	private ImageView top_back, barcode_torch;
	private TextView tuku_tv;
	private FrameLayout previewContainer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_barcode_scan);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();
		commonHandler = new CommonHandler();

		initView();

		initScanner();

	}

	void initView() {
		top_back = (ImageView) findViewById(R.id.top_back);
		tuku_tv = (TextView) findViewById(R.id.tuku_tv);
		barcode_torch = (ImageView) findViewById(R.id.barcode_torch);

		tuku_tv.setOnClickListener(this);
		top_back.setOnClickListener(this);
		barcode_torch.setOnClickListener(this);

		playBeep = true;
		vibrate = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		
		previewContainer = (FrameLayout) findViewById(R.id.cameraPreview);

		initBeepSound();
		
		initScanAnim();
	}

	void initScanAnim() {
		ImageView mQrLineView = (ImageView) findViewById(R.id.capture_scan_line);
		TranslateAnimation animation = (TranslateAnimation) AnimationUtil
				.loadAnimation(this, R.anim.barcode_line_translate);
		mQrLineView.startAnimation(animation);
	}

	// 解码器
	private void initScanner() {
		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		//scanner.setConfig(0, Config.Y_DENSITY, 3);
	}

	private class CommonHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case CameraScanActivity.status_decode_success :
					playBeepSoundAndVibrate();
					previewing = false;
					mCamera.setPreviewCallback(null);
					mCamera.stopPreview();
					SymbolSet syms = scanner.getResults();
					for (Symbol sym : syms) {
						barcodeScanned = true;
						Intent mIntent = new Intent(CameraScanActivity.this,
								QrCodePreviewActivity.class);
						mIntent.putExtra("type", sym.getType());
						mIntent.putExtra("data", sym.getData());
						startActivity(mIntent);
						break;
					}
					decodeing = false;
					break;

				default :
					break;
			}
		}
	}

	private Runnable startScanRunnable = new Runnable() {
		public void run() {
			startScan();
		}
	};

	public void startScan() {
		if (mCamera == null) {
			mCamera = getCameraInstance();
		}
		if (barcodeScanned) {
			barcodeScanned = false;
			previewing = true;
			decodeing=false;

			decodeThread = new Thread(decodeRunnable);
			decodeThread.start();
			
			mPreview = new CameraPreview(this, mCamera, previewCallback,
					autoFocusCB);
			previewContainer.removeAllViews();
			previewContainer.addView(mPreview);
			
			if (flashLighting) {
				flashLightOn();
			} else {
				flashLightOff();
			}
		}
	}
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
				e.printStackTrace();
			}
		}
	}
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		// if (vibrate) {
		// Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// vibrator.vibrate(VIBRATE_DURATION);
		// }
	}

	public void onPause() {
		super.onPause();
		previewContainer.removeAllViews();
		releaseCamera();
	}

	@Override
	protected void onResume() {
		super.onResume();
		barcodeScanned = true;
		commonHandler.postDelayed(startScanRunnable, 100);
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			flashLightOff();
			mCamera.setPreviewCallbackWithBuffer(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	// 相机预览回调，解码
	PreviewCallback previewCallback = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			if (!decodeing) {
				Camera.Parameters parameters = camera.getParameters();
				Size size = parameters.getPreviewSize();
				// 这里可以控制解码范围
				barcode = new Image(size.width, size.height, "Y800");// "Y800"
				barcode.setData(data);
				decodeing = true;
			}
		}
	};
	private Runnable decodeRunnable = new Runnable() {
		public void run() {
			while (previewing) {
				try {
					if (barcode != null && decodeing) {
						int result = scanner.scanImage(barcode);// 解码
						barcode = null;
						if (result != 0) {
							commonHandler
									.sendEmptyMessage(status_decode_success);
						}else {
							decodeing=false;
						}
					}
					Thread.sleep(0);
				} catch (Exception e) {
					e.printStackTrace();
					decodeing = false;
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, autoFocusTime);
		}
	};

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.top_back) {
			finish();
		} else if (id == R.id.tuku_tv) {
			Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
			getImage.addCategory(Intent.CATEGORY_OPENABLE);
			getImage.setType("image/*");
			startActivityForResult(getImage, 999);
		} else if (id == R.id.barcode_torch) {
			flashLighting = !flashLighting;
			if (flashLighting) {
				flashLightOn();
			} else {
				flashLightOff();
			}
		}
	}

	void flashLightOn() {
		barcode_torch.setImageResource(R.drawable.barcode_torch_on);
		CameraConfigurationManager.flashLightOn(mCamera);
	}
	void flashLightOff() {
		barcode_torch.setImageResource(R.drawable.barcode_torch_off);
		CameraConfigurationManager.flashLightOff(mCamera);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 999) {
			String result = ScanDecodeManager.scanningImage(FileUtils
					.getUriPath(this, data.getData()));
			if (result == null) {
				XToast.show(this, "不是正确的二维码图片");
			} else {
				Intent mIntent = new Intent(CameraScanActivity.this,
						QrCodePreviewActivity.class);
				mIntent.putExtra("type", "");
				mIntent.putExtra("data", result);
				startActivity(mIntent);
			}
		}
	}

}
