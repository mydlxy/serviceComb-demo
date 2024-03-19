package com.ca.mfd.prc.core.integrated.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.common.dto.PortalBaseDto;
import com.ca.mfd.prc.common.dto.core.OauthLoginInfo;
import com.ca.mfd.prc.common.entity.DepartmentDTO;
import com.ca.mfd.prc.common.entity.GraphicCodeDTO;
import com.ca.mfd.prc.common.entity.OauthLoginDTO;
import com.ca.mfd.prc.common.entity.QueryUserDTO;
import com.ca.mfd.prc.common.entity.QueryUserOrgDTO;
import com.ca.mfd.prc.common.entity.QueryUserPermissionDTO;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.integrated.config.IntegratedConfig;
import com.ca.mfd.prc.core.integrated.service.UserService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String SUCCESS_CODE = "00000";  // 统一门户验证成功的代码
    @Autowired
    private IntegratedConfig integratedConfig;
    @Autowired
    @Qualifier("casdkrestemplate")
    @Lazy
    private RestTemplate casdkrestemplate;

    @Override
    public QueryUserDTO queryUser(Long userId, String loginId) {
        QueryUserDTO userDTO;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Appkey", integratedConfig.getAppkey());
        headers.set("appCode", integratedConfig.getAppCode());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestparam = new HttpEntity<>(headers);

        // 发起请求
        StringBuilder urlsb = new StringBuilder(integratedConfig.getQueryUserUrl());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString());
        if(userId!=null){
            builder.queryParam("userId", userId);
        }
        if(loginId!=null){
            builder.queryParam("loginId", loginId);
        }
        ParameterizedTypeReference<PortalBaseDto<QueryUserDTO>> responseType = new ParameterizedTypeReference<PortalBaseDto<QueryUserDTO>>() {
        };

        try {
            ResponseEntity<PortalBaseDto<QueryUserDTO>> response = casdkrestemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
            PortalBaseDto dto = response.getBody();
            if (!Objects.equals(dto.getCode(), SUCCESS_CODE)) {
                logger.error("获取用户信息失败 | userId: {}, msg: {}", userId, dto.getMsg());
                throw new InkelinkException("获取用户信息失败 | userId:" + userId + "msg:" + dto.getMsg());
            }
            //返回data数据
            logger.error("获取用户信息成功:" + JSONObject.toJSON(response.getBody()));
            userDTO = (QueryUserDTO) dto.getData();
            return userDTO;
        } catch (Exception e) {
            logger.info("根据工号获取用户信息-调用门户接口异常:" + e.getMessage());
            return null;
        }
    }

    @Override
    public QueryUserOrgDTO queryUserOrg(Long userId) {
        QueryUserOrgDTO userOrgDTO;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Appkey", integratedConfig.getAppkey());
        headers.set("appCode", integratedConfig.getAppCode());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestparam = new HttpEntity<>(headers);

        // 发起请求
        StringBuilder urlsb = new StringBuilder(integratedConfig.getQueryUserOrgUrl());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString())
                .queryParam("userId", userId);
        ParameterizedTypeReference<PortalBaseDto<QueryUserOrgDTO>> responseType = new ParameterizedTypeReference<PortalBaseDto<QueryUserOrgDTO>>() {
        };

        try {
            ResponseEntity<PortalBaseDto<QueryUserOrgDTO>> response = casdkrestemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
            PortalBaseDto dto = response.getBody();
            if (!Objects.equals(dto.getCode(), SUCCESS_CODE)) {
                logger.error("获取用户多组织信息失败 | userId: {}, msg: {}", userId, dto.getMsg());
                throw new InkelinkException("获取用户多组织信息失败 | userId:" + userId + "msg:" + dto.getMsg());
            }
            //返回data数据
            logger.error("获取用户多组织信息成功:" + JSONObject.toJSON(response.getBody()));
            userOrgDTO = (QueryUserOrgDTO) dto.getData();
            return userOrgDTO;
        } catch (Exception e) {
            logger.info("根据工号获取用户多组织信息-调用门户接口异常:" + e.getMessage());
            return null;
        }
    }

    @Override
    public QueryUserPermissionDTO queryUserPermission(Long userId) {
        QueryUserPermissionDTO userPermissionDTO;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Appkey", integratedConfig.getAppkey());
        headers.set("appCode", integratedConfig.getAppCode());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestparam = new HttpEntity<>(headers);

        // 发起请求
        StringBuilder urlsb = new StringBuilder(integratedConfig.getQueryUserPermissionUrl());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString())
                .queryParam("userId", userId);
        ParameterizedTypeReference<PortalBaseDto<QueryUserPermissionDTO>> responseType = new ParameterizedTypeReference<PortalBaseDto<QueryUserPermissionDTO>>() {
        };

        try {
            ResponseEntity<PortalBaseDto<QueryUserPermissionDTO>> response = casdkrestemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
            PortalBaseDto dto = response.getBody();
            if (!Objects.equals(dto.getCode(), SUCCESS_CODE)) {
                logger.error("获取用户权限列表信息失败 | userId: {}, msg: {}", userId, dto.getMsg());
                throw new InkelinkException("获取用户权限列表信息失败 | userId:" + userId + "msg:" + dto.getMsg());
            }
            //返回data数据
            logger.error("获取用户权限列表信息成功:" + JSONObject.toJSON(response.getBody()));
            userPermissionDTO = (QueryUserPermissionDTO) dto.getData();
            return userPermissionDTO;
        } catch (Exception e) {
            logger.info("根据工号获取用户权限列表信息-调用门户接口异常:" + e.getMessage());
            return null;
        }
    }

    @Override
    public GraphicCodeDTO getGraphicCode() {
        GraphicCodeDTO graphicCodeDTO;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Appkey", integratedConfig.getAppkey());
        headers.set("appCode", integratedConfig.getAppCode());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestparam = new HttpEntity<>(headers);

        // 发起请求
        StringBuilder urlsb = new StringBuilder(integratedConfig.getGetGraphicCodeUrl());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString());
        ParameterizedTypeReference<PortalBaseDto<GraphicCodeDTO>> responseType = new ParameterizedTypeReference<PortalBaseDto<GraphicCodeDTO>>() {
        };

        try {
            ResponseEntity<PortalBaseDto<GraphicCodeDTO>> response = casdkrestemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
            PortalBaseDto dto = response.getBody();
            if (!Objects.equals(dto.getCode(), SUCCESS_CODE)) {
                logger.error("获取图形验证码失败 |  msg: {}", dto.getMsg());
                throw new InkelinkException("获取图形验证码失败 | msg:" + dto.getMsg());
            }
            //返回data数据
            logger.error("获取图形验证码成功:" + JSONObject.toJSON(response.getBody()));
            graphicCodeDTO = (GraphicCodeDTO) dto.getData();
            return graphicCodeDTO;
        } catch (Exception e) {
            logger.info("获取图形验证码失败-调用门户接口异常:" + e.getMessage());
            return null;
        }
    }

    @Override
    public ResultVO oauthLogin(OauthLoginInfo oauthLoginInfo) {
        ResultVO result=new ResultVO();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Appkey", integratedConfig.getAppkey());
            headers.set("appCode", integratedConfig.getAppCode());
            headers.setContentType(MediaType.APPLICATION_JSON);
            //密码加密
            oauthLoginInfo.setPassword(encryptByPublicKey(oauthLoginInfo.getPassword()));

            HttpEntity<Object> requestparam = new HttpEntity<>(oauthLoginInfo, headers);

            // 发起请求
            StringBuilder urlsb = new StringBuilder(integratedConfig.getOauthLoginUrl());
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString());
            ParameterizedTypeReference<PortalBaseDto<OauthLoginDTO>> responseType = new ParameterizedTypeReference<PortalBaseDto<OauthLoginDTO>>() {
            };
            ResponseEntity<PortalBaseDto<OauthLoginDTO>> response = casdkrestemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.POST, requestparam, responseType);
            PortalBaseDto dto = response.getBody();
            if (!Objects.equals(dto.getCode(), SUCCESS_CODE)) {
                logger.error("统一门户登录失败 | username: {}, msg: {}", oauthLoginInfo.getUsername(), dto.getMsg());
                result.setCode(-1);
                result.setMessage("统一门户登录失败："+dto.getMsg());
                return result;
            }
            //返回data数据
            logger.error("统一门户登录成功:" + JSONObject.toJSON(response.getBody()));
            OauthLoginDTO oauthLoginDTO = (OauthLoginDTO) dto.getData();
            result.ok(oauthLoginDTO);
            return result;
        } catch (Exception e) {
            logger.info("统一门户登录-调用门户接口异常:" + e.getMessage());
            result.setCode(-1);
            result.setMessage("统一门户登录-调用门户接口异常:" + e.getMessage());
            return result;
        }
    }

    @Override
    public List<DepartmentDTO> departmentQuery(String departmentId) {

        List<DepartmentDTO> departmentDTOList;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Appkey", integratedConfig.getAppkey());
        headers.set("appCode", integratedConfig.getAppCode());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestparam = new HttpEntity<>(headers);

        // 发起请求
        StringBuilder urlsb = new StringBuilder(integratedConfig.getDepartmentQueryUrl());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString())
                .queryParam("departmentId", departmentId);
        ParameterizedTypeReference<PortalBaseDto<List<DepartmentDTO>>> responseType = new ParameterizedTypeReference<PortalBaseDto<List<DepartmentDTO>>>() {
        };

        try {
            ResponseEntity<PortalBaseDto<List<DepartmentDTO>>> response = casdkrestemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
            PortalBaseDto dto = response.getBody();
            if (!Objects.equals(dto.getCode(), SUCCESS_CODE)) {
                logger.error("查询部门及下级部门信息失败 | departmentId: {}, msg: {}", departmentId, dto.getMsg());
                throw new InkelinkException("查询部门及下级部门信息失败 | departmentId:" + departmentId + "msg:" + dto.getMsg());
            }
            //返回data数据
            logger.error("查询部门及下级部门信息成功:" + JSONObject.toJSON(response.getBody()));
            departmentDTOList = (List<DepartmentDTO>) dto.getData();
            return departmentDTOList;
        } catch (Exception e) {
            logger.info("根据部门ID查询部门及下级部门信息-调用门户接口异常:" + e.getMessage());
            return null;
        }
    }

    public static String encryptByPublicKey(String text) throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec2 = new X509EncodedKeySpec(Base64.decodeBase64("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArVEZzgOtDhf+zXC5JkUHNaV/BE76RslOqLxzVl/ZKzASezeFKONX+dOdTC9aLNiwUkEC6nV3h6SUglLjGAbpJ56zTuaAjj4HtBc9MAfKqvnzOoSMzm2kPZTKDcvxruh/eYEKC4UoHj78WgH0whebmIi2tryKPBbKTh/GSR9oi31z2Xlt+7EM1J8JKIq1NsrUxXD6dhhNk9Q9rsAFMFcCc3+AiO+f+IZUaIhTz3/8JphfI5LhPipZQdal1/ShWXD5dCZcbsmpcOMXOvPMZPgQuOUKDownbqxrvYleRny+ujt4JXUUBjEsT9qSZa4zoVOyPyqCXf+c2VwE5Cld6ZMZXQIDAQAB"));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec2);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, publicKey);
        byte[] result = doLongerCipherFinal(1, cipher, text.getBytes());
        return Base64.encodeBase64String(result);
    }

    private static byte[] doLongerCipherFinal(int opMode, Cipher cipher, byte[] source) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (opMode == 2) {
            out.write(cipher.doFinal(source));
        } else {
            int offset = 0;

            int size;
            for (int totalSize = source.length; totalSize - offset > 0; offset += size) {
                size = Math.min(cipher.getOutputSize(0) - 11, totalSize - offset);
                out.write(cipher.doFinal(source, offset, size));
            }
        }

        out.close();
        return out.toByteArray();
    }
}
