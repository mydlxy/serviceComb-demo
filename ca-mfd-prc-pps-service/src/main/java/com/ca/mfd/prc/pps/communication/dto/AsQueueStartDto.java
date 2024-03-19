package com.ca.mfd.prc.pps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author inkelink
 * @Description: AS保留车
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "AS待开工队列")
public class AsQueueStartDto {
    /**
     * 待上线顺序号
     */
    @Schema(title = "待上线顺序号")
    private String beforeOnlineSeq;

    /**
     * 工厂编号
     */
    @Schema(title = "工厂编号")
    private String orgCode;

    /**
     * VRN工程车为空
     */
    @Schema(title = "VRN工程车为空")
    private String vrn;

    /**
     * 车辆VIN号
     */
    @Schema(title = "车辆VIN号")
    private String vin;
}
