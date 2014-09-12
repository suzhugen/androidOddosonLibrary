package com.oddoson.android.common.interfaces;


/**
 * 导航栏 回调
 * @author oddoson
 *
 */
public interface IndexSideCallback
{
    /**
     * 获取listview要显示的位置
     * @param sectionChar 导航选中的字母
     * @param position 导航选中的位置
     * @return  listview要显示的位置
     */
    int getPositionForSection(char sectionChar,int position);
    
    /**
     * 设置listview选中项
     * @param position listview要显示的位置
     * @return
     */
    void setSelection(int position);
    
}
