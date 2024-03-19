package com.ca.mfd.prc.common.dto.pqs;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 推送的铭牌信息
 *
 * @author inkelink eric.zhou
 * @date 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "推送的铭牌信息")
public class PqsDataPlateInfoDto {

    @Schema(title = "车辆型号,就是公告号")
    @JsonProperty("GGH")
    private String vehicleType;

    @Schema(title = "VIN码,车辆识别代号")
    @JsonProperty("Vin")
    private String vin;

    @Schema(title = "动力电池系统额定容量")
    @JsonProperty("DJ_Power")
    private String batteryRatedCapacity;

    @Schema(title = "动力电池系统额定电压")
    @JsonProperty("Voltage")
    private String batteryRatedVoltage;

    @Schema(title = "品牌")
    @JsonProperty("Brand")
    private String brand;

    @Schema(title = "驱动电机型号,合格证共用")
    @JsonProperty("DJ_Type")
    private String electricmotorModel;

    @Schema(title = "驱动电机峰值功率,和能耗标识共用")
    @JsonProperty("MaxDJ_Power")
    private String electricmotorPeakPower;

    @Schema(title = "驱动电机额定功率")
    @JsonProperty("Battery_Vol")
    private String electricmotorRatedPower;

    @Schema(title = "出厂编号,底盘号后八位")
    @JsonProperty("Manufacture_Code")
    private String leaveFactoryNumber;

    @Schema(title = "最大允许总质量,和能耗标识共用")
    @JsonProperty("MaxWeight")
    private String maximumAllowedWeight;

    @Schema(title = "乘坐人数")
    @JsonProperty("Seat_Num")
    private String seatNum;

    @Schema(title = "制造时间")
    @JsonProperty("MadeDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date produceTime;

    @Schema(title = "特征小车车型")
    @JsonProperty("feature1")
    private String feature1;

}
