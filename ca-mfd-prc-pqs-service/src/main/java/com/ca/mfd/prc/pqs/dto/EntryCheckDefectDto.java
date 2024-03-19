package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * 工单缺陷 --来料检验/成品质检/抽检
 *
 * @Author: joel
 * @Date: 2023-04-19-15:13
 * @Description:
 */
@Data
@Schema(description = "工单缺陷 --来料检验/成品质检/抽检")
public class EntryCheckDefectDto {
    /**
     * ID
     */
    @Schema(title = "ID")
    private String id = StringUtils.EMPTY;

    /**
     * 评审单号
     */
    @Schema(title = "评审单号")
    private String inspectionNo = StringUtils.EMPTY;

    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    private String defectCode = StringUtils.EMPTY;

    /**
     * 缺陷描述
     */
    @Schema(title = "缺陷描述")
    private String defectDescription = StringUtils.EMPTY;

    /**
     * 数量
     */
    @Schema(title = "数量")
    private BigDecimal qty = BigDecimal.ONE;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;

    /**
     * 批次
     */
    @Schema(title = "批次")
    private String lotNo = StringUtils.EMPTY;
}
