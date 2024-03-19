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
 * @Description: 单个设备报警数据沉淀
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "单个设备报警数据沉淀")
@TableName("PRC_PMC_ALARM_EQUIPMNT_DETAIL")
public class PmcAlarmEquipmntDetailEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PMC_ALARM_EQUIPMNT_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 位置
     */
    @Schema(title = "位置")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;

    /**
     * 报警等级
     */
    @Schema(title = "报警等级")
    @TableField("ALARM_LEVEL")
    private Integer alarmLevel;

    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("BEGIN_DT")
    private Date beginDt;

    /**
     * 结束时间
     */
    @Schema(title = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("END_DT")
    private Date endDt;

    /**
     * 报警代码
     */
    @Schema(title = "报警代码")
    @TableField("ALARM_EQUIPMNT_DETAIL_CODE")
    private String alarmEquipmntDetailCode = StringUtils.EMPTY;

    /**
     * 报警描述
     */
    @Schema(title = "报警描述")
    @TableField("ALARM_EQUIPMNT_DETAIL_DESCRIPTION")
    private String alarmEquipmntDetailDescription = StringUtils.EMPTY;

    /**
     * DURATION
     */
    @Schema(title = "DURATION")
    @TableField("DURATION")
    private Integer duration = 0;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 是否沉淀
     */
    @Schema(title = "是否沉淀")
    @TableField("IS_DATA_DOWN")
    private Boolean isDataDown = false;

    /**
     * 1 设备报警  2ANDON报警
     */
    @Schema(title = "1设备报警2ANDON报警")
    @TableField("ALARM_EQUIPMNT_DETAIL_TYPE")
    private Integer alarmEquipmntDetailType = 1;

    /**
     * 是否填报, 小于60s标记为已经填报
     */
    @Schema(title = "是否填报,小于60s标记为已经填报")
    @TableField("IS_FILL")
    private Boolean isFill = false;

    /**
     * 停线代码
     */
    @Schema(title = "停线代码")
    @TableField("STOP_CODE")
    private String stopCode = StringUtils.EMPTY;

    /**
     * 停线代码名称
     */
    @Schema(title = "停线代码名称")
    @TableField("STOP_NAME")
    private String stopName = StringUtils.EMPTY;

    /**
     * 停线代码分类
     */
    @Schema(title = "停线代码分类")
    @TableField("STOP_TYPE")
    private String stopType = StringUtils.EMPTY;

    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    @TableField("STOP_DEPARTMENT")
    private String stopDepartment = StringUtils.EMPTY;

    /**
     * 停线原因分类
     */
    @Schema(title = "停线原因分类")
    @TableField("STOP_CAUSE_TYPE")
    private String stopCauseType = StringUtils.EMPTY;

    /**
     * 停线代码描述
     */
    @Schema(title = "停线代码描述")
    @TableField("STOP_CODE_DESC")
    private String stopCodeDesc = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 岗位名称，设备报警的话，这里是空字符串
     */
    @Schema(title = "岗位名称，设备报警的话，这里是空字符串")
    @TableField("WORKPLACE_NAME")
    private String workplaceName = StringUtils.EMPTY;

    /**
     * 填报人工号
     */
    @Schema(title = "填报人工号")
    @TableField("FILL_USER_NO")
    private String fillUserNo = StringUtils.EMPTY;

    /**
     * 填报人昵称
     */
    @Schema(title = "填报人昵称")
    @TableField("FILL_USER_NICK")
    private String fillUserNick = StringUtils.EMPTY;

    /**
     * 停线分类代码
     */
    @Schema(title = "停线分类代码")
    @TableField("STOP_TYPE_CODE")
    private String stopTypeCode = StringUtils.EMPTY;

}
