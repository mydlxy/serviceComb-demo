package com.ca.mfd.prc.eps.communication.dto;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: 车辆设备中间表（车云）实体
 * @author inkelink
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Data
@Schema(description= "车辆设备详情中间表（车云）")
public class CarCloudDeviceDatailDto {

    /**
     * 设备TUID
     */
    @Schema(title = "设备TUID")
    private String ecuTuid = StringUtils.EMPTY;


    /**
     * 设备追溯码
     */
    @Schema(title = "设备追溯码")
    private String traceBarcode = StringUtils.EMPTY;


    /**
     * 设备类型（TBOX,EDC,C2,VIU)
     */
    @Schema(title = "设备类型（TBOX,EDC,C2,VIU)")
    private String ecuTypeCode = StringUtils.EMPTY;

    /**
     * 控制器中文名称
     */
    @Schema(title = "控制器中文名称")
    private String ecuTypeNameCh = StringUtils.EMPTY;


    /**
     * 软件件号
     */
    @Schema(title = "软件件号")
    private String softwareCode = StringUtils.EMPTY;


    /**
     * 硬件件号
     */
    @Schema(title = "硬件件号")
    private String hardwarePartNo = StringUtils.EMPTY;


    /**
     * 软件版本号
     */
    @Schema(title = "软件版本号")
    private String softwareVersion = StringUtils.EMPTY;

    /**
     * 零部件管理者工号
     */
    @Schema(title = "零部件管理者工号")
    private String devEngineerIdUserLoginName=StringUtils.EMPTY;


    /**
     * 零部件管理者姓名
     */
    @Schema(title = "零部件管理者姓名")
    private String devEngineerIdDisplayName=StringUtils.EMPTY;


    /**
     * 应用工程师工号
     */
    @Schema(title = "应用工程师工号")
    private String employeeIdUserLoginName=StringUtils.EMPTY;


    /**
     * 应用工程师姓名
     */
    @Schema(title = "应用工程师姓名")
    private String employeeIdDisplayName=StringUtils.EMPTY;



    /**
     * 配置字IDD(可能不止一个，待数据样例确认)
     */
    @Schema(title = "配置字IDD(可能不止一个，待数据样例确认)")
    private List<EcuSoftwareDidDto> ecuConf = new ArrayList();

    /**
     * 是否支持诊断仪
     */
    @Schema(title = "是否支持诊断仪")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private String isSupportDiaWritten = StringUtils.EMPTY;


    /**
     * 是否支持OTA
     */
    @Schema(title = "是否支持OTA")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private String isSupportOta = StringUtils.EMPTY;

    /**
     * 部署位置
     */
    @Schema(title = "部署位置")
    private String deploymentLocation = StringUtils.EMPTY;

    /**
     * 是否校验软件版本
     */
    @Schema(title = "是否校验软件版本")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private String isCheckSwVersion =StringUtils.EMPTY;
    /**
     * 是否产线刷写
     */
    @Schema(title = "是否产线刷写")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private String isOnlineWritten =StringUtils.EMPTY;

    /**
     * 扩展字段
     */
    @Schema(title = "扩展字段")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private String extensionField =StringUtils.EMPTY;
}