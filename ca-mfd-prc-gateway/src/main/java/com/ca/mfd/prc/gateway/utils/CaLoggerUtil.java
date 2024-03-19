package com.ca.mfd.prc.gateway.utils;


import cn.hutool.core.util.StrUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;

import javax.crypto.Cipher;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * <p>功能描述: CaLoggerUtil.java</p>
 *
 * @author
 * @version 2023年7月4日
 */
public class CaLoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(CaLoggerUtil.class);
    private static final String UNKNOWN = "unknown";

    public static String getForwardedIP(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        try {
            if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
            }
        } catch (Exception e) {
            logger.error("request Forwarded-ip error", e);
        }
        ip = StrUtil.isBlank(ip) ? "-" : ip.split(",")[0];
        logger.info("request Forwarded-ip value: {}", ip);
        return ip;
    }

    public static String getClientIP(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("X-Requested-For");
        try {
            if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeaders().getFirst("Proxy-Client-IP");
            }
            if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
            }
            if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
            }
            if (StrUtil.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddress().getAddress().getHostAddress();
            }
        } catch (Exception e) {
            logger.error("get client-ip error", e);
        }
        return StrUtil.isBlank(ip) ? "-" : ip.split(",")[0];
    }

    public static String getLocationIp() {
        String curip = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            curip = address.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("get localhost-ip error", e);
        }
        return StrUtil.isBlank(curip) ? "-" : curip;
    }

    public static void traceResponse(ResponseEntity<?> response) {
        if (logger.isInfoEnabled()) {
            logger.info("============================response begin==========================================");
            logger.info("Status code  : {}", response.getStatusCode());
            logger.info("Headers      : {}", response.getHeaders());
            logger.info("=======================response end=================================================");
        }
    }

    public static String decryptByPublicKey(String publicKeyText, String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyText));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

    public static void main(String[] args) throws Exception {
        String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNAxmwY49PTotkSTPrShIvOJOYaEVBJfo+YyjkG9PWwtzIos1sphtA6jVwBBT3HdIL0Gs7xNXBaDd/GvqVve4TLT33MNJwdVd1IWkPnPldL4foqeP6iK2g5uSDGzlY/iN06ChWM8WSQz7DhoZg50L/weneKzhlC9hQXebPJzClawIDAQAB";
        String text = "K9s4+7cE9t6Mk727LWx8/YRBopDH3e7ByNxK24fuKrhAH+ePwhLGHN5gis99bLUQo/iy44RzAdG9c4QWvKDZEKInCXL97uXkojoRpUWtcuGcHfa5og1UwGIZVwatJSiFK2vy7RMqRG16Ls46SQrfoJmioz1K8rBQlOC8jY2qrfU=";
        System.out.println(CaLoggerUtil.decryptByPublicKey(key, text));
    }
}
