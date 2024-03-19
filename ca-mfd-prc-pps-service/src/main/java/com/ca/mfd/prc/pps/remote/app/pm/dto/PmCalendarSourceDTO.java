package com.ca.mfd.prc.pps.remote.app.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 工厂日历清洗DTO
 *
 * @author jay.he
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "工厂日历清洗DTO")
public class PmCalendarSourceDTO {

    private String workshopCode;
    private String lineCode;
    private Date workDay;
    private String shiftName;
    private String startTimeShift;
    private String endTimeShift;
    private int overTime;// 加班时间(分钟)
    private boolean isDayAfter;//结束时间跨天
    private String startTimeBreak = "";
    private String endTimeBreak = "";
    private long prcShcCalendarId;

    private int rowInx;//班次内排序
    private int countRow;//班次内计数
    // private int rowNum;//循环顺序

}
