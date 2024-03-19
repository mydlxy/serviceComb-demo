package com.ca.mfd.prc.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author inkelink
 * @Description: AUDIT缺陷记录实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AUDIT缺陷记录")
@TableName("PRC_PQS_AUDIT_ENTRY_DEFECT_ANOMALY")
public class PqsAuditEntryDefectAnomalyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_AUDIT_ENTRY_DEFECT_ANOMALY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 主键
     */
    @Schema(title = "班次ID")
    @TableField(value = "PRC_PPS_SHC_SHIFT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcPpsShcShiftId = Constant.DEFAULT_ID;

    /**
     * Audit评审工单号
     */
    @Schema(title = "Audit评审工单号")
    @TableField("AUDIT_RECORD_NO")
    private String auditRecordNo = StringUtils.EMPTY;


    /**
     * 类别:1、整车  5 冲压(零部件)
     */
    @Schema(title = "类别:1、整车  5 冲压(零部件)")
    @TableField("CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer category = 1;


    /**
     * 产品唯一编码
     */
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 图片
     */
    @Schema(title = "图片")
    @TableField("IMG")
    private String img = StringUtils.EMPTY;


    /**
     * 组合缺陷代码
     */
    @Schema(title = "组合缺陷代码")
    @TableField("DEFECT_ANOMALY_CODE")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC缺陷
     */
    @Schema(title = "ICC缺陷")
    @TableField("DEFECT_ANOMALY_DESCRIPTION")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 缺陷等级代码
     */
    @Schema(title = "缺陷等级代码")
    @TableField("GRADE_CODE")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 缺陷等级描述
     */
    @Schema(title = "缺陷等级描述")
    @TableField("GRADE_NAME")
    private String gradeName = StringUtils.EMPTY;

    @Schema(title = "评价模式代码")
    @TableField("EVALUATION_MODE_CODE")
    private String evaluationModeCode = StringUtils.EMPTY;
    @Schema(title = "评价模式名称")
    @TableField("EVALUATION_MODE_NAME")
    private String evaluationModeName = StringUtils.EMPTY;


    /**
     * 状态;1.激活 2 已修复 3未发现 4 合格 5不合格
     */
    @Schema(title = "状态;1.激活 2 已修复 3未发现 4 合格 5不合格")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;
    /**
     * 扣分
     */
    @Schema(title = "扣分")
    @TableField("SCORE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer score = 0;


    /**
     * 工厂
     */
    @Schema(title = "工厂")
    @TableField("ORGANIZATION_CODE")
    private String organizationCode = StringUtils.EMPTY;


    /**
     * 车间
     */
    @Schema(title = "车间")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;


    /**
     * 是否返修
     */
    @Schema(title = "是否返修")
    @TableField("IS_NEED_REPAIR")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isNeedRepair = false;
    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 责任部门代码
     */
    @Schema(title = "责任部门代码")
    @TableField("RESPONSIBLE_DEPT_CODE")
    private String responsibleDeptCode = StringUtils.EMPTY;


    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    @TableField("RESPONSIBLE_DEPT_NAME")
    private String responsibleDeptName = StringUtils.EMPTY;
    /**
     * 责任班组
     */
    @Schema(title = "责任班组")
    @TableField("RESPONSIBLE_TEAM_NO")
    private String responsibleTeamNo = StringUtils.EMPTY;


    /**
     * 激活人
     */
    @Schema(title = "激活人")
    @TableField("ACTIVATE_USER")
    private String activateUser = StringUtils.EMPTY;


    /**
     * 激活时间
     */
    @Schema(title = "激活时间")
    @TableField("ACTIVATE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date activateTime = new Date();


    /**
     * 修复人
     */
    @Schema(title = "修复人")
    @TableField("REPAIR_USER")
    private String repairUser = StringUtils.EMPTY;


    /**
     * 返修方式
     */
    @Schema(title = "返修方式")
    @TableField("REPAIR_WAY")
    private String repairWay = StringUtils.EMPTY;


    /**
     * 修复时间
     */
    @Schema(title = "修复时间")
    @TableField("REPAIR_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date repairTime;


    /**
     * 花费时长
     */
    @Schema(title = "花费时长")
    @TableField("REPAIR_SPEND_TIME")
    private BigDecimal repairSpendTime = BigDecimal.valueOf(0);


    /**
     * 修复备注
     */
    @Schema(title = "修复备注")
    @TableField("REPAIR_REMARK")
    private String repairRemark = StringUtils.EMPTY;


    /**
     * 复验人
     */
    @Schema(title = "复验人")
    @TableField("RECHECK_USER")
    private String recheckUser = StringUtils.EMPTY;


    /**
     * 复检时间
     */
    @Schema(title = "复检时间")
    @TableField("RECHECK_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date recheckTime;

    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = Strings.EMPTY;

    @Schema(title = "jsondata")
    @TableField("JSON_DATA")
    private String jsonData = Strings.EMPTY;

    /**
     * 是否翻库
     */
    @Schema(title = "是否翻库")
    @TableField("IS_FLIPPING_LIBRARY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isFlippingLibrary = false;

    /**
     * 添加联合查询需要的字段
     */
    @Schema(title = "车型")
    @TableField(exist = false)
    private String model = Strings.EMPTY;



}