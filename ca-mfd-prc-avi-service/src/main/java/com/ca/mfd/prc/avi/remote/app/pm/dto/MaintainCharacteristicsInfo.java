package com.ca.mfd.prc.avi.remote.app.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
public class MaintainCharacteristicsInfo {

    @Schema(title = "产品物料号")
    private String productMaterialNo;

    @Schema(title = "特征版本号")
    private String characteristicsVersions = StringUtils.EMPTY;

    @Schema(title = "是否启用该版本")
    private Boolean isEnable = true;

    @Schema(title = "特征数据")
    private List<CharacteristicsInfo> characteristicsData = new ArrayList<>();
}
