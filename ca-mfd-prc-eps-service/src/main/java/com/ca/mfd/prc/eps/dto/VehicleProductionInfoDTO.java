package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VehicleProductionInfoDTO
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "整车生产信息")
public class VehicleProductionInfoDTO {

    @Schema(title = "Vin号")

    private String vin;

    @Schema(title = "车辆类型(乘用车及客车、货车、半挂牵引车、专用汽车)")
    private String vehicleTpye;
    @Schema(title = "车辆型号")
    private String vehicleModel;
    @Schema(title = "车辆名称")
    private String vehicleName;
    @Schema(title = "车辆品牌")
    private String vehicleBrand;
    @Schema(title = "车辆生产日期（字符串）")
    private String vehicleProduceDate;
    @Schema(title = "储能装置号")
    private String energyStorageNo;
    @Schema(title = "配置号")
    private String materialNo;

}
