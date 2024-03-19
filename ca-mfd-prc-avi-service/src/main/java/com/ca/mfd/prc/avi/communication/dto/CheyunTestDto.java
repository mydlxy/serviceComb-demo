package com.ca.mfd.prc.avi.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
@Data
@Schema(description= "车云手动推送传入参数")
public class CheyunTestDto {
    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    private String vehicleMaterialNumbers = StringUtils.EMPTY;

    /**
     * VIN号
     */
    @Schema(title = "VIN号")
    private String vin = StringUtils.EMPTY;


    /**
     * 定编号
     */
    @Schema(title = "定编号")
    private String dingCode = StringUtils.EMPTY;


    /**
     * 计划上线日期
     */
    @Schema(title = "计划上线日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date specifyDate = new Date();
}
