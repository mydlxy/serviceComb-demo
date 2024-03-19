package com.ca.mfd.prc.pm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author banny.luo
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY banny.luo At 2023年4月28日
 */
@Data
public class MoveStationDto implements Serializable {

    @Schema(title = "旧车间外键", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long oldShopId = Constant.DEFAULT_ID;

    @Schema(title = "旧线体外键", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long oldLineId = Constant.DEFAULT_ID;

    @Schema(title = "旧工位id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long oldStationId = Constant.DEFAULT_ID;

    @Schema(title = "新车间外键", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long newShopId = Constant.DEFAULT_ID;

    @Schema(title = "新线体外键", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long newLineId = Constant.DEFAULT_ID;

    @Schema(title = "新工位id", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long newStationId = Constant.DEFAULT_ID;


}
