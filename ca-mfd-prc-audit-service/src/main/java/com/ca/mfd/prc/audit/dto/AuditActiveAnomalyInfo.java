package com.ca.mfd.prc.audit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * AnomalyActivity
 *
 * @Author: eric.zhou
 * @Date: 2023-08-12-14:49
 * @Description:
 */
@Data
@Schema(description = "AuditActiveAnomalyInfo")
public class AuditActiveAnomalyInfo {

    @Schema(title = "工单号")
    private String recordNo;

    @Schema(title = "评价模式代码")
    private String evaluationModeCode = StringUtils.EMPTY;
    @Schema(title = "评价模式名称")
    private String evaluationModeName = StringUtils.EMPTY;

    /**
     * 类别:1、整车  5 冲压(零部件)
     */
    @Schema(title = "类别:1、整车  5 冲压(零部件)")
    private Integer category = 1;

    /**
     * 需要激活的缺陷列表
     */
    @Schema(title = "需要激活的缺陷列表")
    private List<AuditDefectInfo> anomalyInfos = new ArrayList<>();

}
