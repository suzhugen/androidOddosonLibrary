package com.oddoson.android.common.util;

import java.util.HashMap;
import java.util.Map;
import com.oddoson.android.common.model.RequestParam;

/**
 * url 参数解析类
 * 
 * @author Administrator
 * 
 */
public class RequestParamsUtil {

	/**
	 * 解析URL，[0]=网址，[1]=参数字符串
	 * 
	 * @param strURL
	 * @return
	 */
	public static String[] splitUrl(String url) {
		url = url.trim().toLowerCase();
		String[] arrSplit = new String[2];
		String[] temp = url.split("[?]", 2);
		if (temp.length == 1) {
			if (url.contains("?")) {
				arrSplit[1] = "";
			} else {
				arrSplit[0] = "";
				arrSplit[1] = temp[0];
			}
		} else {
			arrSplit[0] = temp[0];
			arrSplit[1] = temp[1];
		}
		return arrSplit;
	}

	/**
	 * 解析url请求参数，返回 [参数名,值] 键值对
	 * 
	 * @return Map<String, String> <参数名，值>
	 */
	public static RequestParam parseUrlParams(String url) {
		RequestParam params = new RequestParam();
		String paramsString = splitUrl(url)[1];
		String[] arrayStrings = paramsString.split("[&]");
		for (String param : arrayStrings) {
			String[] arrSplitEqual = new String[2];
			String[] temp = param.split("[=]", 2);
			if (temp.length == 1) {
				arrSplitEqual[0] = temp[0];
				arrSplitEqual[1] = "";
			} else {
				arrSplitEqual[0] = temp[0];
				arrSplitEqual[1] = temp[1];
			}
			params.put(arrSplitEqual[0], arrSplitEqual[1]);
		}
		return params;
	}
}
