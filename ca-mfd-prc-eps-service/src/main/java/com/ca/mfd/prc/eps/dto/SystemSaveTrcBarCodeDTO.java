package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * SystemSaveTrcBarCodeDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SystemSaveTrcBarCodeDTO")
public class SystemSaveTrcBarCodeDTO {

    /**
     * 工艺ID
     */
    @Schema(title = "工艺ID")
    private String woId = StringUtils.EMPTY;

    /**
     * 关重件条码
     */
    @Schema(title = "关重件条码")
    private String barCode = StringUtils.EMPTY;

    /**
     * 是否强绑 0 不强绑  1 强绑
     */
    @Schema(title = "是否强绑 0 不强绑  1 强绑")
    private Integer isConstraint = 0;

}