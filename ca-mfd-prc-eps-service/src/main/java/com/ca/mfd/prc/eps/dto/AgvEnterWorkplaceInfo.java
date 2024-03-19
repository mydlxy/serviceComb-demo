package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * AgvEnterWorkplaceInfo
 *
 * @author eric.zhou
 * @since 1.0.0 2023-09-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "AgvEnterWorkplaceInfo", description = "")
public class AgvEnterWorkplaceInfo implements Serializable {

    @Schema(description = "请求唯一码")
    private String reqCode = StringUtils.EMPTY;

    @Schema(description = "产品条码")
    private String vinCode = StringUtils.EMPTY;

    @Schema(description = "AGV编号")
    private String robotId = StringUtils.EMPTY;


    @Schema(description = "工位")
    private String posCode = StringUtils.EMPTY;

    @Schema(description = "方法 upload 上报  apply 忽略  release 离开")
    private String method = StringUtils.EMPTY;

}