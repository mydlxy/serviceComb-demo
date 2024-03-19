package com.ca.mfd.prc.core.prm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "ChangePasswordModel", description = "用户临时权限")
@Data
public class ChangePasswordModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "编码")
    private String oldPassword;

    @Schema(description = "模型")
    private String newPassword;


}

