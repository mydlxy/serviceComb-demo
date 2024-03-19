package com.ca.mfd.prc.eps.remote.app.pqs.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author inkelink
 * @Description: 产品缺陷记录实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "产品缺陷记录")
@TableName("PRC_PQS_PRODUCT_DEFECT_ANOMALY")
public class PqsProductDefectAnomalyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_PRODUCT_DEFECT_ANOMALY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


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


    /**
     * 关联操作ID
     */
    @Schema(title = "关联操作ID")
    @TableField("PRC_EPS_PRODUCT_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcEpsProductWoId = Constant.DEFAULT_ID;


    /**
     * 来源;0、正常录入 1、百格图 2、QG检项项 3、问题排查
     */
    @Schema(title = "来源;0、正常录入 1、百格图 2、QG检项项 3、问题排查")
    @TableField("SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;


    /**
     * 状态;1.激活 2 已修复 3未发现 4 合格 5不合格
     */
    @Schema(title = "状态;1.激活 2 已修复 3未发现 4 合格 5不合格")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


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
     * 班次
     */
    @Schema(title = "班次")
    @TableField("PRC_PPS_SHC_SHIFT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPpsShcShiftId = Constant.DEFAULT_ID;


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
     * QG工位代码
     */
    @Schema(title = "QG工位代码")
    @TableField("QG_WORKSTATION_CODE")
    private String qgWorkstationCode = StringUtils.EMPTY;


    /**
     * QG工位名称
     */
    @Schema(title = "QG工位名称")
    @TableField("QG_WORKSTATION_NAME")
    private String qgWorkstationName = StringUtils.EMPTY;


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
    private Date activateTime;


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
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

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


    /**
     * JSON值用于存储百格图、图片检查项
     */
    @Schema(title = "JSON值用于存储百格图、图片检查项")
    @TableField("JSON_DATA")
    private String jsonData = StringUtils.EMPTY;

    @TableField(exist = false)
    private String barcode;
    @TableField(exist = false)
    private String sequenceNo;
}