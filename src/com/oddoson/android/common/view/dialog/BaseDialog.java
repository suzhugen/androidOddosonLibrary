package com.oddoson.android.common.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

public class BaseDialog extends Dialog{
	
	public BaseDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	protected BaseDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dismiss() {
		if (((Activity)getContext()).isFinishing()) {
			return;
		}
		super.dismiss();
	}

}
