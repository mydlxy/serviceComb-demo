package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * TriggerQgAuditRecordDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "TriggerQgAuditRecordDTO")
public class TriggerQgAuditRecordDTO {

    /**
     * 检查项编号
     */
    @Schema(title = "检查项编号")
    private String dataId = StringUtils.EMPTY;
    /**
     * 岗位编号
     */
    @Schema(title = "岗位编号")
    private String workplaceId = StringUtils.EMPTY;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    private String sn = StringUtils.EMPTY;

    /**
     * 检测结果
     */
    @Schema(title = "检测结果")
    private Integer result = 0;

}