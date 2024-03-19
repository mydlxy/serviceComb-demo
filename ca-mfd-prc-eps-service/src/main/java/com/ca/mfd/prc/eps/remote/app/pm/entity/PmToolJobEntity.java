package com.ca.mfd.prc.eps.remote.app.pm.entity;

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
 * @Description: 作业实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "作业")
@TableName("PRC_PM_TOOL_JOB")
public class PmToolJobEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_TOOL_JOB_ID", type = IdType.INPUT)
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
    @TableField(exist = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_TOOL_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmToolId = Constant.DEFAULT_ID;


    /**
     * 作业号
     */
    @Schema(title = "作业号")
    @TableField("JOB_NO")
    @NotBlank(message = "参数号不能为空")
    private String jobNo = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PM_WO_ID")
    @NotNull(message = "操作不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pmWoId = Constant.DEFAULT_ID;


    /**
     * 车辆特性
     */
    @Schema(title = "车辆特性")
    @TableField("FEATURE_CODE")
    @NotBlank(message = "特性不能为空")
    private String featureCode = StringUtils.EMPTY;

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
    private String toolCode;

    @TableField(exist = false)
    @JsonIgnore
    private String woCode;

    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;


}