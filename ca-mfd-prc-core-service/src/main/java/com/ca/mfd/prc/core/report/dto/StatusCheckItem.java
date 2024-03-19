package com.ca.mfd.prc.core.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * StatusCheckItem class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "StatusCheckItem")
public class StatusCheckItem implements Serializable {
    @Schema(title = "路径")
    private String path;
    @Schema(title = "错误")
    private List<String> IgnoreErrs = new ArrayList<>();
    @Schema(title = "成功")
    private List<String> oks = new ArrayList<>();
    @Schema(title = "路径")
    private List<String> xpaths = new ArrayList<>();
    @Schema(title = "")
    private List<String> replaces = new ArrayList<>();
}
