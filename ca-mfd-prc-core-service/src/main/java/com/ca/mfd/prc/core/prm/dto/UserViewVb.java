package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "UserViewVb", description = "UserViewVb")
@Data
public class UserViewVb implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id = UUIDUtils.getEmpty();

    /**
     * 用户
     */
    @Schema(title = "用户")
    private String userName;

    /**
     * 昵称
     */
    @Schema(title = "昵称")
    private String nickName;

    /**
     * 中文名称
     */
    @Schema(title = "中文名称")
    private String cnName;

    /**
     * 英文名称
     */
    @Schema(title = "英文名称")
    private String enName;

    /**
     * 工号
     */
    @Schema(title = "工号")
    private String code;
}


