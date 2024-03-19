package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 冲压基础信息
 *
 * @Author: joel
 * @Date: 2023-08-20-14:41
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "冲压基础信息")
public class StBaseInfo {
    /**
     * 记录ID
     */
    @Schema(title = "记录ID")
    private String id = StringUtils.EMPTY;

    /**
     * 记录号
     */
    @Schema(title = "记录号")
    private String recordNo;

    /**
     * 物料号
     */
    @Schema(title = "物料号")
    private String materilaNo;

    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    private String materialDesc;

    /**
     * 评审日期
     */
    @Schema(title = "评审日期")
    private String auditDt;

    /**
     * 评审人
     */
    @Schema(title = "评审人")
    private String auditUser;

    /**
     * 车型
     */
    @Schema(title = "车型")
    private String model;

    /**
     * 车型描述
     */
    @Schema(title = "车型描述")
    private String modelDescription;

    /**
     * 生产日期
     */
    @Schema(title = "生产日期")
    private String prodDt;

    /**
     * 项目阶段
     */
    @Schema(title = "项目阶段")
    private String prodGradation;

    /**
     * 状态
     */
    @Schema(title = "状态")
    private Integer status = 0;

    /**
     * 密级
     */
    @Schema(title = "密级")
    private String confidentialityLevel;
}
