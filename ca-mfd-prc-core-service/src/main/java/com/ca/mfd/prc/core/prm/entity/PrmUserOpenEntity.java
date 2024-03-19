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
 * @Description: 用户第三方登录信息
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户第三方登录信息")
@TableName("PRC_PRM_USER_OPEN")
public class PrmUserOpenEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_USER_OPEN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 用户ID
     */
    @Schema(title = "用户ID")
    @TableField("PRC_PRM_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmUserId = Constant.DEFAULT_ID;

    /**
     * 用户类型:1、后台,2、前台
     */
    @Schema(title = "用户类型:1、后台,2、前台")
    @TableField("USER_TYPE")
    private Integer userType = 1;

    /**
     * 类型:1、微信
     */
    @Schema(title = "类型:1、微信")
    @TableField("TYPE")
    private Integer type = 0;

    /**
     * 唯一标示码
     */
    @Schema(title = "唯一标示码")
    @TableField("OPEN_CODE")
    private String openCode = StringUtils.EMPTY;

    /**
     * 联合标示码
     */
    @Schema(title = "联合标示码")
    @TableField("UNION_CODE")
    private String unionCode = StringUtils.EMPTY;

    /**
     * 过期时长:单位s
     */
    @Schema(title = "过期时长:单位s")
    @TableField("EXPIRES_IN")
    private Long expiresIn = 0L;

    /**
     * 触发时间
     */
    @Schema(title = "触发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("ACTIVE_DT")
    private Date activeDt;

    /**
     * accesstoken 字符串
     */
    @Schema(title = "accesstoken字符串")
    @TableField("ACCESS_TOKEN")
    private String accessToken = StringUtils.EMPTY;

    /**
     * refreshtoken 字符串
     */
    @Schema(title = "refreshtoken字符串")
    @TableField("REFRESH_TOKEN")
    private String refreshToken = StringUtils.EMPTY;

    /**
     * 访问范围
     */
    @Schema(title = "访问范围")
    @TableField("SCOPE")
    private String scope = StringUtils.EMPTY;

    /**
     * 扩展字段
     */
    @Schema(title = "扩展字段")
    @TableField("EXT_DATA")
    private String extData = StringUtils.EMPTY;

}
