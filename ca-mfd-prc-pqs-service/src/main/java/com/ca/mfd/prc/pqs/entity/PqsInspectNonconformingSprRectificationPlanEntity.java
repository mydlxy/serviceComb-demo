package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 不合格SPR整改计划表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "不合格SPR整改计划表")
@TableName("PRC_PQS_INSPECT_NONCONFORMING_SPR_RECTIFICATION_PLAN")
public class PqsInspectNonconformingSprRectificationPlanEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableField("PRC_PQS_MONITOR_NONCONFORMING_SPR_RECTIFICATION_PLAN_ID")
    private String id = StringUtils.EMPTY;


    /**
     * 不合格铆点编号
     */
    @Schema(title = "不合格铆点编号")
    @TableField("UNQUALIFIED_RIVET_POINT_NO")
    private String unqualifiedRivetPointNo = StringUtils.EMPTY;


    /**
     * 铆点属性（自制/外协）
     */
    @Schema(title = "铆点属性（自制/外协）")
    @TableField("SOURCE")
    private String source = StringUtils.EMPTY;


    /**
     * 不合格铆点描述
     */
    @Schema(title = "不合格铆点描述")
    @TableField("DESCRIPTION_UNQUALIFIED_RIVETING_POINTS")
    private String descriptionUnqualifiedRivetingPoints = StringUtils.EMPTY;


    /**
     * 不合格铆点发生工位
     */
    @Schema(title = "不合格铆点发生工位")
    @TableField("STATION_UNQUALIFIED_RIVETING_POINTS_OCCUR")
    private String stationUnqualifiedRivetingPointsOccur = StringUtils.EMPTY;


    /**
     * 焊钳编号/机器人编号
     */
    @Schema(title = "焊钳编号/机器人编号")
    @TableField("WELDING_PLIERS_NO")
    private String weldingPliersNo = StringUtils.EMPTY;


    /**
     * 产生原因
     */
    @Schema(title = "产生原因")
    @TableField("CAUSE")
    private String cause = StringUtils.EMPTY;


    /**
     * 解决措施
     */
    @Schema(title = "解决措施")
    @TableField("SOLUTION_MEASURES")
    private String solutionMeasures = StringUtils.EMPTY;


    /**
     * 整改责任单位
     */
    @Schema(title = "整改责任单位")
    @TableField("UNIT_RESPONSIBLE_RECTIFICATION")
    private String unitResponsibleRectification = StringUtils.EMPTY;


    /**
     * 整改牵头人
     */
    @Schema(title = "整改牵头人")
    @TableField("RECTIFICATION_LEADER")
    private String rectificationLeader = StringUtils.EMPTY;


    /**
     * 整改前参数
     */
    @Schema(title = "整改前参数")
    @TableField("PARAMETERS_BEFORE_RECTIFICATION")
    private String parametersBeforeRectification = StringUtils.EMPTY;


    /**
     * 整改后参数
     */
    @Schema(title = "整改后参数")
    @TableField("PARAMETERS_AFTER_RECTIFICATION")
    private String parametersAfterRectification = StringUtils.EMPTY;


    /**
     * 验证时间
     */
    @Schema(title = "验证时间")
    @TableField("VERIFICATION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date verificationTime;


    /**
     * 验证人员
     */
    @Schema(title = "验证人员")
    @TableField("VERIFICATION_PERSONNEL")
    private String verificationPersonnel = StringUtils.EMPTY;


    /**
     * 不合格铆点实物整改效果
     */
    @Schema(title = "不合格铆点实物整改效果")
    @TableField("RECTIFICATION_EFFECT_SUBSTANDARD_RIVETING_POINTS")
    private String rectificationEffectSubstandardRivetingPoints = StringUtils.EMPTY;


}