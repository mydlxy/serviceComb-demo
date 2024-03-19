package com.ca.mfd.prc.rc.rcps.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * RcRouteAreaAttachVO class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RcRouteAreaAttachVO")
public class RcRouteAreaAttachVO implements Serializable {

    @Schema(title = "附加模块ID")
    @JsonAlias(value = {"id", "Id"})
    private String id;


    @Schema(title = "附加模块代码")
    @JsonAlias(value = {"code", "Code"})
    private String code;

    @Schema(title = "附加模块名称")
    @JsonAlias(value = {"name", "Name"})
    private String name;
}
