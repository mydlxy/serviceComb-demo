package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * SaveShootPara
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SaveShootPara")
public class SaveShootPara {

    @Schema(title = "工艺编号")
    private String woId = StringUtils.EMPTY;

    @Schema(title = "岗位编号")
    private String workplaceId = StringUtils.EMPTY;

    @Schema(title = "车身号")
    private String sn;

    @Schema(title = "图片地址")
    private String img;

}