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
 * @Description: 用户授权记录
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户授权记录")
@TableName("PRC_PRM_AUTHORIZE")
public class PrmAuthorizeEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_AUTHORIZE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 用户外键
     */
    @Schema(title = "用户外键")
    @TableField("PRC_PRM_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmUserId = Constant.DEFAULT_ID;

    /**
     * 应用名称
     */
    @Schema(title = "应用名称")
    @TableField("APP")
    private String app = StringUtils.EMPTY;

    /**
     * 工号/用户/名称
     */
    @Schema(title = "工号/用户/名称")
    @TableField("NICK_NAME")
    private String name = StringUtils.EMPTY;

    /**
     * IP地址
     */
    @Schema(title = "IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress = StringUtils.EMPTY;

    /**
     * 授权模块
     */
    @Schema(title = "授权模块")
    @TableField("MODULE")
    private String module = StringUtils.EMPTY;

    /**
     * 授权代码
     */
    @Schema(title = "授权代码")
    @TableField("PERMISSION")
    private String permission = StringUtils.EMPTY;

    /**
     * 授权时间
     */
    @Schema(title = "授权时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("AUTHORIZE_DT")
    private Date authorizeDt;

}
