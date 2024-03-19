package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 色块模型
 *
 * @author eric.zhou
 * @date 2023/4/17
 */
@Data
@Schema(description = "色块模型")
public class GateBlankInfo {
    /**
     * id
     */
    @Schema(title = "id")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

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
}
