package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Date;

@Data
public class ShcCalTempInfo {
    /**
     * 开始时间跨天
     */
    private Boolean isDayBefore = false;
    /**
     * 结束时间跨天
     */
    private Boolean isDayAfter = false;
    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;
    /**
     * 加班时间(分钟)
     */
    private Integer overtime = 0;
    /**
     * 开始时间
     */
    private String breakStartTime;
    /**
     * 结束时间
     */
    private String breakEndTime;
    /**
     * id
     */
    private String shiftId;
    /**
     * 名称
     */
    private String shiftName;
    /**
     * 开始时间
     */
    private String shiftStartTime;
    /**
     * 结束时间
     */
    private String shiftEndTime;
    /**
     * 产能
     */
    private Integer productivity = 0;
    private String id;
}
