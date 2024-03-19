package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * ParamInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "ParamInfo", description = "")
public class ParamInfo {

    @Schema(description = "id")
    private String id;

    public ParamInfo() {
        this.id = StringUtils.EMPTY;
    }

}
