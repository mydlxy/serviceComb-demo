package com.ca.mfd.prc.pps.communication.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 单车软件清单实体
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "单车软件清单")
@TableName("PRC_MID_SOFTWARE_BOM_LIST")
public class MidSoftwareBomListEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_SOFTWARE_BOM_LIST_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    @TableField("PRC_MID_SOFTWARE_BOM_VERSION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidSoftwareBomVersionId = Constant.DEFAULT_ID;



    /**
     * 整车大版本发布单号
     */
    @Schema(title = "整车大版本发布单号")
    @TableField("PUBLISH_CHANGE_CODE")
    private String publishChangeCode = StringUtils.EMPTY;


    /**
     * BOM行标识
     */
    @Schema(title = "BOM行标识")
    @TableField("LINE_NUM")
    private String lineNum = StringUtils.EMPTY;


    /**
     * 软件编码
     */
    @Schema(title = "软件编码")
    @TableField("SOFTWARE_CODE")
    private String softwareCode = StringUtils.EMPTY;


    /**
     * 软件中文名称
     */
    @Schema(title = "软件中文名称")
    @TableField("SOFTWARE_NAME_CH")
    private String softwareNameCh = StringUtils.EMPTY;


    /**
     * 软件英文名称
     */
    @Schema(title = "软件英文名称")
    @TableField("SOFTWARE_NAME_EN")
    private String softwareNameEn=StringUtils.EMPTY;


    /**
     * 控制器类型
     */
    @Schema(title = "控制器类型")
    @TableField("ECU_TYPE_CODE")
    private String ecuTypeCode = StringUtils.EMPTY;


    /**
     * 控制器中文名称
     */
    @Schema(title = "控制器中文名称")
    @TableField("ECU_TYPE_NAME")
    private String ecuTypeName = StringUtils.EMPTY;


    /**
     * 生命周期
     */
    @Schema(title = "生命周期")
    @TableField("LIFE_CYCLE_STATE")
    private String lifeCycleState=StringUtils.EMPTY;


    /**
     * 部署位置
     */
    @Schema(title = "部署位置")
    @TableField("DEPLOYMENT_LOCATION")
    private String deploymentLocation=StringUtils.EMPTY;


    /**
     * 使用规则
     */
    @Schema(title = "使用规则")
    @TableField("USAGE_VALUE")
    private String usageValue=StringUtils.EMPTY;


    /**
     * 规则描述
     */
    @Schema(title = "规则描述")
    @TableField("USAGE_DESC")
    private String usageDesc=StringUtils.EMPTY;


    /**
     * 零部件管理者工号
     */
    @Schema(title = "零部件管理者工号")
    @TableField("DEV_ENGINEER_ID_USER_LOGIN_NAME")
    private String devEngineerIdUserLoginName=StringUtils.EMPTY;


    /**
     * 零部件管理者姓名
     */
    @Schema(title = "零部件管理者姓名")
    @TableField("DEV_ENGINEER_ID_DISPLAY_NAME")
    private String devEngineerIdDisplayName=StringUtils.EMPTY;


    /**
     * 应用工程师工号
     */
    @Schema(title = "应用工程师工号")
    @TableField("EMPLOYEE_ID_USER_LOGIN_NAME")
    private String employeeIdUserLoginName=StringUtils.EMPTY;


    /**
     * 应用工程师姓名
     */
    @Schema(title = "应用工程师姓名")
    @TableField("EMPLOYEE_ID_DISPLAY_NAME")
    private String employeeIdDisplayName=StringUtils.EMPTY;


    /**
     * 软件件号DID
     */
    @Schema(title = "软件件号DID")
    @TableField("SOFTWARE_PART_NUMBER_DID")
    private String softwarePartNumberDid=StringUtils.EMPTY;

    /**
     * 软件版本号DID
     */
    @Schema(title = "软件版本号DID")
    @TableField("SOFTWARE_VERSION_NUMBER_DID")
    private String softwareVersionNumberDid=StringUtils.EMPTY;

    /**
     * 目标IP
     */
    @Schema(title = "目标IP")
    @TableField("TARGET_IP")
    private String targetIp=StringUtils.EMPTY;


    /**
     * 逻辑地址
     */
    @Schema(title = "逻辑地址")
    @TableField("LOGICAL_ADDRESS")
    private String logicalAddress=StringUtils.EMPTY;


    /**
     * 诊断请求ID
     */
    @Schema(title = "诊断请求ID")
    @TableField("DIAGNOSTIC_REQ_ID")
    private String diagnosticReqId=StringUtils.EMPTY;


    /**
     * 诊断响应ID
     */
    @Schema(title = "诊断响应ID")
    @TableField("DIAGNOSTIC_RES_ID")
    private String diagnosticResId=StringUtils.EMPTY;


    /**
     * 软件集成方
     */
    @Schema(title = "软件集成方")
    @TableField("SOFTWARE_INTEGRATOR")
    private String softwareIntegrator=StringUtils.EMPTY;


    /**
     * 是否支持OTA
     */
    @Schema(title = "是否支持OTA")
    @TableField("IS_SUPPORT_OTA")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSupportOta = false;


    /**
     * 是否支持诊断仪刷写
     */
    @Schema(title = "是否支持诊断仪刷写")
    @TableField("IS_SUPPORT_DIA_WRITTEN")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isSupportDiaWritten = false;


    /**
     * 是否电检
     */
    @Schema(title = "是否电检")
    @TableField("IS_ELE_INSPECTION")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEleInspection = false;


    /**
     * 是否校验软件版本
     */
    @Schema(title = "是否校验软件版本")
    @TableField("IS_CHECK_SW_VERSION")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isCheckSwVersion = false;


    /**
     * 是否产线刷写
     */
    @Schema(title = "是否产线刷写")
    @TableField("IS_ONLINE_WRITTEN")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isOnlineWritten = false;


    /**
     * 扩展字段
     */
    @Schema(title = "扩展字段")
    @TableField("EXTENSION_FIELD")
    private String extensionField=StringUtils.EMPTY;


    /**
     * 兼容的硬件
     */
    @Schema(title = "兼容的硬件")
    @TableField("HARDWARE_PART_NO")
    private String hardwarePartNo=StringUtils.EMPTY;


    /**
     * 替代组
     */
    @Schema(title = "替代组")
    @TableField("REPLACE_GROUP")
    private String replaceGroup=StringUtils.EMPTY;


    /**
     * 软件版本
     */
    @Schema(title = "软件版本")
    @TableField("SOFTWARE_VERSION")
    private String softwareVersion = StringUtils.EMPTY;


    /**
     * 软件包信息
     */
    @Schema(title = "软件包信息")
    @TableField("SOFTWARE_PACKAGE")
    private String softwarePackage = StringUtils.EMPTY;


    /**
     * 配套组
     */
    @Schema(title = "配套组")
    @TableField("SUPPORT_GROUP")
    private String supportGroup=StringUtils.EMPTY;

    /**
     * 变更后整车大版本编码
     */
    @Schema(title = "变更后整车大版本编码")
    @TableField("LAST_VEHICLE_VERSION_CODE")
    private String lastVehicleVersionCode=StringUtils.EMPTY;

}