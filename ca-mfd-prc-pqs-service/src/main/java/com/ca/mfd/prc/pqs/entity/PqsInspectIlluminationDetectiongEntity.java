package com.ca.mfd.prc.pqs.entity;

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
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 冲压车间照度检测记录实体
 * @author bo.yang
 * @date 2024年03月14日
 * @变更说明 BY bo.yang At 2024年03月14日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "冲压车间照度检测记录")
@TableName("PRC_PQS_INSPECT_ILLUMINATION_DETECTIONG")
public class PqsInspectIlluminationDetectiongEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_INSPECT_ILLUMINATION_DETECTIONG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 生产线
     */
    @Schema(title = "生产线")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 检测区域
     */
    @Schema(title = "检测区域")
    @TableField("DETECTION_AREA")
    private String detectionArea = StringUtils.EMPTY;


    /**
     * 检测设备
     */
    @Schema(title = "检测设备")
    @TableField("DETECTION_EQUIPMENT")
    private String detectionEquipment = StringUtils.EMPTY;


    /**
     * 检测频次
     */
    @Schema(title = "检测频次")
    @TableField("DETECTION_FREQUENCY")
    private String detectionFrequency = StringUtils.EMPTY;


    /**
     * 照度标准
     */
    @Schema(title = "照度标准")
    @TableField("ILLUMINANCE_STANDARD")
    private String illuminanceStandard = StringUtils.EMPTY;


    /**
     * 测量值1-检测点1
     */
    @Schema(title = "测量值1-检测点1")
    @TableField("MEASURE_FIRST_DETECTION_POINTS_FIRST")
    private String measureFirstDetectionPointsFirst = StringUtils.EMPTY;


    /**
     * 测量值1-检测点2
     */
    @Schema(title = "测量值1-检测点2")
    @TableField("MEASURE_FIRST_DETECTION_POINTS_SECOND")
    private String measureFirstDetectionPointsSecond = StringUtils.EMPTY;


    /**
     * 测量值1-检测点3
     */
    @Schema(title = "测量值1-检测点3")
    @TableField("MEASURE_FIRST_DETECTION_POINTS_THIRD")
    private String measureFirstDetectionPointsThird = StringUtils.EMPTY;


    /**
     * 测量值1-检测点4
     */
    @Schema(title = "测量值1-检测点4")
    @TableField("MEASURE_FIRST_DETECTION_POINTS_FOURTH")
    private String measureFirstDetectionPointsFourth = StringUtils.EMPTY;


    /**
     * 测量值2-检测点1
     */
    @Schema(title = "测量值2-检测点1")
    @TableField("MEASURE_SECOND_DETECTION_POINTS_FIRST")
    private String measureSecondDetectionPointsFirst = StringUtils.EMPTY;


    /**
     * 测量值2-检测点2
     */
    @Schema(title = "测量值2-检测点2")
    @TableField("MEASURE_SECOND_DETECTION_POINTS_SECOND")
    private String measureSecondDetectionPointsSecond = StringUtils.EMPTY;


    /**
     * 测量值2-检测点3
     */
    @Schema(title = "测量值2-检测点3")
    @TableField("MEASURE_SECOND_DETECTION_POINTS_THIRD")
    private String measureSecondDetectionPointsThird = StringUtils.EMPTY;


    /**
     * 测量值2-检测点4
     */
    @Schema(title = "测量值2-检测点4")
    @TableField("MEASURE_SECOND_DETECTION_POINTS_FOURTH")
    private String measureSecondDetectionPointsFourth = StringUtils.EMPTY;


    /**
     * 测量值3-检测点1
     */
    @Schema(title = "测量值3-检测点1")
    @TableField("MEASURE_THIRD_DETECTION_POINTS_FIRST")
    private String measureThirdDetectionPointsFirst = StringUtils.EMPTY;


    /**
     * 测量值3-检测点2
     */
    @Schema(title = "测量值3-检测点2")
    @TableField("MEASURE_THIRD_DETECTION_POINTS_SECOND")
    private String measureThirdDetectionPointsSecond = StringUtils.EMPTY;


    /**
     * 测量值3-检测点3
     */
    @Schema(title = "测量值3-检测点3")
    @TableField("MEASURE_THIRD_DETECTION_POINTS_THIRD")
    private String measureThirdDetectionPointsThird = StringUtils.EMPTY;


    /**
     * 测量值3-检测点4
     */
    @Schema(title = "测量值3-检测点4")
    @TableField("MEASURE_THIRD_DETECTION_POINTS_FOURTH")
    private String measureThirdDetectionPointsFourth = StringUtils.EMPTY;


    /**
     * 测量值4-检测点1
     */
    @Schema(title = "测量值4-检测点1")
    @TableField("MEASURE_FOURTH_DETECTION_POINTS_FIRST")
    private String measureFourthDetectionPointsFirst = StringUtils.EMPTY;


    /**
     * 测量值4-检测点2
     */
    @Schema(title = "测量值4-检测点2")
    @TableField("MEASURE_FOURTH_DETECTION_POINTS_SECOND")
    private String measureFourthDetectionPointsSecond = StringUtils.EMPTY;


    /**
     * 测量值4-检测点3
     */
    @Schema(title = "测量值4-检测点3")
    @TableField("MEASURE_FOURTH_DETECTION_POINTS_THIRD")
    private String measureFourthDetectionPointsThird = StringUtils.EMPTY;


    /**
     * 测量值4-检测点4
     */
    @Schema(title = "测量值4-检测点4")
    @TableField("MEASURE_FOURTH_DETECTION_POINTS_FOURTH")
    private String measureFourthDetectionPointsFourth = StringUtils.EMPTY;


}