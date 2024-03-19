package com.ca.mfd.prc.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 用户token信息集合
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户token信息集合")
public class TokenUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "用户信息")
    @JsonProperty("userInfo")
    private OnlineUserDTO userInfo;

    @Schema(title = "菜单信息")
    @JsonProperty("menu")
    private List<MenuNodeDTO> menu;
}
