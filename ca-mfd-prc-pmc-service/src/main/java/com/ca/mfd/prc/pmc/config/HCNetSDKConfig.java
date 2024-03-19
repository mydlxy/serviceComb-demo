package com.ca.mfd.prc.pmc.config;

import com.sun.jna.Native;
import open.hikvision.com.hcnetsdk.HCNetSDK;
import open.hikvision.com.hcnetsdk.OsUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.net.URISyntaxException;

@Configuration
public class HCNetSDKConfig {

    @Bean
    @Lazy
    public HCNetSDK initHkSDK() throws Exception {
        String hikLibPath;
        try {
            hikLibPath = OsUtils.getLoadLibrary();
        } catch (URISyntaxException e) {
            throw new Exception("未找到HcNetSdk的ddl或so文件");
        }
        return (HCNetSDK) Native.loadLibrary(hikLibPath, HCNetSDK.class);
    }

    public static void main(String[] args) {

    }

}