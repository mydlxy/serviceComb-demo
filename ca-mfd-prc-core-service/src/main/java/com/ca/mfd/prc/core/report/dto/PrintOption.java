package com.ca.mfd.prc.core.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PrintOption class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "PrintOption")
public class PrintOption implements Serializable {
    @Schema(title = "")
    private String pattern = "Server";
    @Schema(title = "")
    private Integer queueSleep = 1000;
    @Schema(title = "")
    private Integer threadSleep = 1000;
    @Schema(title = "")
    private List<String> printer = new ArrayList<>();
    @Schema(title = "")
    private List<PrintPackageItem> pageOrientation = new ArrayList<>();
    @Schema(title = "")
    private Map<String, StatusCheckItem> statusCheck = new HashMap<>();
}
