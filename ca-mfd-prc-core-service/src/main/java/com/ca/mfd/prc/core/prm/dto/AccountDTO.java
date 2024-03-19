package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.enums.LoginStyle;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * @author eric.zhou
 */
public class AccountDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "LogOnModel", description = "LogOnModel")
    @Data
    public static class LogOnModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "站点名")
        private String app;

        @Schema(description = "用户名")
        // [Required(ErrorMessage = "请输入用户名.")]
        private String userName;

        @Schema(description = "密　码")
        // [Required(ErrorMessage = "请输入密码.")]
        private String password;

        @Schema(description = "记住我")
        private Boolean rememberMe = false;

        @Schema(description = "附加信息")
        private String extData;

        @Schema(description = "登录方式")
        private LoginStyle loginStyle = LoginStyle.MesLogin;

        @Schema(description = "用户类型：0忽略用户类型，大于0登陆会验证")
        private Integer userType = 0;

        @Schema(description = "验证码")
        private String hasCode;

        @Schema(description = "输入验证码")
        @JsonAlias({"verificationCode", "VerificationCode"})
        private String verificationCode;

    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "AuthorizeModel", description = "AuthorizeModel")
    @Data
    public static class AuthorizeModel extends BasicServiceModel<PrmUserEntity> {
        private static final long serialVersionUID = 1L;

        @Schema(description = "站点名")
        private String app;

        @Schema(description = "用户名")
        // [Required(ErrorMessage = "请输入用户名.")]
        private String userName;

        @Schema(description = "密　码")
        //[Required(ErrorMessage = "请输入密码.")]
        private String password;

        @Schema(description = "IP地址")
        private String ipAddress;

        @Schema(description = "授权模块")
        private String module;

        @Schema(description = "授权代码")
        private String permission;

        @Schema(description = "登录方式")
        private LoginStyle loginStyle = LoginStyle.MesLogin;

        @Schema(description = "用户类型：0忽略用户类型，大于0登陆会验证")
        private Integer userType = 0;

        @Schema(title = "验证码")
        private String captchaCode;

        @Schema(title = "验证码Token")
        private String captchaToken;


    }
}


