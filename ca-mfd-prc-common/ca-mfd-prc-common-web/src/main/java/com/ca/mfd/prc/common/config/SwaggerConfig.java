///**
// * Copyright (c) 2023 依柯力 All rights reserved.
// *
// * https://www.inkelink.com
// *
// * 版权所有，侵权必究！
// */
//
//package com.ca.common.config;
//
//import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
//import Constant;
//import io.swagger.v3.oas.annotations.Operation;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.ApiKey;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
//
//import java.util.List;
//
//import static com.google.common.collect.Lists.newArrayList;
//
///**
// * Swagger配置
// *
// * @author inkelink
// */
//@Configuration
//@EnableSwagger2WebMvc
//@AllArgsConstructor
//public class SwaggerConfig{
//    private final OpenApiExtensionResolver openApiExtensionResolver;
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//            .apiInfo(apiInfo())
//            .select()
//            //加了ApiOperation注解的类，生成接口文档
//            .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
//            //包下的类，生成接口文档
//            //.apis(RequestHandlerSelectors.basePackage("com.ca.modules.job.controller"))
//            .paths(PathSelectors.any())
//            .build()
//            .extensions(openApiExtensionResolver.buildExtensions("Inkelink"))
//            .directModelSubstitute(java.util.Date.class, String.class)
//            .securitySchemes(security());
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//            .title("依柯力")
//            .description("inkelink-业务接口文档")
//            .termsOfServiceUrl("https://www.inkelink.com")
//            .version("1.0")
//            .build();
//    }
//
//    private List<ApiKey> security() {
//        return newArrayList(
//            new ApiKey(Constant.TOKEN_HEADER, Constant.TOKEN_HEADER, "header")
//        );
//    }
//}