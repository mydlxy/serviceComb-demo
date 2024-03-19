package com.ca.mfd.prc.pm.communication.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 物料主数据中间表实体
 * @author inkelink
 * @date 2023年12月11日
 * @变更说明 BY inkelink At 2023年12月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "物料主数据中间表")
@TableName("PRC_MID_MATERIAL_MASTER")
public class MidMaterialMasterEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_MATERIAL_MASTER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 版本
     */
    @Schema(title = "版本")
    @TableField("MATERIAL_VERSION")
    private String materialVersion = StringUtils.EMPTY;


    /**
     * 合作方编码
     */
    @Schema(title = "合作方编码")
    @TableField("PARTNER_CODE")
    private String partnerCode = StringUtils.EMPTY;


    /**
     * 物料类型名称
     */
    @Schema(title = "物料类型名称")
    @TableField("MATERIAL_TYPE_NAME")
    private String materialTypeName = StringUtils.EMPTY;


    /**
     * 物料中文名称
     */
    @Schema(title = "物料中文名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 物料中文名称备注
     */
    @Schema(title = "物料中文名称备注")
    @TableField("MATERIAL_NAME_REMARK")
    private String materialNameRemark = StringUtils.EMPTY;


    /**
     * 物料英文名称
     */
    @Schema(title = "物料英文名称")
    @TableField("MATERIAL_NAME_EN")
    private String materialNameEn = StringUtils.EMPTY;


    /**
     * 物料英文名称备注
     */
    @Schema(title = "物料英文名称备注")
    @TableField("MATERIAL_NAME_REMARK_EN")
    private String materialNameRemarkEn = StringUtils.EMPTY;


    /**
     * 首次使用项目
     */
    @Schema(title = "首次使用项目")
    @TableField("FIRST_USE_PROJECT")
    private String firstUseProject = StringUtils.EMPTY;


    /**
     * 生命周期
     */
    @Schema(title = "生命周期")
    @TableField("LIFE_CYCLE_STATE")
    private String lifeCycleState = StringUtils.EMPTY;


    /**
     * 计量单位
     */
    @Schema(title = "计量单位")
    @TableField("MEASURE_UNIT")
    private String measureUnit = StringUtils.EMPTY;


    /**
     * 颜色件
     */
    @Schema(title = "颜色件")
    @TableField("COLOR_PART_CODE")
    private String colorPartCode = StringUtils.EMPTY;


    /**
     * 内外饰类型
     */
    @Schema(title = "内外饰类型")
    @TableField("IE_IDENTIFY_TYPE")
    private String ieIdentifyType = StringUtils.EMPTY;


    /**
     * 实体类型
     */
    @Schema(title = "实体类型")
    @TableField("SUBSTANCE_TYPE")
    private String substanceType = StringUtils.EMPTY;


    /**
     * 是否控制器
     */
    @Schema(title = "是否控制器")
    @TableField("IS_CONTOL")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isContol = false;


    /**
     * 整车架构层级
     */
    @Schema(title = "整车架构层级")
    @TableField("VEHICLE_ARCHIT_LEVEL")
    private String vehicleArchitLevel = StringUtils.EMPTY;


    /**
     * 创建者
     */
    @Schema(title = "创建者")
    @TableField("CREATED_BY_DISPLAY_NAME")
    private String createdByDisplayName = StringUtils.EMPTY;


    /**
     * 零部件管理者工号
     */
    @Schema(title = "零部件管理者工号")
    @TableField("DEV_ENGINEER_ID_USER_LOGIN_NAME")
    private String devEngineerIdUserLoginName = StringUtils.EMPTY;


    /**
     * 零部件管理者姓名
     */
    @Schema(title = "零部件管理者姓名")
    @TableField("DEV_ENGINEER_ID_DISPLAY_NAME")
    private String devEngineerIdDisplayName = StringUtils.EMPTY;


    /**
     * 零部件管理者部门
     */
    @Schema(title = "零部件管理者部门")
    @TableField("DEV_ENGINEER_DEPARTMENT_DISPLAY_NAME")
    private String devEngineerDepartmentDisplayName = StringUtils.EMPTY;


    /**
     * 发布时间
     */
    @Schema(title = "发布时间")
    @TableField("EFFECTIVE_FROM")
    private String effectiveFrom = StringUtils.EMPTY;


    /**
     * 件号申请单单号
     */
    @Schema(title = "件号申请单单号")
    @TableField("FORM_CODE")
    private String formCode = StringUtils.EMPTY;


    /**
     * 件号来源
     */
    @Schema(title = "件号来源")
    @TableField("PART_NUMBER_SOURCE")
    private String partNumberSource = StringUtils.EMPTY;


    /**
     * 是否外观件
     */
    @Schema(title = "是否外观件")
    @TableField("IS_EXTERIOR_PIECE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isExteriorPiece = false;


    /**
     * 是否性能件
     */
    @Schema(title = "是否性能件")
    @TableField("IS_PERFORMANCE_PIECE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isPerformancePiece = false;


    /**
     * 黑灰盒子件
     */
    @Schema(title = "黑灰盒子件")
    @TableField("BLACK_GRAY_BOX_PIECE")
    private String blackGrayBoxPiece = StringUtils.EMPTY;


    /**
     * 通用类型
     */
    @Schema(title = "通用类型")
    @TableField("UNIVERSAL_TYPE")
    private String universalType = StringUtils.EMPTY;


    /**
     * 平台属性
     */
    @Schema(title = "平台属性")
    @TableField("PLATFORM_PROPERTIE")
    private String platformPropertie = StringUtils.EMPTY;


    /**
     * 专用件技术方案描述
     */
    @Schema(title = "专用件技术方案描述")
    @TableField("SPEC_PART_TECH_PROP_DESC")
    private String specPartTechPropDesc = StringUtils.EMPTY;


    /**
     * 变更原因
     */
    @Schema(title = "变更原因")
    @TableField("CHANGE_REASON")
    private String changeReason = StringUtils.EMPTY;


    /**
     * 是否备件
     */
    @Schema(title = "是否备件")
    @TableField("IS_SERVICE")
    private String isService = StringUtils.EMPTY;


    /**
     * 生产企业名称
     */
    @Schema(title = "生产企业名称")
    @TableField("LIFE_ENTERPRISE_NAME")
    private String lifeEnterpriseName = StringUtils.EMPTY;


    /**
     * 零部件型号
     */
    @Schema(title = "零部件型号")
    @TableField("PART_MODEL")
    private String partModel = StringUtils.EMPTY;


    /**
     * 3C件类型
     */
    @Schema(title = "3C件类型")
    @TableField("THREE_C_PART_TYPE")
    private String threeCPartType = StringUtils.EMPTY;


    /**
     * 环保件
     */
    @Schema(title = "环保件")
    @TableField("ENVIR_PROTECTION_PART")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean envirProtectionPart = false;


    /**
     * 是否公告件
     */
    @Schema(title = "是否公告件")
    @TableField("IS_ANNOUNCEMENT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isAnnouncement = false;


    /**
     * 证书编号
     */
    @Schema(title = "证书编号")
    @TableField("CERTIFICATE_NUMBER")
    private String certificateNumber = StringUtils.EMPTY;


    /**
     * 认证工厂代码
     */
    @Schema(title = "认证工厂代码")
    @TableField("CERTIFICATE_FACTORY_CODE")
    private String certificateFactoryCode = StringUtils.EMPTY;


    /**
     * 认证标识固定方法
     */
    @Schema(title = "认证标识固定方法")
    @TableField("CERTIFICATE_MARK_FIX_METHOD")
    private String certificateMarkFixMethod = StringUtils.EMPTY;


    /**
     * 定点需求
     */
    @Schema(title = "定点需求")
    @TableField("TARGETED_DEMAND")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean targetedDemand = false;


    /**
     * 物品品类
     */
    @Schema(title = "物品品类")
    @TableField("ITEM_CATEGORY")
    private String itemCategory = StringUtils.EMPTY;


    /**
     * 发动机机型
     */
    @Schema(title = "发动机机型")
    @TableField("ENGINE_MODEL")
    private String engineModel = StringUtils.EMPTY;


    /**
     * 采购类型
     */
    @Schema(title = "采购类型")
    @TableField("PROCUREMENT_TYPE")
    private String procurementType = StringUtils.EMPTY;


    /**
     * 物品类别
     */
    @Schema(title = "物品类别")
    @TableField("ITEM_TYPE")
    private String itemType = StringUtils.EMPTY;


    /**
     * 关重件特性
     */
    @Schema(title = "关重件特性")
    @TableField("IMPORTANT_PARTS_SPECIAL")
    private String importantPartsSpecial = StringUtils.EMPTY;


    /**
     * 长安标识方式
     */
    @Schema(title = "长安标识方式")
    @TableField("CHANGAN_IDENTIFICATION")
    private String changanIdentification = StringUtils.EMPTY;


    /**
     * 质量精准追溯
     */
    @Schema(title = "质量精准追溯")
    @TableField("ACCURATE_AUALITY_TRACEABILIY")
    private String accurateAualityTraceabiliy = StringUtils.EMPTY;


    /**
     * 三包总成
     */
    @Schema(title = "三包总成")
    @TableField("THREE_GUARANTEES_ASSEMBLY")
    private String threeGuaranteesAssembly = StringUtils.EMPTY;


    /**
     * 三包主要零部件种类
     */
    @Schema(title = "三包主要零部件种类")
    @TableField("THREE_GUARANTEES_MAIN_PART_TYPE")
    private String threeGuaranteesMainPartType = StringUtils.EMPTY;


    /**
     * 严重安全性能故障涉及的零部件种类
     */
    @Schema(title = "严重安全性能故障涉及的零部件种类")
    @TableField("SERI_SAFETY_PERF_PART_TYPE")
    private String seriSafetyPerfPartType = StringUtils.EMPTY;


    /**
     * 失效模式
     */
    @Schema(title = "失效模式")
    @TableField("FAILURE_MODE")
    private String failureMode = StringUtils.EMPTY;


    /**
     * 包修模式
     */
    @Schema(title = "包修模式")
    @TableField("REPAIR_MODE")
    private String repairMode = StringUtils.EMPTY;


    /**
     * 包修里程
     */
    @Schema(title = "包修里程")
    @TableField("REPAIR_MILEAGE")
    private String repairMileage = StringUtils.EMPTY;


    /**
     * 规格或型号
     */
    @Schema(title = "规格或型号")
    @TableField("SPECIFICATION_OR_MODEL规格或型号")
    private String specificationOrModel规格或型号 = StringUtils.EMPTY;


    /**
     * 辅料厂家
     */
    @Schema(title = "辅料厂家")
    @TableField("ACCESSORIES_MANUFACTURER")
    private String accessoriesManufacturer = StringUtils.EMPTY;


    /**
     * 所属设备
     */
    @Schema(title = "所属设备")
    @TableField("AFFILIATIONE_QUIPMENT")
    private String affiliationeQuipment = StringUtils.EMPTY;


    /**
     * 提交单位
     */
    @Schema(title = "提交单位")
    @TableField("SUBMIT_DEPARTMENT")
    private String submitDepartment = StringUtils.EMPTY;


    /**
     * 需求类型
     */
    @Schema(title = "需求类型")
    @TableField("REQUIREMENT_TYPE")
    private String requirementType = StringUtils.EMPTY;


    /**
     * 采购工程师
     */
    @Schema(title = "采购工程师")
    @TableField("PURCHAS_ENGINEER_NAME")
    private String purchasEngineerName = StringUtils.EMPTY;


    /**
     * 目标重量(kg)
     */
    @Schema(title = "目标重量(kg)")
    @TableField("DESIGN_WEIGHT")
    private String designWeight = StringUtils.EMPTY;


    /**
     * 设计重量(kg)
     */
    @Schema(title = "设计重量(kg)")
    @TableField("TARGET_WEIGHT")
    private String targetWeight = StringUtils.EMPTY;


    /**
     * 材质分类
     */
    @Schema(title = "材质分类")
    @TableField("MATERIAL_CLASS")
    private String materialClass = StringUtils.EMPTY;


    /**
     * 实际重量(kg)
     */
    @Schema(title = "实际重量(kg)")
    @TableField("ACTUAL_WEIGHT")
    private String actualWeight = StringUtils.EMPTY;


    /**
     * 表面处理
     */
    @Schema(title = "表面处理")
    @TableField("SURFACE_TREATMENT")
    private String surfaceTreatment = StringUtils.EMPTY;


    /**
     * 软件集成方
     */
    @Schema(title = "软件集成方")
    @TableField("SOFTWARE_INTEGRATOR")
    private String softwareIntegrator = StringUtils.EMPTY;


    /**
     * 硬件件号
     */
    @Schema(title = "硬件件号")
    @TableField("HARDWARE_PART_NUMBER")
    private String hardwarePartNumber = StringUtils.EMPTY;


    /**
     * 硬件名称
     */
    @Schema(title = "硬件名称")
    @TableField("HARDWARE_PART_NAME")
    private String hardwarePartName = StringUtils.EMPTY;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer exeStatus = 0;


    /**
     * 处理信息
     */
    @Schema(title = "处理信息")
    @TableField("EXE_MSG")
    private String exeMsg = StringUtils.EMPTY;


    /**
     * 处理时间
     */
    @Schema(title = "处理时间")
    @TableField("EXE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date exeTime = new Date();


    /**
     * 校验结果
     */
    @Schema(title = "校验结果")
    @TableField("CHECK_RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer checkResult = 0;


    /**
     * 校验结果说明
     */
    @Schema(title = "校验结果说明")
    @TableField("CHECK_RESULT_DESC")
    private String checkResultDesc = StringUtils.EMPTY;


}