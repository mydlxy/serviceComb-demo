package com.ca.mfd.prc.audit.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "GetAuditDefectAnomalyRequest")
public class GetAuditDefectAnomalyRequest {


    @Schema(title = "工单号")
    private String recordNo = Strings.EMPTY;

    @Schema(title = "产品唯一码")
    private String sn = Strings.EMPTY;


    @Schema(title = "状态")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    @Schema(title = "等级")
    private String gradeCode = Strings.EMPTY;


    @Schema(title = "来源 -1全部 0 正常录入 1 百格图录入")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = -1;

    @Schema(title = "描述")
    private String description = Strings.EMPTY;


    @Schema(title = "责任区域")
    private String dutyDeptCode = Strings.EMPTY;


    @Schema(title = "工位--根据工位代码或名称模糊筛选")
    private String workstation = Strings.EMPTY;

    @Schema(title = "评价模式代码")
    private String evaluationModeCode = Strings.EMPTY;


    @Schema(title = "工位集合 --班组长精准查询")
    private List<String> workstationCodes = new ArrayList<>();


    @Schema(title = "车间代码")
    private String shopCode = Strings.EMPTY;

    private Integer pageSize = 0;

    private Integer pageIndex = 0;

    /**
     * 类别:1、整车  5 冲压(零部件)
     */
    @Schema(title = "类别:1、整车  5 冲压(零部件)")
    private Integer category = 1;

    /**
     * 是否翻库
     */
    @Schema(title = "是否翻库")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isFlippingLibrary;

    private Integer isfk;
}
