package com.ca.mfd.prc.core.communication.remote.app.pm.dto;

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
 * @author inkelink
 * @Description: 工厂日历实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工厂日历")
@TableName("PRC_PM_SHC_CALENDAR")
public class PmShcCalendarEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_SHC_CALENDAR_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * 班次ID
     */
   /* @Schema(title = "班次ID")
    @TableField("PRC_PM_SHC_SHIFT_ID")
    private Long shcShiftId = Constant.DEFAULT_ID;*/

    /**
     * 班次CODE
     */
    @Schema(title = "班次CODE")
    @TableField("SHC_SHIFT_CODE")
    private Integer shcShiftCode = 0;


    /**
     * 班次名称
     */
    @Schema(title = "班次名称")
    @TableField("SHC_SHIFT_NAME")
    private String shcShiftName = StringUtils.EMPTY;


    /**
     * 产能
     */
    @Schema(title = "产能")
    @TableField("PRODUCTIVITY")
    private Integer productivity = 0;


    /**
     * 日期
     */
    @Schema(title = "日期")
    @TableField("WORK_DAY")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;


    /**
     * 班次开始时间
     */
    @Schema(title = "班次开始时间")
    @TableField("SHC_SHIFT_START_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shcShiftStartDt;


    /**
     * 班次结束时间
     */
    @Schema(title = "班次结束时间")
    @TableField("SHC_SHIFT_END_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shcShiftEndDt;


    /**
     * 加班时间(分钟)
     */
    @Schema(title = "加班时间(分钟)")
    @TableField("OVERTIME")
    private Integer overtime = 0;

    /**
     * 车间还是线体的标识 1-车间；2-线体
     */
    @Schema(title = "车间还是线体的标识 1-车间；2-线体")
    @TableField(exist = false)
    private int typeFlag = 1;

    @TableField(exist = false)
    private String strWorkDay;


}