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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 超声波检测不合格焊点纠正计划实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "超声波检测不合格焊点纠正计划")
@TableName("PRC_PQS_INSPECT_CORR_PLAN_UNQU_WELDING_POINTS_ULTR_TESTING")
public class PqsInspectCorrPlanUnquWeldingPointsUltrTestingEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_CORR_PLAN_UNQU_WELDING_POINTS_ULTR_TESTING_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 分总成名称
     */
    @Schema(title = "分总成名称")
    @TableField("SUB_ASSEMBLY_NAME")
    private String subAssemblyName = StringUtils.EMPTY;


    /**
     * 焊点编号
     */
    @Schema(title = "焊点编号")
    @TableField("SOLDER_JOINT_NUMBER")
    private Integer solderJointNumber = 0;


    /**
     * 缺陷种类
     */
    @Schema(title = "缺陷种类")
    @TableField("DEFECT_TYPE")
    private String defectType = StringUtils.EMPTY;


    /**
     * 发生日期
     */
    @Schema(title = "发生日期")
    @TableField("OCCURRENCE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date occurrenceDate;


    /**
     * 改善措施内容
     */
    @Schema(title = "改善措施内容")
    @TableField("IMPROVEMENT_MEASURES_CONTENT")
    private String improvementMeasuresContent = StringUtils.EMPTY;


    /**
     * 改善日期
     */
    @Schema(title = "改善日期")
    @TableField("IMPROVEMENT_MEASURES_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date improvementMeasuresDate;


    /**
     * 改善责任人
     */
    @Schema(title = "改善责任人")
    @TableField("IMPROVEMENT_PERSON_RESPONSIBLE")
    private String improvementPersonResponsible = StringUtils.EMPTY;


    /**
     * 确认结果
     */
    @Schema(title = "确认结果")
    @TableField("CONFIRM_RESULTS")
    private String confirmResults = StringUtils.EMPTY;


    /**
     * 确认日期
     */
    @Schema(title = "确认日期")
    @TableField("CONFIRM_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date confirmDate;


    /**
     * 确认责任人
     */
    @Schema(title = "确认责任人")
    @TableField("CONFIRM_PERSON_RESPONSIBLE")
    private String confirmPersonResponsible = StringUtils.EMPTY;


}