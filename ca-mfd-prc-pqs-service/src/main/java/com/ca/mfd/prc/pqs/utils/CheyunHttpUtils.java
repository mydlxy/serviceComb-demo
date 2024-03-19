package com.ca.mfd.prc.pqs.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.communication.dto.CarFenceConditonDto;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mason.mei
 * @Description: 车云GPS寻车接口调用工具类
 * @date 2024年01月16日
 */
@Component
public class CheyunHttpUtils {
    private final static Logger logger = LoggerFactory.getLogger(CheyunHttpUtils.class);
    private static String appId = "874aa15f481b45b4b464ac697b4d4921";

    private static String appKey = "ESrORI7Qj2DeM5wqlk8wqg==";

    /**
     * Header 认证关键字
     */
    private static final String AUTHORIZATION = "Authorization";

    /**
     * 签名参数前缀
     */
    private static final String SIGN_PREFIX = "X-VCS";

    /**
     * X-VCS-Timestame header 时间戳
     */
    private static final String X_VCS_TIMESTAMP = "X-VCS-Timestamp";

    /**
     * 签名
     */
    private static final String SIGNATURE = "Signature";

    /**
     * sha256 加密方式
     */
    private static final String HMAC_SHA256 = "HmacSHA256";

    /**
     * utf8 编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";
    /**
     * 默认字符编码
     */
    private static final Charset UTF8 = Charset.forName(DEFAULT_ENCODING);
    /**
     * 签名连接字符
     */
    private static final String CONTACT_CHAR = "\n";

    private static final String ALGORITHM = "VCS-HMAC-SHA256";

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    public CheyunHttpUtils() throws NoSuchAlgorithmException {
    }

    public static String getToken(String tokenUrl, String appId, String appKey) {
        Map<String, Object> map = new HashMap<>(15);
        map.put("client_id", appId);
        map.put("client_secret", appKey);
        map.put("grant_type", "client_credentials");
        String post = HttpUtil.post(tokenUrl, map);
        logger.info(StrUtil.format("VBOSS 获取token 请求链接:{} 请求参数:{} \n 返回结果:{}", tokenUrl, JSONUtil.toJsonStr(map), post));
        if (JSONUtil.isJson(post)) {
            JSONObject jsonObject = JSONUtil.parseObj(post);
            if (ObjectUtil.isNull(jsonObject.getStr("error"))) {
                String access_token = jsonObject.getStr("access_token");
                return access_token;
            }
        }
        String format = StrUtil.format("VBOSS 获取token 失败 请求链接:{} 请求参数:{} \n 返回结果:{}", tokenUrl, JSONUtil.toJsonStr(map), post);
        logger.error(format);
        throw new RuntimeException(format);
    }

    /**
     *
     */
    public ResultVO<CarFenceConditonDto> carFenceConditon(String vin, String fenceCode) {
        ResultVO<CarFenceConditonDto> resultVO = new ResultVO<>();
        try {
            String tokenUrl = sysConfigurationProvider.getConfiguration("token", "cheyunApi");
            if (StringUtils.isBlank(tokenUrl)) {
                throw new InkelinkException("没有配置车云token令牌校验接口地址");
            }
            String gpsUrl = sysConfigurationProvider.getConfiguration("car-fence-conditon", "cheyunApi");
            if (StringUtils.isBlank(gpsUrl)) {
                throw new InkelinkException("没有配置车云GPS寻车接口地址");
            }
            //获取token
            String token = getToken(tokenUrl, appId, appKey);

            //GPS寻车接口调用
            HttpRequest post = HttpUtil.createPost(gpsUrl);
            LinkedHashMap<String, String> param = new LinkedHashMap<>();
            param.put("vin", vin);
            param.put("fenceCode", fenceCode);
            String jsonStr = JSONUtil.toJsonStr(param);
            Map<String, String> headers = new HashMap<>();
            headers.put("X-VCS-Access-Token", token);
            headers.put("X-VCS-Timestamp", String.valueOf(System.currentTimeMillis()));
            headers.put("X-VCS-Nonce", getRandomChar(4));
            String signature = genSignautre(post.getMethod().name(), appKey, ALGORITHM, headers, "/open-apigw/sda-factory-info-api-service/api/v3/gps/car-fence-conditon", null, jsonStr);
            headers.put(AUTHORIZATION, ALGORITHM + " " + SIGNATURE + "=" + signature);
            post.headerMap(headers, false);
            post.body(jsonStr, "application/json");
            String body = post.execute().body();
            logger.info(StrUtil.format("请求链接:{} 请求参数:{} \n 返回结果:{}", gpsUrl, jsonStr, body));
            //转换对象
            resultVO = JsonUtils.parseObject(body, ResultVO.class);
            //获取msg消息
            String msg = JSONUtil.parseObj(body).getStr("msg");
            resultVO.setMessage(msg);
            if (resultVO.getCode() == 0) {
                resultVO.setCode(ErrorCode.SUCCESS);
            }
            return resultVO;
        } catch (Exception e) {
            logger.info("GPS寻车接口调用异常:" + e.getMessage());
            resultVO.setMessage(e.getMessage());
            return resultVO;
        }
    }

    /**
     * 计算签名
     *
     * @param method        请求方式
     * @param signKey       sk
     * @param algorithmType 签名算法
     * @return sign
     */
    private static String genSignautre(String method, String signKey, String algorithmType, Map<String, String> headers, String path, String rawQueryStr, String body) {
        String canonicalRequest = method + CONTACT_CHAR
                + path + CONTACT_CHAR
                + handlerIfNull(rawQueryStr) + CONTACT_CHAR
                + getCanonicalFromHeader(headers) + CONTACT_CHAR
                + getHashedStr(handlerIfNull(body));
        return sha256Hex(signKey, algorithmType + CONTACT_CHAR + getHeaderParamsIgnoreCase(X_VCS_TIMESTAMP, headers) + CONTACT_CHAR + getHashedStr(canonicalRequest));
    }

    /**
     * 获取请求参数签名信息
     *
     * @param content 上下文
     * @return sha256 信息
     */
    private static String getHashedStr(String content) {
        return getSHA256(content).toLowerCase();
    }

    /**
     * 处理null为空字符串
     *
     * @param value value
     * @return String
     */
    private static String handlerIfNull(String value) {
        return null == value ? "" : value;
    }

    /**
     * header 中获取签名串 过滤、排序
     *
     * @param headers header信息
     * @return String
     */
    private static String getCanonicalFromHeader(Map<String, String> headers) {
        return headers.entrySet().stream().filter(x -> (x.getKey().startsWith(SIGN_PREFIX)) || x.getKey().startsWith(SIGN_PREFIX.toLowerCase()))
                // 过滤以sign_Prefix开始的参数
                .sorted(Map.Entry.comparingByKey())
                //转为key：value格式
                .map(value -> value.getKey().toLowerCase() + ":" + value.getValue() + CONTACT_CHAR)
                //转为字符串以Contact_Char连接
                .collect(Collectors.joining());
    }

    /**
     * 获取请求头参数忽略大小写
     *
     * @param name
     * @param headers
     * @return
     */
    private static String getHeaderParamsIgnoreCase(String name, Map<String, String> headers) {
        String param = headers.get(name);
        if (StringUtils.isEmpty(param)) {
            param = headers.get(name.toLowerCase());
        }
        return param;
    }

    /**
     * 计算SHA256值
     *
     * @param str
     * @return
     */
    private static String getSHA256(String str) {
        if (null == str) {
            return null;
        }
        return getSHA256(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 计算SHA256值
     *
     * @param bytes
     * @return
     */
    private static String getSHA256(byte[] bytes) {
        if (null == bytes) {
            return null;
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("计算SHA256出现异常", e);
            return null;
        }
        md.update(bytes);
        byte[] digestRes = md.digest();
        return getDigestStr(digestRes);
    }

    /**
     * @param origBytes
     * @return
     */
    private static String getDigestStr(byte[] origBytes) {
        String tempStr;
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < origBytes.length; i++) {
            // 这里按位与是为了把字节转整时候取其正确的整数，java中一个int是4个字节
            // 如果origBytes[i]最高位为1，则转为int时，int的前三个字节都被1填充了
            tempStr = Integer.toHexString(origBytes[i] & 0xff);
            if (tempStr.length() == 1) {
                stb.append("0");
            }
            stb.append(tempStr);
        }
        return stb.toString();
    }

    /**
     * HMAC sha256 加密
     *
     * @param signingKey
     * @param stringToSign
     * @return
     */
    private static String sha256Hex(String signingKey, String stringToSign) {
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(signingKey.getBytes(UTF8), HMAC_SHA256));
            return bytes2Hex(mac.doFinal(stringToSign.getBytes(UTF8)));
        } catch (Exception e) {
            //System.out.println("Fail to generate the signature");
            return null;
        }
    }

    /**
     * bytes 数组转十六进制字符串
     *
     * @param bytes
     * @return
     */
    private static String bytes2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b & 0xff));
        }
        return builder.toString();
    }

    /**
     * JAVA获得0-9,A-Z,a-z范围的随机数
     *
     * @param length 随机数长度
     * @return String
     */
    private static Random random;

    static {
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getRandomChar(int length) {
        char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(chr[random.nextInt(62)]);
        }
        return buffer.toString();
    }
}
