package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * ReleasePassSuccessPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "ReleasePassSuccessPara", description = "")
public class ReleasePassSuccessPara {

    @Schema(description = "VirtualVin")
    private String virtualVin = StringUtils.EMPTY;


}