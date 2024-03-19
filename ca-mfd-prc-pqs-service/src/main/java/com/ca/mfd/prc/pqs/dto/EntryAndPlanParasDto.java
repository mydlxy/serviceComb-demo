package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class EntryAndPlanParasDto {
    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    private String planNo = StringUtils.EMPTY;

    /**
     * 工单号
     */
    @Schema(title = "工单号")
    private String entryNo = StringUtils.EMPTY;
}
