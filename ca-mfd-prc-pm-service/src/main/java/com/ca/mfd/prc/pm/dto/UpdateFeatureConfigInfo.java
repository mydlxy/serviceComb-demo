package com.ca.mfd.prc.pm.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eric.zhou
 * @Description: AVI信息
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "UpdateFeatureConfigInfo")
public class UpdateFeatureConfigInfo implements Serializable {
    @Schema(title = "SubKey")
    @JsonAlias({"subKey", "subkey"})
    private String subKey;

    @Schema(title = "1 工单  2 队列")
    private Integer relevanceType = 0;

    @Schema(title = "items")
    private List<FeatureConfigInfo> items = new ArrayList<>();
}
