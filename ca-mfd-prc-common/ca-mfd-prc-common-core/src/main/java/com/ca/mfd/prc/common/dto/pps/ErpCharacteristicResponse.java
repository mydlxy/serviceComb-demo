package com.ca.mfd.prc.common.dto.pps;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ErpCharacteristicResponse
 *
 * @author inkelink eric.zhou
 * @date 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "erp请求")
public class ErpCharacteristicResponse {

    @Schema(title = "产品物料号")
    @JsonProperty("ProductMaterialNo")
    private String productMaterialNo;
    @Schema(title = "特征值")
    @JsonProperty("Value")
    private String value;
    @Schema(title = "特征值中文")
    @JsonProperty("ValueCn")
    private String valueCn;
    @Schema(title = "特征项代码")
    @JsonProperty("Name")
    private String name;
    @Schema(title = "特征项描述")
    @JsonProperty("DescriptionCn")
    private String descriptionCn;

    public ErpCharacteristicResponse() {
        this.productMaterialNo = "";
        this.value = "";
        this.valueCn = "";
        this.name = "";
        this.descriptionCn = "";
    }

}