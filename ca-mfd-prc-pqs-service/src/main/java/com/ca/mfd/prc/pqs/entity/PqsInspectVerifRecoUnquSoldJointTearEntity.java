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
 * @Description: 撕裂不合格焊点验证记录表实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "撕裂不合格焊点验证记录表")
@TableName("PRC_PQS_INSPECT_VERIF_RECO_UNQU_SOLD_JOINT_TEAR")
public class PqsInspectVerifRecoUnquSoldJointTearEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_VERIF_RECO_UNQU_SOLD_JOINT_TEAR_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 不合格焊点发生工位
     */
    @Schema(title = "不合格焊点发生工位")
    @TableField("DEFE_SOLDER_JOINT_OCCU_STATION")
    private String defeSolderJointOccuStation = StringUtils.EMPTY;


    /**
     * 自制/外协
     */
    @Schema(title = "自制/外协")
    @TableField("SOURCE")
    private String source = StringUtils.EMPTY;


    /**
     * 零(部件)名称
     */
    @Schema(title = "零(部件)名称")
    @TableField("METERINAL_NAME")
    private String meterinalName = StringUtils.EMPTY;


    /**
     * 焊钳编号/机器人编号
     */
    @Schema(title = "焊钳编号/机器人编号")
    @TableField("WELDING_PLIERS_NUMBER")
    private String weldingPliersNumber = StringUtils.EMPTY;


    /**
     * 焊点编号
     */
    @Schema(title = "焊点编号")
    @TableField("SOLDER_JOINT_NUMBER")
    private String solderJointNumber = StringUtils.EMPTY;


    /**
     * 缺陷类型描述
     */
    @Schema(title = "缺陷类型描述")
    @TableField("DEFECT_TYPE_DESCRIPTION")
    private String defectTypeDescription = StringUtils.EMPTY;


    /**
     * 班组
     */
    @Schema(title = "班组")
    @TableField("TEAMS")
    private String teams = StringUtils.EMPTY;


    /**
     * 检查结果记录
     */
    @Schema(title = "检查结果记录")
    @TableField("RECORD_INSPECTION_RESULTS")
    private String recordInspectionResults = StringUtils.EMPTY;


    /**
     * 相同焊钳其他焊点验证
     */
    @Schema(title = "相同焊钳其他焊点验证")
    @TableField("VERI_OTHER_WELD_POINTS_WITH_SAME_WELD_PLIERS")
    private String veriOtherWeldPointsWithSameWeldPliers = StringUtils.EMPTY;


    /**
     * 记录时间
     */
    @Schema(title = "记录时间")
    @TableField("RECORD_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date recordDate;


    /**
     * 记录人员
     */
    @Schema(title = "记录人员")
    @TableField("RECORDER")
    private String recorder = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}