package com.ca.mfd.prc.pm.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author eric.zhou
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
public class MidCharacteristicsInfo {

    @Schema(title = "整车物料号")
    private String vehicleMaterialNo = StringUtils.EMPTY;

    @Schema(title = "特征族编码串")
    private String familyCode = StringUtils.EMPTY;

    @Schema(title = "特征值编码串")
    private String featureCode = StringUtils.EMPTY;

    @Schema(title = "是否选装")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isOptional;

    @Schema(title = "选装包")
    private String optionalPackage = StringUtils.EMPTY;

}
