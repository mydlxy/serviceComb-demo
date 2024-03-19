package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 线体对象
 *
 * @author jay.he
 * @since 1.0.0 2023-08-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "线体对象")
public class LineDTO {

    @Schema(title = "线体ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pmLineId;

    @Schema(title = "车间ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pmShopId;

    @Schema(title = "线体代码")
    private String pmLineCode;

    @Schema(title = "线体名称")
    private String pmLineName;


}
