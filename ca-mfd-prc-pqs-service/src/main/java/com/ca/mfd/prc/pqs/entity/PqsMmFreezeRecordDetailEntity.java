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
 * @Description: 物料质量冻结明细实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "物料质量冻结明细")
@TableName("PRC_PQS_MM_FREEZE_RECORD_DETAIL")
public class PqsMmFreezeRecordDetailEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_MM_FREEZE_RECORD_DETAIL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 冻结单号
     */
    @Schema(title = "冻结单号")
    @TableField("FREEZE_NO")
    private String freezeNo = StringUtils.EMPTY;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 批次
     */
    @Schema(title = "批次")
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
     * 物料图号
     */
    @Schema(title = "物料图号")
    @TableField("DRAWING_NO")
    private String drawingNo = StringUtils.EMPTY;


    /**
     * 数量
     */
    @Schema(title = "数量")
    @TableField("QTY")
    private BigDecimal qty = BigDecimal.valueOf(0);


    /**
     * 状态 1待处理 2已处理
     */
    @Schema(title = "状态 1待处理 2已处理")
    @TableField("STATUS")
    private Integer status = 0;


    /**
     * 库存地点
     */
    @Schema(title = "库存地点")
    @TableField("WMS_WAREHOUSE_CODE")
    private String wmsWarehouseCode = StringUtils.EMPTY;


    /**
     * BIN
     */
    @Schema(title = "BIN")
    @TableField("WMS_LOCATION_BIN")
    private String wmsLocationBin = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 是否寄售
     */
    @Schema(title = "是否寄售")
    @TableField("IS_CONSIGNMENT")
    private Boolean isConsignment = false;


}