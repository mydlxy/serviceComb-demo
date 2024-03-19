package com.ca.mfd.prc.pps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author edwards.qu
 * @ClassName GroupCountDto
 * @description:
 * @date
 * @version: 1.0
 */
@Data
public class GroupCountDto {

    /**
     * key
     */
    @Schema(description = "key")
    private Integer key;

    /**
     * count
     */
    @Schema(description = "count")
    private Integer count;
}
