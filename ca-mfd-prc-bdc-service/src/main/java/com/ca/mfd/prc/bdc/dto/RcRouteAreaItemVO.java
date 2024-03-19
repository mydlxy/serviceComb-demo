package com.ca.mfd.prc.bdc.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RcRouteAreaItemVO")
public class RcRouteAreaItemVO implements Serializable {
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;

    @Schema(title = "路由区代码")
    private Integer areaCode;

    @Schema(title = "路由区名称")
    private String areaName;
}
