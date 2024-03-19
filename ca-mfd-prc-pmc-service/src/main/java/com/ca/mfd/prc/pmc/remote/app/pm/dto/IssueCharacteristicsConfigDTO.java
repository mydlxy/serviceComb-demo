package com.ca.mfd.prc.pmc.remote.app.pm.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author banny.luo
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY banny.luo At 2023年4月28日
 */
@Data
public class IssueCharacteristicsConfigDTO {

    @Schema(title = "下发特征标识键")
    private String featureKey = StringUtils.EMPTY;

    @Schema(title = "特征项")
    private String featureName = StringUtils.EMPTY;

}
