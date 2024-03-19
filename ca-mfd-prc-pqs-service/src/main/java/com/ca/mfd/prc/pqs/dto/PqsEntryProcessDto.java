package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 过程检验实体
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Data
@Schema(description = "过程检验 基础模型")
public class PqsEntryProcessDto {

    /**
     * 主键
     */
    @Schema(title = "主键")
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车系/机型
     */
    @Schema(title = "车系/机型")
    private String model = StringUtils.EMPTY;

    /**
     * 检验单号
     */
    @Schema(title = "检验单号")
    private String inspectionNo = StringUtils.EMPTY;

    /**
     * 订单类型
     */
    @Schema(title = "订单类型")
    private Integer orderCategory = 0;

    /**
     * 条码
     */
    @Schema(title = "条码")
    private String barcode = StringUtils.EMPTY;

    /**
     * 工单号
     */
    @Schema(title = "工单号")
    private String entryNo = StringUtils.EMPTY;

    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    private String planNo = StringUtils.EMPTY;

    /**
     * 检验类型编码
     */
    @Schema(title = "检验类型编码")
    private String entryType = StringUtils.EMPTY;

    /**
     * 检验类型名称
     */
    @Schema(title = "检验类型名称")
    private String entryTypeDesc = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    private String lineName = StringUtils.EMPTY;

    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    private String materialNo = StringUtils.EMPTY;

    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    private String materialCn = StringUtils.EMPTY;

    /**
     * 质检单状态(1:创建 2进行中 90已完成）
     */
    @Schema(title = "质检单状态(1:创建 2进行中 90已完成）")
    private Integer status = 0;

    /**
     * 质检单结论(-1 不适用 0待判定 1通过 2不通过）
     */
    @Schema(title = "质检单结论(-1 不适用 0待判定 1通过 2不通过）")
    private Integer result = 0;

    /**
     * 质检时间
     */
    @Schema(title = "质检时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date qcDt;

    /**
     * 处理人
     */
    @Schema(title = "处理人")
    private String qcUser = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;

    /**
     * 取样数
     */
    @Schema(title = "取样数")
    private Integer sampleQty;

    /**
     * 工序编码
     */
    @Schema(title = "工序编码")
    private String processCode = StringUtils.EMPTY;

    /**
     * 工序描述
     */
    @Schema(title = "工序描述")
    private String processName = StringUtils.EMPTY;

}