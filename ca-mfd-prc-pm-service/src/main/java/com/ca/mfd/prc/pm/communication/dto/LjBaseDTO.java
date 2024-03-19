package com.ca.mfd.prc.pm.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 给扭矩控制系统dto基类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "基类")
public class LjBaseDTO implements Serializable {

    @Schema(title = "msgId")
    private String msgId = StringUtils.EMPTY;

    @Schema(title = "是否成功")
    private boolean isSuccess = false;

    @Schema(title = "错误信息")
    private String errMsg = StringUtils.EMPTY;

    @Schema(title = "返回数据")
    private Object data;
}
