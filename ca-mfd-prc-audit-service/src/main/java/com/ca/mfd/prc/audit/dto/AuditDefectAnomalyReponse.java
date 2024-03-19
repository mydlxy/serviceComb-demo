package com.ca.mfd.prc.audit.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;

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
@Schema(description = "AuditDefectAnomalyReponse")
public class AuditDefectAnomalyReponse {


    @Schema(title = "数据编号")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long dataId = Constant.DEFAULT_ID;
    @Schema(title = "激活缺陷岗位")
    private String workstationName = Strings.EMPTY;
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
    @Schema(title = "扣分")
    private Integer score = Constant.FAIL;
    @Schema(title = "责任部门")
    private String responsibleDeptCode = Strings.EMPTY;
    @Schema(title = "责任部门描述")
    private String responsibleDeptName = Strings.EMPTY;
    @Schema(title = "评价模式代码")
    private String evaluationModeCode = Strings.EMPTY;
    @Schema(title = "评价模式名称")
    private String evaluationModeName = Strings.EMPTY;
    @Schema(title = "状态描述")
    private String statusDis = Strings.EMPTY;

    @Schema(title = "(1.未修复;2.已修复;3.未发现;4.合格;5.不合格)")
    private Integer status = 0;


    @Schema(title = "来源;0、正常录入 1、百格图 2、QG检项项 3、问题排查")
    private Integer source = -1;
    @Schema(title = "激活时间")
    private Date creationDate = new Date();

    @Schema(title = "备注")
    private String remark = Strings.EMPTY;

    @Schema(title = "是否翻库")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isFlippingLibrary = false;
}
