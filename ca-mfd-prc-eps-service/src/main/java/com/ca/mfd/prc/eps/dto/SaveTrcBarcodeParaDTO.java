package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * SaveTrcBarcodeParaDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SaveTrcBarcodeParaDTO")
public class SaveTrcBarcodeParaDTO {

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    private String productCode = StringUtils.EMPTY;
    /**
     * 条码
     */
    @Schema(title = "条码")
    private String barCode = StringUtils.EMPTY;
    /**
     * 岗位
     */
    @Schema(title = "岗位")
    private String workplaceId = StringUtils.EMPTY;

    /**
     * 操作编号集合
     */
    @Schema(title = "操作编号集合")
    private List<Long> woIds;

    /**
     * 0 手动  1 自动
     */
    @Schema(title = "0 手动  1 自动")
    private Integer autoState = 0;

}