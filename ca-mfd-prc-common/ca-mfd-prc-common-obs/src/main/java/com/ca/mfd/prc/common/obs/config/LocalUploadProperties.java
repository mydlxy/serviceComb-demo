package com.ca.mfd.prc.common.obs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 媒体中心-上传配置
 *
 * @author inkelink
 * @date 2023/10/11
 */
@Primary
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ca.cloud.local-upload")
public class LocalUploadProperties {

    private String rootDir;
    private Integer downCount = 100;
    private List<String> fileTypes  = new ArrayList<>(Arrays.asList(".html",".jpg",".jpeg",".gif",".png",".bmp",".pdf",".mp3",".zip"));

    public String getRootDir() {
        if("/".equals(rootDir)){
            rootDir = System.getProperty("user.dir");
        }
        return rootDir;
    }

    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    public Integer getDownCount() {
        return downCount;
    }

    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    public List<String> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }
}
