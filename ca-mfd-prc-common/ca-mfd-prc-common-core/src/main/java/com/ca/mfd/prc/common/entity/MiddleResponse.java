package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * TokenDataInfo信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "TokenDataInfo信息")
public class MiddleResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int SUCCESS_CODE = 200;

    @Schema(title = "原始数据内容")
    private String body;

    @Schema(title = "错误提示")
    private String errorMessage;

    @Schema(title = "错误编码")
    private Integer code = SUCCESS_CODE;

    @Schema(title = "是否成功")
    private Boolean success = false;

    public Boolean getSuccess() {
        if (code != null && code == SUCCESS_CODE) {
            return true;
        }
        return false;
    }

    public void setSuccess(Boolean value) {
        success = value;
    }

}

