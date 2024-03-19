package com.ca.mfd.prc.common.entity;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.dto.core.MenuItemsPremissmsInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户基本信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户基本信息")
public class OnlineUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @Schema(title = "唯一键")
    @JsonProperty("id")
    private Long id = 0L;
    @Schema(title = "用户类型")
    @JsonProperty("userType")
    private Integer userType = 0;
    @Schema(title = "名称")
    @JsonProperty("userName")
    private String userName;
    @Schema(title = "昵称")
    @JsonProperty("nickName")
    private String nickName;
    @Schema(title = "编号")
    @JsonProperty("Code")
    private String code;
    @Schema(title = "登录名")
    @JsonProperty("loginName")
    private String loginName;
    @Schema(title = "语言")
    @JsonProperty("language")
    private String language;
    @Schema(title = "第三方Token")
    @JsonProperty("token")
    private String token;
    @Schema(title = "第三方刷新Token")
    @JsonProperty("refreshToken")
    private String refreshToken;
    @Schema(title = "角色组")
    @JsonProperty("roles")
    private List<String> roles;
    @Schema(title = "权限组")
    @JsonProperty("permissions")
    private List<String> permissions;
    @Schema(title = "是否到强制过期")
    @JsonProperty("isExpire")
    private Boolean isExpire = false;
    @Schema(title = "登录时间")
    @JsonProperty("loginTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date loginTime;
    @Schema(title = "完整名称 格式：中文名/英文名/工号")
    @JsonProperty("fullName")
    private String fullName;
    @Schema(title = "登录Token")
    @JsonProperty("userToken")
    private String userToken;
    @Schema(title = "附加信息")
    @JsonProperty("extData")
    private String extData;
    @Schema(title = "用户登录域")
    @JsonProperty("domain")
    private String domain = StringUtils.EMPTY;
    @Schema(title = "产品类型")
    @JsonProperty("productCode")
    private String productCode;
    @Schema(title = "分组")
    @JsonProperty("groupName")
    private String groupName;
    @Schema(title = "邮箱")
    @JsonProperty("email")
    private String email;
    @Schema(title = "手机")
    @JsonProperty("phone")
    private String phone;
    @Schema(title = "部门名称")
    @JsonProperty("dpartmentName")
    private String dpartmentName = StringUtils.EMPTY;
    @Schema(title = "部门代码")
    @JsonProperty("departmentCode")
    private String departmentCode = StringUtils.EMPTY;
    @Schema(title = "menuName")
    @JsonProperty("menuName")
    private String menuName= StringUtils.EMPTY;
    @Schema(title = "菜单信息")
    @JsonProperty("menuPermissions")
    private List<MenuItemsPremissmsInfo> menuPermissions;
    @Schema(title = "门户:按钮权限")
    @JsonProperty("btnPermission")
    private List<String> btnPermission;
    @Schema(title = "门户:接口权限")
    @JsonProperty("uriPermission")
    private List<String> uriPermission;


    public OnlineUserDTO() {
        this.id = Constant.DEFAULT_USER_ID;
        this.userName = StringUtils.EMPTY;
        this.nickName = StringUtils.EMPTY;
        this.code = StringUtils.EMPTY;
        this.loginName = StringUtils.EMPTY;
        this.roles = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.loginTime = new Date();
        this.language = "CN";
        this.token = StringUtils.EMPTY;
        this.refreshToken = StringUtils.EMPTY;
        this.fullName = StringUtils.EMPTY;
        this.extData = StringUtils.EMPTY;
        this.productCode = "MES";
        this.groupName = "System";
    }

}

