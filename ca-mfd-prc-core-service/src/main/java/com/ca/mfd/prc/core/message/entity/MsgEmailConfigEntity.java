package com.ca.mfd.prc.core.message.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jay.he
 * @Description: 邮箱维护账号
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "邮箱维护账号")
@TableName("PRC_MSG_EMAIL_CONFIG")
public class MsgEmailConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MSG_EMAIL_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 邮箱账号
     */
    @Schema(title = "邮箱账号")
    @TableField("EMAIL_ACCOUNT")
    private String emailAccount;

    /**
     * 邮箱密码
     */
    @Schema(title = "邮箱密码")
    @TableField("EMAIL_PASSWORD")
    private String emailPassword;

    /**
     * 邮箱主机 比如smtp.263.net
     */
    @Schema(title = "邮箱主机")
    @TableField("EMAIL_HOST")
    private String emailHost;

    /**
     * 邮箱端口
     */
    @Schema(title = "邮箱端口")
    @TableField("EMAIL_PORT")
    private Integer emailPort;

    /**
     * 重发次数
     */
    @Schema(title = "重发次数")
    @TableField("EMAIL_SENDTIMES")
    private Integer emailSendtimes;

    /**
     * 业务类型 比如0,1,2,3,4,5,6,7,8,9,99,10，都好隔开
     */
    @Schema(title = "业务类型")
    @TableField("EMAIL_TARGETTYPES")
    private String emailTargettypes;

    /**
     * 邮件主题
     */
    @Schema(title = "邮件主题")
    @TableField("EMAIL_TITLE")
    private String emailTitle;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private boolean enableFlag;
    //  private boolean isEnable;

}