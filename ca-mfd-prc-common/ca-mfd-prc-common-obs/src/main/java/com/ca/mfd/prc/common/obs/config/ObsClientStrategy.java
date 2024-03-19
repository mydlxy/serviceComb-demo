package com.ca.mfd.prc.common.obs.config;

import com.obs.services.ObsClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ca.cloud.obs")
@ConditionalOnExpression("#{'true'.equals(environment.getProperty('ca.cloud.obs.enable'))}")
public class ObsClientStrategy {
    private Map<String, BucketConfig> obsBucketConfigs;

    private Map<String, String> bucketNames;

    private Map<String, String> endpoints;
    private List<BucketConfig> buckets;


    public List<BucketConfig> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<BucketConfig> buckets) {
        this.buckets = buckets;
    }

    @PostConstruct
    public void initObsClients() {
        obsBucketConfigs = new HashMap<>();
        bucketNames = new HashMap<>();
        endpoints = new HashMap<>();
        for (BucketConfig bucketConfig : buckets) {
            String code = bucketConfig.getCode();
            String endPoint = bucketConfig.getEndPoint();
            String bucketName = bucketConfig.getBucketName();
            // 存储到映射中
            obsBucketConfigs.put(code, bucketConfig);
            bucketNames.put(code, bucketName);
            endpoints.put(code, endPoint);
        }
    }

    public ObsClient getClientByCode(String code) {
        BucketConfig bucketConfig = obsBucketConfigs.get(code);
        String endPoint = bucketConfig.getEndPoint();
        String ak = bucketConfig.getAk();
        String sk = bucketConfig.getSk();

        // 创建 OSS 客户端实例
        return new ObsClient(ak, sk, endPoint);
    }

    public String getBucketNameByCode(String code) {
        return bucketNames.get(code);
    }

    public String getEndpointByCode(String code) {
        return endpoints.get(code);
    }

    public static class BucketConfig {
        private String code;
        private String bucketName;
        private String endPoint;
        private String ak;
        private String sk;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public String getAk() {
            return ak;
        }

        public void setAk(String ak) {
            this.ak = ak;
        }

        public String getSk() {
            return sk;
        }

        public void setSk(String sk) {
            this.sk = sk;
        }
    }

}
