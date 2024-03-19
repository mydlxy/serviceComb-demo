package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author edwards.qu
 * @ClassName EntryReportPartsDto
 * @description:
 * @date
 * @version: 1.0
 */
@Data
public class EntryReportPartsDto {

    /**
     * 报工单号
     */
    @Schema(description = "报工单号")
    private String entryReportNo = StringUtils.EMPTY;

    /**
     * 条码
     */
    @Schema(description = "条码")
    private String barcode = StringUtils.EMPTY;

    /**
     * 计划单号
     */
    @Schema(description = "计划单号")
    private String planNo = StringUtils.EMPTY;

    /**
     * 生产工单号
     */
    @Schema(description = "生产工单号")
    private String entryNo = StringUtils.EMPTY;

    /**
     * 物料号
     */
    @Schema(description = "物料号")
    private String materialNo = StringUtils.EMPTY;

    /**
     * 工序编码
     */
    @Schema(description = "工序编码")
    private String processCode = StringUtils.EMPTY;

    /**
     * 工序名称
     */
    @Schema(description = "工序名称")
    private String processName = StringUtils.EMPTY;

    /**
     * 日历班次ID
     */
    @Schema(description = "日历班次ID")
    private Long prcPmShcCalendarId;

    /**
     * 报工类型;1 合格  2 待检 3 不合格 4 待质检
     */
    @Schema(description = "报工类型;1 合格  2 待检 3 不合格 4 待质检")
    private Integer reportType;

    /**
     * 报工工单类型（1 生产 2 返修）
     */
    @Schema(description = "报工工单类型（1 生产 2 返修）")
    private Integer entryReportType;

    /**
     * 订单大类;3：压铸  4：机加   5：冲压  6：电池上盖
     */
    @Schema(description = "订单大类;3：压铸  4：机加   5：冲压  6：电池上盖")
    private Integer orderCategory;

    /**
     * 报工车间
     */
    @Schema(description = "报工车间")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 报工线体
     */
    @Schema(description = "报工线体")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 报工工位
     */
    @Schema(description = "报工工位")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 报工数量
     */
    @Schema(description = "报工数量")
    private Integer entryReportCount;

    /**
     * 是否绑定载具
     */
    @Schema(description = "是否绑定载具")
    private Boolean isBindCarrier;

    /**
     * 载具条码
     */
    @Schema(description = "载具条码")
    private String carrierCode = StringUtils.EMPTY;

    /**
     * 是否抽检
     */
    @Schema(description = "是否抽检")
    private String qualityCheckType = StringUtils.EMPTY;
}
