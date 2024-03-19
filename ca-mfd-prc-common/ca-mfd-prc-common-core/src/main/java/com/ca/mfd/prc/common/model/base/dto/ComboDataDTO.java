package com.ca.mfd.prc.common.model.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ComboDataDTO
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "通用实体")
public class ComboDataDTO {

    @Schema(description = "label")
    private String label;
    @Schema(description = "text")
    private String text;
    @Schema(description = "value")
    private String value;

    public ComboDataDTO() {
    }

    public ComboDataDTO(String label, String text, String value) {
        this.label = label;
        this.text = text;
        this.value = value;
    }
}
