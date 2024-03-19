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
public class ApiSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "Code")
    private String code;

    @Schema(title = "类型1、TOKEN,2、操作，3、Both")
    private Integer type = 1;

    @Schema(title = "Remark")
    private String remark;

    @Schema(title = "Path")
    private String path;


}

