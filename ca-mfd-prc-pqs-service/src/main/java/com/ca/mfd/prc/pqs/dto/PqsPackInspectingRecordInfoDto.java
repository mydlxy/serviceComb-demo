package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-04-19-16:37
 * @Description:
 */
@Data
public class PqsPackInspectingRecordInfoDto {
    /**
     * 检测人
     */
    @Schema(title = "检测人")
    private String workplaceCode;

    /**
     * 检测人
     */
    @Schema(title = "检测人")
    private String templateName;

    /**
     * 判定结果
     */
    @Schema(title = "判定结果")
    private Integer result = 0;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;

    /**
     * 巡检明细
     */
    @Schema(title = "巡检明细")
    private List<PqsPackInspectRecordDetailDto> recordDetail;
}
