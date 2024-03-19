package com.ca.mfd.prc.common.dto;

import lombok.Data;

/**
 * @author 阳波
 * @ClassName TextAndValueMappingDTO
 * @description: 用于下拉列表暂时
 * @date 2023年08月10日
 * @version: 1.0
 */
@Data
public class TextAndValueMappingDTO {
    public TextAndValueMappingDTO(){}
    public TextAndValueMappingDTO(String value, String text){
        this.text = text;
        this.value = value;
    }
    private String text;
    private String value;
}
