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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 令牌表
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "令牌表")
@TableName("PRC_PRM_TOKEN")
public class PrmTokenEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_TOKEN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 系统名称
     */
    @Schema(title = "系统名称")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;

    /**
     * 令牌
     */
    @Schema(title = "令牌")
    @TableField("TOKEN")
    private String token = StringUtils.EMPTY;

    /**
     * 令牌名称
     */
    @Schema(title = "令牌名称")
    @TableField("TOKEN_NAME")
    private String tokenName = StringUtils.EMPTY;

    /**
     * 过期时间
     */
    @Schema(title = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("EXPIRE_DT")
    private Date expireDt;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 令牌是否启用
     */
    @Schema(title = "令牌是否启用")
    @TableField("TOKEN_ENABLE")
    private Boolean tokenEnable = false;

    /**
     * permissions
     */
    @Schema(title = "permissions")
    @TableField(exist = false)
    private List<PrmPermissionEntity> permissions = new ArrayList<>();

}
