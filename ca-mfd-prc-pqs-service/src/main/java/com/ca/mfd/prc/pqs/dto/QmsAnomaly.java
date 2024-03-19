package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: joel
 * @Date: 2023-04-25-14:41
 * @Description:
 */
@Data
public class QmsAnomaly {
    /**
     * 二级部件代码
     */
    @Schema(title = "二级部件代码")
    private String secondaryComponentCode;

    /**
     * 方位代码
     */
    @Schema(title = "方位代码")
    private String azimuthcode;

    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    private String defectcode;

    /**
     * 组合名称
     */
    @Schema(title = "组合名称")
    private String combinationname;

    /**
     * 不良级别
     */
    @Schema(title = "不良级别")
    private String defectlevel;

    /**
     * 组合代码
     */
    @Schema(title = "组合代码")
    private String combinationcode;

    /**
     * 责任部门
     */
    @Schema(title = "责任部门")
    private String dutyDepartment;
}
