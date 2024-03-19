package com.ca.mfd.prc.pm.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 从AS班次中间表获得的班次信息
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "从AS班次中间表获得的班次信息")
public class ShiftFromASDTO {
    @Schema(title = "班次编码")
    private String shiftCode;

    @Schema(title = "班次名称")
    private String shiftName;

    @Schema(title = "班次开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date startTime;

    @Schema(title = "班次结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endTime;

    @Schema(title = "是否跨天")
    private boolean isCross = false;

}
