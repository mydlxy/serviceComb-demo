package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-04-17-19:45
 * @Description:
 */
@Data
@Schema(description = "色块模型")
public class GateBlank {
    /**
     * 色块类型
     */
    @Schema(title = "色块类型")
    private Integer blockType = 0;

    /**
     * 色块高度
     */
    @Schema(title = "色块高度")
    private BigDecimal blockHeight = BigDecimal.valueOf(0);

    /**
     * 色块宽度
     */
    @Schema(title = "色块宽度")
    private BigDecimal blockWidth = BigDecimal.valueOf(0);

    /**
     * 色块TOP
     */
    @Schema(title = "色块TOP")
    private BigDecimal blockTop = BigDecimal.valueOf(0);

    /**
     * 色块LEFT
     */
    @Schema(title = "色块LEFT")
    private BigDecimal blockLeft = BigDecimal.valueOf(0);

    /**
     * 色块JSON
     */
    @Schema(title = "色块JSON")
    private String blockJson;

    /**
     * 缺陷编号列表
     */
    @Schema(title = "缺陷编号列表")
    private List<String> anomalyIds = new ArrayList<>();
}
