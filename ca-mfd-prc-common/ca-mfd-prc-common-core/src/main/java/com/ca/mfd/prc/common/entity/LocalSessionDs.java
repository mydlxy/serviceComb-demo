package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * LocalSessionDs
 *
 * @author inkelink
 * @date 2023-08-17
 */
@EqualsAndHashCode(callSuper = false)
@Schema(description = "LocalSessionDs")
@Data
public class LocalSessionDs implements Serializable {
    @Schema(description = "id")
    private String id;
    @Schema(description = "userType")
    private int userType;
    @Schema(description = "basic")
    private Map<String, String> basic;
    @Schema(description = "apiMd5")
    private String apiMd5;
}
