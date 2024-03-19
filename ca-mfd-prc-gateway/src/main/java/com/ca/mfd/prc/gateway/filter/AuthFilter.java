package com.ca.mfd.prc.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.ca.mfd.prc.common.config.ApiPlatformConfig;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.TokenUserDTO;
import com.ca.mfd.prc.common.redis.RedisKeys;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.utils.IpUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.gateway.config.InkelinkGatewayProperties;
import com.ca.mfd.prc.gateway.remote.app.IAccountService;
import com.ca.mfd.prc.gateway.service.IApiPlatformService;
import com.ca.mfd.prc.gateway.utils.CaLoggerUtil;
import com.ca.mfd.prc.gateway.utils.JsonResponseUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Log logger = LogFactory.getLog(AuthFilter.class);
    /**
     * 存储缓存（5分钟）
     */
    private final Cache<String, Object> cacheToken = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5 * 60, TimeUnit.SECONDS).build();

    @Autowired
    private InkelinkGatewayProperties gatewayProperties;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private CaAuthFilter caAuthFilter;
    @Value("${inkelink.gateway.isOpenCaTokenCheck:false}")
    private boolean isOpenCaTokenCheck;
    @Autowired
    private ApiPlatformConfig apiPlatformConfig;
    @Autowired
    private IApiPlatformService apiPlatformService;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath().trim().toLowerCase();
        logger.info(Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getHostString());
        if (isNoToken(requestPath)) {
            return setIpHeader(exchange,chain,request.mutate());
        }
        if (!isOpenCaTokenCheck) {
            List<String> tokenPms = getHeaderToken(request);
            if (tokenPms == null || tokenPms.size() < 2 || StringUtils.isEmpty(tokenPms.get(0))) {
                return checkNoToken(exchange, chain);
            } else {
                return checkToken(tokenPms, exchange, chain);
            }
        }
        //走门户集成验证token
        else {

            /******************************************************
             * api平台网关处理
             *****************************************************/
            String apiAppKey = request.getHeaders().getFirst("appkey");
            String apiPlatformKey = request.getHeaders().getFirst("apiplatformkey");
            if (StrUtil.isNotBlank(apiAppKey) && StrUtil.isNotBlank(apiPlatformKey)) {
                if (apiPlatformService.chkApiPlatform(apiAppKey, apiPlatformKey)) {
                    return setIpHeader(exchange,chain,request.mutate());
                } else {
                    logger.error("API平台转发请求验证失败：" + apiAppKey + "," + apiPlatformKey);
                    return authError(exchange.getResponse(), JsonResponseUtils.API_AUTH_ERROR);
                }
            } else {
                Map<String, String> map = caAuthFilter.getHeaderToken(request);
                if (!map.containsKey("token")) {
                    return checkNoToken(exchange, chain);
                } else {
                    return caAuthFilter.checkToken(map, exchange, chain);
                }
            }
        }
    }

    private Boolean authUrl(String c, String requestPath) {
        if (!StringUtils.isBlank(c)) {
            String mathPath = c.trim().toLowerCase();
            if (mathPath.contains("**")) {
                return requestPath.startsWith(mathPath.replace("**", ""));
            } else {
                return StringUtils.equalsIgnoreCase(requestPath, mathPath);
            }
        }
        return false;
    }

    /**
     * 是否不校验token名单
     *
     * @param requestPath
     * @return
     */
    private boolean isNoToken(String requestPath) {
        return !CollectionUtils.isEmpty(gatewayProperties.getNoCheckTokenUrls())
                && gatewayProperties.getNoCheckTokenUrls().stream().anyMatch(c -> authUrl(c, requestPath));
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

    /**
     * 没有token的验证
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> checkNoToken(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        String requestPath = request.getURI().getPath().trim().toLowerCase();
        if (isNoAuthorization(requestPath)) {
            return setIpHeader(exchange,chain,request.mutate());
        } else {
            return authError(response, JsonResponseUtils.AUTH_UNLOGIN_ERROR);
        }
    }

    /**
     * 有token的验证
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> checkToken(List<String> tokenPms, ServerWebExchange exchange, GatewayFilterChain chain) {
        TokenUserDTO userDTO = getUserInfo(tokenPms.get(0), tokenPms.get(1), exchange);
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        if (userDTO == null) {
            String requestPath = request.getURI().getPath().trim().toLowerCase();
            if (isNoAuthorization(requestPath)) {
                return setIpHeader(exchange,chain,request.mutate());
            } else {
                return authError(response, JsonResponseUtils.AUTH_UNLOGIN_ERROR);
            }
        } else {
            ServerHttpRequest.Builder mutate = request.mutate();
            //mutate.header("userinfo", JSONUtil.toJsonStr(userDTO.getUserInfo()));
            //String userinfoJson = JSONUtil.toJsonStr(userDTO);
            mutate.header("token", tokenPms.get(0));
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


    private TokenUserDTO getUserInfo(String token, String tokenType, ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        //ServerHttpResponse response = exchange.getResponse();
        //token = "Bearer " + token;
        TokenUserDTO userDTO = (TokenUserDTO) cacheToken.getIfPresent(token);
        logger.info("====请求接口地址：" + request.getURI());
        if (userDTO != null) {
            logger.info("====获取到缓存：");
            return userDTO;
        }
        logger.info("====开始调用用户信息：");
//        Set<Map.Entry<String, List<String>>> headpms = null;
//        if (StringUtils.equals(tokenType, "query")) {
//
//            LinkedHashMap<String, List<String>> headers = new LinkedHashMap<>();
//            List<String> heads = new ArrayList<>();
//            heads.add("Bearer " + token);
//            headers.put("Authorization", heads);
//            headpms = headers.entrySet();
//        } else {
//            headpms = request.getHeaders().entrySet();
//        }

        //根据token获取userId
        Object objToken = redisUtils.get(RedisKeys.getToken(token));
        if (objToken == null) {
            ResultVO account = accountService.info("Bearer " + token);
            if (account == null || account.getData() == null) {
                logger.info("====(本地)获取用户信息失败：");
                return null;
            }
            userDTO = JsonUtils.parseObject(JsonUtils.toJsonString(account.getData()), TokenUserDTO.class);
            if (userDTO == null || userDTO.getUserInfo() == null) {
                logger.info("====(本地)转换用户信息失败：");
                return null;
            }
            redisUtils.set(RedisKeys.getTokenUserId(userDTO.getUserInfo().getId()), userDTO.getUserInfo());
            redisUtils.set(RedisKeys.getToken(token), userDTO.getUserInfo().getId());
        } else {
            Object tokenObj = redisUtils.get(RedisKeys.getToken(token));
            if (tokenObj == null) {
                logger.info("====(本地)获取redis用户信息失败：");
                return null;
            }
            Object userObj = redisUtils.get(RedisKeys.getTokenUserId(Long.valueOf(tokenObj.toString())));
            OnlineUserDTO onlineUserDTO = JsonUtils.parseObject(JsonUtils.toJsonString(userObj), OnlineUserDTO.class);
            if (onlineUserDTO == null) {
                logger.info("====(本地)获取redis用户信息转换失败：");
                return null;
            }
            userDTO = new TokenUserDTO();
            userDTO.setUserInfo(onlineUserDTO);
        }
        if (userDTO.getUserInfo() == null) {
            logger.info("====(本地)获取用户信息转换失败：");
            return null;
        }
        cacheToken.put(token, userDTO);
        return userDTO;
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private Mono<Void> authError(ServerHttpResponse response, String json) {
        //返回错误
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 获取头的token
     *
     * @return String
     */
    private List<String> getHeaderToken(ServerHttpRequest request) {
        if (request == null) {
            return null;
        }
        //通过header获取token
        String token = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.isBlank(token)) {
            token = token.replace("Bearer ", "").replace("bearer ", "").trim();
        }
        if (!StringUtils.isBlank(token)) {
            return Arrays.asList(token, "token");
        }
//        //通过cookie获取token
//        if (request.getCookies() != null && request.getCookies().containsKey("ACCESS-TOKEN")) {
//            HttpCookie tokenCookie = request.getCookies().getFirst("ACCESS-TOKEN");
//            if (tokenCookie != null) {
//                token = tokenCookie.getValue();
//            }
//        }
//        if (!StringUtils.isBlank(token)) {
//            token = token.replace("Bearer", "").replace("bearer", "").trim();
//        }
//        if (!StringUtils.isBlank(token)) {
//           return Arrays.asList(token,"cookie");
//        }
        //通过url参数获取
        token = request.getQueryParams().getFirst("bearer");
        if (StringUtils.isBlank(token)) {
            token = request.getQueryParams().getFirst("Bearer");
        }
        if (!StringUtils.isBlank(token)) {
            logger.info("====获取到query中token参数：" + token);
            token = token.trim();
            return Arrays.asList(token, "query");
        }
        return null;
    }
}