package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author eric.zhou
 * @Description: IssueFeatureInfo
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "IssueFeatureInfo", description = "")
public class IssueFeatureInfo implements Serializable {

    @Schema(description = "特征项1")
    private String feature1 = StringUtils.EMPTY;

    @Schema(description = "特征项2")
    private String feature2 = StringUtils.EMPTY;

    @Schema(description = "特征项3")
    private String feature3 = StringUtils.EMPTY;

    @Schema(description = "特征项4")
    private String feature4 = StringUtils.EMPTY;

    @Schema(description = "特征项5")
    private String feature5 = StringUtils.EMPTY;

    @Schema(description = "天幕")
    private String feature6 = StringUtils.EMPTY;
}