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
 * @Description: 质检工单-评审工单实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "质检工单-评审工单")
@TableName("PRC_PQS_ENTRY_AUDIT")
public class PqsEntryAuditEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_AUDIT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 质检单号
     */
    @Schema(title = "质检单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 区域
     */
    @Schema(title = "区域")
    @TableField("AREA_CODE")
    private String areaCode = StringUtils.EMPTY;


    /**
     * 工单类型
     */
    @Schema(title = "工单类型")
    @TableField("ENTRY_TYPE")
    private String entryType = StringUtils.EMPTY;


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
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


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
     * 检验数量
     */
    @Schema(title = "检验数量")
    @TableField("CHECK_QTY")
    private BigDecimal checkQty = BigDecimal.valueOf(0);


    /**
     * 不合格数量
     */
    @Schema(title = "不合格数量")
    @TableField("UNPASS_QTY")
    private BigDecimal unpassQty = BigDecimal.valueOf(0);


    /**
     * 接收数量
     */
    @Schema(title = "接收数量")
    @TableField("ACCEPT_QTY")
    private BigDecimal acceptQty = BigDecimal.valueOf(0);


    /**
     * 接收数量
     */
    @Schema(title = "接收数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty = BigDecimal.valueOf(0);


    /**
     * 返修数量
     */
    @Schema(title = "返修数量")
    @TableField("REDO_QTY")
    private BigDecimal redoQty = BigDecimal.valueOf(0);

    @TableField(exist = false)
    private String statusDescription = StringUtils.EMPTY;
}