package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author eric.zhou
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
public class RecieveCharacteristicsDataRequest {

    @Schema(title = "ProductMaterialNo")
    private String productMaterialNo = StringUtils.EMPTY;

    @Schema(title = "版本")
    private String version = StringUtils.EMPTY;

    @Schema(title = "特征项")
    private String name = StringUtils.EMPTY;

    @Schema(title = "特征项（中文）")
    private String descriptionCn = StringUtils.EMPTY;

    @Schema(title = "特征项（英文）")
    private String descriptionEn = StringUtils.EMPTY;

    @Schema(title = "特征代码")
    private String code = StringUtils.EMPTY;

    @Schema(title = "特征值")
    private String value = StringUtils.EMPTY;

    @Schema(title = "特征值（中文）")
    private String valueCn = StringUtils.EMPTY;

    @Schema(title = "特征值（英文）")
    private String valueEn = StringUtils.EMPTY;

    @Schema(title = "数据来源")
    private Integer source = 0;
}