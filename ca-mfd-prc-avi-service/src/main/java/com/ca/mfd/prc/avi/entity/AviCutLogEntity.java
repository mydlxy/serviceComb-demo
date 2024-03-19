package com.ca.mfd.prc.avi.entity;

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

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: AVICUT记录表
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AVICUT记录表")
//@TableName("AVI_CUT_LOG")
@TableName("PRC_AVI_CUT_LOG")
public class AviCutLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_CUT_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 切入切除类型0-切入 1-切出
     */
    //    @Schema(title = "切入切除类型0-切入1-切出")
    //    @TableField("CUT_TYPE")
    //    private Integer cutType = 0;
    @Schema(title = "切入切除类型0-切入 1-切出")
    @TableField("CUT_TYPE")
    private Integer cutType = 0;

    /**
     * 车辆识别码
     */
    //    @Schema(title = "车辆识别码")
    //    @TableField("TPS_CODE")
    //    private String tpsCode = StringUtils.EMPTY;
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 前车辆识别码
     */
    //    @Schema(title = "前车辆识别码")
    //    @TableField("PRV_TPS_CODE")
    //    private String prvTpsCode = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    //    @Schema(title = "车间代码")
    //    @TableField("PM_AREA_CODE")
    //    private String pmAreaCode = StringUtils.EMPTY;
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 切出、切入SN
     */
    @Schema(title = "切出、切入SN")
    @TableField("CUT_SN")
    private String cutSn = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    //    @Schema(title = "线体代码")
    //    @TableField("PM_WORK_CENTER_CODE")
    //    private String pmWorkCenterCode = StringUtils.EMPTY;
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    //    @Schema(title = "AVI名称")
    //    @TableField("PM_AVI_NAME")
    //    private String pmAviName = StringUtils.EMPTY;
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * 点位代码
     */
    //    @Schema(title = "点位")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;

    /**
     * 点位代码
     */
    //    @Schema(title = "点位代码")
    //    @TableField("PM_AVI_CODE")
    //    private String pmAviCode = StringUtils.EMPTY;
    @Schema(title = "点位代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * 原因
     */
    //    @Schema(title = "原因")
    //    @TableField("REASON")
    //    private String reason = StringUtils.EMPTY;
    @Schema(title = "原因")
    @TableField("REASON")
    private String reason = StringUtils.EMPTY;

    /**
     * 备注
     */
    //    @Schema(title = "备注")
    //    @TableField("REMARK")
    //    private String remark = StringUtils.EMPTY;
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 完成时间
     */
    //    @Schema(title = "完成时间")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    //    @JsonDeserialize(using = JsonDeserializeDate.class)
    //    @TableField("FINISH_DT")
    //    private Date finishDt;
    @Schema(title = "完成时间")
    @TableField("FINISH_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date finishDt;

    /**
     * 状态
     */
    //    @Schema(title = "状态")
    //    @TableField("STATUS")
    //    private Integer status = 0;
    @Schema(title = "状态")
    @TableField("STATUS")
    private Integer status = 0;

}
