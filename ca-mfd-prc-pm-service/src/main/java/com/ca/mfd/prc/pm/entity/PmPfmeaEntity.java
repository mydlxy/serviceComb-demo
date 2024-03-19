package com.ca.mfd.prc.pm.entity;

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
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: PFMEA配置实体
 * @author inkelink
 * @date 2024年01月10日
 * @变更说明 BY inkelink At 2024年01月10日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "PFMEA配置")
@TableName("PRC_PM_PFMEA")
public class  PmPfmeaEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_PFMEA_ID", type = IdType.INPUT)
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
     * BOPID
     */
    @Schema(title = "BOPID")
    @TableField("PRC_PM_BOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmBopId = Constant.DEFAULT_ID;


    /**
     * 过程步骤
     */
    @Schema(title = "过程步骤")
    @TableField("FROCK_CODE")
    private String frockCode = StringUtils.EMPTY;


    /**
     * 操作描述
     */
    @Schema(title = "操作描述")
    @TableField("OPERATION_DESCRIPTION")
    private String operationDescription = StringUtils.EMPTY;


    /**
     * 过程工作要素
     */
    @Schema(title = "过程工作要素")
    @TableField("PROCESS_WORK_ELEMENTS")
    private String processWorkElements = StringUtils.EMPTY;


    /**
     * 过程步骤功能
     */
    @Schema(title = "过程步骤功能")
    @TableField("PROCESS_STEP_FUNCTION")
    private String processStepFunction = StringUtils.EMPTY;


    /**
     * 产品特性
     */
    @Schema(title = "产品特性")
    @TableField("PRODUCT_CHARACTERISTICS")
    private String productCharacteristics = StringUtils.EMPTY;


    /**
     * 产品特性规范
     */
    @Schema(title = "产品特性规范")
    @TableField("PRODUCT_CHARACTERISTIC_SPECIFICATIONS")
    private String productCharacteristicSpecifications = StringUtils.EMPTY;


    /**
     * 过程工作要素功能
     */
    @Schema(title = "过程工作要素功能")
    @TableField("PROCESS_WORK_ELEMENT_FUNCTIONS")
    private String processWorkElementFunctions = StringUtils.EMPTY;


    /**
     * 过程特性
     */
    @Schema(title = "过程特性")
    @TableField("PROCESS_CHARACTERISTICS")
    private String processCharacteristics = StringUtils.EMPTY;


    /**
     * 过程特性规范
     */
    @Schema(title = "过程特性规范")
    @TableField("PROCESS_CHARACTERISTIC_SPECIFICATIONS")
    private String processCharacteristicSpecifications = StringUtils.EMPTY;


    /**
     * 失效后果(FE)
     */
    @Schema(title = "失效后果(FE)")
    @TableField("FAILURE_CONSEQUENCE")
    private String failureConsequence = StringUtils.EMPTY;


    /**
     * 严重度S(P)
     */
    @Schema(title = "严重度S(P)")
    @TableField("SEVERITY")
    private String severity = StringUtils.EMPTY;


    /**
     * 失效模式(FM)
     */
    @Schema(title = "失效模式(FM)")
    @TableField("FAILURE_MODE")
    private String failureMode = StringUtils.EMPTY;


    /**
     * 失效原因(FC)
     */
    @Schema(title = "失效原因(FC)")
    @TableField("REASON_FOR_FAILURE")
    private String reasonForFailure = StringUtils.EMPTY;


    /**
     * 预防控制(PC)
     */
    @Schema(title = "预防控制(PC)")
    @TableField("PREVENTION_CONTROL")
    private String preventionControl = StringUtils.EMPTY;


    /**
     * 发生度O(P)
     */
    @Schema(title = "发生度O(P)")
    @TableField("OCCURRENCE_DEGREE")
    private String occurrenceDegree = StringUtils.EMPTY;


    /**
     * 探测控制(DC)
     */
    @Schema(title = "探测控制(DC)")
    @TableField("DETECTION_CONTROL")
    private String detectionControl = StringUtils.EMPTY;


    /**
     * 探测度D(P)
     */
    @Schema(title = "探测度D(P)")
    @TableField("DETECTION_DEGREE")
    private String detectionDegree = StringUtils.EMPTY;


    /**
     * 优先级AP(P)
     */
    @Schema(title = "优先级AP(P)")
    @TableField("PRIORITY")
    private String priority = StringUtils.EMPTY;

    /**
     * 潜在特性分类
     */
    @Schema(title = "潜在特性分类")
    @TableField("POTENTIAL_FEATURE_CLASSIFICATION")
    private String potentialFeatureClassification = StringUtils.EMPTY;


    /**
     * 历史变更授权
     */
    @Schema(title = "历史变更授权")
    @TableField("HISTORICAL_CHANGE_AUTHORIZATION")
    private String historicalChangeAuthorization = StringUtils.EMPTY;


    /**
     * 产品特殊特性
     */
    @Schema(title = "产品特殊特性")
    @TableField("SPECIAL_PRODUCT_CHARACTERISTIC")
    private String specialProductCharacteristic = StringUtils.EMPTY;


    /**
     * 工艺特殊特性
     */
    @Schema(title = "工艺特殊特性")
    @TableField("PROCESS_SPECIAL_CHARACTERISTICS")
    private String processSpecialCharacteristics = StringUtils.EMPTY;


    /**
     * PTC
     */
    @Schema(title = "PTC")
    @TableField("PTC")
    private String ptc = StringUtils.EMPTY;


    /**
     * 过滤代码(可选)
     */
    @Schema(title = "过滤代码(可选)")
    @TableField("FILTER_CODE")
    private String filterCode = StringUtils.EMPTY;


    /**
     * 预防措施
     */
    @Schema(title = "预防措施")
    @TableField("PREVENTIVE_MEASURE")
    private String preventiveMeasure = StringUtils.EMPTY;


    /**
     * 探测措施
     */
    @Schema(title = "探测措施")
    @TableField("DETECTION_MEASURES")
    private String detectionMeasures = StringUtils.EMPTY;


    /**
     * 负责人
     */
    @Schema(title = "负责人")
    @TableField("NAME_OF_PERSON_CHARGE")
    private String nameOfPersonCharge = StringUtils.EMPTY;


    /**
     * 目标完成时间
     */
    @Schema(title = "目标完成时间")
    @TableField("TARGET_COMPLETION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date targetCompletionTime;


    /**
     * 完成时间
     */
    @Schema(title = "完成时间")
    @TableField("COMPLETION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date completionTime;


    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("PFMEA_STATE")
    private Integer pfmeaState = 0;


    /**
     * 采取的措施及证据
     */
    @Schema(title = "采取的措施及证据")
    @TableField("MEASURES_EVIDENCE_TAKEN")
    private String measuresEvidenceTaken = StringUtils.EMPTY;

    /**
     * 严重度S(D)
     */
    @Schema(title = "严重度S(D)")
    @TableField("SEVERITY_D")
    private String severityD = StringUtils.EMPTY;


    /**
     * 发生度O(D)
     */
    @Schema(title = "发生度O(D)")
    @TableField("OCCURRENCE_DEGREE_D")
    private String occurrenceDegreeD = StringUtils.EMPTY;

    /**
     * 探测度D(D)
     */
    @Schema(title = "探测度D(D)")
    @TableField("DETECTION_DEGREE_D")
    private String detectionDegreeD = StringUtils.EMPTY;;


    /**
     * 优先级AP(D)
     */
    @Schema(title = "优先级AP(D)")
    @TableField("PRIORITY_D")
    private String priorityD = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


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


}