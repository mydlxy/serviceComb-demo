package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author inkelink
 * @Description: 评审工单明细实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "评审工单明细")
@TableName("PRC_PQS_ENTRY_AUDIT_DETAIL")
public class PqsEntryAuditDetailEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_AUDIT_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 评审单单号
     */
    @Schema(title = "评审单单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


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
    @TableField("MATERIAL_DESCRIPTION")
    private String materialDescription = StringUtils.EMPTY;


    /**
     * 物料图号
     */
    @Schema(title = "物料图号")
    @TableField("MATERIAL_DRAWING_NO")
    private String materialDrawingNo = StringUtils.EMPTY;


    /**
     * 处置方式 -1不适用 1让步接收 2返工返修 3工废 4料废
     */
    @Schema(title = "处置方式 -1不适用 1让步接收 2返工返修 3工废 4料废")
    @TableField("HANDLE_WAY")
    private Integer handleWay = 0;


    /**
     * 数量
     */
    @Schema(title = "数量")
    @TableField("QTY")
    private BigDecimal qty = BigDecimal.valueOf(0);


    /**
     * 让步接受数量
     */
    @Schema(title = "让步接受数量")
    @TableField("ACCEPT_QTY")
    private BigDecimal acceptQty = BigDecimal.valueOf(0);


    /**
     * 返工数量
     */
    @Schema(title = "返工数量")
    @TableField("REDO_QTY")
    private BigDecimal redoQty = BigDecimal.valueOf(0);


    /**
     * 工废数量
     */
    @Schema(title = "工废数量")
    @TableField("WORK_SCRAP_QTY")
    private BigDecimal workScrapQty = BigDecimal.valueOf(0);


    /**
     * 料废数量
     */
    @Schema(title = "料废数量")
    @TableField("MATERIAL_SCRAP_QTY")
    private BigDecimal materialScrapQty = BigDecimal.valueOf(0);


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}