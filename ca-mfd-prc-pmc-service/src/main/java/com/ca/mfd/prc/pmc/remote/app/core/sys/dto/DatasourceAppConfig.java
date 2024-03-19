package com.ca.mfd.prc.pmc.remote.app.core.sys.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author inkelink
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourceAppConfig {
    @Value(value = "${spring.datasource.druid.username}")
    private String username;
    @Value(value = "${spring.datasource.druid.password}")
    private String password;
    @Value(value = "${spring.datasource.druid.driver-class-name}")
    private String driverclassname;
}
