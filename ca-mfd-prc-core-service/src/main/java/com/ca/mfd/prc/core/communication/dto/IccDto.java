package com.ca.mfd.prc.core.communication.dto;

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
     * ICC主键
     */
    @Schema(title = "ICC主键")
    @JsonAlias(value = {"ICC_ID","iccId"})
    private String iccId = StringUtils.EMPTY;
    /**
     * VRT代码
     */
    @Schema(title = "VRT代码")
    @JsonAlias(value = {"ICC_VRT_CODE","iccVrtCode"})
    private String iccVrtCode = StringUtils.EMPTY;


    /**
     * VRT名称
     */
    @Schema(title = "VRT名称")
    @JsonAlias(value = {"ICC_VRT_NAME","iccVrtName"})
    private String iccVrtName = StringUtils.EMPTY;


    /**
     * VFG代码
     */
    @Schema(title = "VFG代码")
    @JsonAlias(value = {"ICC_VFG_CODE","iccVfgCode"})
    private String iccVfgCode = StringUtils.EMPTY;


    /**
     * VFG名称
     */
    @Schema(title = "VFG名称")
    @JsonAlias(value = {"ICC_VFG_NAME","iccVfgName"})
    private String iccVfgName = StringUtils.EMPTY;


    /**
     * CCC代码
     */
    @Schema(title = "CCC代码")
    @JsonAlias(value = {"ICC_CCC_CODE","iccCccCode"})
    private String iccCccCode = StringUtils.EMPTY;


    /**
     * CCC名称
     */
    @Schema(title = "CCC名称")
    @JsonAlias(value = {"ICC_CCC_NAME","iccCccName"})
    private String iccCccName = StringUtils.EMPTY;


    /**
     * 部位
     */
    @Schema(title = "部位")
    @JsonAlias(value = {"ICC_PARTS","iccParts"})
    private String iccParts = StringUtils.EMPTY;


    /**
     * 模式
     */
    @Schema(title = "模式")
    @JsonAlias(value = {"ICC_MODEL","iccModel"})
    private String iccModel = StringUtils.EMPTY;


    /**
     * ICC代码
     */
    @Schema(title = "ICC代码")
    @JsonAlias(value = {"ICC_CODE","iccCode"})
    private String iccCode = StringUtils.EMPTY;


    /**
     * 故障名称
     */
    @Schema(title = "故障名称")
    @JsonAlias(value = {"ICC_FAULT_NAME","iccFaultName"})
    private String iccFaultName = StringUtils.EMPTY;


    /**
     * 分类标准编码
     */
    @Schema(title = "分类标准编码")
    @JsonAlias(value = {"ICC_TAXONOMY_CODE","iccTaxonomyCode"})
    private String iccTaxonomyCode = StringUtils.EMPTY;


    /**
     * 分类标准名称
     */
    @Schema(title = "分类标准名称")
    @JsonAlias(value = {"ICC_TAXONOMY_NAME","iccTaxonomyName"})
    private String iccTaxonomyName = StringUtils.EMPTY;


    /**
     * 等级
     */
    @Schema(title = "等级")
    @JsonAlias(value = {"ICC_GRADE","iccGrade"})
    private String iccGrade = StringUtils.EMPTY;


    /**
     * 分值
     */
    @Schema(title = "分值")
    @JsonAlias(value = {"ICC_SCORE","iccScore"})
    private Integer iccScore = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @JsonAlias(value = {"ICC_REMARK","iccRemark"})
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