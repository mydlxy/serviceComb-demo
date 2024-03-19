package com.ca.mfd.prc.gateway.filter;

import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.redis.RedisKeys;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.gateway.config.InkelinkGatewayProperties;
import com.ca.mfd.prc.gateway.remote.app.IntegratedService;
import com.ca.mfd.prc.gateway.utils.CaLoggerUtil;
import com.ca.mfd.prc.gateway.utils.JsonResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
/**
 * 门户Token验证
 * @author inkelink mason
 * @since 1.0.0 2023-10-17
 */
@Component
public class CaAuthFilter {
    @Autowired
    private InkelinkGatewayProperties gatewayProperties;
    @Autowired
    private IntegratedService integratedService;
    @Autowired
    private RedisUtils redisUtils;
    private static final Log logger = LogFactory.getLog(AuthFilter.class);
    /**
     * 获取头的token
     *
     * @return String
     */
    public Map<String, String> getHeaderToken(ServerHttpRequest request) {
        if (request == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap();

        //获取token
        String token = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.isBlank(token)) {
            token = token.replace("Bearer", "").replace("bearer", "").replace("\"","").trim();
            map.put("token", token);
        } else {

            token = request.getQueryParams().getFirst("bearer");
            if (StringUtils.isBlank(token)) {
                token = request.getQueryParams().getFirst("Bearer");
            }
            if (!StringUtils.isBlank(token)) {
                logger.info("====获取到query中token参数：" + token);
                token = token.replace("\"","").trim();
                map.put("token", token);
            }
        }
        //获取Ai-Refresh-Token
        String refreshToken = request.getHeaders().getFirst("Ai-Refresh-Token");
        if (!StringUtils.isBlank(refreshToken)) {
            map.put("refreshToken", refreshToken);
        }
        return map;
    }

    /**
     * 有token的验证
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> checkToken(Map<String, String> tokenMap, ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath().trim().toLowerCase();
        String refreshToken = "";
        if (tokenMap.containsKey("refreshToken")) {
            refreshToken = tokenMap.get("refreshToken");
        }
        OnlineUserDTO userDTO = getUserInfo(tokenMap.get("token"), refreshToken, exchange);
        if (userDTO == null) {
            if (isNoAuthorization(requestPath)) {
                return setIpHeader(exchange,chain,request.mutate());
            } else {
                return authError(response, JsonResponseUtils.AUTH_UNLOGIN_ERROR);
            }
        } else {
            if(userDTO.getUriPermission()!=null && !userDTO.getUriPermission().stream().anyMatch(c->authUrl(c, requestPath))){
                //return authError(response, JsonResponseUtils.NOT_URL_AUTH);
            }
            ServerHttpRequest.Builder mutate = request.mutate();
            mutate.header("token",userDTO.getToken());
            return setIpHeader(exchange,chain,mutate);
        }
    }

    private Mono<Void> setIpHeader(ServerWebExchange exchange, GatewayFilterChain chain,ServerHttpRequest.Builder mutate){
        String ip = CaLoggerUtil.getForwardedIP(exchange.getRequest());
        if(!StringUtils.isBlank(ip) && !StringUtils.equals(ip,"-")){
            mutate.header("X-real-ip", ip);
        }
        //return chain.filter(exchange);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    private Mono<Void> authError(ServerHttpResponse response, String json) {
        //返回错误
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    private Boolean authUrl(String c, String requestPath) {
        if (!StringUtils.isBlank(c)) {
            String mathPath = c.trim().toLowerCase();
            if (mathPath.contains("/**")) {
                return requestPath.startsWith(mathPath.replace("/**", "/"));
            } else {
                return StringUtils.equalsIgnoreCase(requestPath, mathPath);
            }
        }
        return false;
    }

    /**
     * 是否白名单
     *
     * @param requestPath
     * @return
     */
    private boolean isNoAuthorization(String requestPath) {
        return !CollectionUtils.isEmpty(gatewayProperties.getNoAuthorizationUrls())
                && gatewayProperties.getNoAuthorizationUrls().stream().anyMatch(c -> authUrl(c, requestPath));
    }

    private OnlineUserDTO getUserInfo(String token, String refreshToken, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        OnlineUserDTO userDTO;
        logger.info("====请求接口地址：" + request.getURI());
        //根据token获取userId
        Object objToken = redisUtils.get(RedisKeys.getToken(token));
        if (objToken == null) {
            ResultVO checkTokenResult = integratedService.getUserInfoByToken(token, refreshToken);
            if (checkTokenResult == null || checkTokenResult.getData() == null) {
                logger.info("用户令牌校验失败| token："+token+"，refreshToken："+refreshToken);
                return null;
            } else {
                userDTO = JsonUtils.parseObject(JsonUtils.toJsonString(checkTokenResult.getData()), OnlineUserDTO.class);
            }
        } else {
            Object objUser = redisUtils.get(RedisKeys.getTokenUserId(Long.valueOf(objToken.toString())));
            userDTO = JsonUtils.parseObject(JsonUtils.toJsonString(objUser), OnlineUserDTO.class);
        }
        return userDTO;
    }
}
