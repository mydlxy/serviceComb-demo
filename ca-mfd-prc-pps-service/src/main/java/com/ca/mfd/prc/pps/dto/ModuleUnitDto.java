package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 小单元
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-10-18
 */
@Data
@Schema(title = "小单元", description = "")
public class ModuleUnitDto {

    @Schema(description = "单位编码")
    @JsonProperty("UnitCode")
    private String unitCode = StringUtils.EMPTY;

    @Schema(description = "小单元数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonProperty("UnitNum")
    private Integer unitNum = 0;

    @Schema(description = "电芯编码")
    @JsonProperty("CellCode")
    private String cellCode = StringUtils.EMPTY;

    @Schema(description = "电芯数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonProperty("CellNum")
    private Integer cellNum = 0;

    @Schema(description = "上端板物料号")
    @JsonProperty("UpBoardCode")
    private String upBoardCode = StringUtils.EMPTY;

    @Schema(description = "上端板数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonProperty("UpBoardNum")
    private Integer upBoardNum = 0;

    @Schema(description = "下端板物料号")
    @JsonProperty("DownBoardCode")
    private String downBoardCode = StringUtils.EMPTY;

    @Schema(description = "下端板数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonProperty("DownBoardNum")
    private Integer downBoardNum = 0;
}
