package com.ca.mfd.prc.core.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * PaperItem class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "PrinterStatusItems")
public class PrinterStatusItems implements Serializable {
    @Schema(title = "结果")
    private Boolean result;
    @Schema(title = "返回信息")
    private String message;
}
