package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-14-9:50
 * @Description:
 */
@Data
public class InitCheckItemModel {
    /**
     * 检验单号
     */
    @Schema(title = "检验单号")
    private String inspectionNo;

    /**
     * 模板代码
     */
    @Schema(title = "模板代码")
    private Long templateId;

    /**
     * 工单类型
     */
    @Schema(title = "工单类型")
    private String entryType;
}
