package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author edwards.qu
 * @ClassName SaveEntryProcessInfo
 * @description:
 * @date
 * @version: 1.0
 */
@Data
public class SaveEntryProcessInfo {

    /**
     * 评审工单号
     */
    @Schema(description = "评审工单号")
    private String inspectionNo = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark = StringUtils.EMPTY;

    /**
     * 质检结论
     */
    @Schema(description = "质检结论")
    private Integer reuslt;
}
