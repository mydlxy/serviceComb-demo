package com.ca.mfd.prc.pmc.dto;


import com.ca.mfd.prc.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "措施审核对象")
public class AuditMeasureInfoDTO {

    @Schema(title = "报警编号ID")
    private Serializable areaStopRecordReasonId = Constant.DEFAULT_ID;

    @Schema(title = "短期措施审核 0 未处理 1 合理 2 不合理")
    private Integer shortMeasureAuditStatus = 0;

    @Schema(title = "长期措施审核 0 未处理 1 合理 2 不合理")
    private Integer longMeasureAuditStatus = 0;
}
