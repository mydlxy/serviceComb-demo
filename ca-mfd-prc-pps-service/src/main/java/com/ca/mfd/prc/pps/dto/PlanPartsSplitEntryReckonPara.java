package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 锁定计划参数模型
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-10-24
 */
@Data
@Schema(title = "PlanPartsSplitEntryReckonPara", description = "锁定计划参数模型")
public class PlanPartsSplitEntryReckonPara {
    /**
     * 订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7:备件 8:预成组
     */
    @Schema(description = "订单大类")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int category;

    /**
     * 计划单号
     */
    @Schema(description = "计划单号")
    private String planNo;

    /**
     * 生产数量
     */
    @Schema(description = "生产数量")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private int lockCount = 0;
}
