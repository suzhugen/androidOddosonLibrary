package com.oddoson.android.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import com.oddoson.android.common.R;

public class QrCodePreviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code_preview);
		initView();
	}

	public void initView() {
		String msg=getIntent().getStringExtra("msg");
		TextView msgTextView=(TextView) findViewById(R.id.msg);
		if (TextUtils.isEmpty(msg)) {
			msgTextView.setText("无内容");
		}else {
			msgTextView.setText(msg);
		}
		
		ImageView backImageView=(ImageView) findViewById(R.id.top_back);
		backImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	

}
