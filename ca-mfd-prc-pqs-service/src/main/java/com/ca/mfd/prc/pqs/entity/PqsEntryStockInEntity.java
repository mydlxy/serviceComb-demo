package com.ca.mfd.prc.pqs.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author inkelink
 * @Description: 入库质检工单实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "入库质检工单")
@TableName("PRC_PQS_ENTRY_STOCK_IN")
public class PqsEntryStockInEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_STOCK_IN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 检验单号
     */
    @Schema(title = "检验单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 工单类型
     */
    @Schema(title = "工单类型")
    @TableField("ENTRY_TYPE")
    private String entryType = StringUtils.EMPTY;


    /**
     * 仓库
     */
    @Schema(title = "仓库")
    @TableField("WHAREHOUSE_CODE")
    private String wharehouseCode = StringUtils.EMPTY;


    /**
     * 状态(1:创建 2进行中 97已完成）
     */
    @Schema(title = "状态(1:创建 2进行中 97已完成）")
    @TableField("STATUS")
    private Integer status = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("PRODUCT_IMG")
    private String productImg = StringUtils.EMPTY;


    /**
     * 最终结论（0未判定 1 通过 2不通过）
     */
    @Schema(title = "最终结论（0未判定 1 通过 2不通过）")
    @TableField("FINAL_RESULT")
    private Integer finalResult = 0;


    /**
     * 是否特判
     */
    @Schema(title = "是否特判")
    @TableField("IS_TP")
    private String isTp = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 申请日期
     */
    @Schema(title = "申请日期")
    @TableField("APPLY_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date applyDt;


    /**
     * 申请人
     */
    @Schema(title = "申请人")
    @TableField("APPLY_USER_NAME")
    private String applyUserName = StringUtils.EMPTY;


    /**
     * 质检时间
     */
    @Schema(title = "质检时间")
    @TableField("QC_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date qcDt;


    /**
     * 质检人员
     */
    @Schema(title = "质检人员")
    @TableField("QC_USER")
    private String qcUser = StringUtils.EMPTY;


    /**
     * 外部单号（收货单/退货单/生产计划单)
     */
    @Schema(title = "外部单号（收货单/退货单/生产计划单)")
    @TableField("OUTER_NO")
    private String outerNo = StringUtils.EMPTY;


    /**
     * 入库单号
     */
    @Schema(title = "入库单号")
    @TableField("WMS_IN_STOCK_NO")
    private String wmsInStockNo = StringUtils.EMPTY;


    /**
     * 批次号
     */
    @Schema(title = "批次号")
    @TableField("LOT_NO")
    private String lotNo = StringUtils.EMPTY;


    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 检验类型编码
     */
    @Schema(title = "检验类型编码")
    @TableField("INSPECTION_TYPE")
    private String inspectionType = StringUtils.EMPTY;


    /**
     * 检验类型描述
     */
    @Schema(title = "检验类型描述")
    @TableField("INSPECTION_TYPE_DESC")
    private String inspectionTypeDesc = StringUtils.EMPTY;


    /**
     * 供应商/客户代码
     */
    @Schema(title = "供应商/客户代码")
    @TableField("DELIVERIER_CODE")
    private String deliverierCode = StringUtils.EMPTY;


    /**
     * 供应商/客户名称
     */
    @Schema(title = "供应商/客户名称")
    @TableField("DELIVERIER_NAME")
    private String deliverierName = StringUtils.EMPTY;


    /**
     * 初检查结论;（0未判定 1 通过 2不通过）
     */
    @Schema(title = "初检查结论;（0未判定 1 通过 2不通过）")
    @TableField("FIRST_RESULT")
    private Integer firstResult = 0;


    /**
     * 特判时间
     */
    @Schema(title = "特判时间")
    @TableField("SPECIAL_CHECK_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date specialCheckDt;


    /**
     * 特判状结论;（0未判定 1合格 2不合格）
     */
    @Schema(title = "特判状结论;（0未判定 1合格 2不合格）")
    @TableField("SPECIAL_CHECK_RESULT")
    private Integer specialCheckResult = 0;


    /**
     * 特判人员
     */
    @Schema(title = "特判人员")
    @TableField("SPECIAL_CHECK_USER")
    private String specialCheckUser = StringUtils.EMPTY;


    /**
     * 特判备注
     */
    @Schema(title = "特判备注")
    @TableField("SPECIAL_CHECK_REMARK")
    private String specialCheckRemark = StringUtils.EMPTY;


    /**
     * 来料数量
     */
    @Schema(title = "来料数量")
    @TableField("QTY")
    private BigDecimal qty = BigDecimal.valueOf(0);


    /**
     * 抽检数量
     */
    @Schema(title = "抽检数量")
    @TableField("CHECK_QTY")
    private BigDecimal checkQty = BigDecimal.valueOf(0);


    /**
     * 不合格数量
     */
    @Schema(title = "不合格数量")
    @TableField("UNPASS_QTY")
    private BigDecimal unpassQty = BigDecimal.valueOf(0);


    /**
     * 收货数量
     */
    @Schema(title = "收货数量")
    @TableField("FINAL_ACCEPT_QTY")
    private BigDecimal finalAcceptQty = BigDecimal.valueOf(0);


    /**
     * 退货数量
     */
    @Schema(title = "退货数量")
    @TableField("FINAL_UNPASS_QTY")
    private BigDecimal finalUnpassQty = BigDecimal.valueOf(0);


}