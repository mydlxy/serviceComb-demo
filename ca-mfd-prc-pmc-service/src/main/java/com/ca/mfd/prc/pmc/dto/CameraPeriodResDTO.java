package com.ca.mfd.prc.pmc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(title = "摄像头时间段-响应实体")
@Data
public class CameraPeriodResDTO {
    @Schema(title = "开始时间")
    private Date startTime;
    @Schema(title = "结束时间")
    private Date endTime;
}
