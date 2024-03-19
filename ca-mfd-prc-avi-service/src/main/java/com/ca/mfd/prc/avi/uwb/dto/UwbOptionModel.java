package com.ca.mfd.prc.avi.uwb.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * UWBOptionModel
 *
 * @author eric.zhou
 * @date 2023/05/18
 */
@Data
@Configuration
public class UwbOptionModel {

    /**
     * 登录地址
     */
    @Value("${LsWebsocketClient.LoginUrl:}")
    private String loginUrl;

    /**
     * ip地址
     */
    @Value("${LsWebsocketClient.Ip:}")
    private String ip;

    /**
     * 端口
     */
    @Value("${LsWebsocketClient.Port:0}")
    private Integer port = 0;

    /**
     * 用户名
     */
    @Value("${LsWebsocketClient.UserName:}")
    private String userName;

    /**
     * 密码
     */
    @Value("${LsWebsocketClient.PassWord:}")
    private String passWord;

    /**
     * 掩码
     */
    @Value("${LsWebsocketClient.Salt:}")
    private String salt;

    @Value("${LsWebsocketClient.X64:false}")
    private Boolean x64 = false;

    @Value("${LsWebsocketClient.IsWss:false}")
    private Boolean isWss = false;

    /**
     * 绑定地址
     */
    @Value("${LsWebsocketClient.BindUrl:}")
    private String bindUrl = StringUtils.EMPTY;

    /**
     * 查询地址
     */
    @Value("${LsWebsocketClient.InfoUrl:}")
    private String infoUrl = StringUtils.EMPTY;
}
