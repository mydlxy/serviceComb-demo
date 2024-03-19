package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * RecieveCharacteristicsDataRequest
 *
 * @author inkelink
 * @since 1.0.0 2023-10-26
 */
@Data
@Schema(title = "RecieveCharacteristicsDataRequest", description = "")
public class RecieveCharacteristicsDataRequest {

    private String productMaterialNo = StringUtils.EMPTY;

    /**
     * 版本
     */
    private String version = StringUtils.EMPTY;

    /**
     * 特征项
     */
    private String name = StringUtils.EMPTY;

    /**
     * 特征项（中文）
     */
    private String descriptionCn = StringUtils.EMPTY;
    /**
     * 特征项（英文）
     */
    private String descriptionEn = StringUtils.EMPTY;

    /**
     * 特征代码
     */
    private String code = StringUtils.EMPTY;
    /**
     * 特征值
     */
    private String value = StringUtils.EMPTY;

    /**
     * 特征值（中文）
     */
    private String valueCn = StringUtils.EMPTY;

    /**
     * 特征值（英文）
     */
    private String valueEn = StringUtils.EMPTY;
    /**
     * 数据来源
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int source = 0;
}
