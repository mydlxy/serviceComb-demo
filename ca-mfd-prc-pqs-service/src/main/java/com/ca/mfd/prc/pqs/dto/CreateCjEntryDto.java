package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 创建评审工单
 *
 * @Author: joel
 * @Date: 2023-04-13-18:42
 * @Description:
 */
@Data
@Schema(description = "创建评审工单")
public class CreateCjEntryDto {

    /**
     * 抽检单类型
     */
    @Schema(title = "抽检单类型")
    private String entryType = StringUtils.EMPTY;

    /**
     * 条码
     */
    @Schema(title = "条码")
    private String barcode;

    /**
     * 物料号
     */
    @Schema(title = "物料号")
    private String materialNo = StringUtils.EMPTY;

    /**
     * 区域不能为空
     */
    @Schema(title = "区域不能为空")
    private String areaCode = StringUtils.EMPTY;

    /**
     * 批次
     */
    @Schema(title = "批次")
    private String lotNo = StringUtils.EMPTY;

    /**
     * 数量
     */
    @Schema(title = "数量")
    private BigDecimal qty = BigDecimal.valueOf(0);
}
