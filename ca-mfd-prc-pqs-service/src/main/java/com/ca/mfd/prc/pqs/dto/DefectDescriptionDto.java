package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: 缺陷描述信息
 * @Date: 2024年02月27日
 * @Description: 设置缺陷描述
 */
@Data
@Schema(title = "设置缺陷描述")
public class DefectDescriptionDto {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id;

    /**
     * 备注描述信息
     */
    @Schema(title = "备注/描述信息")
    private String description = StringUtils.EMPTY;
}
