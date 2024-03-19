package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * SystemSaveWoDataDTO
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SystemSaveWoDataDTO")
public class SystemSaveWoDataDTO {

    /**
     * 工艺ID
     */
    @Schema(title = "工艺ID")
    private String woId = StringUtils.EMPTY;
    /**
     * jsonData
     */
    @Schema(title = "jsonData")
    private String jsonData = StringUtils.EMPTY;

}