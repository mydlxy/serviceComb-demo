package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "RoleView", description = "RoleView")
@Data
public class RoleView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id = UUIDUtils.getEmpty();

    @Schema(title = "编码")
    private String code;

    @Schema(title = "用户")
    private String name;
}


