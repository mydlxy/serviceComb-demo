package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 追溯工艺
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "OperWoWoDTO")
public class TraceWoDTO extends ProductWoDTO {

    /**
     * TrcRuleCodeLength
     */
    @Schema(title = "TrcRuleCodeLength")
    private Integer trcRuleCodeLength = 0;

    /**
     * TrcRuleBeginIndex
     */
    @Schema(title = "TrcRuleBeginIndex")
    private Integer trcRuleBeginIndex = 0;

    /**
     * TrcRulePartLength
     */
    @Schema(title = "TrcRulePartLength")
    private Integer trcRulePartLength = 0;

    /**
     * TrcByGroup
     */
    @Schema(title = "TrcByGroup")
    private Boolean trcByGroup = false;

    /**
     * 批次件自动完成所需要
     */
    @Schema(title = "批次件自动完成所需要")
    private String barCode = StringUtils.EMPTY;

    /**
     * 批次件追溯自动配置状态  -1 不显示按钮 0 手动模式 1 自动模式
     */
    @Schema(title = "批次件追溯自动配置状态")
    private Integer configStatus = -1;

    /**
     * 批次件追溯自动完成数
     */
    @Schema(title = "批次件追溯自动完成数")
    private Integer count = 0;

}