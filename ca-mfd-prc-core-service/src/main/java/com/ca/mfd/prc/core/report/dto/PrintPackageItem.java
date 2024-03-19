package com.ca.mfd.prc.core.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * PrintPackageItem class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "PrintPackageItem")
public class PrintPackageItem implements Serializable {
    @Schema(title = "")
    private String printName;
    @Schema(title = "")
    private int orientation;
    @Schema(title = "")
    private PaperItem paper;
    @Schema(title = "")
    private List<Integer> pageNumbers = new ArrayList<>();
    @Schema(title = "")
    private String code;
}
