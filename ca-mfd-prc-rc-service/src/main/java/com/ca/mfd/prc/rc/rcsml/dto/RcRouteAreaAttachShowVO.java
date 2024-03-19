package com.ca.mfd.prc.rc.rcsml.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author banny.luo
 * @Description: 查询所有附加的列表
 * @date 2023年08月08日
 * @变更说明 BY banny.luo At 2023年08月08日
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RcRouteAreaAttachShowVO")
public class RcRouteAreaAttachShowVO implements Serializable {

    @Schema(title = "附加模块ID")
    @JsonAlias(value = {"attachId", "AttachId"})
    private String attachId;

    @Schema(title = "附加模块代码")
    @JsonAlias(value = {"attachCode", "AttachCode"})
    private String attachCode;

    @Schema(title = "附加模块名称")
    @JsonAlias(value = {"attachName", "AttachName"})
    private String attachName;
}
