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
@Schema(description = "设置实例属性对象")
public class EdataPropertyDto {
    @Schema(title = "属性Key")
    private String propertyKey;
    @Schema(title = "设置值")
    private String value;
    @Schema(title = "来源")
    private String source;
}
