package com.ca.mfd.prc.pm.remote.app.core.sys.entity;

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
 * @Description: 授权码自动配置
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "授权码自动配置")
@TableName("PRC_SYS_LICENSE_NOTIFIER")
public class SysLicenseNotifierEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_LICENSE_NOTIFIER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 用户ID
     */
    @Schema(title = "用户ID")
    @TableField("USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId = Constant.DEFAULT_ID;

    /**
     * 用户名
     */
    @Schema(title = "用户名")
    @TableField("USER_NAME")
    private String userName = StringUtils.EMPTY;

    /**
     * 用户邮箱
     */
    @Schema(title = "用户邮箱")
    @TableField("USER_EMAIL")
    private String userEmail = StringUtils.EMPTY;

    /**
     * 过期时间
     */
    @Schema(title = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("NOTIFIER_DAY")
    private Date notifierDay;

    /**
     * 是否过期
     */
    @Schema(title = "是否过期")
    @TableField("IS_TIME_LIMIT")
    private Boolean isTimeLimit = false;

}
