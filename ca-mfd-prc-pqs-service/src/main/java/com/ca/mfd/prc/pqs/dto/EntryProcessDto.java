package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author edwards.qu
 * @ClassName EntryProcessDto
 * @description:
 * @date
 * @version: 1.0
 */
@Data
public class EntryProcessDto {

    /**
     * 检验单号
     */
    @Schema(description = "检验单号")
    private String inspectionNo = StringUtils.EMPTY;

    /**
     * 报工单号
     */
    @Schema(description = "报工单号")
    private String entryReportNo = StringUtils.EMPTY;

    /**
     * 质检工单类型
     */
    @Schema(description = "质检工单类型")
    private String pqsEntryType = StringUtils.EMPTY;

    /**
     * 抽检结果
     */
    @Schema(description = "抽检结果")
    private Boolean isQualified;
}
