//package com.ca.mfd.prc.common;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http;
//import org.springframework.stereotype.Component;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**
// * httpClient 工具类
// *
// * @author eric.zhou@hg2mes.com
// * @date 2023/4/7
// */
//@Component
//public class RestTemplateUtil {
//
//  @Autowired
//  private RestTemplate restTemplate;
//
//  public RestTemplateUtil() {}
//
//  public RestTemplateUtil(RestTemplate restTemplate) {
//    this.restTemplate = restTemplate;
//  }
//
//  public RestTemplate getRestTemplate() {
//    return this.restTemplate;
//  }
//
//  /**
//   * .发送GET请求
//   *
//   * @param url
//   * @param param
//   * @return String
//   */
//  public String getMap(String url, Map<String, String> param) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//    return restTemplate.getForEntity(url, String.class, param).getBody();
//  }
//
//  /**
//   * 发送GET请求
//   *
//   * @param url
//   * @param headpms
//   * @return JSONObject
//   */
//  public JSONObject getJson(String url, HashMap<String, String> headpms) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.add("user-agent",
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                    + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    if (headpms != null) {
//      for (Map.Entry<String, String> entry : headpms.entrySet()) {
//        headers.add(entry.getKey(), entry.getValue());
//      }
//    }
//    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
//    return restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class).getBody();
//  }
//
//    public JSONObject getJson(String url, Set<Map.Entry<String, List<String>>> headpms) {
//      HttpHeaders headers = new HttpHeaders();
//      headers.add("user-agent",
//              "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                      + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//      headers.setContentType(MediaType.APPLICATION_JSON);
//      if (headpms != null) {
//        for (Map.Entry<String, List<String>> entry : headpms) {
//          headers.add(entry.getKey(), entry.getValue().get(0));
//        }
//      }
//      HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
//      return restTemplate.exchange(url, HttpMethod.GET, requestEntity, JSONObject.class).getBody();
//    }
//
//  /**
//   * 发送GET请求
//   *
//   * @param url
//   * @return String
//   */
//  public String getString(String url) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.add("user-agent",
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                    + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(headers);
//    return restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
//  }
//
//  /**
//   * 发起post请求(json为参数)
//   *
//   * @param url
//   * @param param
//   * @param header
//   * @return JSONObject
//   */
//  public JSONObject postJson(String url, JSONArray param, Map<String, String> header) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//    headers.add("user-agent",
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                    + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//    if (header != null) {
//      for (Map.Entry<String, String> entry : header.entrySet()) {
//        headers.add(entry.getKey(), entry.getValue());
//      }
//    }
//    HttpEntity<JSONArray> requestEntity = new HttpEntity<JSONArray>(param, headers);
//    JSONObject body = restTemplate.postForEntity(url, requestEntity, JSONObject.class).getBody();
//    return body;
//  }
//
//  /**
//   * 发起post请求(json为参数)
//   *
//   * @param url          url
//   * @param param        参数列表
//   * @param responseType 返回实体
//   * @return T
//   */
//  public <T> ResponseEntity<T> postJson(
//          String url, Object param, Map<String, String> header, Class<T> responseType) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//    headers.add("user-agent",
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                    + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//    if (header != null) {
//      for (Map.Entry<String, String> entry : header.entrySet()) {
//        headers.add(entry.getKey(), entry.getValue());
//      }
//    }
//    HttpEntity requestEntity = new HttpEntity<>(param, headers);
//    ResponseEntity<T> body = restTemplate.postForEntity(url, requestEntity, responseType);
//    return body;
//  }
//
//  /**
//   * 发起post请求(map为参数)
//   *
//   * @param url    url
//   * @param params 参数列表
//   * @param header 参数列表
//   * @return String
//   */
//  public String postMap(String url, Map<String, Object> params, Map<String, String> header) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.add(
//            "user-agent",
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
//                    + " (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    headers.setContentType(MediaType.APPLICATION_JSON);
//    headers.add("Accept", MediaType.APPLICATION_JSON.toString());
//    if (header != null) {
//      for (Map.Entry<String, String> entry : header.entrySet()) {
//        headers.add(entry.getKey(), entry.getValue());
//      }
//    }
//    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
//    String body1 = restTemplate.postForEntity(url, requestEntity, String.class).getBody();
//    System.out.println(body1);
//    return body1;
//  }
//
//  /**
//   * post——from-urlencoded格式请求
//   *
//   * @param url
//   * @param params
//   * @param header
//   * @return String
//   */
//  public String postFromUrlencoded(
//          String url, MultiValueMap<String, Object> params, Map<String, String> header) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.add(
//            "user-agent",
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                    + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//    if (CollUtil.isNotEmpty(header)) {
//      for (Map.Entry<String, String> entry : header.entrySet()) {
//        headers.add(entry.getKey(), entry.getValue());
//      }
//    }
//    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
//    return restTemplate.postForEntity(url, requestEntity, String.class).getBody();
//  }
//
//  /**
//   * post——from-urlencoded格式请求
//   *
//   * @param url
//   * @param params
//   * @param header
//   * @return String
//   */
//  public ResponseEntity<String> postFromUrlencodedResponse(
//          String url, MultiValueMap<String, Object> params, Map<String, String> header) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.add(
//            "user-agent",
//            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
//                    + "(KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//    if (CollUtil.isNotEmpty(header)) {
//      for (Map.Entry<String, String> entry : header.entrySet()) {
//        headers.add(entry.getKey(), entry.getValue());
//      }
//    }
//    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
//    return restTemplate.postForEntity(url, requestEntity, String.class);
//  }
//
//  /**
//   * post——from-multi格式请求
//   *
//   * @param url
//   * @param params
//   * @param header
//   * @return String
//   */
//  public String postMultiFromData(
//          String url, MultiValueMap<String, Object> params, Map<String, String> header) {
//    HttpHeaders headers = new HttpHeaders();
//    headers.add("user-agent", "ApiPOST Runtime +https://www.apipost.cn");
//    headers.add("accept", MediaType.ALL_VALUE);
//    headers.add("connection", "keep-alive");
//    headers.add("accept-encoding", "gzip, deflate, br");
//    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//    if (CollUtil.isNotEmpty(header)) {
//      for (Map.Entry<String, String> entry : header.entrySet()) {
//        headers.add(entry.getKey(), entry.getValue());
//      }
//    }
//    HttpEntity requestEntity;
//    if (params == null || params.size() == 0) {
//      requestEntity = new HttpEntity<>(headers);
//    } else {
//      requestEntity = new HttpEntity<>(params, headers);
//    }
//    return restTemplate.postForEntity(url, requestEntity, String.class).getBody();
//  }
//}
