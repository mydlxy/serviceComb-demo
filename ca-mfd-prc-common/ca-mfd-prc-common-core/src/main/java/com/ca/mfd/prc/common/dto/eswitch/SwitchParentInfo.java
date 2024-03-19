package com.ca.mfd.prc.common.dto.eswitch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Switch访问父类
 *
 * @author inkelink eric.zhou
 * @date 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "Switch访问父类")
public class SwitchParentInfo {

    @Schema(title = "访问地址IP")
    private String urlIp;
    @Schema(title = "请求标识")
    private String token;

    public SwitchParentInfo(String token, String urlIp) {
        this.urlIp = urlIp;
        this.token = token;
    }

}
