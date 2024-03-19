package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "TokenPermissionDTO", description = "TokenPermissionDTO")
@Data
public class TokenPermissionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private String id = UUIDUtils.getEmpty();

    @Schema(description = "Key")
    private String key;
    @Schema(description = "TempId")
    private String tempId;
    @Schema(description = "TempKey")
    private String tempKey;
    @Schema(description = "Name")
    private String name;
    @Schema(description = "TokenName")
    private String tokenName;
    @Schema(description = "GroupName")
    private String groupName;
    @Schema(description = "TokenEnable")
    private String tokenEnable;
}


