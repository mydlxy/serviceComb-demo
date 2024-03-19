package com.ca.mfd.prc.core.prm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author inkelink ${email}
 * @Description: 令牌权限表
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "令牌权限表")
@TableName("PRC_PRM_TOKEN_PERMISSION")
public class PrmTokenPermissionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_TOKEN_PERMISSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 权限ID
     */
    @Schema(title = "权限ID")
    @TableField("PRC_PRM_PERMISSION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmPermissionId = Constant.DEFAULT_ID;

    /**
     * 令牌ID
     */
    @Schema(title = "令牌ID")
    @TableField("PRC_PRM_TOKEN_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmTokenId = Constant.DEFAULT_ID;
}
