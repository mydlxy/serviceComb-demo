package com.ca.mfd.prc.pps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 模组结构
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "模组结构", description = "")
public class ModuleStructDto {

    @Schema(description = "生产线体")
    @JsonProperty("LineCode")
    private String lineCode;

    @Schema(description = "模组编码")
    @JsonProperty("ModuleCode")
    private String moduleCode;

    @Schema(description = "模组数量")
    @JsonProperty("ModuleNum")
    private Integer moduleNum;

    @Schema(description = "隔热垫集合")
    @JsonProperty("Spacers")
    private List<ModuleSpacerDto> spacers = new ArrayList<>();

    @Schema(description = "小单元集合")
    @JsonProperty("Units")
    private List<ModuleUnitDto> units = new ArrayList<>();
}
