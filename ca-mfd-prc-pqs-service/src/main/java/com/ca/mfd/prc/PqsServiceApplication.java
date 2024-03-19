/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * inkelink-pm
 *
 * @author inkelink
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class PqsServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(PqsServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PqsServiceApplication.class);
    }
}