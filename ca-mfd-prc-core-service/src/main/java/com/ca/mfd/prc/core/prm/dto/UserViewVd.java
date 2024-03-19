package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author eric.zhou
 */
@Schema(title = "UserViewVc", description = "UserViewVc")
@Data
public class UserViewVd implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id = UUIDUtils.getEmpty();

    @Schema(title = "用户")
    private String nickName;

    @Schema(title = "用户")
    private String userName;

    @Schema(title = "用户")
    private String name;

    @Schema(title = "工号")
    private String no;

    @Schema(title = "联系电话")
    private String phone;

    @Schema(title = "邮箱")
    private String email;
}


