package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

/**
 * 首巡检检测模板
 *
 * @Author: joel
 * @Date: 2023-04-19-16:40
 * @Description:
 */
@Data
@Schema(description = "首巡检检测模板")
public class PqsPackInspectRecordDetailDto {
    /**
     * 模板ID
     */
    @Schema(title = "模板ID")
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
    private BigDecimal target;

    /**
     * 下限值
     */
    @Schema(title = "下限值")
    private BigDecimal lowestLimit;

    /**
     * 上限值
     */
    @Schema(title = "下限值")
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

    /**
     * 样本号
     */
    @Schema(title = "样本号")
    private String sample;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;

    /**
     * 判定结果
     */
    @Schema(title = "判定结果")
    private Integer result = 0;

    /**
     * 结果值
     */
    @Schema(title = "结果值")
    private BigDecimal value;
}
