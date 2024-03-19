package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: ICC分类接口中间表实体
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Data
public class IccCategoryDto {

    /**
     * 主键
     */
    /*@Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonAlias(value = {"id","ID"})
    private Long id = Constant.DEFAULT_ID;*/

    /**
     * 分类标准/问题分类
     */
    @Schema(title = "分类标准/问题分类")
    @JsonAlias(value = {"iccCsTaxonomyIssueClassification","ICCCSTAXONOMYISSUECLASSIFICATION"})
    private String iccCsTaxonomyIssueClassification = StringUtils.EMPTY;


    /**
     * 等级/ITGW权重值
     */
    @Schema(title = "等级/ITGW权重值")
    @JsonAlias(value = {"iccCsGradeItgwWeightValue","ICCCSGRADEITGWWEIGHTVALUE"})
    private String iccCsGradeItgwWeightValue = StringUtils.EMPTY;


    /**
     * 分值/Audit分值
     */
    @Schema(title = "分值/Audit分值")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonAlias(value = {"iccCsScoreAuditScore","ICCCSSCOREAUDITSCORE"})
    private Integer iccCsScoreAuditScore = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @JsonAlias(value = {"iccCsRemark","ICCCSREMARK"})
    private String iccCsRemark = StringUtils.EMPTY;


    /**
     * 主数据状态0：正常， 2：废止
     */
    @Schema(title = "主数据状态0：正常， 2：废止")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonAlias(value = {"status","STATUS"})
    private Integer status = 0;


    /**
     * 变化记录序列号
     */
    @Schema(title = "变化记录序列号")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonAlias(value = {"subId","SUB_ID"})
    private Long subId = Constant.DEFAULT_ID;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @JsonAlias(value = {"opCode","OP_CODE"})
    private Integer opCode = 0;

    /**
     * 校验结果
     */
    @Schema(title = "校验结果")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer checkResult = 0;


    /**
     * 校验结果说明
     */
    @Schema(title = "校验结果说明")
    private String checkResultDesc = StringUtils.EMPTY;


}