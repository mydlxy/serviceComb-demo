package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuyi
 * @ClassName ComboDataDto
 * @description: 通用实体
 * @date 2023年08月10日
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "通用实体")
public class ComboDataDto {

    private String label;
    private String text;
    private String value;

    public ComboDataDto() {
    }

    public ComboDataDto(String label, String text, String value) {
        this.label = label;
        this.text = text;
        this.value = value;
    }
}
