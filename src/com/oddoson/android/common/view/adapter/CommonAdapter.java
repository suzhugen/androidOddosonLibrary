package com.oddoson.android.common.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 公共适配器,减少重复代码。
 * @author oddoson
 * <pre>
 * 用法：
 *     需定制，则重写相应方法即可。
       public int getCount()
       {
         return super.getCount();
       }
  ---------------------------------------------
     final List<String> datas = new ArrayList<String>();
        datas.add("aaaaaa");
        datas.add("bbbbbb");
        datas.add("cccccc");
        datas.add("dddddd");
        mListView.setAdapter(new CommonAdapter<String>(this, R.layout.item,datas)
        {
            @Override
            protected void fillItemData(CommonViewHolder viewHolder,
                    String item, int position)
            {
                viewHolder.setTextForTextView(R.id.textView1, item);
            }
        });
 * </pre>
 *
 */
public abstract class CommonAdapter<T> extends BaseAdapter 
{
    /** 
     * Context 
     */  
    private Context mContext;  
    /** 
     * 要展示的数据列表 
     */  
    private List<T> mData;  
    /** 
     * 每一项的布局id,例如R.layout.my_listview_item. 
     */  
    private int mItemLayoutId = -1;  
  
    /** 
     * @param context Context 
     * @param itemLayoutResId 
     *            item的布局资源id (适用于listview、gridview等AbsListView子类)
     * @param dataSource 数据源 
     */  
    public CommonAdapter(Context context, int itemLayoutResId, List<T> dataSource) {  
        checkParams(context, itemLayoutResId, dataSource);  
        mContext = context;  
        mItemLayoutId = itemLayoutResId;  
        mData = dataSource;  
    }  
  
    /** 
     * 检查参数的有效性 
     *  
     * @param context 
     * @param itemLayoutResId 
     * @param dataSource 
     */  
    private void checkParams(Context context, int itemLayoutResId, List<T> dataSource) {  
        if (context == null || itemLayoutResId < 0 || dataSource == null) {  
            throw new RuntimeException(  
                    "context == null || itemLayoutResId < 0 || dataSource == null, please check your params");  
        }  
    }  
    /**
     * 重置数据,自动更新view
     * @param dataSource
     */
    public void setDatas(List<T> dataSource){
        mData = dataSource;
        notifyDataSetChanged();
    }
  
    /** 
     * 返回数据的总数 
     */  
    @Override  
    public int getCount() {  
        return mData.size();  
    }  
  
    /** 
     * 返回position位置的数据 
     */  
    @Override  
    public T getItem(int position) {  
        return mData.get(position);  
    }  
  
    /** 
     * item id, 返回position 
     */  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    /** 
     * 返回position位置的view, 即listview、gridview的第postion个view 
     */  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        CommonViewHolder viewHolder = CommonViewHolder.getViewHolder(mContext, convertView,  
                mItemLayoutId);  
        // 填充数据  
        fillItemData(viewHolder, getItem(position),position);  
        return viewHolder.getConvertView();  
    }  
  
    /** 
     * 将数据填充到视图中 
     *  
     * @param viewHolder 通用的ViewHolder, 里面会装载listview, 
     *            gridview等组件的每一项的视图
     * @param item 数据源的第position项数据 
     */  
    protected abstract void fillItemData(CommonViewHolder viewHolder, T itemData ,int position);
    
}
