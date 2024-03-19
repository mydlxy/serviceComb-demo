package com.ca.mfd.prc.pm.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author eric.zhou
 * @Description: AVI信息
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "AVI信息")
public class AviInfoDTO implements Serializable {

    @Schema(title = "AVI关键点")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pmAviId;

    @Schema(title = "车间代码")
    private String pmShopCode;

    @Schema(title = "车间名称")
    private String pmShopName;

    @Schema(title = "线体代码")
    private String pmLineCode;

    @Schema(title = "线体名称")
    private String pmLineName;

    @Schema(title = "AVI代码")
    private String pmAviCode;

    @Schema(title = "AVI名称")
    private String pmAviName;
}
