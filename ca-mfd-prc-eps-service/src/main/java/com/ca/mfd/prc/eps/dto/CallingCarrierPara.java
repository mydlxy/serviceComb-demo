package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * CallingCarrierPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "CallingCarrierPara", description = "")
public class CallingCarrierPara {

    @Schema(description = "workstationCode")
    private String workstationCode = StringUtils.EMPTY;


}