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
 * @Description: 区域多设备配置
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "区域多设备配置")
@TableName("PRC_PMC_ALARM_AREA_STOP_SET")
public class PmcAlarmAreaStopSetEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PMC_ALARM_AREA_STOP_SET_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 位置集合
     */
    @Schema(title = "位置集合")
    @TableField("POSITIONS")
    private String positions = StringUtils.EMPTY;

    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("ALARM_AREA_STOP_SET_NAME")
    private String alarmAreaStopSetName = StringUtils.EMPTY;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("ALARM_AREA_STOP_SET_DESCRIPTION")
    private String alarmAreaStopSetDescription = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

}
