package com.ca.mfd.prc.common.dto.pps;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Erp特征信息
 *
 * @author inkelink eric.zhou
 * @date 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "erp请求")
public class ErpCharacteristicItemResponse {

    @Schema(title = "特征名称")
    private String featureItemName;
    @Schema(title = "特征值")
    private String featureItemNumber;
    @Schema(title = "FeatureValueList")
    private List<FeatureItemToValue> featureValueList;

    public ErpCharacteristicItemResponse() {
        this.featureItemName = "";
        this.featureItemNumber = "";
        this.featureValueList = new ArrayList<>();
    }

}