package com.ca.mfd.prc.common.model.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ComboInfoDTO class
 *
 * @author inkelink
 * @date 2023/04/03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "通用实体")
public class ComboInfoDTO {

    @Schema(title = "Text")
    private String text;
    @Schema(title = "Value")
    private String value;

    public ComboInfoDTO() {
    }

    public ComboInfoDTO(String text, String value) {
        this.text = text;
        this.value = value;
    }
}
