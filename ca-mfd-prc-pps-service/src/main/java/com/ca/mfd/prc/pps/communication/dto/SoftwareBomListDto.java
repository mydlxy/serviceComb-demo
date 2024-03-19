package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 单车软件清单实体
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
@Data
@Schema(description= "软件清单")
public class SoftwareBomListDto  {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;




    /**
     * 整车大版本发布单号
     */
    @Schema(title = "整车大版本发布单号")
    private String publishChangeCode = StringUtils.EMPTY;


    /**
     * BOM行标识
     */
    @Schema(title = "BOM行标识")
    private String lineNum = StringUtils.EMPTY;


    /**
     * 软件编码
     */
    @Schema(title = "软件编码")
    private String softwareCode = StringUtils.EMPTY;


    /**
     * 软件中文名称
     */
    @Schema(title = "软件中文名称")
    private String softwareNameCh = StringUtils.EMPTY;


    /**
     * 软件英文名称
     */
    @Schema(title = "软件英文名称")
    private String softwareNameEn;


    /**
     * 控制器类型
     */
    @Schema(title = "控制器类型")
    private String ecuTypeCode = StringUtils.EMPTY;


    /**
     * 控制器中文名称
     */
    @Schema(title = "控制器中文名称")
    private String ecuTypeName = StringUtils.EMPTY;


    /**
     * 生命周期
     */
    @Schema(title = "生命周期")
    private String lifeCycleState;


    /**
     * 部署位置
     */
    @Schema(title = "部署位置")
    private String deploymentLocation;


    /**
     * 使用规则
     */
    @Schema(title = "使用规则")
    private String usageValue;


    /**
     * 规则描述
     */
    @Schema(title = "规则描述")
    private String usageDesc;


    /**
     * 零部件管理者工号
     */
    @Schema(title = "零部件管理者工号")
    private String devEngineerIdUserLoginName;


    /**
     * 零部件管理者姓名
     */
    @Schema(title = "零部件管理者姓名")
    private String devEngineerIdDisplayName;


    /**
     * 应用工程师工号
     */
    @Schema(title = "应用工程师工号")
    private String employeeIdUserLoginName;


    /**
     * 应用工程师姓名
     */
    @Schema(title = "应用工程师姓名")
    private String employeeIdDisplayName;


    /**
     * 目标IP
     */
    @Schema(title = "目标IP")
    private String targetIp;


    /**
     * 逻辑地址
     */
    @Schema(title = "逻辑地址")
    private String logicalAddress;


    /**
     * 诊断请求ID
     */
    @Schema(title = "诊断请求ID")
    private String diagnosticReqId;


    /**
     * 诊断响应ID
     */
    @Schema(title = "诊断响应ID")
    private String diagnosticResId;


    /**
     * 软件集成方
     */
    @Schema(title = "软件集成方")
    private String softwareIntegrator;


    /**
     * 是否支持OTA
     */
    @Schema(title = "是否支持OTA")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSupportOta = false;


    /**
     * 是否支持诊断仪刷写
     */
    @Schema(title = "是否支持诊断仪刷写")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSupportDiaWritten = false;


    /**
     * 是否电检
     */
    @Schema(title = "是否电检")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEleInspection = false;


    /**
     * 是否校验软件版本
     */
    @Schema(title = "是否校验软件版本")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isCheckSwVersion = false;


    /**
     * 是否产线刷写
     */
    @Schema(title = "是否产线刷写")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isOnlineWritten = false;


    /**
     * 扩展字段
     */
    @Schema(title = "扩展字段")
    private String extensionField;


    /**
     * 兼容的硬件
     */
    @Schema(title = "兼容的硬件")
    private String hardwarePartNo;


    /**
     * 替代组
     */
    @Schema(title = "替代组")
    private String replaceGroup;


    /**
     * 软件版本
     */
    @Schema(title = "软件版本")
    private String softwareVersion = StringUtils.EMPTY;


    /**
     * 软件包信息
     */
    @Schema(title = "软件包信息")
    private String softwarePackage = StringUtils.EMPTY;


    /**
     * 配套组
     */
    @Schema(title = "配套组")
    private String supportGroup= StringUtils.EMPTY;
    /**
     * 变更后整车大版本编码
     */
    @Schema(title = "变更后整车大版本编码")
    private String lastVehicleVersionCode= StringUtils.EMPTY;

    @Schema(title = "软件件号DID")
    private String softwarePartNumberDid= StringUtils.EMPTY;

    @Schema(title = "软件版本号DID")
    private String softwareVersionNumberDid= StringUtils.EMPTY;

}