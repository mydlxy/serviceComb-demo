package com.ca.mfd.prc.pps.communication.entity;

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
 * @Description: 车型主数据中间表实体
 * @author inkelink
 * @date 2023年12月11日
 * @变更说明 BY inkelink At 2023年12月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "车型主数据中间表")
@TableName("PRC_MID_VEHICLE_MASTER")
public class MidVehicleMasterEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_VEHICLE_MASTER_ID", type = IdType.INPUT)
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
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    @TableField("VEHICLE_MATERIAL_NUMBER")
    private String vehicleMaterialNumber = StringUtils.EMPTY;


    /**
     * 配置特征清单
     */
    @Schema(title = "配置特征清单")
    @TableField("CONFIGURATION_FEATURE_LIST")
    private String configurationFeatureList;


    /**
     * 配置特征清单描述
     */
    @Schema(title = "配置特征清单描述")
    @TableField("CONFIGURATION_FEATURE_LIST_DESC")
    private String configurationFeatureListDesc = StringUtils.EMPTY;


    /**
     * BOM房间
     */
    @Schema(title = "BOM房间")
    @TableField("BOM_ROOM")
    private String bomRoom = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("PRODUCT_CODE")
    private String productCode = StringUtils.EMPTY;


    /**
     * 基础车型
     */
    @Schema(title = "基础车型")
    @TableField("BASIC_VEHICLE_MODEL")
    private String basicVehicleModel = StringUtils.EMPTY;


    /**
     * 车型号
     */
    @Schema(title = "车型号")
    @TableField("VEHICLE_MODEL_NUMBER")
    private String vehicleModelNumber = StringUtils.EMPTY;


    /**
     * 车型状态号
     */
    @Schema(title = "车型状态号")
    @TableField("VEHICLE_STATUS_NUMBER")
    private String vehicleStatusNumber = StringUtils.EMPTY;


    /**
     * 选装包清单
     */
    @Schema(title = "选装包清单")
    @TableField("OPTIONAL_PACKAGE_LIST")
    private String optionalPackageList;


    /**
     * 选装包清单描述
     */
    @Schema(title = "选装包清单描述")
    @TableField("ONAL_PACKAGE_LIST_DESC")
    private String onalPackageListDesc = StringUtils.EMPTY;


    /**
     * 车身颜色代码
     */
    @Schema(title = "车身颜色代码")
    @TableField("BODY_COLOR_CODE")
    private String bodyColorCode = StringUtils.EMPTY;


    /**
     * 生成方式
     */
    @Schema(title = "生成方式")
    @TableField("GENERATION_MODE")
    private String generationMode = StringUtils.EMPTY;


    /**
     * 整车物料号类型
     */
    @Schema(title = "整车物料号类型")
    @TableField("VEHICLE_MATERIAL_NUMBER_TYPE")
    private String vehicleMaterialNumberType = StringUtils.EMPTY;


    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    private String status = StringUtils.EMPTY;


    /**
     * 生效日期起
     */
    @Schema(title = "生效日期起")
    @TableField("EFFECTIVE_FROM")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveFrom = new Date();


    /**
     * 生效日期止
     */
    @Schema(title = "生效日期止")
    @TableField("EFFECTIVE_TO")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date effectiveTo = new Date();


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