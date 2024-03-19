package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 操作工艺
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "OperWoWoDTO")
public class OperWoWoDTO extends ProductWoDTO {

    /**
     * GroupName
     */
    @Schema(title = "GroupName")
    private String groupName = StringUtils.EMPTY;

    /**
     * ToolId
     */
    @Schema(title = "ToolId")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long toolId = Constant.DEFAULT_ID;

    /**
     * IsScrew
     */
    @Schema(title = "IsScrew")
    private Boolean isScrew = false;

    /**
     * IsShoot
     */
    @Schema(title = "IsShoot")
    private Boolean isShoot = false;
}