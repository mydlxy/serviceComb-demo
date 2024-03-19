package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * OutsourceEntryAreaDTO
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "OutsourceEntryAreaDTO", description = "")
public class SplitEntryPara {

    @Schema(description = "生产工单标识")
    private String entryNo;

    @Schema(description = "拆分数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer splitCount;

    @Schema(description = "接收线体编码")
    private String lineCode;

    @Schema(description = "预计生产开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startTime;

    @Schema(description = "预计完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date endTime;

}
