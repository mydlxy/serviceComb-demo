package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RuleTrcConfigModel
 *
 * @author eric.zhou
 * @since 1.0.0 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RuleTrcConfigModel")
public class RuleTrcConfigModel {

    @Schema(title = "工艺条码")
    private String woCode;

    @Schema(title = "规则")
    private RuleIndex rule;
}