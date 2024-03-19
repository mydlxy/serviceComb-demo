package com.ca.mfd.prc.pps.entity;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 零部件-变更履历实体
 * @author inkelink
 * @date 2024年02月21日
 * @变更说明 BY inkelink At 2024年02月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "零部件-变更履历")
@TableName("PRC_PPS_PARTS_LOG")
public class PpsPartsLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PARTS_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


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
     * 报工类型;1 合格  2 待检 3 不合格 4 待质检  5、返修合格 6 返修不合格 7工废 8 料废
     */
    @Schema(title = "报工类型;1 合格  2 待检 3 不合格 4 待质检  5、返修合格 6 返修不合格 7工废 8 料废")
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
     * 订单大类;3：压铸  4：机加
     */
    @Schema(title = "订单大类;3：压铸  4：机加")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;


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
     * 操作工位
     */
    @Schema(title = "操作工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 操作工位名称
     */
    @Schema(title = "操作工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 来源;1、报工 2、AVI 3、抽检 4、返修
     */
    @Schema(title = "来源;1、报工 2、AVI 3、抽检 4、返修")
    @TableField("SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;

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

}