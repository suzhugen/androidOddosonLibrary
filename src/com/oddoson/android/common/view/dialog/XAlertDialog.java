package com.oddoson.android.common.view.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.oddoson.android.common.R;

public class XAlertDialog extends BasicDialog {

	private Button button1, button2;
	private TextView contentTextView;

	public XAlertDialog(Context context, String message) {
		super(context, com.oddoson.android.common.R.style.Dialog_bocop);
		setContentView(R.layout.view_alert_dialog);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		contentTextView = (TextView) findViewById(R.id.textView1);
		contentTextView.setText(message);
	}

	public Button getConfirmButton() {
		return button1;
	}

	public Button getCancelButton() {
		return button2;
	}

	public void setConfirmOnClickListener(
			android.view.View.OnClickListener clickListener) {
		button1.setOnClickListener(clickListener);
	}

	public void setCancelOnClickListener(
			android.view.View.OnClickListener clickListener) {
		button2.setOnClickListener(clickListener);
	}

}
