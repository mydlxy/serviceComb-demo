package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "UpdatePassword", description = "用户临时权限")
@Data
public class UpdatePassword implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "userId")
    private Serializable userId = Constant.DEFAULT_ID;

    @Schema(description = "编码")
    private String newPassword;

}

