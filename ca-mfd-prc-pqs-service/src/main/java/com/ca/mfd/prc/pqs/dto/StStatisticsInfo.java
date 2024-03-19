package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 冲压统计信息
 *
 * @Author: joel
 * @Date: 2023-08-20-14:49
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "冲压统计信息")
public class StStatisticsInfo {
    /**
     * 质量目标
     */
    @Schema(title = "质量目标")
    private BigDecimal targetValue = BigDecimal.valueOf(0);

    /**
     * 扣分
     */
    @Schema(title = "扣分")
    private BigDecimal scores = BigDecimal.valueOf(0);

    /**
     * 缺陷数
     */
    @Schema(title = "缺陷数")
    private Integer defectCount = 0;

    /**
     * A级评分
     */
    @Schema(title = "A级评分")
    private BigDecimal grade1 = BigDecimal.valueOf(0);

    /**
     * B级评分
     */
    @Schema(title = "B级评分")
    private BigDecimal grade2 = BigDecimal.valueOf(0);

    /**
     * C级评分
     */
    @Schema(title = "C级评分")
    private BigDecimal grade3 = BigDecimal.valueOf(0);


    /**
     * D级评分
     */
    @Schema(title = "D级评分")
    private BigDecimal grade4 = BigDecimal.valueOf(0);

    /**
     * E级评分
     */
    @Schema(title = "E级评分")
    private BigDecimal grade5 = BigDecimal.valueOf(0);

    /**
     * 预留分数1
     */
    @Schema(title = "预留分数1")
    private BigDecimal grade6 = BigDecimal.valueOf(0);

    /**
     * 预留分数2
     */
    @Schema(title = "预留分数2")
    private BigDecimal grade7 = BigDecimal.valueOf(0);
}
