package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 批次件返修记录表实体
 * @author inkelink
 * @date 2023年11月10日
 * @变更说明 BY inkelink At 2023年11月10日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "批次件返修记录表")
@TableName("PRC_PQS_MM_DEFECT_REPAIR_RECORD")
public class PqsMmDefectRepairRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_MM_DEFECT_REPAIR_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 返修单号
     */
    @Schema(title = "返修单号")
    @TableField("REPAIR_NO")
    private String repairNo = StringUtils.EMPTY;


    /**
     * 订单类型：3：压铸  4：机加   5：冲压  6：电池上盖
     */
    @Schema(title = "订单类型：3：压铸  4：机加   5：冲压  6：电池上盖")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;


    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 批次号
     */
    @Schema(title = "批次号")
    @TableField("LOT_NO")
    private String lotNo = StringUtils.EMPTY;


    /**
     * 状态：0:待处理，1:已处理
     */
    @Schema(title = "状态：0:待处理，1:已处理")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


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
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 合格数量
     */
    @Schema(title = "合格数量")
    @TableField("ACCEPT_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer acceptQty = 0;


    /**
     * 报废数量
     */
    @Schema(title = "报废数量")
    @TableField("SCRAP_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer scrapQty = 0;


    /**
     * 维修耗时
     */
    @Schema(title = "维修耗时")
    @TableField("WORK_HOURS")
    private BigDecimal workHours = BigDecimal.valueOf(0);


    /**
     * 维修人
     */
    @Schema(title = "维修人")
    @TableField("REPAIR_PERSON")
    private String repairPerson = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 申请工位代码
     */
    @Schema(title = "申请工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 申请工位名称
     */
    @Schema(title = "申请工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

    /**
     * 检测QG-工位代码
     */
    @Schema(title = "检测QG-工位代码")
    @TableField("QG_WORKSTATION_CODE")
    private String qgWorkstationCode = StringUtils.EMPTY;

    /**
     * 检测QG-工位名称
     */
    @Schema(title = "检测QG-工位名称")
    @TableField("QG_WORKSTATION_NAME")
    private String qgWorkstationName = StringUtils.EMPTY;

    /**
     * ICC代码
     */
    @Schema(title = "ICC代码")
    @TableField("ICC_CODE")
    private String iccCode = StringUtils.EMPTY;

    /**
     * 故障名称
     */
    @Schema(title = "故障名称")
    @TableField("ICC_FAULT_NAME")
    private String iccFaultName = StringUtils.EMPTY;

    /**
     * NG图片地址 （压铸-百格图）
     */
    @Schema(title = "NG图片地址 （压铸-百格图）")
    @TableField("NG_JSON_DATA")
    private String ngJsonData = StringUtils.EMPTY;

    /**
     * NG区域（压铸-百格图）
     */
    @Schema(title = "NG区域（压铸-百格图）")
    @TableField("NG_AREA")
    private String ngArea = StringUtils.EMPTY;

    /**
     * NG点位（压铸）
     */
    @Schema(title = "NG点位（压铸）")
    @TableField("NG_POINT")
    private String ngPoint = StringUtils.EMPTY;

    /**
     * 工序代码
     */
    @Schema(title = "工序代码")
    @TableField("PROCESS_CODE")
    private String processCode = StringUtils.EMPTY;

    /**
     * 工序名称
     */
    @Schema(title = "工序名称")
    @TableField("PROCESS_NAME")
    private String processName = StringUtils.EMPTY;


    /**
     * 返修结果
     */
    @Schema(title = "返修结果")
    @TableField("RESULT")
    private String result = StringUtils.EMPTY;

    /**
     * 返修时间
     */
    @Schema(title = "返修时间")
    @TableField(value = "REPAIR_DT", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date repairDt = new Date();


}