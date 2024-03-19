package com.ca.mfd.prc.eps.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * EpsVehicleWoDataTrcExcel
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
public class EpsVehicleWoDataTrcExcel {

    @Excel(name = "车辆识别码(VIN)")
    private String vin;
    @Excel(name = "车辆类型")
    private String vehicleTpye;
    @Excel(name = "车辆型号")
    private String vehicleModel;
    @Excel(name = "车辆名称")
    private String vehicleName;
    @Excel(name = "车辆品牌")
    private String vehicleBrand;
    @Excel(name = "车辆制造日期")
    private String vehicleProduceDate;
    @Excel(name = "储能装置号")
    private String energyStorageNo;
    @Excel(name = "配置号")
    private String materialNo;

}
