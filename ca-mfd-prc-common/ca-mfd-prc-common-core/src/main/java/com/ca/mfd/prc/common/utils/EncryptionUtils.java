/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Optional;

/**
 * 加解密工具类
 *
 * @author eric.zhou
 */
public class EncryptionUtils {
    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);
    private static final int HEX_TO_BYTE_ARRAY = 2;

    /**
     * MD5
     */
    public static String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    private static IvParameterSpec createiv(final int ivSizeBytes, final Optional<SecureRandom> rng) {
        final byte[] iv = new byte[ivSizeBytes];
        final SecureRandom therng = rng.orElse(new SecureRandom());
        therng.nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * AES算法加密
     *
     * @Param:text原文
     */
    public static String encryptByAesForCryptoJs(String text) throws Exception {
        return aesEncrypt(text, "00000000000000000000000000000000", "0000000000000000");
    }

    /**
     * AES算法解密
     *
     * @Param:text原文
     */
    public static String decryptByAesForCryptoJs(String text) throws Exception {
        return aesDecrypt(text, "00000000000000000000000000000000", "0000000000000000");
    }

    /**
     * AES算法加密
     *
     * @Param text 原文
     * @Param key 密钥
     * @Param iv 密钥
     */
    public static String aesEncrypt(byte[] texts, byte[] key, byte[] iv) throws Exception {

        // 创建AES加密算法实例(根据传入指定的秘钥进行加密) ECB
        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivforcbc = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivforcbc);
        // 将传入的文本加密
        byte[] encrypted = cipher.doFinal(texts);
        return bytesToHex(encrypted);
    }


    /**
     * AES算法加密
     *
     * @Param text原文
     * @Param key密钥
     * @Param iv
     */
    public static String aesEncrypt(String text, String key, String iv) throws Exception {
        return aesEncrypt(text.getBytes(), key.getBytes(StandardCharsets.UTF_8), iv.substring(0, 16).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * AES算法解密
     *
     * @Param:base64Encrypted密文
     */
    public static String aesDecrypt(String base64Encrypted) throws Exception {
        return aesDecrypt(base64Encrypted, "00000000000000000000000000000000", "0000000000000000");
    }

    /**
     * AES算法解密
     *
     * @Param:base64Encrypted密文
     * @Param:key密钥
     */
    public static String aesDecrypt(String base64Encrypted, String key, String iv) throws Exception {
        // 创建AES解密算法实例 CBC  ECB
        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

        // 初始化为解密模式，并将密钥注入到算法中
        IvParameterSpec ivforcbc = new IvParameterSpec(iv.substring(0, 16).getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivforcbc);

        // 将Base64编码的密文解码
        byte[] encrypted = hexToByteArray(base64Encrypted);
        // 解密
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

//    public static String aesDecrypt1(String base64Encrypted, String encryptionKey, String iv) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
//        try {
//            // 将密文进行 Base64 解码
//            byte[] encryptedBytes = hexStringToByteArray(base64Encrypted);
//            // 设置 AES 密码及密钥长度
//            byte[] keyBytes = encryptionKey.getBytes("UTF-8");
//            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
//
//            // 设置加密模式和填充方式
//            Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
//            // 设置初始向量
//            byte[] ivBytes = iv.getBytes("utf-8");
//            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//
//            // 进行解密操作
//            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
//            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//
//            // 将解密结果转为字符串
//            String decryptedText = new String(decryptedBytes);
//            return decryptedText;
//        } catch (Exception e) {
//            logger.error("aes解密失败", e);
//            throw e;
//        }
//    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % HEX_TO_BYTE_ARRAY == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += HEX_TO_BYTE_ARRAY) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

//    public static byte[] hexStringToByteArray(String hexStr) {
//        hexStr = hexStr.toUpperCase();
//        int len = hexStr.length() / 2;
//        char[] hexChars = hexStr.toCharArray();
//        byte[] bytes = new byte[len];
//        for (int i = 0; i < len; i++) {
//            int pos = i * 2;
//            bytes[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//        }
//        return bytes;
//    }

    public static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * DES算法加密
     *
     * @Param text 原文
     * @Param key 密钥
     * @Param iv 密钥
     */
//    public static byte[] desEncrypt(byte[] datasource, byte[] key, byte[] iv) {
//        try {
//            DESKeySpec desKey = new DESKeySpec(key);
//            //创建一个密匙工厂，然后用它把DESKeySpec转换成
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey securekey = keyFactory.generateSecret(desKey);
//            //Cipher对象实际完成加密操作
//            Cipher cipher = Cipher.getInstance("DES/GCM/PKCS5Padding");
//            //用密匙初始化Cipher对象
//            IvParameterSpec ivforcbc = new IvParameterSpec(iv);
//            cipher.init(Cipher.ENCRYPT_MODE, securekey, ivforcbc);
//            //现在，获取数据并加密
//            return cipher.doFinal(datasource);
//        } catch (Throwable e) {
//            logger.error(e.getMessage());
//        }
//        return null;
//    }

    /**
     * DES算法解密
     *
     * @Param text 原文
     * @Param key 密钥
     * @Param iv 密钥
     */
//    public static byte[] desDecrypt(byte[] src, byte[] key, byte[] iv) throws Exception {
//        DESKeySpec desKey = new DESKeySpec(key);
//        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//        SecretKey securekey = keyFactory.generateSecret(desKey);
//        Cipher cipher = Cipher.getInstance("DES/GCM/PKCS5Padding");
//        IvParameterSpec ivforcbc = new IvParameterSpec(iv);
//        cipher.init(Cipher.DECRYPT_MODE, securekey, ivforcbc);
//        return cipher.doFinal(src);
//    }

}