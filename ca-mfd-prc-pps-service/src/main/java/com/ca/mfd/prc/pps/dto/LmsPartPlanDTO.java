package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * LmsPartPlanDTO
 *
 * @author inkelink bo.yang
 * @since 1.0.0 2024-02-28
 */
@Data
@Schema(title = "LmsPartPlanDTO", description = "")
public class LmsPartPlanDTO {

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "线体编码")
    private String lineCode;

    @Schema(description = "计划单号")
    private String planNumber;

    @Schema(description = "产品号")
    private String productionObject;

    @Schema(description = "生产量")
    private String productionQuantity;

    @Schema(description = "唯一标识")
    private String uniqueCode;

    @Schema(description = "计划上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date planOnlineTime;

    @Schema(description = "计划类型 1、大计划，2、小计划")
    private String planType;

}
