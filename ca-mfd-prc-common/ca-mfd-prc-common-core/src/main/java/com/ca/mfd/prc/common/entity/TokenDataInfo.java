package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * TokenDataInfo信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "TokenDataInfo信息")
public class TokenDataInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "应用AppId")
    private String appId;

    @Schema(title = "密钥")
    private String secret;

    @Schema(title = "是否启用验证")
    private Boolean secretEnable = false;

    @Schema(title = "分组名称")
    private String groupName;

    @Schema(title = "授权代码")
    private List<String> permissions;

    @Schema(title = "授权的路径列表")
    private List<String> paths;

}

