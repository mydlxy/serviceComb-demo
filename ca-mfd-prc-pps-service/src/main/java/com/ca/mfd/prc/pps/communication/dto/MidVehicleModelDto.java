package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
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
public class MidVehicleModelDto {


    /**
     * 整车物料号
     */
    private String vehicleMaterialNumber = Strings.EMPTY;
    /**
     * 配置特征清单
     */
    private String configurationFeatureList = Strings.EMPTY;
    /**
     * 配置特征清单描述
     */
    private String configurationFeatureListDesc = Strings.EMPTY;
    /**
     * 车型
     */
    private String productCode = Strings.EMPTY;
    /**
     * 基础车型
     */
    private String basicVehicleModel = Strings.EMPTY;
    /**
     * 车型号
     */
    private String vehicleNumber = Strings.EMPTY;
    /**
     * 车型状态号
     */
    private String statusCode = Strings.EMPTY;
    /**
     * 选装包清单
     */
    private String optionalPackageList = Strings.EMPTY;
    /**
     * 选装包清单描述
     */
    private String onalPackageListDesc = Strings.EMPTY;
    /**
     * 车身颜色代码
     */
    private String bodyColorCode = Strings.EMPTY;
    /**
     * 生成方式
     */
    private String generationMode = Strings.EMPTY;
    /**
     * 整车物料号类型
     */
    private String vehicleMaterialNumberType = Strings.EMPTY;
    /**
     * 状态
     */
    private String status = Strings.EMPTY;
    /**
     * 生效日期起
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom;
    /**
     * 生效日期止
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo;



}