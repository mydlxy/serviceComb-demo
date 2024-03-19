package com.ca.mfd.prc.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @author inkelink
 * @Description: AS车辆实际过点实体
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AS车辆实际过点")
@TableName("PRC_PPS_AS_AVI_POINT")
public class PpsAsAviPointEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_AS_AVI_POINT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;

//
//    /**
//     * 过点记录ID
//     */
//    @Schema(title = "过点记录ID")
//    @TableField("PRC_AVI_TRACKING_RECORD_ID")
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = JsonDeserializeDefault.class)
//    private Long prcAviTrackingRecordId = Constant.DEFAULT_ID;
//
//
//    /**
//     * 计划履历ID
//     */
//    @Schema(title = "计划履历ID")
//    @TableField("PRC_PPS_PLAN_AVI_ID")
//    @JsonSerialize(using = ToStringSerializer.class)
//    @JsonDeserialize(using = JsonDeserializeDefault.class)
//    private Long prcPpsPlanAviId = Constant.DEFAULT_ID;

    /**
     * 工厂组织代码
     */
    @Schema(title = "工厂组织代码")
    @TableField("ORG_CODE")
    private String orgCode = StringUtils.EMPTY;

    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;

    /**
     * 车辆VIN号
     */
    @Schema(title = "车辆VIN号")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * 订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件
     */
    @Schema(title = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * 报工数量（合格）
     */
    @Schema(title = "报工数量（合格）")
    @TableField("QUALIFIED_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer qualifiedCount = 1;

    /**
     * 报工数量（不合格）
     */
    @Schema(title = "报工数量（不合格）")
    @TableField("BAD_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer badCount = 0;

    /**
     * 触发时间
     */
    @Schema(title = "触发时间")
    @TableField("SCAN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date scanTime = new Date();

    /**
     * 触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线)
     */
    @Schema(title = "触发类型(1：正常通过;2：车辆SET OUT;3：车辆SET IN;X：车辆下线;S：车辆上线)")
    @TableField("SCAN_TYPE")
    private String scanType = StringUtils.EMPTY;

    /**
     * AS发送标识（0：未发送，1：已发送,2:发送失败,3:忽略）
     */
    @Schema(title = "AS发送标识（0：未发送，1：已发送,2:发送失败,3:忽略）")
    @TableField("AS_SEND_FLAG")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer asSendFlag = 0;

    /**
     * LMS发送标识（0：未发送，1：已发送,2:发送失败,3:忽略）
     */
    @Schema(title = "LMS发送标识（0：未发送，1：已发送,2:发送失败,3:忽略）")
    @TableField("LMS_SEND_FLAG")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer lmsSendFlag = 0;

    /**
     * 班次
     */
    @Schema(title = "班次")
    @TableField("ACTUAL_SHIFT")
    private String actualShift  = StringUtils.EMPTY;

//    /**
//     * 反馈类型
//     */
//    @Schema(title = "反馈类型")
//    @TableField("FREE_BACK_TYPE")
//    private String freeBackType  = StringUtils.EMPTY;


}