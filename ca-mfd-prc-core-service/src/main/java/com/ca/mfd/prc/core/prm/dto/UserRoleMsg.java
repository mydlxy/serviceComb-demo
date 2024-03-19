package com.ca.mfd.prc.core.prm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "UserRoleMsg", description = "UserRoleMsg")
@Data
public class UserRoleMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    private String name;
}


