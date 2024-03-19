package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 缺陷项
 *
 * @Author: joel
 * @Date: 2023-04-10-14:46
 * @Description:
 */
@Data
@Schema(description = "缺陷项")
public class QmsCodeItem {
    /**
     * 缺陷名称
     */
    @Schema(title = "缺陷名称")
    private String defectname;
    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    private String defectcode;
}
