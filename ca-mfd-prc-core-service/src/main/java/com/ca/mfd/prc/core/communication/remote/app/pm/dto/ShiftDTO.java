package com.ca.mfd.prc.core.communication.remote.app.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 车间班次信息
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-7
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "车间班次信息")
public class ShiftDTO {

    @Schema(title = "车间ID")
    private String pmShopCode = StringUtils.EMPTY;

    @Schema(title = "班次ID")
    private String shiftId = StringUtils.EMPTY;

    @Schema(title = "班次名称")
    private String name;

    @Schema(title = "班次开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shiftStartTime;

    @Schema(title = "班次结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date shiftEndTime;

    @Schema(title = "产能")
    private Integer productivity = 0;

    @Schema(title = "工作日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;

    @Schema(title = "班次代码")
    private String shiftCode;

    @Schema(title = "加班时间，分钟")
    private Integer overtime = 0;

    @Schema(title = "工厂日历排班id")
    private String shcCalenderId = StringUtils.EMPTY;

    private Boolean isDayBefore = false;
    private Boolean isDayAfter = false;
    private String startTime;
    private String endTime;
}
