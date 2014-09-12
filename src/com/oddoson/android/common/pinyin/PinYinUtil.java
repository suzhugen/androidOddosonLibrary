package com.oddoson.android.common.pinyin;

import java.util.ArrayList;

import android.text.TextUtils;

import com.oddoson.android.common.pinyin.HanziToPinyin.Token;

/**
 * 也可以使用 pinyin4j.jar 里的工具
 * 
 * @author oddoson
 * 
 */
public class PinYinUtil
{
    // 汉字返回拼音，字母原样返回，都转换为大写
    public static String getPinYin(String input)
    {
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0)
        {
            for (Token token : tokens)
            {
                if (Token.PINYIN == token.type)
                {
                    sb.append(token.target);
                }
                else
                {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString().toUpperCase();
    }
    
    /**
     * 获取第一个字的首字母拼音
     * 
     * @param input
     * @return
     */
    public static char getFirstCharPinYin(String input)
    {
        if (TextUtils.isEmpty(input))
        {
            return ' ';
        }
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(String.valueOf(input.toCharArray()[0]));
        char[] sb ;
        if (tokens != null && tokens.size() > 0)
        {
            for (Token token : tokens)
            {
                if (Token.PINYIN == token.type)
                {
                    sb=token.target.toUpperCase().toCharArray();
                    int count=sb.length;
                    if (count>0)
                    {
                        return sb[0];
                    }
                }else
                {
                    return token.source.toUpperCase().toCharArray()[0];
                }
            }
        }
        return ' ';
    }
    
    /**
     * 获取第一个字的拼音
     * @param input
     * @return
     */
    public static String getFirstPinYin(String input)
    {
        if (TextUtils.isEmpty(input))
        {
            return "";
        }
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(input.substring(0, 1));
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0)
        {
            for (Token token : tokens)
            {
                if (Token.PINYIN == token.type)
                {
                    sb.append(token.target);
                    return sb.toString().toUpperCase();
                }else
                {
                    return token.source.toUpperCase();
                }
            }
        }
        return "";
    }
    
}
