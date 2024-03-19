package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * AssignJsonInfo
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "AssignJsonInfo", description = "")
public class AssignJsonInfo implements Serializable {

    @Schema(description = "线体编码")
    private String lineCode = StringUtils.EMPTY;

    @Schema(description = "工位编码")
    private String gummingWorkstation = StringUtils.EMPTY;

    @Schema(description = "工位编码")
    private String inBoxWorkstation = StringUtils.EMPTY;

    @Schema(description = "位置")
    private String location = StringUtils.EMPTY;

    @Schema(description = "标记")
    private String routeSign = StringUtils.EMPTY;

    @Schema(description = "模组编码")
    private String moduleCode = StringUtils.EMPTY;
}