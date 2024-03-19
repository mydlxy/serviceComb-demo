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
@Schema(description = "PaperItem")
public class PaperItem implements Serializable {
    @Schema(title = "名称")
    private String name;
    @Schema(title = "宽度")
    private int width;
    @Schema(title = "高度")
    private int height;
}
