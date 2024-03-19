package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DownModuleEntryInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "DownModuleEntryInfo", description = "")
public class DownModuleEntryInfo {

    @Schema(description = "计划号")
    private String planNo;

    @Schema(description = "工单号")
    private String entryNo;

    @Schema(description = "电池型号")
    private String packModel;

    @Schema(description = "电池数量")
    private Integer packCount;

    @Schema(description = "生产区域")
    private String areaCode;

    @Schema(description = "模组下发信息")
    private List<ModuleStructDto> modules = new ArrayList<>();

}
