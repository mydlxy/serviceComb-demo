package com.ca.mfd.prc.pmc.entity;

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
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 报警原始记录
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报警原始记录")
@TableName("PRC_PMC_ALARM_COMPONENT_ALARM")
public class PmcAlarmComponentAlarmEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PMC_ALARM_COMPONENT_ALARM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 报警代码
     */
    @Schema(title = "报警代码")
    @TableField("ALARM_COMPONENT_ALARM_CODE")
    private String alarmComponentAlarmCode = StringUtils.EMPTY;

    /**
     * 设备建模编号
     */
    @Schema(title = "设备建模编号")
    @TableField("PRC_PMC_ALARM_EQUIPMNT_MODEL_ID")
    private Long prcPmcAlarmEquipmntModelId;

    /**
     * 报警状态
     */
    @Schema(title = "报警状态")
    @TableField("STATUS")
    private Integer status;

    /**
     * 位置
     */
    @Schema(title = "位置")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;

    /**
     * 报警描述
     */
    @Schema(title = "报警描述")
    @TableField("ALARM_COMPONENT_ALARM_DESCRIPTION")
    private String alarmComponentAlarmDescription = StringUtils.EMPTY;

    /**
     * 报警等级
     */
    @Schema(title = "报警等级")
    @TableField("ALARM_LEVEL")
    private Integer alarmLevel;

    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("ALARM_DT")
    private Date alarmDt;

    /**
     * 持续时间
     */
    @Schema(title = "持续时间")
    @TableField("DURATION")
    private Integer duration;

    /**
     * 车间编号
     */
    @Schema(title = "车间编号")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 是否数据沉淀
     */
    @Schema(title = "是否数据沉淀")
    @TableField("IS_DATA_DOWN")
    private Boolean isDataDown;

    /**
     * WINCC报警ID
     */
    @Schema(title = "WINCC报警ID")
    @TableField("WINCC_COUNTER")
    private Long winccCounter = Constant.DEFAULT_ID;

}
