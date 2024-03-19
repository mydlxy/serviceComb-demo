package com.ca.mfd.prc.core.dc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "DcTreeData", description = "树数据")
@Data
public class DcTreeData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id = StringUtils.EMPTY;
    @Schema(description = "key")
    private String key = StringUtils.EMPTY;
    @Schema(description = "name")
    private String name = StringUtils.EMPTY;
    @Schema(description = "color")
    private String color = StringUtils.EMPTY;
    @Schema(description = "authorizationCode")
    private String authorizationCode = StringUtils.EMPTY;
    @Schema(description = "type")
    private Integer type;

}
