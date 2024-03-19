/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.pm.communication.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

/**
 * AS响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "特征主数据入参")
@Data
public class BomDto {

    @Schema(title = "开始日期，YYYYMMDD")
    private String startDate = "19700101";
    @Schema(title = "结束日期，YYYYMMDD")
    private String endDate = Strings.EMPTY;
    @Schema(title = "当前页数")
    private Integer currentPage = 1;



}
