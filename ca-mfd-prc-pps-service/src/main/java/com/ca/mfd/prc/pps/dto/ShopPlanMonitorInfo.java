package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * ShopPlanMonitorInfo
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
@Data
@Schema(title = "ShopPlanMonitorInfo", description = "")
public class ShopPlanMonitorInfo {

    @Schema(description = "每页显示的条数")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer pageSize = 0;

    @Schema(description = "传入的当前页")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer pageIndex = 0;

    @Schema(description = "车间代码")
    private String shopCode;

    @Schema(description = "计划编号")
    private String planNo;

    @Schema(description = "Tps码(车身号)")
    private String tpsCode;

    @Schema(description = "订单状态")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    @Schema(description = "返回的分页数据总条数")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer pageCount = 0;

    @Schema(description = "返回的数据")
    private List<Map> datas;
}
