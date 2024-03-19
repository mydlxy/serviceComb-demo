//package com.ca.gateway.filter;
//
//
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.ca.gateway.service.ITokenVerService;
//import com.ca.gateway.utils.ResultConstant;
//import com.ca.mfd.prc.common.utils.Result;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * <p>功能描述: 网关拦截器，用于Api确权与处理</p>
// *
// * @author
// * @version 2020年6月15日
// */
//@Configuration
//public class AccessGatewayFilter implements GlobalFilter, Ordered {
//    private static final Log logger = LogFactory.getLog(AccessGatewayFilter.class);
//    private static final String X_CLIENT_TOKEN_USER = "userId";
//
//    @Autowired
//    @Qualifier("tokenverservice")
//    private ITokenVerService tokenverservice;
//
//    /**
//     * <p>功能描述: AccessGatewayFilter.java</p>
//     *
//     * @param exchange
//     * @param chain
//     * @param
//     * @author
//     * @version 2023年7月4日
//     * @parent org.springframework.cloud.gateway.filter.GlobalFilter#filter(org.springframework.web.server.ServerWebExchange, org.springframework.cloud.gateway.filter.GatewayFilterChain)
//     */
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        exchange.getAttributes().put("startTime", System.currentTimeMillis());
//        ServerHttpRequest request = exchange.getRequest();
//        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        String deftoken = request.getHeaders().getFirst("token");
//        /******************************************************
//         * 白名单弱加签验证 appid+timestamp+noncestr 非token加签
//         *****************************************************/
//        String url = request.getPath().value();
//        if (tokenverservice.ignoreAuthentication(url)) {
//            return chain.filter(exchange);
//        }
//        /******************************************************
//         * token验证
//         *****************************************************/
//        String token = StrUtil.isNotBlank(authentication) ? authentication : deftoken;
//        if (StrUtil.isNotBlank(token)) {
//            JSONObject obj = tokenverservice.getTokenInfo(token);
//            String userId = (obj != null && StrUtil.isNotBlank(obj.getString("userId"))) ? obj.getString("userId") : "";
//            if (StrUtil.isNotBlank(userId)) {
//                ServerHttpRequest.Builder builder = request.mutate();
//                builder.header(X_CLIENT_TOKEN_USER, userId);
//                builder.header(HttpHeaders.AUTHORIZATION, StringUtils.isNotBlank(token) ? token : "");
//                return chain.filter(exchange.mutate().request(builder.build()).build());
//            } else {
//                //无效
//                logger.error("无效token:" + token);
//            }
//        }
//        /******************************************************
//         * api平台网关处理
//         *****************************************************/
//        String apiAppKey = request.getHeaders().getFirst("ApiAppKey");
//        String apiPlatformKey = request.getHeaders().getFirst("ApiPlatformKey");
//        if (StrUtil.isNotBlank(apiAppKey) && StrUtil.isNotBlank(apiPlatformKey)) {
//            if (tokenverservice.chkApiPlatfrom(apiAppKey, apiPlatformKey)) {
//                return chain.filter(exchange);
//            } else {
//                logger.error("API平台转发请求验证失败：" + apiAppKey + "," + apiPlatformKey);
//            }
//        }
//        //失败处理
//        return unauthorized(exchange);
//    }
//
//    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
//        //log.error(HttpStatus.UNAUTHORIZED+"：未授权");
//        ServerHttpRequest request = serverWebExchange.getRequest();
//        logger.warn("remote address:" + request.getRemoteAddress().getHostString() + "not have token：" + HttpStatus.UNAUTHORIZED + request.getPath() != null ? request.getPath().toString() : "-");
//        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        Result<String> result = new Result<String>();
//        result.setCode(ResultConstant.ERROR);
//        result.setMessage(HttpStatus.UNAUTHORIZED + ":未授权，请登录!");
//        ServerHttpResponse serverHttpResponse = serverWebExchange.getResponse();
//        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
//        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSON(result).toString().getBytes());
//        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
//    }
//
//    @Override
//    public int getOrder() {
//        // TODO Auto-generated method stub
//        return -1;
//    }
//}
