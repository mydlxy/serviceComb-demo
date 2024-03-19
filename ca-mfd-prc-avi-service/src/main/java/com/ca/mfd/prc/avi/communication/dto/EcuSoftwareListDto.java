package com.ca.mfd.prc.avi.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description= "软件清单")
public class EcuSoftwareListDto {



    @Schema(title = "ECU类型")
    private String ecuTypeCode = StringUtils.EMPTY;
    @Schema(title = "软件件号DID")
    private String softwarePartNumberDid = StringUtils.EMPTY;
    @Schema(title = "软件版本号DID")
    private String softwareVersionNumberDid = StringUtils.EMPTY;
    @Schema(title = "目标IP")
    private String targetIp = StringUtils.EMPTY;
    @Schema(title = "逻辑地址")
    private String logicalAddress = StringUtils.EMPTY;
    @Schema(title = "CAN诊断请求ID")
    private String diagnosticReqId = StringUtils.EMPTY;
    @Schema(title = "CAN诊断响应ID")
    private String diagnosticResId = StringUtils.EMPTY;
    @Schema(title = "整车大版本号")
    private String publishChangeCode = StringUtils.EMPTY;
    @Schema(title = "整车大版本号")
    private String lastVehicleVersionCode = StringUtils.EMPTY;
    @Schema(title = "软件件号")
    private String softwareCode = StringUtils.EMPTY;
    @Schema(title = "软件版本")
    private String softwareVersion = StringUtils.EMPTY;
    @Schema(title = "是否校验软件版本")
    private String isCheckSwVersion = StringUtils.EMPTY;
    @Schema(title = "硬件件号")
    private String hardwarePartNo = StringUtils.EMPTY;
    @Schema(title = "硬件版本号")
    private String hardwarePartVersion = StringUtils.EMPTY;
    @Schema(title = "部署位置")
    private String deploymentLocation = StringUtils.EMPTY;
    @Schema(title = "配置版本号")
    private String changeCode = StringUtils.EMPTY;

    private List<EcuSoftwareDidDto> ecuConf =new ArrayList<>();


    @Schema(title = "刷写信息")
    @JsonProperty("softwarePackage")
    private String softwarePackage = StringUtils.EMPTY;
    @Schema(title = "是否支持OTA")
    private String isSupportOta = StringUtils.EMPTY;
    @Schema(title = "是否支持产线刷写")
    private String isOnlineWritten = StringUtils.EMPTY;
    @Schema(title = "是否支持诊断仪刷写")
    private String isSupportDiaWritten = StringUtils.EMPTY;

}
