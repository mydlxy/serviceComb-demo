//package com.ca.mfd.prc.common;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * 用户基本信息
// *
// * @author inkelink eric.zhou@hg2mes.com
// * @since 1.0.0 2023-08-06
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//public class OnlineUserDTO implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    public OnlineUserDTO() {
//        this.id = StringUtils.EMPTY;
//        this.userName = StringUtils.EMPTY;
//        this.nickName = StringUtils.EMPTY;
//        this.code = StringUtils.EMPTY;
//        this.loginName = StringUtils.EMPTY;
//        this.roles = new ArrayList<>();
//        this.permissions = new ArrayList<>();
//        this.loginTime = new Date();
//        this.language = "CN";
//        this.token = StringUtils.EMPTY;
//        this.refreshToken = StringUtils.EMPTY;
//        this.fullName = StringUtils.EMPTY;
//        this.extData = StringUtils.EMPTY;
//        this.productCode = "MES";
//        this.groupName = "System";
//    }
//
//    @JsonProperty("id")
//    private String id = UUIDUtils.getEmpty();
//
//    @JsonProperty("userType")
//    private Integer userType = 0;
//
//    @JsonProperty("userName")
//    private String userName;
//
//    @JsonProperty("nickName")
//    private String nickName;
//
//    @JsonProperty("Code")
//    private String code;
//
//    @JsonProperty("loginName")
//    private String loginName;
//
//    @JsonProperty("language")
//    private String language;
//
//    @JsonProperty("token")
//    private String token;
//
//    @JsonProperty("refreshToken")
//    private String refreshToken;
//
//    @JsonProperty("roles")
//    private List<String> roles;
//
//    @JsonProperty("permissions")
//    private List<String> permissions;
//
//    @JsonProperty("isExpire")
//    private Boolean isExpire = false;
//
//
//    @JsonProperty("loginTime")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
//    private Date loginTime;
//
//    @JsonProperty("fullName")
//    private String fullName;
//
//    @JsonProperty("userToken")
//    private String userToken;
//
//    @JsonProperty("extData")
//    private String extData;
//
//    @JsonProperty("domain")
//    private String domain = StringUtils.EMPTY;
//
//    @JsonProperty("productCode")
//    private String productCode = StringUtils.EMPTY;
//
//    @JsonProperty("groupName")
//    private String groupName;
//
//    @JsonProperty("email")
//    private String email;
//
//
//    @JsonProperty("phone")
//    private String phone;
//
//
//    @JsonProperty("dpartmentName")
//    private String dpartmentName = StringUtils.EMPTY;
//
//
//    @JsonProperty("departmentCode")
//    private String departmentCode = StringUtils.EMPTY;
//
//}
//
