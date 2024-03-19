package com.ca.mfd.prc.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("inkelink.gateway")
public class InkelinkGatewayProperties {


    private List<String> noAuthorizationUrls;
    private List<String> noCheckTokenUrls;

    public List<String> getNoAuthorizationUrls() {
        return this.noAuthorizationUrls;
    }

    public void setNoAuthorizationUrls(List<String> noAuthorizationUrls) {
        this.noAuthorizationUrls = noAuthorizationUrls;
    }

    public List<String> getNoCheckTokenUrls() {
        return this.noCheckTokenUrls;
    }

    public void setNoCheckTokenUrls(List<String> noCheckTokenUrls) {
        this.noCheckTokenUrls = noCheckTokenUrls;
    }

}
