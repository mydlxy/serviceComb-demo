package com.ca.mfd.prc.pm.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Schema(description = "")
public class MidLmsDatasVo implements Serializable {
    /**
     * 物料号
     */
    private String productCode;
    /**
     * 加密字段
     */
    private String sigtrue;
    /**
     * 唯一码
     */
    @Schema(title = "唯一码")
    private Long uniqueCode;
    /**
     * 工位清单
     */
    private List<WorkstationMaterialDto> data;
}
