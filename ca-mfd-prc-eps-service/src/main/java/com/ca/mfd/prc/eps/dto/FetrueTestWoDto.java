package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * FetrueTestWoDto
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "FetrueTestWoDto", description = "")
public class FetrueTestWoDto implements Serializable {

    @Schema(description = "车间编码")
    private String shopCode = StringUtils.EMPTY;

    @Schema(description = "线体编码")
    private String lineCode = StringUtils.EMPTY;

    @Schema(description = "工位编码")
    private String workstationCode = StringUtils.EMPTY;

    @Schema(description = "操作类型")
    private Integer operType = 0;

    @Schema(description = "操作编码")
    private String pmWoCode = StringUtils.EMPTY;

    @Schema(description = "操作描述")
    private String pmWoDescription = StringUtils.EMPTY;

}