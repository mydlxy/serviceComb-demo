package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * SaveTrcBarcodeResultDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SaveTrcBarcodeResultDTO")
public class SaveTrcBarcodeResultDTO {

    /**
     * 工艺ID
     */
    @Schema(title = "工艺ID")
    private Long woId = Constant.DEFAULT_ID;
    /**
     * result
     */
    @Schema(title = "result")
    private Integer result = 0;

    /**
     * woDescription
     */
    @Schema(title = "woDescription")
    private String woDescription = StringUtils.EMPTY;

    /**
     * errorMessage
     */
    @Schema(title = "errorMessage")
    private String errorMessage = StringUtils.EMPTY;

}