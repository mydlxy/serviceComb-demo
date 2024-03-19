package com.ca.mfd.prc.pmc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 停线记录
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "停线记录")
@TableName("PRC_PMC_ALARM_AREA_STOP_RECORD_REASON")
public class PmcAlarmAreaStopRecordReasonEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PMC_ALARM_AREA_STOP_RECORD_REASON_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    private Integer status = 0;

    /**
     * 车间编号
     */
    @Schema(title = "车间编号")
    @TableField("SHOP_CODE")
    private String shopCode = StringUtils.EMPTY;

    /**
     * 所属区域
     */
    @Schema(title = "所属区域")
    @TableField("POSITIONS")
    private String positions = StringUtils.EMPTY;

    /**
     * 原因分析
     */
    @Schema(title = "原因分析")
    @TableField("REASON_ANALYSIS")
    private String reasonAnalysis = StringUtils.EMPTY;

    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("START_DT")
    private Date startDt;

    /**
     * 结束时间
     */
    @Schema(title = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("END_DT")
    private Date endDt;

    /**
     * 持续时间
     */
    @Schema(title = "持续时间")
    @TableField("DURATION")
    private Integer duration = 0;

    /**
     * 短期措施
     */
    @Schema(title = "短期措施")
    @TableField("SHORT_TERM_MEASURE")
    private String shortTermMeasure = StringUtils.EMPTY;

    /**
     * 长期措施
     */
    @Schema(title = "长期措施")
    @TableField("LONG_TERM_MEASURE")
    private String longTermMeasure = StringUtils.EMPTY;

    /**
     * 短期措施审核状态
     */
    @Schema(title = "短期措施审核状态")
    @TableField("SHORT_MEASURE_AUDIT_STATUS")
    private Integer shortMeasureAuditStatus = 0;

    /**
     * 长期措施审核状态
     */
    @Schema(title = "长期措施审核状态")
    @TableField("LONG_MEASURE_AUDIT_STATUS")
    private Integer longMeasureAuditStatus = 0;

    /**
     * 审核人ID
     */
    @Schema(title = "审核人ID")
    @TableField("AUDIT_PRC_PMC_USER_ID")
    private Long auditPrcPmcUserId = 0L;

    /**
     * 审核人
     */
    @Schema(title = "审核人")
    @TableField("AUDIT_PRC_PMC_USER_NAME")
    private String auditPrcPmcUserName = StringUtils.EMPTY;

    /**
     * 审核时间
     */
    @Schema(title = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("AUDIT_TIME")
    private Date auditTime;

    /**
     * 区域多设备配置外键
     */
    @Schema(title = "区域多设备配置外键")
    @TableField("PRC_PMC_ALARM_AREA_STOP_SET_ID")
    private Long prcPmcAlarmAreaStopSetId;

    /**
     * 配置名称
     */
    @Schema(title = "配置名称")
    @TableField("ALARM_AREA_STOP_SET_NAME")
    private String alarmAreaStopSetName;

    /**
     * 发送时间
     */
    @Schema(title = "发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("SEND_DT")
    private Date sendDt;

    /**
     * 发送次数
     */
    @Schema(title = "发送次数")
    @TableField("SEND_TIMES")
    private Integer sendTimes = 0;

    /**
     * 停线代码分类
     */
    @Schema(title = "停线代码分类")
    @TableField("STOP_TYPE")
    private String stopType = StringUtils.EMPTY;

    /**
     * 停线分类代码
     */
    @Schema(title = "停线分类代码")
    @TableField("STOP_TYPE_CODE")
    private String stopTypeCode = StringUtils.EMPTY;

    /**
     * 停线原因分类
     */
    @Schema(title = "停线原因分类")
    @TableField("STOP_CAUSE_TYPE")
    private String stopCauseType = StringUtils.EMPTY;

    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    @TableField("STOP_DEPARTMENT")
    private String stopDepartment = StringUtils.EMPTY;

    /**
     * 停线代码名称
     */
    @Schema(title = "停线代码名称")
    @TableField("STOP_NAME")
    private String stopName = StringUtils.EMPTY;

    /**
     * 停线代码
     */
    @Schema(title = "停线代码")
    @TableField("STOP_CODE")
    private String stopCode = StringUtils.EMPTY;

    /**
     * 停线代码描述
     */
    @Schema(title = "停线代码描述")
    @TableField("STOP_CODE_DESC")
    private String stopCodeDesc = StringUtils.EMPTY;

    /**
     * 时间
     */
    @TableField(exist = false)
    private String time = StringUtils.EMPTY;

}
