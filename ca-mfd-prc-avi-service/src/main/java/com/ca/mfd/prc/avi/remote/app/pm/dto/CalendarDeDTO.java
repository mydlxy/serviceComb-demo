package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 重置日历
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "重置日历")
public class CalendarDeDTO {

    @Schema(title = "车间代码")
    private String shopCode;

    @Schema(title = "线体代码")
    private String lineCode;

    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date beginDt;

    @Schema(title = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endDt;

}
