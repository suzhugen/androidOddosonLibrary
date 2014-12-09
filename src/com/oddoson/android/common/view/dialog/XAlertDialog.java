package com.oddoson.android.common.view.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.oddoson.android.common.R;

public class XAlertDialog extends BaseDialog {

	public static final int style1=R.layout.view_alert_dialog;
	public static final int style2=R.layout.view_alert_dialog_style2;
	
	private Button button1, button2;
	private TextView contentTextView,titleTextView;
	String message,title="";

	public XAlertDialog(Context context, String message) {
		super(context, com.oddoson.android.common.R.style.Dialog_bocop);
		setContentView(R.layout.view_alert_dialog);
		this.message=message;
		init();
	}
	
	public XAlertDialog(Context context,String title,String message,int layoutId) {
		super(context, com.oddoson.android.common.R.style.Dialog_bocop);
		setContentView(layoutId);
		this.title=title;
		this.message=message;
		init();
	}
	
	void init(){
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		contentTextView = (TextView) findViewById(R.id.textView1);
		titleTextView = (TextView) findViewById(R.id.title);
		contentTextView.setText(message);
		titleTextView.setText(title);
	}
	
	public void setTitle(String titleString){
		title=titleString;
		init();
	}
	public void setMessage(String messageString){
		messageString=messageString;
		init();
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
