package com.oddoson.android.common.entity;
/**
 * 导航实体类
 * @author oddoson
 *
 * @param <T>
 */
public class IndexSideEntity<T>
{
    private char firstChar;
    private Boolean isFirst;
    private T data;
    
    public char getFirstChar()
    {
        return firstChar;
    }
    public void setFirstChar(char firstChar)
    {
        this.firstChar = firstChar;
    }
    public Boolean getIsFirst()
    {
        return isFirst;
    }
    public void setIsFirst(Boolean isFirst)
    {
        this.isFirst = isFirst;
    }
    public T getData()
    {
        return data;
    }
    public void setData(T data)
    {
        this.data = data;
    }
    
}
