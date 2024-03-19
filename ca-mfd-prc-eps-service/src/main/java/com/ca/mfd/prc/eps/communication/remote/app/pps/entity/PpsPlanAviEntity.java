package com.ca.mfd.prc.eps.communication.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @author eric.zhou
 * @Description: 计划履历;实体
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "计划履历")
@TableName("PRC_PPS_PLAN_AVI")
public class PpsPlanAviEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PLAN_AVI_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * AVI编码
     */
    @Schema(title = "AVI编码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;


    /**
     * AVI类型
     */
    @Schema(title = "AVI类型")
    @TableField("AVI_TYPE")
    private String aviType = StringUtils.EMPTY;


    /**
     * 班次
     */
    @Schema(title = "班次")
    @TableField("SHIFT_CODE")
    private String shiftCode = StringUtils.EMPTY;


    /**
     * 工作日
     */
    @Schema(title = "工作日")
    @TableField("WORK_DAY")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date workDay;


    /**
     * 过点时间
     */
    @Schema(title = "过点时间")
    @TableField("PASS_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date passDt;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("SEQUENCE_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sequenceNo = 0;


    /**
     * 计划上线时间
     */
    @Schema(title = "计划上线时间")
    @TableField("BEGIN_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date beginDt;


    /**
     * 计划下线时间
     */
    @Schema(title = "计划下线时间")
    @TableField("END_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endDt;


    /**
     * 开工AVI站点
     */
    @Schema(title = "开工AVI站点")
    @TableField("START_AVI_CODE")
    private String startAviCode = StringUtils.EMPTY;


    /**
     * 结束AVI站点
     */
    @Schema(title = "结束AVI站点")
    @TableField("END_AVI_CODE")
    private String endAviCode = StringUtils.EMPTY;

}