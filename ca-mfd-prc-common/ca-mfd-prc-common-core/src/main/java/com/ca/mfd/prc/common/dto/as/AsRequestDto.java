/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.dto.as;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "请求数据")
@Data
public class AsRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 系统编码
     */
    @Schema(title = "系统编码")
    private String systemCode = StringUtils.EMPTY;
    /**
     * apiurl
     */
    @Schema(title = "apiurl")
    private String apiUrl = StringUtils.EMPTY;
    /**
     * 响应数据
     */
    @Schema(title = "响应数据")
    private Map params = new HashMap();

    public AsRequestDto() {
        this.systemCode = StringUtils.EMPTY;
        this.apiUrl = StringUtils.EMPTY;
    }

    public AsRequestDto(String systemCode, String apiUrl) {
        this.systemCode = systemCode;
        this.apiUrl = apiUrl;
    }

}