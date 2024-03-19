package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 报工检验模型
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Data
@Schema(description = "报工检验模型")
public class PqsEntryProcessReportingDto extends PqsEntryProcessDtoBase {


    /**
     * 计划单号
     */
    @Schema(title = "计划单号")
    private String ppsPlanNo = StringUtils.EMPTY;

    /**
     * 生产工单号
     */
    @Schema(title = "生产工单号")
    private String ppsEntryNo = StringUtils.EMPTY;

    /**
     * 报工单类型
     */
    @Schema(title = "报工单类型")
    private int entryReportType;
    /**
     * 生产工单号
     */
    @Schema(title = "生产工单号")
    private int ppsEntryReportNo;
    /**
     * 数量
     */
    @Schema(title = "数量")
    private double qty;
    /**
     * 抽检数量
     */
    @Schema(title = "抽检数量")
    private double checkQty;
    /**
     * 不合格数量
     */
    @Schema(title = "不合格数量")
    private double unpassQty;
    /**
     * 接收数量
     */
    @Schema(title = "接收数量")
    private double acceptQty;
    /**
     * 接收数量
     */
    @Schema(title = "接收数量")
    private double lostQty;

}