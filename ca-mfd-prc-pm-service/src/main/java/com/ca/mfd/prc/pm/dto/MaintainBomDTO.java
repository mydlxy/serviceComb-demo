package com.ca.mfd.prc.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eric.zhou
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
public class MaintainBomDTO {

    @Schema(title = "产品物料号")
    private String productMaterialNo;

    @Schema(title = "BOM版本号")
    private String bomVersions = StringUtils.EMPTY;

    @Schema(title = "是否启用该版本")
    private Boolean isEnable = true;

    @Schema(title = "BOM数据")
    private List<BomInfo> bomData = new ArrayList<>();
}
