package com.ca.mfd.prc.eps.entity;

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
 * @Description: 载具日志实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "载具日志")
@TableName("PRC_EPS_CARRIER_LOG")
public class EpsCarrierLogEntity extends BaseEntity {

    /**
     * 主键id
     */
    @Schema(title = "主键id")
    @TableId(value = "PRC_EPS_CARRIER_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 载具ID
     */
    @Schema(title = "载具ID")
    @TableField("PRC_EPS_CARRIER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcEpsCarrierId = Constant.DEFAULT_ID;


    /**
     * 载具条码
     */
    @Schema(title = "载具条码")
    @TableField("CARRIER_BARCODE")
    private String carrierBarcode = StringUtils.EMPTY;


    /**
     * 批次号
     */
    @Schema(title = "批次号")
    @TableField("BATCH_NUMBER")
    private String batchNumber = StringUtils.EMPTY;


    /**
     * 载具装载物料号
     */
    @Schema(title = "载具装载物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 载具装载物料描述
     */
    @Schema(title = "载具装载物料描述")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


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
     * 单件条码
     */
    @Schema(title = "单件条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 报工工位
     */
    @Schema(title = "报工工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 数量
     */
    @Schema(title = "数量")
    @TableField("MATERIAL_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer materialCount = 0;


    /**
     * 是否使用
     */
    @Schema(title = "是否使用")
    @TableField("IS_USE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isUse = false;


}