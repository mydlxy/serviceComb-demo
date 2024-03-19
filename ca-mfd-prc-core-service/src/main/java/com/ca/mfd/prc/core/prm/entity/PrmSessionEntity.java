package com.ca.mfd.prc.core.prm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 登录日志
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "登录日志")
@TableName("PRC_PRM_SESSION")
public class PrmSessionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_SESSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 用户id
     */
    @Schema(title = "用户id")
    @TableField("PRC_PRM_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmUserId = Constant.DEFAULT_ID;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    @TableField("USER_NAME")
    private String userName = StringUtils.EMPTY;

    /**
     * 详情
     */
    @Schema(title = "详情")
    @TableField("TEXT")
    private String text = StringUtils.EMPTY;

    /**
     * 登录ip
     */
    @Schema(title = "登录ip")
    @TableField("IP")
    private String ip = StringUtils.EMPTY;

    /**
     * 登录端口
     */
    @Schema(title = "登录端口")
    @TableField("PORT")
    private String port = StringUtils.EMPTY;

    /**
     * 状态:0:锁定,1:可用,2:登出,3:切换登出,4:修改密码
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    private Integer status = 0;

    /**
     * 类型
     */
    @Schema(title = "类型")
    @TableField("TYPE")
    private Integer type = 0;

    /**
     * 应用
     */
    @Schema(title = "应用")
    @TableField("APP")
    private String app = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 激活次数
     */
    @Schema(title = "激活次数")
    @TableField("ACTIVE_COUNT")
    private Integer activeCount = 0;

    /**
     * 过期时间
     */
    @Schema(title = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("EXPIRE_DT")
    private Date expireDt;

    /**
     * 登录时间
     */
    @Schema(title = "登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("LOGIN_DT")
    private Date loginDt;

    /**
     * 激活时间
     */
    @Schema(title = "激活时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("ACTIVE_DT")
    private Date activeDt;

    /**
     * ua 刷新token
     */
    @Schema(title = "ua刷新token")
    @TableField("REFRESH_TOKEN")
    private String refreshToken = StringUtils.EMPTY;

    /**
     * ua token
     */
    @Schema(title = "uatoken")
    @TableField("ACCESS_TOKEN")
    private String accessToken = StringUtils.EMPTY;

    /**
     * 权限版本
     */
    @Schema(title = "权限版本")
    @TableField("PERMISSION_VERSION")
    private String permissionVersion = StringUtils.EMPTY;

    /**
     * 用户信息
     */
    @Schema(title = "用户信息")
    @TableField("USER_INFO_VERSION")
    private String userInfoVersion;

    /**
     * 用户类型
     */
    @Schema(title = "用户类型")
    @TableField("USER_TYPE")
    private Integer userType = 0;

}
