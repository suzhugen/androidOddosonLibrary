package com.oddoson.android.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.oddoson.android.common.R;
import com.oddoson.android.common.view.XToast;

public class QrCodePreviewActivity extends Activity implements OnClickListener {

	Button copy_btn,open_view_btn;
	String data = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_preview);
		initView();
	}

	public void initView() {
		data = getIntent().getStringExtra("data");

		copy_btn = (Button) findViewById(R.id.copy_btn);
		open_view_btn = (Button) findViewById(R.id.open_view_btn);
		TextView msgTextView = (TextView) findViewById(R.id.msg);
		copy_btn.setOnClickListener(this);
		open_view_btn.setOnClickListener(this);

		if (TextUtils.isEmpty(data)) {
			msgTextView.setText("无内容");
		} else {
			msgTextView.setText(data);
		}
		if (data.startsWith("http://") || data.startsWith("https://")) {
			open_view_btn.setVisibility(View.VISIBLE);
		}else {
			open_view_btn.setVisibility(View.GONE);
		}

		ImageView backImageView = (ImageView) findViewById(R.id.top_back);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.copy_btn) {
			ClipboardManager clip = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clip.setText(data); // 复制
			XToast.show(this, "已复制");
		} else if (id == R.id.open_view_btn) {
			// 网页，或下载
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(data);
			intent.setData(content_url);
			startActivity(intent);
		} else {
		}
	}

}
