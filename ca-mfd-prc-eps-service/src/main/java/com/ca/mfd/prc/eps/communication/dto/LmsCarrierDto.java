package com.ca.mfd.prc.eps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author inkelink
 * @Description: 车辆基础数据中间表（车云）实体
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Data
@Schema(description = "Lms推送料框基础数据")
public class LmsCarrierDto {
    /**
     * 料框编码
     */
    private String packageCode;

    /**
     * 物料编码
     */
    private String materialCode;
}
