package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 质量缺陷位置业务模型
 *
 * @Author: joel
 * @Date: 2023-04-12-20:34
 * @Description:
 */
@Data
@Schema(description = "质量缺陷位置业务模型")
public class BatchAddPositionInfo {
    /**
     * 组
     */
    @Schema(title = "组")
    private String groupName;

    /**
     * 子组
     */
    @Schema(title = "子组")
    private String subGroupName;
    /**
     * 代码
     */
    @Schema(title = "代码")
    private String code;
    /**
     * 描述
     */
    @Schema(title = "描述")
    private String description;
}
