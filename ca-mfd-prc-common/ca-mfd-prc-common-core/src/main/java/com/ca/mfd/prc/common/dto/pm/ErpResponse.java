package com.ca.mfd.prc.common.dto.pm;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * erp返回
 *
 * @author inkelink eric.zhou
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "erp返回")
public class ErpResponse<T> {

    @Schema(title = "Code")
    private Integer code;

    @Schema(title = "Success")
    private String success;

    @Schema(title = "data")
    @JsonAlias(value = {"Data", "data"})
    private T data;
}
