package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * BindingUnitPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "BindingUnitPara", description = "")
public class BindingUnitPara implements Serializable {

    @Schema(description = "模组编码")
    private String moduleCode = StringUtils.EMPTY;

    @Schema(description = "模组条码")
    private String moduleBarcode = StringUtils.EMPTY;

    @Schema(description = "元素条码")
    private String cellBarcode = StringUtils.EMPTY;

}