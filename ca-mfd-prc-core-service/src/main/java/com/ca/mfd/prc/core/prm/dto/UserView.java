package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "UserView", description = "UserView")
@Data
public class UserView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id = UUIDUtils.getEmpty();

    @Schema(description = "名称")
    private String name;

    @Schema(description = "工号")
    private String jobNo;
}


