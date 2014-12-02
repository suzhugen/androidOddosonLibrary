package com.oddoson.android.common.model;

import java.util.HashMap;
import java.util.Map;
import com.oddoson.android.common.util.RequestParamsUtil;

/**
 * url 请求参数类
 * 
 * @author Administrator
 * 
 */
public class RequestParam {
	Map<String, String> params;

	public RequestParam() {
		params = new HashMap<String, String>();
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, String value) {
		params.put(key, value);
	}

	/**
	 * 添加参数
	 * 
	 * @param key
	 * @param value
	 */
	public void put(String key, int value) {
		params.put(key, String.valueOf(value));
	}

	public String get(String key) {
		return params.get(key);
	}

	public Map<String, String> getParams() {
		return params;
	}
	
	public void setParams(Map<String, String>  params) {
		this.params=params;
	}

	/**
	 * 返回字符串参数 id=1&name=sss&key=8888
	 * 
	 * @return
	 */
	public String getParamsString() {
		String paramString = "";
		for (String key : params.keySet()) {
			paramString += key + "=" + params.get(key);
		}
		return paramString;
	}

	/**
	 * 解析 url请求参数为 RequestParam 对象
	 * @param urlOrParamsString
	 * @return
	 */
	public RequestParam parseUrlParams(String urlOrParamsString){
		setParams(RequestParamsUtil.parseUrlParams(urlOrParamsString).getParams());
		return this;
	}
}
