package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * SessionEntity
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SessionEntity")
public class SessionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "当前请求的应用")
    private String app;

    @Schema(title = "浏览器备注")
    private String remark;

    @Schema(title = "当前时间")
    private String nowTime;

    @Schema(title = "登录ip")
    private String userHostAddress;

    @Schema(title = "访问端口")
    private String port;

    @Schema(title = "前端透传数据")
    private String extData;
}
