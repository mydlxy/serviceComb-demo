package com.ca.mfd.prc.avi.host.scheduling.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "报工统计")
public class AviTrackRecordDto implements Serializable {
    /**
     * 主键
     */
    @Schema(title = "主键")
    private Long id;
    /**
     * 条码
     */
    private String sn;

    /**
     * AVI代码
     */
    private String aviCode;

    /**
     * 线体代码
     */
    private String lineCode;
}
