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
public class UserViewVc implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private String id = UUIDUtils.getEmpty();

    /**
     * 用户
     */
    @Schema(title = "用户")
    private String name;


    /**
     * 联系电话
     */
    @Schema(title = "联系电话")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(title = "邮箱")
    private String email;
}


