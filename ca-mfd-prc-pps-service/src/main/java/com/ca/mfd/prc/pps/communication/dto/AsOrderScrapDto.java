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
 * @Description: AS车辆报废
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "AS车辆报废")
public class AsOrderScrapDto {

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
     * 车辆报废操作时间
     */
    @Schema(title = "车辆报废操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date scrapTime = new Date();

}