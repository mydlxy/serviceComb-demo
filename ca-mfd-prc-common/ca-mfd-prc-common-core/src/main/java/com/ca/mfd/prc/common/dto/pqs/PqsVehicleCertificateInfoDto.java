package com.ca.mfd.prc.common.dto.pqs;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 合格证信息
 *
 * @author inkelink eric.zhou
 * @date 2023-08-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "合格证信息")
public class PqsVehicleCertificateInfoDto {

    @Schema(title = "API key")
    @JsonProperty("deipaaskeyauth")
    private String apiKey = StringUtils.EMPTY;

    @Schema(title = "请求唯一标识")
    @JsonProperty("wyid")
    private String wyid = StringUtils.EMPTY;

    @Schema(title = "合格证类型")
    @JsonProperty("hgzlx")
    private String hgzlx = StringUtils.EMPTY;

    @Schema(title = "vin码")
    @JsonProperty("vin")
    private String vin = StringUtils.EMPTY;

    @Schema(title = "车辆型号,公告号")
    @JsonProperty("item_code")
    private String vehicleModel = StringUtils.EMPTY;

    @Schema(title = "电机型号")
    @JsonProperty("master_p_c")
    private String motorModel = StringUtils.EMPTY;

    @Schema(title = "电机条码")
    @JsonProperty("p_sequence")
    private String motorCode = StringUtils.EMPTY;

    @Schema(title = "出厂日期")
    @JsonProperty("date_out")
    private String onlineTime;

    @Schema(title = "车辆质量")
    @JsonProperty("zzl")
    private String vehicleWeight = StringUtils.EMPTY;

    @Schema(title = "座位数")
    @JsonProperty("edzk")
    private String seatNum = StringUtils.EMPTY;

    @Schema(title = "整车物料代码")
    @JsonProperty("wlbm")
    private String materialNo = StringUtils.EMPTY;

    @Schema(title = "整车物料名称")
    @JsonProperty("wlmc")
    private String materialName = StringUtils.EMPTY;

    @Schema(title = "事业部代码")
    @JsonProperty("sybdm")
    private String sybdm = StringUtils.EMPTY;

    @Schema(title = "车辆类型")
    @JsonProperty("cllb")
    private String cllb = StringUtils.EMPTY;

    @Schema(title = "工厂")
    @JsonProperty("gc")
    private String gc = StringUtils.EMPTY;

    @Schema(title = "车身颜色")
    @JsonProperty("color")
    private String vehicleColor = StringUtils.EMPTY;

    @Schema(title = "燃料种类")
    @JsonProperty("fuel_type")
    private String fuelType = StringUtils.EMPTY;

    @Schema(title = "轮胎规格")
    @JsonProperty("tyre_code")
    private String tyreCode = StringUtils.EMPTY;

    @Schema(title = "公告批次")
    @JsonProperty("pc")
    private String pc = StringUtils.EMPTY;

    @Schema(title = "企业其他信息")
    @JsonProperty("veh_qyqtxx")
    private String enterpriseOtherInformation = StringUtils.EMPTY;

    @Schema(title = "备注")
    @JsonProperty("Veh_bz")
    private String remarks = StringUtils.EMPTY;

    @Schema(title = "长")
    @JsonProperty("veh_wkc")
    private String vehicleLength = StringUtils.EMPTY;

    @Schema(title = "宽")
    @JsonProperty("veh_wkk")
    private String vehicleWidth = StringUtils.EMPTY;

    @Schema(title = "高")
    @JsonProperty("veh_wkg")
    private String vehicleHigh = StringUtils.EMPTY;

    @Schema(title = "整备质量")
    @JsonProperty("zhbzhl")
    private String vehicleCurbWeight = StringUtils.EMPTY;

    @Schema(title = "配置序列号")
    @JsonProperty("veh_pzxlh")
    private String configurationSerialNumber = StringUtils.EMPTY;

    @Schema(title = "双积分ID")
    @JsonProperty("veh_jfpzid")
    private String doubleIntegralId = StringUtils.EMPTY;

    @Schema(title = "生效日期")
    @JsonProperty("veh_ggsxrq")
    private String effectiveDate = StringUtils.EMPTY;

    @Schema(title = "失效日期")
    @JsonProperty("veh_ggwxrq")
    private String expirationDate = StringUtils.EMPTY;

}
