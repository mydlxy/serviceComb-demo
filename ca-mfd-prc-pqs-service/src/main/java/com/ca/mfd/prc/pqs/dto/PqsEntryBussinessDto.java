package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 工单业务对象
 *
 * @Author: joel
 * @Date: 2023-04-19-15:10
 * @Description:
 */
@Data
@Schema(description = "工单业务对象")
public class PqsEntryBussinessDto {
    /**
     * 工单信息
     */
    @Schema(title = "工单信息")
    private Object inspectionEntry;

    /**
     * 工单缺陷
     */
    @Schema(title = "工单缺陷")
    private List<EntryCheckDefectDto> defects;

    /**
     * 检验项
     */
    @Schema(title = "检验项")
    private List<PqsEntryCheckItemDto> checkItem;
}
