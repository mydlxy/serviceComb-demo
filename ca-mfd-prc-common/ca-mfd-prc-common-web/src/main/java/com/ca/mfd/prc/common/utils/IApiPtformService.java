package com.ca.mfd.prc.common.utils;

import com.alibaba.fastjson.JSON;
import com.ca.mfd.prc.common.config.ApiPlatformConfig;
import org.apache.commons.lang3.ObjectUtils;
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

import java.nio.charset.StandardCharsets;
import java.util.Map;


/**
 * servicecomb 不支持url直接访问方式，不能使用feign url访问API平台。
 *
 * @FeignClient(qualifiers = "apiPtService", name="ca-app-order-service",configuration = ApiPlatFeignConfiguration.class,
 * url = "${ca.cloud.apiplatform.platform-url}",fallbackFactory = IApiPtfromService.ApiPtfromServiceFallbackFactory.class)
 * @GetMapping(value ="/storagemanager/getstorage",produces = MediaType.APPLICATION_JSON_VALUE)
 * ResultVO<StorageEntity> getStorage(@RequestHeader(name = "userId",required=false)String userId,
 * @RequestParam(name = "storageId") String storageId);
 */

/**
 *
 * <p>功能描述: api生命周期调用 </p>
 * @author
 * @version 2023年7月5日
 */
@Service("apiPtService")
public class IApiPtformService implements IApiPtformBaseService {

    @Autowired
    private ApiPlatformConfig apiRemoteConfig;

    @Autowired
    @Qualifier("apiPtRestTemplate")
    @Lazy
    private RestTemplate apiPtRestTemplate;


    public String getapi(String path, Object pms, Map<String, String> heads) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("appKey", apiRemoteConfig.getApiAppKey());
        if (heads != null) {
            for (Map.Entry<String, String> hd : heads.entrySet()) {
                headers.add(hd.getKey(), hd.getValue());
            }
        }
        HttpEntity<Object> requestparam = new HttpEntity<>(pms, headers);
        StringBuilder urlsb = new StringBuilder();
        path = path == null ? "" : path.trim();
        if (path.startsWith("http:") || path.startsWith("https:")) {
            urlsb.append(path);
        } else {
            urlsb.append(apiRemoteConfig.getPlatformUrl());
            urlsb.append(path);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString());
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> response = apiPtRestTemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
        String rs = response.getBody();
        ApiPlatResponseUtil.traceResponse(response);
        return rs;
    }

    /**
     * http get 请求带上URL对象参数
     * @param path
     * @param pms
     * @param heads
     * @return
     */
    public String getApiEx(String path, Object pms, Map<String, String> heads) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("appKey", apiRemoteConfig.getApiAppKey());
        if (heads != null) {
            for (Map.Entry<String, String> hd : heads.entrySet()) {
                headers.add(hd.getKey(), hd.getValue());
            }
        }

        HttpEntity<Object> requestparam = new HttpEntity<>(pms, headers);
        StringBuilder urlsb = new StringBuilder();
        path = path == null ? "" : path.trim();
        if (path.startsWith("http:") || path.startsWith("https:")) {
            urlsb.append(path);
        } else {
            urlsb.append(apiRemoteConfig.getPlatformUrl());
            urlsb.append(path);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString());
        if (ObjectUtils.isNotEmpty(pms)) {
            Map<String, Object> paramMaps = JSON.parseObject(JSON.toJSONString(pms), Map.class);
            paramMaps.forEach((key, value) -> builder.queryParam(key, value.toString()));
        }
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {};
        ResponseEntity<String> response = apiPtRestTemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
        String rs = response.getBody();
        ApiPlatResponseUtil.traceResponse(response);
        return rs;
    }

    public String postapi(String path, Object pms, Map<String, String> heads) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("appKey", apiRemoteConfig.getApiAppKey());
        if (heads != null) {
            for (Map.Entry<String, String> hd : heads.entrySet()) {
                headers.add(hd.getKey(), hd.getValue());
            }
        }
        HttpEntity<Object> requestparam = new HttpEntity<>(pms, headers);
        StringBuilder urlsb = new StringBuilder();
        path = path == null ? "" : path.trim();
        if (path.startsWith("http:") || path.startsWith("https:")) {
            urlsb.append(path);
        } else {
            urlsb.append(apiRemoteConfig.getPlatformUrl());
            urlsb.append(path);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString());
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> response = apiPtRestTemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.POST, requestparam, responseType);
        String rs = response.getBody();
        ApiPlatResponseUtil.traceResponse(response);
        return rs;
    }

    public String lmsSupplierGetApi(String path, String materialCode, Map<String, String> heads) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("appKey", apiRemoteConfig.getApiAppKey());
        if (heads != null) {
            for (Map.Entry<String, String> hd : heads.entrySet()) {
                headers.add(hd.getKey(), hd.getValue());
            }
        }
        HttpEntity<Object> requestparam = new HttpEntity<>(headers);
        StringBuilder urlsb = new StringBuilder();
        path = path == null ? "" : path.trim();
        if (path.startsWith("http:") || path.startsWith("https:")) {
            urlsb.append(path);
        } else {
            urlsb.append(apiRemoteConfig.getPlatformUrl());
            urlsb.append(path);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString()).queryParam("materialCode",materialCode);
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> response = apiPtRestTemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.GET, requestparam, responseType);
        String rs = response.getBody();
        ApiPlatResponseUtil.traceResponse(response);
        return rs;
    }


    public String qmsTestPostApi(String path, Object pms, Map<String, String> heads) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("appKey", "64670b44784d421b58a9b8aa");
        if (heads != null) {
            for (Map.Entry<String, String> hd : heads.entrySet()) {
                headers.add(hd.getKey(), hd.getValue());
            }
        }
        HttpEntity<Object> requestparam = new HttpEntity<>(pms, headers);
        StringBuilder urlsb = new StringBuilder();
        path = path == null ? "" : path.trim();
        if (path.startsWith("http:") || path.startsWith("https:")) {
            urlsb.append(path);
        } else {
            urlsb.append(apiRemoteConfig.getPlatformUrl());
            urlsb.append(path);
        }
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlsb.toString());
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };
        ResponseEntity<String> response = apiPtRestTemplate.exchange(builder.build().encode(StandardCharsets.UTF_8).toUri(), HttpMethod.POST, requestparam, responseType);
        String rs = response.getBody();
        ApiPlatResponseUtil.traceResponse(response);
        return rs;
    }

}
