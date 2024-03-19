package com.ca.mfd.prc.pps.remote.app.pm.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工厂日历查询参数
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "工厂日历查询参数")
public class CalendarParamDTO {

    @Schema(title = "车间代码")
    private String pmShopCode;

    @Schema(title = "线体代码")
    private String pmLineCode;

    @Schema(title = "年")
    private Integer year = 0;

    @Schema(title = "月")
    private Integer month = 0;

    @Schema(title = "日")
    private Integer day = 0;
}
