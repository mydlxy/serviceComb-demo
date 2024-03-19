package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ShcCalendarDetailInfo {

    private String id = StringUtils.EMPTY;
    /**
     * 工作日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;
    /**
     * 班次ID
     */
    private String shiftId = StringUtils.EMPTY;
    /**
     * 班次名称
     */
    private String shiftName;
    /**
     * 班次开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shiftStartTime;
    /**
     * 班次结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shiftEndTime;
    /**
     * 班次产能
     */
    private Integer productIvity = 0;
    /**
     * 加班时间
     */
    private Integer overTime = 0;
    /**
     * 休息时间
     */
    private List<ShcBreakDetailInfo> breakItemDetails;
    /**
     * 班次code
     */
    private Integer shiftCode = 0;
    /**
     * 车间ID
     */
    private String pmShopId = StringUtils.EMPTY;

    public ShcCalendarDetailInfo() {
        breakItemDetails = new ArrayList<>();
    }


}
