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

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 用户权限关联表
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户权限关联表")
@TableName("PRC_PRM_USER_PERMISSION")
public class PrmUserPermissionEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_USER_PERMISSION_ID", type = IdType.INPUT)
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
     * 用户ID
     */
    @Schema(title = "用户ID")
    @TableField("PRC_PRM_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmUserId = Constant.DEFAULT_ID;

    /**
     * 回收时间
     */
    @Schema(title = "回收时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("RECYCLE_DT")
    private Date recycleDt;

}
