package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author liuyi
 * @ClassName CreateRepairEntryDto
 * @description: 创建返修工单
 * @date 2023年08月10日
 * @version: 1.0
 */
@Data
@Schema(description = "创建返修工单")
public class CreateRepairEntryDto {

    @Schema(description = "条码")
    private String barcode = "";

    @Schema(description = "物料号")
    private String materialNo = "";
}
