package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 计划日历
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Data
public class PmCalendarSource {

    private String pmShopCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;

    private String shiftName;

    private String startTimeShift;

    private String endTimeShift;

    private Integer overTime = 0;

    private Boolean isDayAfter = false;

    private String startTimeBreak;

    private String endTimeBreak;

    private String shcCalendarId = StringUtils.EMPTY;
}
