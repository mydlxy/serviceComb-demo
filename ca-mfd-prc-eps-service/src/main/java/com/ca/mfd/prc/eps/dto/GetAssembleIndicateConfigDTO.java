package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * GetAssembleIndicateConfigDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "GetAssembleIndicateConfigDTO")
public class GetAssembleIndicateConfigDTO {

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    private Integer sequenceNum = 0;
    /**
     * 指示内容
     */
    @Schema(title = "指示内容")
    private String operContent = StringUtils.EMPTY;
    /**
     * 车型
     */
    @Schema(title = "车型")
    private String vehicleModel = StringUtils.EMPTY;
    /**
     * 图片
     */
    @Schema(title = "图片")
    private String operImg = StringUtils.EMPTY;

}