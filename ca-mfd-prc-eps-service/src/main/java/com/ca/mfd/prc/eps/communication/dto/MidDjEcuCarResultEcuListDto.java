package com.ca.mfd.prc.eps.communication.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 电检软件结果信息ecuList
 * @date 2024年2月19日
 */
@Data
@Schema(description = "电检软件结果信息ecuList")
public class MidDjEcuCarResultEcuListDto {

    @Schema(title = "ECU类型")
    private String ecuType = StringUtils.EMPTY;

    @Schema(title = "软件件号DID")
    private String softwarePartNumberDid = StringUtils.EMPTY;

    @Schema(title = "软件版本号DID")
    private String softwareVersionNumberDid = StringUtils.EMPTY;

    @Schema(title = "以太网诊断目标IP")
    private String targetIp = StringUtils.EMPTY;

    @Schema(title = "以太网诊断逻辑地址")
    private String logicalAddress = StringUtils.EMPTY;

    @Schema(title = "CAN诊断请求ID")
    private String diagnosticReqId = StringUtils.EMPTY;

    @Schema(title = "CAN诊断响应ID")
    private String diagnosticResId = StringUtils.EMPTY;

    @Schema(title = "整车大版本发布单号")
    private String publishChangeCode = StringUtils.EMPTY;

    @Schema(title = "整车大版本号")
    private String lastVehicleVersionCode = StringUtils.EMPTY;

    @Schema(title = "软件件号")
    private String softwareCode = StringUtils.EMPTY;

    @Schema(title = "软件版本")
    private String softwareVersion = StringUtils.EMPTY;

    @Schema(title = "硬件件号")
    private String hardwarePartNo = StringUtils.EMPTY;

    @Schema(title = "部署位置")
    private String deploymentLocation = StringUtils.EMPTY;

    @Schema(title = "硬件版本号")
    private String hardwareVersion = StringUtils.EMPTY;

    @Schema(title = "配置版本号")
    private String changeCode = StringUtils.EMPTY;

    @Schema(title = "配置字数组")
    private List<MidDjEcuCarResultEcuConfDto> ecuConf;

    @Schema(title = "刷写信息")
    private List<MidDjEcuCarResultSoftwarePackageDto> softwarePackage;

    @Schema(title = "ECU唯一识别号")
    private String ecuTuid = StringUtils.EMPTY;

    @Schema(title = "控制器追溯码")
    private String barcode = StringUtils.EMPTY;

    @Schema(title = "ECU生产日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date ecuProductionDate = new Date();

    @Schema(title = "ECU供应商")
    private String ecuSupplier = StringUtils.EMPTY;
}
