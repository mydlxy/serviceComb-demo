package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽检结果保存
 *
 * @Author: joel
 * @Date: 2023-08-19-15:40
 * @Description:
 */
@Data
@Schema(description = "抽检结果保存")
public class SaveCjResultDto {
    /**
     * 质检单号
     */
    @Schema(title = "质检单号")
    private String inspectionNo = StringUtils.EMPTY;
    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;
    /**
     * 结论 1合格 2不合格
     */
    @Schema(title = "结论 1合格 2不合格")
    private Integer result = 1;
    /**
     * 来料数量
     */
    @Schema(title = "来料数量")
    private BigDecimal qty = BigDecimal.valueOf(0);
    /**
     * 抽检数量
     */
    @Schema(title = "抽检数量")
    private BigDecimal checkQty = BigDecimal.valueOf(0);
    /**
     * 不合格数量
     */
    @Schema(title = "不合格数量")
    private BigDecimal unPassQty = BigDecimal.valueOf(0);
    /**
     * 合格数量
     */
    @Schema(title = "合格数量")
    private BigDecimal passQty = BigDecimal.valueOf(0);
    /**
     * 让步接收数量
     */
    @Schema(title = "让步接收数量")
    private BigDecimal acceptQty = BigDecimal.valueOf(0);
    /**
     * 工单缺陷
     */
    @Schema(title = "工单缺陷")
    private List<EntryCheckDefectDto> defects = new ArrayList<>();
    /**
     * 检验项目
     */
    @Schema(title = "检验项目")
    private List<PqsEntryCheckItemDto> checkItem = new ArrayList<>();

    public SaveCjResultDto() {
        inspectionNo = StringUtils.EMPTY;
        remark = StringUtils.EMPTY;
    }
}
