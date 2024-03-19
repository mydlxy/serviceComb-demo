package com.ca.mfd.prc.common.config;

import com.ca.mfd.prc.common.constant.Constant;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


/**
 * @author hhoa
 */
@Configuration
public class SwaggerV3Config {
    /**
     * 您可以根据以下组合定义自己的 API 组：API 路径和要扫描的包。
     * 每个组都应该有一个独特的 groupName.
     * 该组的 OpenAPI 描述，默认情况下可用于：
     * http://server:port/context-path/v3/api-docs/groupName
     * 开启了
     * http://server:port/actoator/openapi/groupName
     *
     * @return GroupedOpenApi
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("default")
                .packagesToScan("com.ca")
                .build();

    }


    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(apiInfo())
                //.servers(services())
                //必须,这个一般在controller里面生成，不在这里定义
                //.paths("/**")
                .components(new Components())
//                .servers(Lists.newArrayList(new Server()))
                .security(Lists.newArrayList(security()))
                .tags(Lists.newArrayList(new Tag()));
//                .externalDocs(new ExternalDocumentation()
//                        .description("swagger官方文档")
//                        .url("https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/"));

    }


    private Info apiInfo() {
        return new Info()
                //必须
                .title("依柯力")
                .description("inkelink-业务接口文档")
                .version("1.0")
                //必须, 版本
                .license(new License()
                        //如果定义了license就是必须
                        .name("inkelink")
                        .url("https://www.inkelink.com"))
                .contact(new Contact()
                        .name("inkelink")
                        .url("https://www.inkelink.com")
                        .email("inkelink@a.com"));
    }

    private List<Server> services() {
        Server ser = new Server()
                //必须
                .url("服务器网址")
                .description("描述")
                .variables(new ServerVariables()
                        .addServerVariable("变量名称", new ServerVariable()
                                ._enum(Lists.newArrayList("枚举"))
                                //必须
                                ._default("参数值")
                                .description("描述")));
        return Lists.newArrayList(ser);
    }

    private List<SecurityRequirement> security() {
        //3.0改变了返回结果
        List<SecurityRequirement> result = new ArrayList<>();
        SecurityRequirement apiKey = new SecurityRequirement();
        apiKey.addList("header", Constant.TOKEN_HEADER);
        //Constant.TOKEN_HEADER, Constant.TOKEN_HEADER, "header");
        result.add(apiKey);
        return result;
    }
}
