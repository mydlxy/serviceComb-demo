package com.ca.mfd.prc.avi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * ResetQueueParaDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ResetQueueParaDTO")
public class ResetQueueParaDTO {

    /**
     * 队列代码
     */
    @Schema(title = "队列代码")
    private String sn = StringUtils.EMPTY;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    private String queueCode = StringUtils.EMPTY;

}