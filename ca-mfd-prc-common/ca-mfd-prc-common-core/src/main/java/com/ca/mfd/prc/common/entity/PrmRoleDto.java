package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * PrmMsgConstant
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Schema(title = "PrmRoleDto", description = "PrmRoleDto")
@Data
public class PrmRoleDto implements Serializable {
    @Schema(description = "name")
    private String name;
}
