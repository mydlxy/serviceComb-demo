package com.ca.mfd.prc.common.model.base.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ComboSelectDTO
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "通用实体")
public class ComboSelectDTO {

    private String label;
    private String id;
    private String code;
    private String name;

    public ComboSelectDTO() {
    }

    public ComboSelectDTO(String label, String id, String code, String name) {
        this.label = label;
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
