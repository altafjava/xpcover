package com.gmc.main.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptorDecryptor {

	private static SecretKeySpec secretKey;
	private static byte[] keyArray;
	static String myKey = "altaf";
	static {
		MessageDigest sha = null;
		try {
			keyArray = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			keyArray = sha.digest(keyArray);
			keyArray = Arrays.copyOf(keyArray, 16);
			secretKey = new SecretKeySpec(keyArray, "AES");
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			log.error("Error while creating MessageDigest: {}", e);
		}

	}

	public static String encrypt(String rawValue) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(rawValue.getBytes("UTF-8")));
		} catch (Exception e) {
			log.error("Error while encrypting: {}", e);
		}
		return null;
	}

	public static String decrypt(String encryptedValue) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedValue)));
		} catch (Exception e) {
			log.error("Error while decrypting: {}" + e);
		}
		return null;
	}
}
