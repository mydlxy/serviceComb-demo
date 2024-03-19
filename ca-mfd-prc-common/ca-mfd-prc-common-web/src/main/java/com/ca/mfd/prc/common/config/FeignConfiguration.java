package com.ca.mfd.prc.common.config;

import com.ca.mfd.prc.common.utils.HttpContextUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Author: joel
 * @Date: 2023-08-30-17:13
 * @Description:
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {
    static final String CONTENT_LENGTH = "content-length";

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        if (request != null) {
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    // 跳过 content-length(解决too many bytes written executing报错--eric)
                    if (CONTENT_LENGTH.equals(name)) {
                        continue;
                    }
                    template.header(name, values);
                }
            }
        }
    }
}
