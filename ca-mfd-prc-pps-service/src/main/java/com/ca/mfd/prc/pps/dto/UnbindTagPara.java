package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * UnbindTagPara
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "UnbindTagPara", description = "")
public class UnbindTagPara {

    @Schema(description = "吊牌条码")
    private String tagNo = StringUtils.EMPTY;
}
