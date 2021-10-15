package com.pageOfficeServer.loginkingdee;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES加密解密工具类
 * 
 * @author LiangMingHui
 *
 */
public class DESUtil {


	/**
	 * 安全密钥
	 */
	private final static String KEY_DATA = "ABCDEFGHIJKLMNOPQRSTWXYZabcdefghijklmnopqrstwxyz0123456789-_.";

	/**
	 * 数据加密
	 * 
	 * @param data
	 * @return
	 */
	public static String encrypt(String data) {
		return encrypt(data, "utf-8");
	}

	/**
	 * 数据解密
	 * 
	 * @param data
	 * @return
	 */
	public static String decrypt(String data) {
		return decrypt(data, "utf-8");
	}

	/**
	 * 数据加密
	 * 
	 * @param data
	 * @param charset
	 * @return
	 */
	public static String encrypt(String data, String charset) {
		String result = "";
		try {
			byte[] bytes = encrypt(data.getBytes(charset));
			if (null != bytes) {
				result = new String(new Base64().encode(bytes));
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 数据解密
	 * 
	 * @param data
	 * @param charset
	 * @return
	 */
	public static String decrypt(String data, String charset) {
		String result = "";
		try {
			byte[] bytes = decrypt(new Base64().decode(data));
			if (null != bytes) {
				result = new String(bytes, charset);
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 数据加密
	 * 
	 * @param data
	 * @return
	 */
	private static byte[] encrypt(byte[] data) {
		byte[] result = null;

		DESKeySpec desKeySpec = null;
		SecretKeyFactory secretKeyFactory = null;
		SecretKey secretKey = null;
		Cipher cipher = null;
		try {
			// 使用原始密钥数据创建DESKeySpec对象
			desKeySpec = new DESKeySpec(KEY_DATA.getBytes());

			// 创建密钥工厂
			secretKeyFactory = SecretKeyFactory.getInstance("DES");

			// 密钥工厂把DESKeySpec转换成一个SecretKey对象
			secretKey = secretKeyFactory.generateSecret(desKeySpec);

			// 利用Cipher对象实际完成加密操作
			cipher = Cipher.getInstance("DES");

			// 利用密钥初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());

			// 正式执行加密操作
			result = cipher.doFinal(data);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 数据解密
	 * 
	 * @param data
	 * @return
	 */
	private static byte[] decrypt(byte[] data) {
		byte[] result = null;

		DESKeySpec desKeySpec = null;
		SecretKeyFactory secretKeyFactory = null;
		SecretKey secretKey = null;
		Cipher cipher = null;
		try {
			// 使用原始密钥数据创建DESKeySpec对象
			desKeySpec = new DESKeySpec(KEY_DATA.getBytes());

			// 创建密钥工厂
			secretKeyFactory = SecretKeyFactory.getInstance("DES");

			// 密钥工厂把DESKeySpec转换成一个SecretKey对象
			secretKey = secretKeyFactory.generateSecret(desKeySpec);

			// 利用Cipher对象实际完成加密操作
			cipher = Cipher.getInstance("DES");

			// 利用密钥初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new SecureRandom());

			// 正式执行加密操作
			result = cipher.doFinal(data);
		} catch (Exception e) {
		}
		return result;
	}

}
