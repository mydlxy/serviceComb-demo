package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @Author: joel
 * @Date: 2023-04-18-10:19
 * @Description:
 */
@Data
@Schema(description = "首巡检检测模板")
public class PqsPackInspectTemplateItemDto {
    /**
     * 主键ID
     */
    @Schema(title = "主键ID")
    private String tempalteId = StringUtils.EMPTY;

    /**
     * 检查项代码
     */
    @Schema(title = "检查项代码")
    private String code;

    /**
     * 检查项名称
     */
    @Schema(title = "检查项名称")
    private String name;

    /**
     * 结果类型
     */
    @Schema(title = "结果类型")
    private Integer resultType = 0;

    /**
     * 检测目标
     */
    @Schema(title = "检测目标")
    private String target;

    /**
     * 下限值
     */
    @Schema(title = "下限值")
    private BigDecimal lowestLimit;

    /**
     * 上限值
     */
    @Schema(title = "上限值")
    private BigDecimal upperLimit;

    /**
     * 单位
     */
    @Schema(title = "单位")
    private String unit;

    /**
     * 显示顺序
     */
    @Schema(title = "显示顺序")
    private Integer displayNo = 0;
}
