package com.ca.mfd.prc.avi.remote.app.pm.entity;

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

/**
 * @author inkelink
 * @Description: 操作终端实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "操作终端")
@TableName("PRC_PM_OT")
public class PmOtEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_OT_ID", type = IdType.INPUT)
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
     * 名称
     */
    @Schema(title = "名称")
    @TableField("OT_NAME")
    @NotBlank(message = "OT名称不能为空")
    private String otName = StringUtils.EMPTY;


    /**
     * IP地址
     */
    @Schema(title = "IP地址")
    @TableField("IP_ADDRESS")
    @NotBlank(message = "IP地址不能为空")
    private String ipAddress = StringUtils.EMPTY;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("OT_DESCRIPTION")
    @NotBlank(message = "描述不能为空")
    private String otDescription = StringUtils.EMPTY;


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
     * 模板
     */
    @Schema(title = "模板")
    @TableField("TEMPLATE")
    @NotBlank(message = "OT模板不能为空")
    private String template = StringUtils.EMPTY;

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
    private String deleteFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String enableFlag;


}