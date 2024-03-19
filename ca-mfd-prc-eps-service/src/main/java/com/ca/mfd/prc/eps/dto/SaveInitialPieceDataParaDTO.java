package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SaveInitialPieceDataParaDTO
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SaveInitialPieceDataParaDTO")
public class SaveInitialPieceDataParaDTO {

    @Schema(title = "测试模板编号")
    private String epsInitialPieceTemplateId;
    @Schema(title = "测试结果(JSON)")
    private String parameterData;
}
