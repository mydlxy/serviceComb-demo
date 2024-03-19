package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * EqumentWoItem
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "EqumentWoItem", description = "")
public class EqumentWoItem implements Serializable {

    @Schema(description = "顺序号")
    private Integer disPlayNo;

    @Schema(description = "参数名称")
    private String woName = StringUtils.EMPTY;

    @Schema(description = "参数编码")
    private String woCode = StringUtils.EMPTY;

    @Schema(description = "参数单位")
    private String woUnit = StringUtils.EMPTY;

    @Schema(description = "标准值")
    private String woStandard = StringUtils.EMPTY;

    @Schema(description = "上限值")
    private String woUplimit = StringUtils.EMPTY;

    @Schema(description = "下限值")
    private String woDownlimit = StringUtils.EMPTY;

    @Schema(description = "参数值")
    private String woValue = StringUtils.EMPTY;

    @Schema(description = "参数结果")
    private String woResult = StringUtils.EMPTY;

}