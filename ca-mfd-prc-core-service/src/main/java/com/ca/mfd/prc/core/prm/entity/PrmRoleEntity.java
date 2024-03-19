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

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 角色表
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色表")
@TableName("PRC_PRM_ROLE")
public class PrmRoleEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_ROLE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("ROLE_CODE")
    private String roleCode;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("ROLE_NAME")
    private String roleName;

    /**
     * 组名
     */
    @Schema(title = "组名")
    @TableField("GROUP_NAME")
    private String groupName;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("REMARK")
    private String remark;

    @Schema(title = "权限")
    @TableField(exist = false)
    private List<PrmPermissionEntity> permissions = new ArrayList();

}
