package com.ca.mfd.prc.gateway.exception;


import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.gateway.utils.CaLoggerUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>功能描述: CustomErrorWebExceptionHandler.java</p>
 *
 * @author
 * @version 2023年7月4日
 */
public class CustomErrorWebExceptionHandler extends DefaultErrorWebExceptionHandler {
    private static final Log logger = LogFactory.getLog(CustomErrorWebExceptionHandler.class);
    private static final Map<HttpStatus.Series, String> SERIES_VIEWS;

    static {
        Map<HttpStatus.Series, String> views = new EnumMap<>(HttpStatus.Series.class);
        views.put(HttpStatus.Series.CLIENT_ERROR, "4xx");
        views.put(HttpStatus.Series.SERVER_ERROR, "5xx");
        SERIES_VIEWS = Collections.unmodifiableMap(views);
    }

    private final ErrorProperties errorProperties;
    @Autowired
    private GateWayExceptionHandlerAdvice gateWayExceptionHandlerAdvice;


    public CustomErrorWebExceptionHandler(ErrorAttributes errorAttributes, WebProperties webProperties,
                                          ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, webProperties.getResources(), errorProperties, applicationContext);
        this.errorProperties = errorProperties;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }


    @Override
    protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        Throwable throwable = getError(request);
        if (logger.isWarnEnabled()) {
            try {
                Long startTime = request.exchange().getAttribute("startTime");
                if (startTime != null) {
                    Long executeTime = (System.currentTimeMillis() - startTime);
                    if (logger.isInfoEnabled()) {
                        JSONObject ob = new JSONObject();
                        try {

                            URI uri = request.exchange().getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                            Set<URI> originaluris = request.exchange().getAttributeOrDefault(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR, Collections.emptySet());
                            String originalUri = "";
                            String originalapi = "";
                            if (!originaluris.isEmpty()) {
                                Iterator<URI> its = originaluris.iterator();
                                originalUri = its.hasNext() ? its.next().toString() : "unknown";
                                originalapi = its.hasNext() ? its.next().toString() : "unknown";

                            }
                            ob.put("http-code", getHttpStatus(error));
                            ob.put("spend-time", executeTime / 1000.00);
                            ob.put("client-ip", CaLoggerUtil.getClientIP(request.exchange().getRequest()));
                            ob.put("X-Forwarded-For", CaLoggerUtil.getForwardedIP(request.exchange().getRequest()));
                            ob.put("local-ip", CaLoggerUtil.getLocationIp());
                            ob.put("http-url", uri != null ? uri.toString() : "--");
                            ob.put("api", originalapi);
                            ob.put("request-uri", originalUri);
                            logger.info(ob.toString());

                        } catch (Exception e) {
                        }
                    }
                }
                logger.warn("request excepiton:" + getHttpStatus(error) + "," + request.methodName() + "," + request.path() + "," + throwable.getMessage());
            } catch (Exception e) {
            }
        }

        return ServerResponse.status(getHttpStatus(error))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(gateWayExceptionHandlerAdvice.handle(throwable)))
                .doOnNext((resp) -> logError(request, resp, throwable));
    }

    @Override
    protected Mono<ServerResponse> renderErrorView(ServerRequest request) {

        Map<String, Object> error = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML));
        int errorStatus = getHttpStatus(error);
        ServerResponse.BodyBuilder responseBody = ServerResponse.status(errorStatus).contentType(new MediaType("text", "html", StandardCharsets.UTF_8));
        Throwable throwable = getError(request);
        return Flux.just(getData(errorStatus).toArray(new String[]{}))
                .flatMap((viewName) -> renderErrorView(viewName, responseBody, error))
                .switchIfEmpty(this.errorProperties.getWhitelabel().isEnabled()
                        ? renderDefaultErrorView(responseBody, error) : Mono.error(getError(request)))
                .next().doOnNext((response) -> logError(request, response, throwable));


    }

    private List<String> getData(int errorStatus) {
        List<String> data = new ArrayList<>();
        data.add("error/" + errorStatus);
        HttpStatus.Series series = HttpStatus.Series.resolve(errorStatus);
        if (series != null) {
            data.add("error/" + SERIES_VIEWS.get(series));
        }
        data.add("error/error");
        return data;
    }
}
