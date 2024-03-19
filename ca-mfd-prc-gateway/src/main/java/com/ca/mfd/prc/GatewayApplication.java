package com.ca.mfd.prc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


//
//@EnableFeignClients
//@EnableDiscoveryClient
//@SpringBootApplication
////@ComponentScan({"com.ca","org.springdoc"})
//public class GatewayApplication extends SpringBootServletInitializer {
//
//    public static void main(String[] args) {
//        SpringApplication.run(GatewayApplication.class, args);
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(GatewayApplication.class);
//    }
//}


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GatewayApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(GatewayApplication.class, args);
        } catch (Exception e) {
            LOGGER.error("start up failed.", e);
        }
    }

//    @Bean
//    @LoadBalanced
//    public WebClient.Builder loadBalancedWebClientBuilder() {
//        return WebClient.builder();
//    }


//    @Bean
//    public  AbstractRequestService createAbstractRequestService(){
//        return new AbstractRequestService() {
//            @Override
//            public Operation build(HandlerMethod handlerMethod, RequestMethod requestMethod, Operation operation, MethodAttributes methodAttributes, OpenAPI openAPI) {
//                return super.build(handlerMethod, requestMethod, operation, methodAttributes, openAPI);
//            }
//        };
//    }
}
