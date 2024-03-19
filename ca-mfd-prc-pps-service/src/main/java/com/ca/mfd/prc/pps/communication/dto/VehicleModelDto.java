package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;

/**
 * @author inkelink
 * @Description: AS整车计划和车间计划
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@Schema(description = "整车车辆信息")
public class VehicleModelDto {


    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    private String vehicleMaterialNumber = StringUtils.EMPTY;


    /**
     * 配置特征清单
     */
    @Schema(title = "配置特征清单")
    private String configurationFeatureList;


    /**
     * 配置特征清单描述
     */
    @Schema(title = "配置特征清单描述")
    private String configurationFeatureListDesc = StringUtils.EMPTY;


    /**
     * BOM房间
     */
    @Schema(title = "BOM房间")
    private String bomRoom = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    private String productCode = StringUtils.EMPTY;


    /**
     * 基础车型
     */
    @Schema(title = "基础车型")
    private String basicVehicleModel = StringUtils.EMPTY;


    /**
     * 车型号
     */
    @Schema(title = "车型号")
    private String vehicleModelNumber = StringUtils.EMPTY;


    /**
     * 车型状态号
     */
    @Schema(title = "车型状态号")
    private String vehicleStatusNumber = StringUtils.EMPTY;


    /**
     * 选装包清单
     */
    @Schema(title = "选装包清单")
    private String optionalPackageList;


    /**
     * 选装包清单描述
     */
    @Schema(title = "选装包清单描述")
    private String onalPackageListDesc = StringUtils.EMPTY;


    /**
     * 车身颜色代码
     */
    @Schema(title = "车身颜色代码")
    private String bodyColorCode = StringUtils.EMPTY;


    /**
     * 生成方式
     */
    @Schema(title = "生成方式")
    private String generationMode = StringUtils.EMPTY;


    /**
     * 整车物料号类型
     */
    @Schema(title = "整车物料号类型")
    private String vehicleMaterialNumberType = StringUtils.EMPTY;


    /**
     * 状态
     */
    @Schema(title = "状态")
    private String status = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom = new Date();


    /**
     * 生效日期止
     */
    @Schema(title = "生效日期止")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo = new Date();

}