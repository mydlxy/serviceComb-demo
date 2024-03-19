package com.ca.mfd.prc.pqs.communication.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: ICC接口中间表实体
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Data
public class IccDto {

    /**
     * 主键
     */
    /*@Schema(title = "主键")
    @JsonAlias(value = {"ID","id"})
    private Long id = Constant.DEFAULT_ID;*/



    /**
     * VRT代码
     */
    @Schema(title = "VRT代码")
    @JsonAlias(value = {"ICCVRTCODE","iccVrtCode"})
    private String iccVrtCode = StringUtils.EMPTY;


    /**
     * VRT名称
     */
    @Schema(title = "VRT名称")
    @JsonAlias(value = {"ICCVRTNAME","iccVrtName"})
    private String iccVrtName = StringUtils.EMPTY;


    /**
     * VFG代码
     */
    @Schema(title = "VFG代码")
    @JsonAlias(value = {"ICCVFGCODE","iccVfgCode"})
    private String iccVfgCode = StringUtils.EMPTY;


    /**
     * VFG名称
     */
    @Schema(title = "VFG名称")
    @JsonAlias(value = {"ICCVFGNAME","iccVfgName"})
    private String iccVfgName = StringUtils.EMPTY;


    /**
     * CCC代码
     */
    @Schema(title = "CCC代码")
    @JsonAlias(value = {"ICCCCCCODE","iccCccCode"})
    private String iccCccCode = StringUtils.EMPTY;


    /**
     * CCC名称
     */
    @Schema(title = "CCC名称")
    @JsonAlias(value = {"ICCCCCNAME","iccCccName"})
    private String iccCccName = StringUtils.EMPTY;


    /**
     * 部位
     */
    @Schema(title = "部位")
    @JsonAlias(value = {"ICCPARTS","iccParts"})
    private String iccParts = StringUtils.EMPTY;


    /**
     * 模式
     */
    @Schema(title = "模式")
    @JsonAlias(value = {"ICCMODEL","iccModel"})
    private String iccModel = StringUtils.EMPTY;


    /**
     * ICC代码
     */
    @Schema(title = "ICC代码")
    @JsonAlias(value = {"ICCCODE","iccCode"})
    private String iccCode = StringUtils.EMPTY;


    /**
     * 故障名称
     */
    @Schema(title = "故障名称")
    @JsonAlias(value = {"ICCFAULTNAME","iccFaultName"})
    private String iccFaultName = StringUtils.EMPTY;


    /**
     * 分类标准编码
     */
    @Schema(title = "分类标准编码")
    @JsonAlias(value = {"ICCTAXONOMYCODE","iccTaxonomyCode"})
    private String iccTaxonomyCode = StringUtils.EMPTY;


    /**
     * 分类标准名称
     */
    @Schema(title = "分类标准名称")
    @JsonAlias(value = {"ICCTAXONOMYNAME","iccTaxonomyName"})
    private String iccTaxonomyName = StringUtils.EMPTY;


    /**
     * 等级
     */
    @Schema(title = "等级")
    @JsonAlias(value = {"ICCGRADE","iccGrade"})
    private String iccGrade = StringUtils.EMPTY;


    /**
     * 分值
     */
    @Schema(title = "分值")
    @JsonAlias(value = {"ICCSCORE","iccScore"})
    private Integer iccScore = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @JsonAlias(value = {"ICCREMARK","iccRemark"})
    private String iccRemark = StringUtils.EMPTY;




    /**
     * 主数据状态0：正常， 2：废止
     */
    @Schema(title = "主数据状态0：正常， 2：废止")
    @JsonAlias(value = {"STATUS","status"})
    private Integer status = 0;


    /**
     * 变化记录序列号
     */
    @Schema(title = "变化记录序列号")
    @JsonAlias(value = {"SUB_ID","subId"})
    private Long subId = Constant.DEFAULT_ID;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @JsonAlias(value = {"OP_CODE","opCode"})
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