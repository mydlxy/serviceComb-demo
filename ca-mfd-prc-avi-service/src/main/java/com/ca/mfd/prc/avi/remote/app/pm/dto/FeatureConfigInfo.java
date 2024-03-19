package com.ca.mfd.prc.avi.remote.app.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author eric.zhou
 * @Description: FeatureConfigInfo
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "FeatureConfigInfo")
public class FeatureConfigInfo implements Serializable {

    @Schema(title = "下发特征标识键")
    private String featureKey = StringUtils.EMPTY;

    @Schema(title = "特征项")
    private String featureName = StringUtils.EMPTY;

    public FeatureConfigInfo(String featureKey, String featureName) {
        this.featureKey = featureKey;
        this.featureName = featureName;
    }
}
