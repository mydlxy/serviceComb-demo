package com.ca.mfd.prc.pqs.entity;

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
 *
 * @Description: 过程检验实体
 * @author inkelink
 * @date 2023年10月27日
 * @变更说明 BY inkelink At 2023年10月27日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "过程检验")
@TableName("PRC_PQS_ENTRY_PROCESS")
public class PqsEntryProcessEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ENTRY_PROCESS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 检验单号
     */
    @Schema(title = "检验单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 订单类型：3：压铸  4：机加   5：冲压  6：电池上盖
     */
    @Schema(title = "订单类型：3：压铸  4：机加   5：冲压  6：电池上盖")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 报工单号
     */
    @Schema(title = "报工单号")
    @TableField("ENTRY_REPORT_NO")
    private String entryReportNo = StringUtils.EMPTY;


    /**
     * 检验类型编码
     */
    @Schema(title = "检验类型编码")
    @TableField("ENTRY_TYPE")
    private String entryType = StringUtils.EMPTY;


    /**
     * 检验类型名称
     */
    @Schema(title = "检验类型名称")
    @TableField("ENTRY_TYPE_DESC")
    private String entryTypeDesc = StringUtils.EMPTY;


    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;


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
     * 质检单状态;(1:创建 2进行中 90已完成）
     */
    @Schema(title = "质检单状态;(1:创建 2进行中 90已完成）")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 质检单结论;(-1 不适用 0待判定 1通过 2不通过）
     */
    @Schema(title = "质检单结论;(-1 不适用 0待判定 1通过 2不通过）")
    @TableField("RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;


    /**
     * 质检时间
     */
    @Schema(title = "质检时间")
    @TableField("QC_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date qcDt = new Date();

    /**
     * 处理人
     */
    @Schema(title = "处理人")
    @TableField("QC_USER")
    private String qcUser = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 取样数
     */
    @Schema(title = "取样数")
    @TableField("SAMPLE_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sampleQty = 0;

    /**
     * 车系/机型
     */
    @Schema(title = "车系/机型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;

    /**
     * 检测-工位代码
     */
    @Schema(title = "检测-工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 检测-工位名称
     */
    @Schema(title = "检测-工位名称")
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
     * 抽检策略
     */
    @Schema(title = "抽检策略")
    @TableField("POLICY_CODE")
    private String policyCode = StringUtils.EMPTY;

    /**
     * 抽检策略名称
     */
    @Schema(title = "抽检策略名称")
    @TableField("POLICY_NAME")
    private String policyName = StringUtils.EMPTY;

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
     * 刀具
     */
    @Schema(title = "刀具")
    @TableField("CNC_BLADES ")
    private String cncBlades = StringUtils.EMPTY;

}