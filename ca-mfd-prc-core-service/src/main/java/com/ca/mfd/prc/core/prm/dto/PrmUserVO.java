package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmUserVO", description = "PrmUserVO")
@Data
public class PrmUserVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Serializable id = Constant.DEFAULT_ID;

    @Schema(description = "全名")
    private String fullName;

}

