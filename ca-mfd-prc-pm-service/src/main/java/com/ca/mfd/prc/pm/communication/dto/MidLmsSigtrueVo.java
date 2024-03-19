package com.ca.mfd.prc.pm.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author inkelink
 * @Description: Lms令牌返回
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Data
@Schema(description = "Lms令牌返回")
public class MidLmsSigtrueVo implements Serializable {
    /**
     * 令牌
     */
    @Schema(title = "令牌")
    private String sigtrue;

    /**
     * 物料编码
     */
    @Schema(title = "物料编码")
    private String materialCode;

    /**
     * 错误信息
     */
    private String message;
    /**
     * 唯一码
     */
    @Schema(title = "唯一码")
    private Long uniqueCode;
}
