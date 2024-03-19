package com.ca.mfd.prc.pps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author eric.zhou
 * @Description: IdDto
 * @date 2023年4月28日
 * @变更说明 BY eric.zhou At 2023年4月28日
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "IdDto", description = "")
public class IdDto implements Serializable {

    @Schema(description = "id")
    private String id = StringUtils.EMPTY;

}