package com.oddoson.android.common.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.oddoson.android.common.R;

public class BasicDialog extends Dialog{
	
	private Context context;
	
	private void init(){
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		getWindow().setBackgroundDrawableResource(R.color.transparent);
		setCanceledOnTouchOutside(true);
		setCancelable(true);
	}
	
	public BasicDialog(Context context) {
		//一定要加上主题，否则不显示
		super(context,com.oddoson.android.common.R.style.Dialog_bocop);
		this.context=context;
		init();
	}

	public BasicDialog(Context context, int theme) {
		super(context, theme);
		this.context=context;
		init();
	}	
	
	public Context getCurrentContext(){
		return context;
	}
	
    @Override
    public void show()
    {
        if (((Activity) getCurrentContext()).isFinishing())
        {
            return;
        }
        super.show();
    }

	@Override
	public void dismiss() {
		if (((Activity)context).isFinishing()) {
			return;
		}
		super.dismiss();
	}

}
