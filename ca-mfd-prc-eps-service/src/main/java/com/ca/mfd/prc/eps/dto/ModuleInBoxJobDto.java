package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * ModuleInBoxJobDto
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "ModuleInBoxJobDto", description = "")
public class ModuleInBoxJobDto {

    @Schema(description = "模组生产线体")
    private String lineCode = StringUtils.EMPTY;

    @Schema(description = "模组入箱工位")
    private String workstationCode = StringUtils.EMPTY;

    @Schema(description = "入箱位置")
    private String location = StringUtils.EMPTY;

    @Schema(description = "模组类型")
    private String moduleCode = StringUtils.EMPTY;
}