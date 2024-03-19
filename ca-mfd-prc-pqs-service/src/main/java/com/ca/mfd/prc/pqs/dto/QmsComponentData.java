package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-25-16:13
 * @Description:
 */
@Data
public class QmsComponentData {
    /**
     * 二级部件名称
     */
    @Schema(title = "二级部件名称")
    private String secondarycomponentname;

    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    private String secondaryComponentCode;

    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    private String componentdescription;

    /**
     * 组件位置描述
     */
    @Schema(title = "组件位置描述")
    private String componentLocationDescription;
}
