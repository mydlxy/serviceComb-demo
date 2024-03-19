package com.ca.mfd.prc.pmc.entity;

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

/**
 * @author inkelink ${email}
 * @Description: 停线代码
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "停线代码")
@TableName("PRC_PMC_STOP_CODE")
public class PmcStopCodeEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PMC_STOP_CODE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * CODE分类
     */
    @Schema(title = "CODE分类")
    @TableField("STOP_TYPE")
    private String stopType = StringUtils.EMPTY;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("STOP_NAME")
    private String stopName = StringUtils.EMPTY;

    /**
     * 停线CODE
     */
    @Schema(title = "停线CODE")
    @TableField("STOP_CODE")
    private String stopCode = StringUtils.EMPTY;

    /**
     * 停线CODE描述
     */
    @Schema(title = "停线CODE描述")
    @TableField("STOP_CODE_DESC")
    private String stopCodeDesc = StringUtils.EMPTY;

    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    @TableField("STOP_DEPARTMENT")
    private String stopDepartment = StringUtils.EMPTY;

    /**
     * 停线原因分类
     */
    @Schema(title = "停线原因分类")
    @TableField("STOP_CAUSE_TYPE")
    private String stopCauseType = StringUtils.EMPTY;

    /**
     * 停线分类代码
     */
    @Schema(title = "停线分类代码")
    @TableField("STOP_TYPE_CODE")
    private String stopTypeCode = StringUtils.EMPTY;

}
