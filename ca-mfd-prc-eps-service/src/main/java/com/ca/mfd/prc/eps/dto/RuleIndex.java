package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RuleIndex
 *
 * @author eric.zhou
 * @since 1.0.0 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RuleIndex")
public class RuleIndex {

    @Schema(title = "开始截取索引")
    private Integer startIndex = 0;

    @Schema(title = "截取长度")
    private Integer length = 0;
}