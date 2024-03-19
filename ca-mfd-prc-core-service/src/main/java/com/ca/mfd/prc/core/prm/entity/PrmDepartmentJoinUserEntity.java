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
 * @Description: 部门关联员工表
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门关联员工表")
@TableName("PRC_PRM_DEPARTMENT_JOIN_USER")
public class PrmDepartmentJoinUserEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_DEPARTMENT_JOIN_USER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 部门ID
     */
    @Schema(title = "部门ID")
    @TableField("PRC_PRM_DEPARTMENT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmDepartmentId = Constant.DEFAULT_ID;

    /**
     * 员工ID
     */
    @Schema(title = "员工ID")
    @TableField("PRC_PRM_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPrmUserId = Constant.DEFAULT_ID;

}
