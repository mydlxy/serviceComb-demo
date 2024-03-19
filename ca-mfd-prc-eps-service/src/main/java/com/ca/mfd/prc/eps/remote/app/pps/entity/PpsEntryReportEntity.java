package com.ca.mfd.prc.eps.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink ${email}
 * @Description: 报工单表
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报工单表")
@TableName("PRC_PPS_ENTRY_REPORT")
public class PpsEntryReportEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ENTRY_REPORT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 报工单号
     */
    @Schema(title = "报工单号")
    @TableField("ENTRY_REPORT_NO")
    private String entryReportNo = StringUtils.EMPTY;

    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    @TableField("PRODUCT_CODE")
    private String productCode = StringUtils.EMPTY;

    /**
     * 计划物料描述
     */
    @Schema(title = "计划物料描述")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;

    /**
     * 生产订单号
     */
    @Schema(title = "生产订单号")
    @TableField("ORDER_NO")
    private String orderNo = StringUtils.EMPTY;

    /**
     * 生产工单号
     */
    @Schema(title = "生产工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;

    /**
     * 日历班次ID
     */
    @Schema(title = "日历班次ID")
    @TableField("PRC_PM_SHC_CALENDAR_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmShcCalendarId = Constant.DEFAULT_ID;

    /**
     * 报工类型（1 合格  2 待检 3 不合格 4 待质检 5 预报工）
     */
    @Schema(title = "报工类型（1 合格  2 待检 3 不合格 4 待质检 5 预报工）")
    @TableField("REPORT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer reportType = 1;

    /**
     * 报工工单类型（1 生产 2 返修）
     */
    @Schema(title = "报工工单类型（1 生产 2 返修）")
    @TableField("ENTRY_REPORT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer entryReportType = 1;


    /**
     * 报工车间
     */
    @Schema(title = "报工车间")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 报工区域（线体）
     */
    @Schema(title = "报工区域（线体）")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 报工机台/工台（岗位）
     */
    @Schema(title = "报工机台/工台（岗位）")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

    /**
     * 报工数量
     */
    @Schema(title = "报工数量")
    @TableField("ENTRY_REPORT_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer entryReportCount = 0;

    /**
     * 是否过点 IsRevocation
     */
    @Schema(title = "是否过点")
    @TableField("IS_PASS_AVI")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isPassAvi = false;

    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;

//    /**
//     * 是否允许撤销
//     */
//    @Schema(title = "是否允许撤销")
//    @TableField(exist = false)
//    private Boolean isRevocation = true;

}
