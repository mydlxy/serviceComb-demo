package com.ca.mfd.prc.core.prm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Schema(title = "ApiSessionDTO", description = "ApiSessionDTO")
@Data
public class ApiSessionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "编码")
    private String code;

    /**
     * 类型1、TOKEN,2、操作，3、Both
     */
    @Schema(description = "类型1、TOKEN,2、操作，3、Both")
    private Integer type;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "路径")
    private String path;
}
