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
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 部门管理
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门管理")
@TableName("PRC_PRM_DEPARTMENT")
public class PrmDepartmentEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_DEPARTMENT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 组织机构
     */
    @Schema(title = "组织机构")
    @TableField("ORGANIZATION_NAME")
    private String organizationName = StringUtils.EMPTY;

    /**
     * 部门名称
     */
    @Schema(title = "部门名称")
    @TableField("DEPARTMENT_NAME")
    private String departmentName = StringUtils.EMPTY;

    /**
     * 部门代码
     */
    @Schema(title = "部门代码")
    @TableField("DEPARTMENT_CODE")
    private String departmentCode = StringUtils.EMPTY;

    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;

    /**
     * 部门排序
     */
    @Schema(title = "部门排序")
    @TableField("SEQ_NUMBER")
    private Integer seqNumber = 0;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 位置
     */
    @Schema(title = "位置")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;

    @Schema(title = "子集")
    @TableField(exist = false)
    private List<PrmDepartmentEntity> children;

}
