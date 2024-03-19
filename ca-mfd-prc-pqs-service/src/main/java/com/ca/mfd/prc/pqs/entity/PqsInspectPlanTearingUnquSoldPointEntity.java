package com.ca.mfd.prc.pqs.entity;

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
 * @Description: 撕裂不合格焊点整改计划表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "撕裂不合格焊点整改计划表")
@TableName("PRC_PQS_INSPECT_PLAN_TEARING_UNQU_SOLD_POINT")
public class PqsInspectPlanTearingUnquSoldPointEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_PLAN_TEARING_UNQU_SOLD_POINT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 不合格焊点编号
     */
    @Schema(title = "不合格焊点编号")
    @TableField("UNQUALIFIED_SOLDER_JOINT_NUMBER")
    private String unqualifiedSolderJointNumber = StringUtils.EMPTY;


    /**
     * 焊点属性（自制/外协）
     */
    @Schema(title = "焊点属性（自制/外协）")
    @TableField("SOLDER_JOINT_PROPERTIES")
    private String solderJointProperties = StringUtils.EMPTY;


    /**
     * 焊点问题实际图
     */
    @Schema(title = "焊点问题实际图")
    @TableField("SOLDER_JOINT_ISSUES_ACTUAL_DIAGRAM")
    private String solderJointIssuesActualDiagram = StringUtils.EMPTY;


    /**
     * 不合格焊点描述
     */
    @Schema(title = "不合格焊点描述")
    @TableField("DESCRIPTION_UNQUALIFIED_SOLDER_JOINTS")
    private String descriptionUnqualifiedSolderJoints = StringUtils.EMPTY;


    /**
     * 不合格焊点发生工位
     */
    @Schema(title = "不合格焊点发生工位")
    @TableField("DEFECTIVE_SOLDER_JOINT_OCCURRENCE_STATION")
    private String defectiveSolderJointOccurrenceStation = StringUtils.EMPTY;


    /**
     * 焊钳编号/机器人编号
     */
    @Schema(title = "焊钳编号/机器人编号")
    @TableField("WELDING_PLIERS_NUMBER")
    private String weldingPliersNumber = StringUtils.EMPTY;


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
     * 整改责任人
     */
    @Schema(title = "整改责任人")
    @TableField("PERSON_RESPONSIBLE_RECTIFICATION")
    private String personResponsibleRectification = StringUtils.EMPTY;


    /**
     * 整改前参数
     */
    @Schema(title = "整改前参数")
    @TableField("BEFORE_RECTIFICATION_PARAMETER")
    private String beforeRectificationParameter = StringUtils.EMPTY;


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
    private String verificationTime = StringUtils.EMPTY;


    /**
     * 验证人员
     */
    @Schema(title = "验证人员")
    @TableField("VERIFICATION_PERSONNEL")
    private String verificationPersonnel = StringUtils.EMPTY;


    /**
     * 验证状态
     */
    @Schema(title = "验证状态")
    @TableField("VALIDATING")
    private String validating = StringUtils.EMPTY;


    /**
     * 相同焊钳其他焊点验证
     */
    @Schema(title = "相同焊钳其他焊点验证")
    @TableField("VERI_OTHER_WELD_POINTS_WITH_SAME_WELD_PLIERS")
    private String veriOtherWeldPointsWithSameWeldPliers = StringUtils.EMPTY;


    /**
     * 不合格焊点实物整改效果
     */
    @Schema(title = "不合格焊点实物整改效果")
    @TableField("RECT_EFFE_SUBS_SOLD_JOINTS")
    private String rectEffeSubsSoldJoints = StringUtils.EMPTY;


    /**
     * 相同焊钳其他焊点验证效果
     */
    @Schema(title = "相同焊钳其他焊点验证效果")
    @TableField("VERI_EFFE_OTHER_WELD_POINTS_WITH_SAME_WELD_PLIERS_IMG")
    private String veriEffeOtherWeldPointsWithSameWeldPliersImg = StringUtils.EMPTY;


}