package com.oddoson.android.common.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Des {
	/**
	 * 加密（使用DES算法）
	 * 
	 * @param txt
	 *            需要加密的文本
	 * @param key
	 *            密钥
	 * @return 成功加密的文本
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String enCrypto(String txt, String key)
			throws InvalidKeySpecException, InvalidKeyException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		StringBuffer sb = new StringBuffer();
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
		SecretKeyFactory skeyFactory = null;
		Cipher cipher = null;
		try {
			skeyFactory = SecretKeyFactory.getInstance("DES");
			cipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
		cipher.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherText = cipher.doFinal(txt.getBytes());
		for (int n = 0; n < cipherText.length; n++) {
			String stmp = (java.lang.Integer.toHexString(cipherText[n] & 0XFF));

			if (stmp.length() == 1) {
				sb.append("0" + stmp);
			} else {
				sb.append(stmp);
			}
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 解密（使用DES算法）
	 * 
	 * @param txt
	 *            需要解密的文本
	 * @param key
	 *            密钥
	 * @return 成功解密的文本
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String deCrypto(String txt, String key)
			throws InvalidKeyException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		String deStr = "";
		if (txt != null && key != null) {
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory skeyFactory = null;
			Cipher cipher = null;
			try {
				skeyFactory = SecretKeyFactory.getInstance("DES");
				cipher = Cipher.getInstance("DES");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			byte[] btxts = new byte[txt.length() / 2];
			for (int i = 0, count = txt.length(); i < count; i += 2) {
				btxts[i / 2] = (byte) Integer.parseInt(txt.substring(i, i + 2),
						16);
			}
			deStr = new String(cipher.doFinal(btxts));
		}
		return deStr;
	}

	/**
	 * 解密（使用DES算法） 解密前先对密文进行BASE64解密
	 * 
	 * @param txt
	 *            需要解密的文本
	 * @param key
	 *            密钥
	 * @return 成功解密的文本
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String base64DeCrypto(String txt, String key)
			throws InvalidKeyException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		if (txt != null) {
			txt = new String(Base64.decode(txt));
			txt = deCrypto(txt, key);
		} else {
			txt = "";
		}
		return txt;
	}

	/**
	 * 加密（使用DES算法） 加密后再对密文进行BASE64加密
	 * 
	 * @param txt
	 *            需要加密的文本
	 * @param key
	 *            密钥
	 * @return 成功加密的文本
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String base64EnCrypto(String txt, String key)
			throws InvalidKeySpecException, InvalidKeyException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		if (txt != null) {
			txt = enCrypto(txt, key);
			txt = Base64.encode(txt.getBytes());
		} else {
			txt = "";
		}
		return txt;
	}

}