package com.oddoson.android.common.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 公共适配器ViewHolder
 * 
 * @author oddoson
 * 
 */
public class CommonViewHolder
{
    
    private View mContentView;
    /**
     * 每项的View的子控件。任意多种
     */
    private SparseArray<View> mViewMap = new SparseArray<View>();
    
    /**
     * 构造函数
     * 
     * @param context
     *            Context
     * @param layoutId
     *            ListView、GridView或者其他AbsListVew子类的 Item View的资源布局id
     */
    protected CommonViewHolder(Context context, int layoutId)
    {
        // 初始化布局, 装载ContentView
        LayoutInflater mInflater = LayoutInflater.from(context);
        mContentView = mInflater.inflate(layoutId, null);
        if (mInflater == null || mContentView == null)
        {
            throw new RuntimeException(
                    "ViewFinder init failed, mInflater == null || mContentView == null.");
        }
        // 将ViewHolder存储在ContentView的tag中
        mContentView.setTag(this);
    }
    
    /**
     * 获取CommonViewHolder，当convertView为空的时候从布局xml装载item view,
     * 并且将该CommonViewHolder设置为convertView的tag, 便于复用convertView.
     * 
     * @param context
     *            Context
     * @param convertView
     *            Item view
     * @param layoutId
     *            布局资源id, 例如R.layout.my_listview_item.
     * @return 通用的CommonViewHolder实例
     */
    public static CommonViewHolder getViewHolder(Context context,
            View convertView, int layoutId)
    {
        if (convertView == null)
        {
            return new CommonViewHolder(context, layoutId);
        }
        return (CommonViewHolder) convertView.getTag();
    }
    
    /**
     * @return 当前项的convertView, 在构造函数中装载
     */
    public View getConvertView()
    {
        return mContentView;
    }
    
    /**
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T findViewById(int viewId)
    {
        // 先从view map中查找,如果有的缓存的话直接使用,否则再从mContentView中找
        View tagetView = mViewMap.get(viewId);
        if (tagetView == null)
        {
            tagetView = mContentView.findViewById(viewId);
            mViewMap.put(viewId, tagetView);
        }
        return (T) tagetView;
    }
    
    /**
     * 查找 任意类型控件
     * 
     * @param imageViewId
     * @return
     */
    public <T extends View> T getTextView(int textViewId)
    {
        return findViewById(textViewId);
    }
    
    /**
     * 为id为textViewId的TextView设置文本内容
     * 
     * @param textViewId
     *            视图id
     * @param text
     *            要设置的文本内容
     */
    public void setTextForTextView(int textViewId, CharSequence text)
    {
        TextView textView = findViewById(textViewId);
        if (textView != null)
        {
            textView.setText(text);
        }
    }
    
    /**
     * 为ImageView设置图片
     * 
     * @param imageViewId
     *            ImageView的id, 例如R.id.my_imageview
     * @param drawableId
     *            Drawable图片的id, 例如R.drawable.my_photo
     */
    public void setImageForView(int imageViewId, int drawableId)
    {
        ImageView imageView = findViewById(imageViewId);
        if (imageView != null)
        {
            imageView.setImageResource(drawableId);
        }
    }
    
    /**
     * 为ImageView设置图片
     * 
     * @param imageViewId
     *            ImageView的id, 例如R.id.my_imageview
     * @param bmp
     *            Bitmap图片
     */
    public void setImageForView(int imageViewId, Bitmap bmp)
    {
        ImageView imageView = findViewById(imageViewId);
        if (imageView != null)
        {
            imageView.setImageBitmap(bmp);
        }
    }
    
    /**
     * 为CheckBox设置是否选中
     * 
     * @param checkViewId
     *            CheckBox的id
     * @param isCheck
     *            是否选中
     */
    public void setCheckForCheckBox(int checkViewId, boolean isCheck)
    {
        CheckBox checkBox = findViewById(checkViewId);
        if (checkBox != null)
        {
            checkBox.setChecked(isCheck);
        }
    }
}
