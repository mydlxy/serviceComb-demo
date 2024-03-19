package com.ca.mfd.prc.pm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 控制计划实体(扩展字段属性1、属性2、属性3分别作为反应计划的反应编码，反应步骤，反应步骤描述)
 * @author inkelink
 * @date 2023年11月22日
 * @变更说明 BY inkelink At 2023年11月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "控制计划")
@TableName("PRC_PM_CONTROL_PLAN")
public class PmControlPlanEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_CONTROL_PLAN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间ID
     */
    @Schema(title = "车间ID")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("PRC_PM_WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 线体ID
     */
    @Schema(title = "线体ID")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("PRC_PM_LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 工位ID
     */
    @Schema(title = "工位ID")
    @TableField("PRC_PM_WORKSTATION_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkstationId = Constant.DEFAULT_ID;


    /**
     * 工位编码
     */
    @Schema(title = "工位编码")
    @TableField("PRC_PM_WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * BOP_ID
     */
    @Schema(title = "BOP_ID")
    @TableField("PRC_PM_BOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmBopId = Constant.DEFAULT_ID;

    /**
     * 作业步骤
     */
    @Schema(title = "作业步骤")
    @TableField("FROCK_CODE")
    private String frockCode = StringUtils.EMPTY;


    /**
     * 配置模块
     */
    @Schema(title = "配置模块")
    @TableField("CONFIG_MODEL")
    private String configModel = StringUtils.EMPTY;


    /**
     * 过程编号
     */
    @Schema(title = "过程编号")
    @TableField("CONFIG_NO")
    private String configNo = StringUtils.EMPTY;


    /**
     * 零件号
     */
    @Schema(title = "零件号")
    @TableField("MASTER_NO")
    private String masterNo = StringUtils.EMPTY;


    /**
     * 过程名称/操作描述/制造装置机器/装置/夹具/工装
     */
    @Schema(title = "过程名称/操作描述/制造装置机器/装置/夹具/工装")
    @TableField("OPERATION_DESCRIPTION")
    private String operationDescription = StringUtils.EMPTY;


    /**
     * 控制特性编号
     */
    @Schema(title = "控制特性编号")
    @TableField("CONTROL_CHARACTERISTIC_CODE")
    private String controlCharacteristicCode = StringUtils.EMPTY;


    /**
     * 控制特性产品
     */
    @Schema(title = "控制特性产品")
    @TableField("CONTROL_CHARACTERISTIC_PRODUCT")
    private String controlCharacteristicProduct = StringUtils.EMPTY;


    /**
     * 控制特性过程
     */
    @Schema(title = "控制特性过程")
    @TableField("CONTROL_CHARACTERISTIC_PROCESS")
    private String controlCharacteristicProcess = StringUtils.EMPTY;


    /**
     * 特殊特性分类
     */
    @Schema(title = "特殊特性分类")
    @TableField("SPECIAL_CHARACTERISTIC_CLASS")
    private String specialCharacteristicClass = StringUtils.EMPTY;


    /**
     * 产品/过程规范/公差
     */
    @Schema(title = "产品/过程规范/公差")
    @TableField("PROCESS_SPECIFICATION")
    private String processSpecification = StringUtils.EMPTY;


    /**
     * 评价/测量技术1
     */
    @Schema(title = "评价/测量技术1")
    @TableField("MEASUREMENT_FIRST")
    private String measurementFirst = StringUtils.EMPTY;


    /**
     * 评价/测量技术2
     */
    @Schema(title = "评价/测量技术2")
    @TableField("MEASUREMENT_SECOND")
    private String measurementSecond = StringUtils.EMPTY;


    /**
     * 样本容量
     */
    @Schema(title = "样本容量")
    @TableField("SAMPLE_SIZE")
    private String sampleSize = StringUtils.EMPTY;


    /**
     * 样本频率
     */
    @Schema(title = "样本频率")
    @TableField("SAMPLE_FREQUENCY")
    private String sampleFrequency = StringUtils.EMPTY;


    /**
     * 控制方法
     */
    @Schema(title = "控制方法")
    @TableField("CONTROL_METHODS")
    private String controlMethods = StringUtils.EMPTY;


    /**
     * 反应计划
     */
    @Schema(title = "反应计划")
    @TableField("RESPONSE_PLAN")
    private String responsePlan = StringUtils.EMPTY;


    /**
     * 通知人
     */
    @Schema(title = "通知人")
    @TableField("NOTIFIER")
    private String notifier = StringUtils.EMPTY;


}