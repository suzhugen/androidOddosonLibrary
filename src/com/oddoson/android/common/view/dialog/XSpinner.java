package com.oddoson.android.common.view.dialog;

import java.util.List;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.oddoson.android.common.R;
import com.oddoson.android.common.util.ScreenUtil;
import com.oddoson.android.common.view.adapter.CommonAdapter;
import com.oddoson.android.common.view.adapter.CommonViewHolder;

/**
 * 通用spinner
 * @author Administrator
 *
 */
public class XSpinner extends Dialog  implements OnItemClickListener
{
    
    public static final String[] Kind={"","Question","Resource","Prattle"};
    private List<String> datas;
    ListView listView;
    OnSelectListener listener;
    
    public XSpinner(Context context,List<String> datas){
        super(context,R.style.dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        
        this.datas=datas;
        
        listView=new ListView(context);
        listView.setBackgroundResource(R.drawable.dialog_bg);
        listView.setDivider(new ColorDrawable(0xffeaeaea));
        listView.setSelector(new ColorDrawable(0x00000000));
        listView.setDividerHeight(1);
       
        listView.setAdapter(new CommonAdapter<String>(context,R.layout.item_x_spinner,datas)
        {

            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                    String itemData, int position)
            {
                   viewHolder.setTextForTextView(R.id.tv, itemData);
            }
        });
        
        listView.setOnItemClickListener(this);
         
        LayoutParams layoutParams=new LayoutParams( ScreenUtil.getScreenWidth(getContext())*4/5, ScreenUtil.getScreenHeight(getContext())/2);
        setContentView(listView, layoutParams);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
        if (listener!=null)
        {
            listener.select(position, datas.get(position));
        }
        dismiss();
    }
    
    public void setOnItemClickListener(OnSelectListener listener){
        this.listener=listener;
    }

    public interface OnSelectListener{
        void select(int position,String data);
    }
}
