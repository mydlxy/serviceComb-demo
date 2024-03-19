package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @Description: 质检工单-评审工单实体
 * @date 2023年09月17日
 * @变更说明 BY inkelink At 2023年09月17日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "质检工单-评审工单")
@TableName("PRC_PQS_PARTS_SCRAP")
public class PqsPartsScrapEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_PARTS_SCRAP_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 质检单号
     */
    @Schema(title = "质检单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 质检单状态;(1:创建 2进行中 90已完成）
     */
    @Schema(title = "质检单状态;(1:创建 2进行中 90已完成）")
    @TableField("STATUS")
    private Integer status = 0;


    /**
     * 质检时间
     */
    @Schema(title = "质检时间")
    @TableField("QC_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date qcDt;


    /**
     * 质检人
     */
    @Schema(title = "质检人")
    @TableField("QC_USER")
    private String qcUser = StringUtils.EMPTY;


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
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 处置方式 -1不适用 1让步接收 2返工返修 3工废 4料废
     */
    @Schema(title = "处置方式 -1不适用 1让步接收 2返工返修 3工废 4料废")
    @TableField("HANDLE_WAY")
    private Integer handleWay = 0;


    /**
     * 报废数量
     */
    @Schema(title = "报废数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty = BigDecimal.valueOf(0);


    /**
     * 单位
     */
    @Schema(title = "单位")
    @TableField("UNIT")
    private String unit = StringUtils.EMPTY;


}