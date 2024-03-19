package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.entity.CookieSession;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.OpenData;
import com.ca.mfd.prc.common.entity.SessionEntity;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author banny.luo
 */

public class SessionDTO {
    /**
     * @author banny.luo
     */
    @Schema(title = "MemberLoginModel", description = "MemberLoginModel")
    @Data
    public static class MemberLoginModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(title = "用户名")
        @JsonAlias(value = {"UserName", "userName"})
        private String userName;

        @Schema(title = "密码")
        @JsonAlias(value = {"Pwd", "pwd"})
        private String pwd;

        @Schema(title = "1、mes defalut Login;2、AD login；3AuthOpenLogin")
        @JsonAlias(value = {"LoginStyle", "loginStyle"})
        private int loginStyle;

        @Schema(title = "用户类型")
        @JsonAlias(value = {"UserType", "userType"})
        private int userType = -1;

        @Schema(title = "类型")
        @JsonAlias(value = {"RememberType", "rememberType"})
        private int rememberType = 4;

        @Schema(title = "prm sdk")
        @JsonAlias(value = {"SessionEntity", "sessionEntity"})
        private SessionEntity sessionEntity;
    }

    @Schema(title = "MemberThirdLoginModel", description = "MemberThirdLoginModel")
    @Data
    public static class MemberThirdLoginModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(title = "token")
        @JsonAlias(value = {"token", "Token"})
        private OpenData token;
        @Schema(title = "用户类型")
        @JsonAlias(value = {"userType", "UserType"})
        private int userType;

        @Schema(title = "true，自动注册是 UserName 和 Pwd 不需要填写，false,需要录入用户绑定的后台账号用户名和密码")
        @JsonAlias(value = {"autoReg", "AutoReg"})
        private Boolean autoReg;

        @Schema(title = "用户名类型 1用户名,2手机3邮箱4工号5IDCard")
        @JsonAlias(value = {"userNameType", "UserNameType"})
        private int userNameType = 1;

        @Schema(title = "用户名")
        @JsonAlias(value = {"userName", "UserName"})
        private String userName;

        @Schema(title = "密码")
        @JsonAlias(value = {"pwd", "Pwd"})
        private String pwd;

        @Schema(title = "类型")
        @JsonAlias(value = {"rememberType", "RememberType"})
        private int rememberType = 4;

        @Schema(title = "是否允许多个三方账号绑定同一个后台账号")
        @JsonAlias(value = {"mutiBind", "MutiBind"})
        private Boolean mutiBind = false;

        @Schema(title = "自动注册时的默认角色")
        @JsonAlias(value = {"roles", "Roles"})
        private List<String> roles;

        @Schema(title = "sessionEntity")
        @JsonAlias(value = {"sessionEntity", "SessionEntity"})
        private SessionEntity sessionEntity;
    }

    @Schema(title = "MemberAutoLoginModel", description = "MemberAutoLoginModel")
    @Data
    public static class MemberAutoLoginModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(title = "CookieSession信息")
        @JsonAlias(value = {"cookieSession", "CookieSession"})
        private CookieSession cookieSession;

        @Schema(title = "用户地址")
        @JsonAlias(value = {"userHostAddress", "UserHostAddress"})
        private String userHostAddress;
    }

    @Schema(title = "MemberLogOutModel", description = "MemberLogOutModel")
    @Data
    public static class MemberLogOutModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(title = "CookieSession信息")
        @JsonAlias(value = {"cookieSession", "CookieSession"})
        private CookieSession cookieSession;

        @Schema(title = "状态")
        @JsonAlias(value = {"status", "Status"})
        private int status;
    }

    @Schema(title = "MemberUnLockModel", description = "MemberUnLockModel")
    @Data
    public static class MemberUnLockModel implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(title = "CookieSession信息")
        @JsonAlias(value = {"cookieSession", "CookieSession"})
        private CookieSession cookieSession;

        @Schema(title = "密码")
        @JsonAlias(value = {"password", "Password"})
        private String password;
    }

    @Schema(title = "MemberUnLockModel", description = "MemberUnLockModel")
    @Data
    public static class UserAddDto {
        @JsonAlias(value = {"userData", "UserData"})
        private List<OnlineUserDTO> userData;
    }

}
