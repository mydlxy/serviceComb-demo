package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: AS保留车
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "AS保留车")
public class AsKeepCarDto {

    /**
     * 工厂编号
     */
    @Schema(title = "工厂编号")
    private String orgCode = StringUtils.EMPTY;
    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    private String vrn = StringUtils.EMPTY;

    /**
     * 车辆VIN号
     */
    @Schema(title = "车辆VIN号")
    private String vin = StringUtils.EMPTY;
    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    private String ulocNo = StringUtils.EMPTY;

    /**
     * HOLD（解HOLD）时间
     */
    @Schema(title = "HOLD（解HOLD）时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date holdTime = new Date();

    /**
     * HOLD（解HOLD）类型 1：HOLD 2：解除HOLD
     */
    @Schema(title = "HOLD（解HOLD）类型")
    private String holdType = StringUtils.EMPTY;

}