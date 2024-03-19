package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-25-16:36
 * @Description:
 */
@Data
public class QmsPositionInfo {
    /**
     * 缺陷位置一级分组名称
     */
    @Schema(title = "缺陷位置一级分组名称")
    private String firstgroupingname;

    /**
     * 缺陷位置二级分组名称
     */
    @Schema(title = "缺陷位置二级分组名称")
    private String secondarygroupingname;

    /**
     * 位置名称
     */
    @Schema(title = "位置名称")
    private String azimuthname;

    /**
     * 位置代码
     */
    @Schema(title = "位置代码")
    private String azimuthcode;
}
