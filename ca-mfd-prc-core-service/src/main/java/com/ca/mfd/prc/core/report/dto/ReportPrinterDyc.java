package com.ca.mfd.prc.core.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * ReportPrinterDyc class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ReportPrinterDyc")
public class ReportPrinterDyc implements Serializable {

    @Schema(title = "主键")
    private Long id;

    @Schema(title = "打印机名称")
    private String printName;

    @Schema(title = "ip")
    private String ip;

    @Schema(title = "型号")
    private String model;
}
