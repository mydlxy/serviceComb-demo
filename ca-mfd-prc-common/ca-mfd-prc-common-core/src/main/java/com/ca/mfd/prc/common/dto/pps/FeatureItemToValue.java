package com.ca.mfd.prc.common.dto.pps;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * FeatureItemToValue
 *
 * @author inkelink eric.zhou
 * @date 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "erp请求")
public class FeatureItemToValue {

    @Schema(title = "特征值")
    @JsonProperty("objectnumber")
    private String featureValue;

    @Schema(title = "特征值名称")
    @JsonProperty("name")
    private String valueCn;

}