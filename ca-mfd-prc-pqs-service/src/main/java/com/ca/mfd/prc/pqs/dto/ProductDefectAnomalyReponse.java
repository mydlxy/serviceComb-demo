package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;
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
@Schema(description = "ProductDefectAnomalyReponse")
public class ProductDefectAnomalyReponse {


    @Schema(title = "数据编号")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long dataId = Constant.DEFAULT_ID;
    @Schema(title = "激活缺陷岗位")
    private String workstationName = Strings.EMPTY;
    @Schema(title = "评审单")
    private String inspectionNo = Strings.EMPTY;
    @Schema(title = "缺陷代码")
    private String defectAnomalyCode = Strings.EMPTY;
    @Schema(title = "缺陷描述")
    private String defectAnomalyDescription = Strings.EMPTY;
    @Schema(title = "图片")
    private String img = Strings.EMPTY;
    @Schema(title = "等级")
    private String gradeCode = Strings.EMPTY;
    @Schema(title = "等级描述")
    private String gradeName = Strings.EMPTY;
    @Schema(title = "责任部门")
    private String responsibleDeptCode = Strings.EMPTY;
    @Schema(title = "责任部门描述")
    private String responsibleDeptName = Strings.EMPTY;
    @Schema(title = "json数据 白格图，qg检查项目存在此数据")
    private String jsonData = Strings.EMPTY;
    @Schema(title = "状态描述")
    private String statusDis = Strings.EMPTY;

    @Schema(title = "(1.未修复;2.已修复;3.未发现;4.合格;5.不合格)")
    private Integer status = 0;
    @Schema(title = "操作编号")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long woId = Constant.DEFAULT_ID;
    @Schema(title = "车间代码")
    private String shopCode = Strings.EMPTY;
    @Schema(title = "工位集合 --班组长精准查询")
    private List<String> workstationCodes = Lists.newArrayList();

    @Schema(title = "来源;0、正常录入 1、百格图 2、QG检项项 3、问题排查")
    private Integer source = -1;
    @Schema(title = "激活时间")
    private Date creationDate = new Date();


}
