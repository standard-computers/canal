package org.Canal.Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class Crypter {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";
    private static final int IV_SIZE = 16;

    public static String encrypt(String key, String content) throws Exception {
        SecretKeySpec secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[IV_SIZE];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(content.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String key, String encryptedContent) throws Exception {
        SecretKeySpec secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[IV_SIZE];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedContent);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static SecretKeySpec generateKey(String key) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(key.getBytes(StandardCharsets.UTF_8));
        keyBytes = Arrays.copyOf(keyBytes, 16);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String md5(String content) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(content.getBytes(CHARSET));
        return Base64.getEncoder().encodeToString(md.digest());
    }
}