package com.ca.mfd.prc.pps.entity;

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
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 报工单-零部件实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "报工单-零部件")
@TableName("PRC_PPS_ENTRY_REPORT_PARTS")
public class PpsEntryReportPartsEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ENTRY_REPORT_PARTS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 报工单号
     */
    @Schema(title = "报工单号")
    @TableField("ENTRY_REPORT_NO")
    private String entryReportNo = StringUtils.EMPTY;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 生产工单号
     */
    @Schema(title = "生产工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 工序编码
     */
    @Schema(title = "工序编码")
    @TableField("PROCESS_CODE")
    private String processCode = StringUtils.EMPTY;


    /**
     * 工序名称
     */
    @Schema(title = "工序名称")
    @TableField("PROCESS_NAME")
    private String processName = StringUtils.EMPTY;


    /**
     * 日历班次ID
     */
    @Schema(title = "日历班次ID")
    @TableField("PRC_PM_SHC_CALENDAR_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcPmShcCalendarId = Constant.DEFAULT_ID;


    /**
     * 报工类型;1 合格  2 待检 3 不合格 4 待质检  5质检合格（传递给as后） 6质检不合格（传递给as后） 7 报废（报废单）
     */
    @Schema(title = "报工类型;1 合格  2 待检 3 不合格 4 待质检  5质检合格（传递给as后） 6质检不合格（传递给as后） 7 报废（报废单）")
    @TableField("REPORT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer reportType = 0;


    /**
     * 报工工单类型（1 生产 2 返修）
     */
    @Schema(title = "报工工单类型（1 生产 2 返修）")
    @TableField("ENTRY_REPORT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer entryReportType = 0;


    /**
     * 订单大类;3：压铸  4：机加   5：冲压  6：电池上盖
     */
    @Schema(title = "订单大类;3：压铸  4：机加   5：冲压  6：电池上盖")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;


    /**
     * 报工车间
     */
    @Schema(title = "报工车间")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;


    /**
     * 报工线体
     */
    @Schema(title = "报工线体")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;


    /**
     * 报工工位
     */
    @Schema(title = "报工工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 报工工位
     */
    @Schema(title = "报工工位")
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
     * 是否过点
     */
    @Schema(title = "是否过点")
    @TableField("IS_PASS_AVI")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isPassAvi = false;


    /**
     * 是否绑定载具
     */
    @Schema(title = "是否绑定载具")
    @TableField("IS_BIND_CARRIER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isBindCarrier = false;


    /**
     * 载具条码
     */
    @Schema(title = "载具条码")
    @TableField("CARRIER_CODE")
    private String carrierCode = StringUtils.EMPTY;


}