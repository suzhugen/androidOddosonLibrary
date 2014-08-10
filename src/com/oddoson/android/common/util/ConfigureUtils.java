package com.oddoson.android.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import com.oddoson.android.common.security.Base64;

import android.util.Log;
import android.util.Xml.Encoding;

/**
 * 配置保存类
 * @author su
 *
 */
public class ConfigureUtils {

	/**
	 * 保存配置
	 * 
	 * @param key
	 * @param value
	 * @param tableName
	 * @param path
	 */
	public static void saveByKey(String key, String value, String tableName,
			String path) {
		String dataPath = path + "/";
		String fileName = obfuscator(tableName, key);
		File file = new File(dataPath + fileName);
		File dir = new File(dataPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
		if (value == null) {
			value = "";
		}
		value = Base64.encode(value.getBytes());

		try {
			if (file.createNewFile()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(value);
				writer.close();
			} else {
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteByKey(String key, String tableName, String path) {
		String dataPath = path + "/";
		String fileName = obfuscator(tableName, key);
		File file = new File(dataPath + fileName);
		File dir = new File(dataPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (file.exists()) {
			file.delete();
		}
	}

	public static String loadByKey(String key, String tableName, String path) {
		String dataPath = path + "/";
		String fileName = obfuscator(tableName, key);
		String value = "";
		File file = new File(dataPath + fileName);
		if (file.exists()) {
			value = new String(Base64.decode(FileUtils.readFile(file.getPath(),
					Encoding.UTF_8.toString()).toString()));
		}
		return value;
	}

	private static String obfuscator(String tableName, String key) {
		String result = tableName.hashCode() + "_" + key.hashCode() + ".key";
		return result;
	}
}
