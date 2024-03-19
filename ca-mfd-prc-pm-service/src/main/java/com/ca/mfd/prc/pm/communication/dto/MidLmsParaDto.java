package com.ca.mfd.prc.pm.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author inkelink
 * @Description: Lms请求令牌
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@Schema(description = "Lms请求令牌")
public class MidLmsParaDto implements Serializable {
    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    private String productCode;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    private String[] shopCodes;

    /**
     * 1、整车 2、电池
     */
    @Schema(title = "类型")
    private int type;

    /**
     * 物料有效期开始时间
     */
    @Schema(title = "物料有效期开始时间")
    private String specifyDate;
}
