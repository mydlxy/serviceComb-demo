package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 模组涂胶JOB对象
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "模组涂胶JOB对象", description = "")
public class ModuleGluingJobDto {

    @Schema(description = "workstationCode")
    private String workstationCode = StringUtils.EMPTY;

    @Schema(description = "job")
    private String job = StringUtils.EMPTY;
}