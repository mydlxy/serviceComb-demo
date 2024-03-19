package com.ca.mfd.prc.common.entity;

import com.ca.mfd.prc.common.enums.LocalSessionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户基本信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "用户基本信息")
public class LocalSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "用户基础信息")
    private OnlineUserDTO userInfo;

    @Schema(title = "CookieSession")
    private CookieSession cookieSession;

    @Schema(title = "sessionType")
    private LocalSessionType sessionType = LocalSessionType.User;

    @Schema(title = "CookieSession")
    private String apiMd5;

    @Schema(title = "CookieSession")
    private Boolean result = true;

    @Schema(title = "CookieSession")
    private String meessage = "成功";

    @Schema(title = "CookieSession")
    private Map<String, String> basic;

    @Schema(title = "CookieSession")
    private String extData;

    @Schema(title = "CookieSession")
    private String md5;

}

