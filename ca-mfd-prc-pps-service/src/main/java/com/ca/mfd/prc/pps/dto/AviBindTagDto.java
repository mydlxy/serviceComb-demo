package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * AviBindTagDto
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "AviBindTagDto", description = "")
public class AviBindTagDto {

    @Schema(description = "吊牌条码")
    private String tagNo = StringUtils.EMPTY;

    @Schema(description = "aviCode")
    private String aviCode = StringUtils.EMPTY;

    @Schema(description = "vin")
    private String vin = StringUtils.EMPTY;

    @Schema(description = "BindingMedium")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer bindingMedium = 1;
}
