package com.ca.mfd.prc.pqs.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author inkelink
 * @Description: 岗位操作实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位操作")
@TableName("PRC_PM_WO")
public class PmWoEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_WO_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("WO_CODE")
    @NotBlank(message = "工艺代码不能为空")
    private String woCode = StringUtils.EMPTY;


    /**
     * 工艺类型
     */
    @Schema(title = "工艺类型")
    @TableField("WO_TYPE")
    @NotNull(message = "工艺类型不能为空")
    private Integer woType = 0;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("WO_DESCRIPTION")
    @NotBlank(message = "工艺描述不能为空")
    private String woDescription = StringUtils.EMPTY;


    /**
     * 分组
     */
    @Schema(title = "分组")
    @TableField("WO_GROUP_NAME")
    private String woGroupName = StringUtils.EMPTY;


    /**
     * 车辆特性
     */
    @Schema(title = "车辆特性")
    @TableField("FEATURE_CODE")
    @NotBlank(message = "工艺特性不能为空")
    private String featureCode = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("QM_DEFECT_ANOMALY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long qmDefectAnomalyId = Constant.DEFAULT_ID;


    /**
     * 异常代码
     */
    @Schema(title = "异常代码")
    @TableField("QM_DEFECT_ANOMALY_CODE")
    private String qmDefectAnomalyCode = StringUtils.EMPTY;


    /**
     * 异常代码描述
     */
    @Schema(title = "异常代码描述")
    @TableField("QM_DEFECT_ANOMALY_DESCRIPTION")
    private String qmDefectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 组件
     */
    @Schema(title = "")
    @TableField("QM_DEFECT_COMPONENT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long qmDefectComponentId = Constant.DEFAULT_ID;


    /**
     * 批量追溯
     */
    @Schema(title = "批量追溯")
    @TableField("TRC_BY_GROUP")
    private Boolean trcByGroup = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @NotNull(message = "顺序号不能为空")
    private Integer displayNo = 0;


    /**
     * 操作类型
     */
    @Schema(title = "操作类型")
    @TableField("OPER_TYPE")
    @NotNull(message = "操作类型不能为空")
    private Integer operType = 0;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PQS_CODERULE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pqsCoderuleId = Constant.DEFAULT_ID;

    @TableField(exist = false)
    @JsonIgnore
    private String workshopCode;

    @TableField(exist = false)
    @JsonIgnore
    private String lineCode;

    @TableField(exist = false)
    @JsonIgnore
    private String workstationCode;

    @TableField(exist = false)
    @JsonIgnore
    private String traceComponentCode;

    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String enableFlag;


    public String getDefectAnomalyCode() {
        return this.qmDefectAnomalyCode;
    }


}