package com.ca.mfd.prc.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author banny.luo
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY banny.luo At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "MaterialDataVO")
public class MaterialDataVO {

    private String label;
    private String text;
    private String chinese;
    private String english;

    public MaterialDataVO() {
    }

    public MaterialDataVO(String label, String text, String chinese, String english) {
        this.label = label;
        this.text = text;
        this.chinese = chinese;
        this.english = english;
    }
}
