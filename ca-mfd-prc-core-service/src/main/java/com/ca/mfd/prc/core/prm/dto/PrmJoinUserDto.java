package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmJoinUserDto", description = "菜单项配置")
@Data
public class PrmJoinUserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "DeparId")
    private Serializable deparId = UUIDUtils.getEmpty();

    @Schema(description = "UserId")
    private List<Serializable> userId;

}

