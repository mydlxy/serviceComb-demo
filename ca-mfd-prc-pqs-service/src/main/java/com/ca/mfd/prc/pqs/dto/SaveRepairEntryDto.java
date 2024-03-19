package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liuyi
 * @ClassName SaveRepairEntryDto
 * @description: 返修结果保存
 * @date 2023年08月10日
 * @version: 1.0
 */
@Data
@Schema(description = "返修结果保存")
public class SaveRepairEntryDto {

    @Schema(description = "条码")
    private String repairNo = "";

    @Schema(description = "维修耗时")
    private double workHours = 0;

    @Schema(description = "维修说明")
    private String remark = "";

}
