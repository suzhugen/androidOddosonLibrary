package com.oddoson.android.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.text.TextUtils;

public class MD5 {

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final char HEX_DIGITS_LOWER[] = { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 
	 * @param val
	 * @param lower
	 *            是否小写字母
	 * @return
	 */
	public static String getMD5(byte[]  val, Boolean lower) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(val);
			byte[] m = md5.digest();// 加密
			if (lower) {
				return toHexLowerString(m);
			} else {
				return toHexString(m);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 默认小写 md5
	 * @param s
	 * @return
	 */
	public static String md5(String s) {
		return getMD5(s.getBytes(), true);
	}
	
	/**
	 * 16位加密,小写
	 * @param s
	 * @return
	 */
	public static String md5_16(String s) {
		String mi=getMD5(s.getBytes(), true);
		if (!TextUtils.isEmpty(mi)) {
			return mi.substring(8, 24);
		}
		return mi;
	}
	
	/**
	 * 大写16位
	 * @param s
	 * @return
	 */
	public static String md5_16_upper(String s) {
		String mi=getMD5(s.getBytes(), false);
		if (!TextUtils.isEmpty(mi)) {
			return mi.substring(7, 23);
		}
		return mi;
	}
	

	/**
	 * 转大写
	 * @param b
	 * @return
	 */
	public static String toHexString(byte[] b) {
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * 转小写
	 * 
	 * @param b
	 * @return
	 */
	public static String toHexLowerString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS_LOWER[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS_LOWER[b[i] & 0x0f]);
		}
		return sb.toString();
	}

}
