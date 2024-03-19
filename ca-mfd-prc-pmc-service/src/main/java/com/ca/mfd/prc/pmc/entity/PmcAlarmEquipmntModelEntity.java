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
 * @Description: 报警设备建模
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报警设备建模")
@TableName("PRC_PMC_ALARM_EQUIPMNT_MODEL")
public class PmcAlarmEquipmntModelEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PMC_ALARM_EQUIPMNT_MODEL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 设备代码
     */
    @Schema(title = "设备代码")
    @TableField("ALARM_EQUIPMNT_MODEL_CODE")
    private String alarmEquipmntModelCode = StringUtils.EMPTY;

    /**
     * 设备名称
     */
    @Schema(title = "设备名称")
    @TableField("ALARM_EQUIPMNT_MODEL_NAME")
    private String alarmEquipmntModelName = StringUtils.EMPTY;

    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 设备等级
     */
    @Schema(title = "设备等级")
    @TableField("LEVEL")
    private Integer level;

    /**
     * 设备路径
     */
    @Schema(title = "设备路径")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;

}
