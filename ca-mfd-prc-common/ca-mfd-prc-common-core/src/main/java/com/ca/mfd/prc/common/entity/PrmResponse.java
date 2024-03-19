package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TokenDataInfo信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "TokenDataInfo信息")
public class PrmResponse extends MiddleResponse {
    private static final long serialVersionUID = 1L;
    private static final int SUCCESS_CODE = 200;

    @Schema(title = "错误编码")
    private Integer code = -1;

    @Schema(title = "是否成功")
    private Boolean success = false;

    @Override
    public Boolean getSuccess() {
        if (code != null && code == SUCCESS_CODE) {
            return true;
        }
        return false;
    }

    @Override
    public void setSuccess(Boolean value) {
        success = value;
    }

}

