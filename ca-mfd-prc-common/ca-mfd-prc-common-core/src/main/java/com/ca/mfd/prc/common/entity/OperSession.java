package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * OperSession
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "OperSession")
public class OperSession implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "apiList")
    private List<ApiSession> apiList;

    @Schema(title = "Md5")
    private String md5;
}

