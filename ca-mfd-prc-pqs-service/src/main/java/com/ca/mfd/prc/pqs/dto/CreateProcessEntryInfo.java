package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author edwards.qu
 * @ClassName CreateProcessEntryInfo
 * @description:
 * @date
 * @version: 1.0
 */
@Data
public class CreateProcessEntryInfo {

    /**
     * 条码
     */
    @Schema(description = "条码")
    private String barcode = StringUtils.EMPTY;

    /**
     * 工单号
     */
    @Schema(description = "工单号")
    private String entryNo = StringUtils.EMPTY;

    /**
     * 工单类型
     */
    @Schema(description = "工单类型")
    private String entryType = "CJ";

    /**
     * 报工单号
     */
    @Schema(description = "报工单号")
    private String entryReportNo = StringUtils.EMPTY;
}
