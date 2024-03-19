package com.ca.mfd.prc.pps.remote.app.pm.entity;

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
 * @author jay.he
 * @Description: 工厂日历清洗
 * @date 2023年09月08日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工厂日历清洗")
@TableName("PRC_PM_SHC_CALENDAR_ATOM")
public class PmShcCalendarAtomEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_SHC_CALENDAR_ATOM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 工厂日历主键ID
     */
    @Schema(title = "工厂日历主键ID")
    @TableField("PRC_SHC_CALENDAR_ID")
    private Long prcShcCalendarId = Constant.DEFAULT_ID;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("WORK_DAY")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;

    /**
     * 班次名称
     */
    @Schema(title = "班次名称")
    @TableField("SHIFT_NAME")
    private String shiftName = StringUtils.EMPTY;


    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date startTime;

    /**
     * 结束时间
     */
    @Schema(title = "结束时间")
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endTime;

    /**
     * 有效工作时间
     */
    @Schema(title = "有效工作时间")
    @TableField("WORK_TIME")
    private Integer workTime = 0;

    /**
     * 班次开始时间
     */
    @Schema(title = "班次开始时间")
    @TableField("SHIFT_START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shiftStartTime;


    /**
     * 班次结束时间
     */
    @Schema(title = "班次结束时间")
    @TableField("SHIFT_END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shiftEndTime;

    /**
     * 车间还是线体的标识 1-车间；2-线体
     */
    @Schema(title = "车间还是线体的标识 1-车间；2-线体")
    @TableField(exist = false)
    private int typeFlag = 1;


}