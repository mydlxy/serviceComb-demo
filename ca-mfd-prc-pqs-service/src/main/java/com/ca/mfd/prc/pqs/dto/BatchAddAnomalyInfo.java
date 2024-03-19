package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 质量缺陷业务模型
 *
 * @Author: joel
 * @Date: 2023-04-11-18:22
 * @Description:
 */
@Data
@Schema(description = "质量缺陷业务模型")
public class BatchAddAnomalyInfo {
    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    private String code;
    /**
     * 缺陷描述
     */
    @Schema(title = "缺陷描述")
    private String description;
    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    private Integer level = 0;
    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    private String componentCode;
    /**
     * 位置代码
     */
    @Schema(title = "位置代码")
    private String defectCode;
    /**
     * 分类代码
     */
    @Schema(title = "分类代码")
    private String defectPositionCode;
    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    private String dutyDepartment;
}
