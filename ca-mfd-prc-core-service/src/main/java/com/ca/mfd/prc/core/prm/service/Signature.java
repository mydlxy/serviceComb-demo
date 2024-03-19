package com.ca.mfd.prc.core.prm.service;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

/**
 * @author tom.wu
 */
public class Signature {

    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
    private static final String ENCRYPTKEY = "@GSMASH1";
    private static final String ENCRYPTKEYCOOKIE = "@GSMASH2";
    /**
     * Authorization=Bearer app:{appId}:{sign}
     */
    private static final String AUTHORIZATION_FORMAT = "app:%s:%s:%s";
    private static final String DELIMITER = "\n";

    public static String signature(String timestamp, String pathWithQuery, String secret) {
        String stringToSign = timestamp + DELIMITER + pathWithQuery.toLowerCase();
        return HmacSha1Utils.signString(stringToSign, secret);
    }

    public static Map<String, String> buildHttpHeaders(String url, String appId, String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        long currentTimeMillis = System.currentTimeMillis();
        String timestamp = String.valueOf(currentTimeMillis);

        String pathWithQuery = url2PathWithQuery(url);
        String signature = signature(timestamp, pathWithQuery, secret);

        String auth = String.format(AUTHORIZATION_FORMAT, appId, timestamp, signature);
        byte[] signData = auth.getBytes(StandardCharsets.UTF_8);
        String authBase64 = Base64.getEncoder().encodeToString(signData);
        Map<String, String> headers = Maps.newHashMap();
        headers.put(HTTP_HEADER_AUTHORIZATION, "Bearer " + authBase64);

        return headers;
    }


    private static String url2PathWithQuery(String urlString) {
        try {
            URL url = new URL(urlString);
            String path = url.getPath();
            String query = url.getQuery();

            String pathWithQuery = path;
            if (query != null && query.length() > 0) {
                pathWithQuery += "?" + query;
            }
            return pathWithQuery;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid url pattern: " + urlString, e);
        }
    }

    /**
     * 加密Token
     *
     * @param txt
     * @return 结果
     */
    public static String encryptToken(String txt) {
        String t = txt == null ? "" : txt;
        Integer length = 36;
        if (t.length() > length) {
            char[] text = t.toCharArray();
            //第二位和第三位对调
            char secondChar = text[1];
            text[1] = text[2];
            text[2] = secondChar;
            //第十位和第是一位对调
            char tenChar = text[9];
            text[9] = text[10];
            text[10] = tenChar;
            ArrayUtils.reverse(text);
            t = String.valueOf(text);
        }
        return Base64.getEncoder().encodeToString(t.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解密token
     *
     * @param txt
     * @return 结果
     */
    public static String decryptToken(String txt) {
        String t = txt == null ? "" : txt;
        byte[] base64 = org.apache.commons.codec.binary.Base64.decodeBase64(t);
        t = new String(base64, StandardCharsets.UTF_8);
        Integer length = 36;
        if (t.length() > length) {
            char[] text = t.toCharArray();
            ArrayUtils.reverse(text);
            //第二位和第三位对调
            char secondChar = text[1];
            text[1] = text[2];
            text[2] = secondChar;
            //第十位和第是一位对调
            char tenChar = text[9];
            text[9] = text[10];
            text[10] = tenChar;
            t = String.valueOf(text);
        }
        return t;
    }

    /**
     * 解密Cookie
     *
     * @param txt
     * @return 结果
     */
//    public static String decryptCookie(String txt) throws Exception {
//        return decrypt(txt, ENCRYPTKEYCOOKIE);
//    }

    /**
     * 解密数据
     *
     * @param txt
     * @param sKey
     * @return 结果
     */
//    private static String decrypt(String txt, String sKey) throws Exception {
//        int len = txt.length() / 2;
//        byte[] inputByteArray = new byte[len];
//        int x, i;
//        for (x = 0; x < len; x++) {
//            i = Integer.parseInt(txt.substring(x * 2, (x * 2) + 2), 16);
//            inputByteArray[x] = (byte) i;
//        }
//        byte[] key = EncryptionUtils.md5(sKey).substring(0, 8).getBytes(StandardCharsets.UTF_8);
//        byte[] iv = EncryptionUtils.md5(sKey).substring(0, 8).getBytes(StandardCharsets.UTF_8);
//
//        byte[] dsrc = EncryptionUtils.desDecrypt(inputByteArray, key, iv);
//        return new String(dsrc, StandardCharsets.UTF_8);
//
//    }

    /**
     * 加密Cookie
     *
     * @param txt
     * @return 结果
     */
//    public static String encryptCookie(String txt) throws Exception {
//        return encrypt(txt, ENCRYPTKEYCOOKIE);
//    }

    /**
     * 加密数据
     *
     * @param txt
     * @return 结果
     */
//    public static String encrypt(String txt, String sKey) {
//        byte[] inputByteArray = txt.getBytes(StandardCharsets.UTF_8);
//        byte[] key = EncryptionUtils.md5(sKey).substring(0, 8).getBytes(StandardCharsets.UTF_8);
//        byte[] iv = EncryptionUtils.md5(sKey).substring(0, 8).getBytes(StandardCharsets.UTF_8);
//        byte[] dsrc = EncryptionUtils.desEncrypt(inputByteArray, key, iv);
//        return EncryptionUtils.bytesToHex(dsrc);
//    }

}