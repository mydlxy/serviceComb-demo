package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-14-9:33
 * @Description:
 */
@Data
public class RestCheckItemModel {
    /**
     * 检验单号
     */
    @Schema(title = "检验单号")
    private String inspectNo;
}
