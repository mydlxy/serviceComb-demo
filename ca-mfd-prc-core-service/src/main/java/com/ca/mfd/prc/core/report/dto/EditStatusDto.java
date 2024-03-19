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
@Schema(description = "修改状态dto")
public class EditStatusDto implements Serializable {
    @Schema(title = "打印机ID")
    private Long id;
    @Schema(title = "打印机状态")
    private String status;
}
