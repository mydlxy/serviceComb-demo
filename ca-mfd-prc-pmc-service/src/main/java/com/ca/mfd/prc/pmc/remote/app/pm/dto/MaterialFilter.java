package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author banny.luo
 * @Description:
 * @date 2023年4月28日
 * @变更说明 BY banny.luo At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "MaterialFilter")
public class MaterialFilter {
    @Schema(title = "页码")
    private Integer pageIndex;
    @Schema(title = "每页数量")
    private Integer pageSize;
    @Schema(title = "关键字")
    private String key = StringUtils.EMPTY;
    @Schema(title = "物料类型")
    private String types = StringUtils.EMPTY;
}
