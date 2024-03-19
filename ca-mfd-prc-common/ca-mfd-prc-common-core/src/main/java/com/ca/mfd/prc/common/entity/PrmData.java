package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 添加对象
 *
 * @author inkelink
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "PrmData")
public class PrmData implements Serializable {
    @Schema(description = "code")
    private String code;
    @Schema(description = "recycleDt")
    private Date recycleDt;
}
