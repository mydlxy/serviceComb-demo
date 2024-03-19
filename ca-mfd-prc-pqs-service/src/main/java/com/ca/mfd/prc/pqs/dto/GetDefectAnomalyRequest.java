package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
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
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "GetDefectAnomalyRequest")
public class GetDefectAnomalyRequest {


    @Schema(title = "产品唯一码")
    private String sn = Strings.EMPTY;

    @Schema(title = "评审单")
    private String inspectionNo = Strings.EMPTY;

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


    @Schema(title = "QG岗位")
    private String qgWorkstationCode = Strings.EMPTY;


    @Schema(title = "工位--根据工位代码或名称模糊筛选")
    private String workstation = Strings.EMPTY;


    @Schema(title = "工位集合 --班组长精准查询")
    private List<String> workstationCodes = new ArrayList<>();


    @Schema(title = "车间代码")
    private String shopCode = Strings.EMPTY;


    private Integer pageIndex = Constant.FAIL;

    private Integer pageSize = Constant.FAIL;


}
