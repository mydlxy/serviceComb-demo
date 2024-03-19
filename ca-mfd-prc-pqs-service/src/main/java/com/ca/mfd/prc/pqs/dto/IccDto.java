package com.ca.mfd.prc.pqs.dto;

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
     * VRT代码
     */
    @Schema(title = "VRT代码")
    private String iccVrtCode = StringUtils.EMPTY;


    /**
     * VRT名称
     */
    @Schema(title = "VRT名称")
    private String iccVrtName = StringUtils.EMPTY;


    /**
     * VFG代码
     */
    @Schema(title = "VFG代码")
    private String iccVfgCode = StringUtils.EMPTY;


    /**
     * VFG名称
     */
    @Schema(title = "VFG名称")
    private String iccVfgName = StringUtils.EMPTY;


    /**
     * CCC代码
     */
    @Schema(title = "CCC代码")
    private String iccCccCode = StringUtils.EMPTY;


    /**
     * CCC名称
     */
    @Schema(title = "CCC名称")
    private String iccCccName = StringUtils.EMPTY;


    /**
     * 部位
     */
    @Schema(title = "部位")
    private String iccParts = StringUtils.EMPTY;


    /**
     * 模式
     */
    @Schema(title = "模式")
    private String iccModel = StringUtils.EMPTY;


    /**
     * ICC代码
     */
    @Schema(title = "ICC代码")
    private String iccCode = StringUtils.EMPTY;


    /**
     * 故障名称
     */
    @Schema(title = "故障名称")
    private String iccFaultName = StringUtils.EMPTY;


    /**
     * 分类标准编码
     */
    @Schema(title = "分类标准编码")
    private String iccTaxonomyCode = StringUtils.EMPTY;


    /**
     * 分类标准名称
     */
    @Schema(title = "分类标准名称")
    private String iccTaxonomyName = StringUtils.EMPTY;


    /**
     * 等级
     */
    @Schema(title = "等级")
    private String iccGrade = StringUtils.EMPTY;


    /**
     * 分值
     */
    @Schema(title = "分值")
    private Integer iccScore = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    private String iccRemark = StringUtils.EMPTY;




    /**
     * 主数据状态0：正常， 2：废止
     */
    @Schema(title = "主数据状态0：正常， 2：废止")
    private Integer status = 0;


    /**
     * 变化记录序列号
     */
    @Schema(title = "变化记录序列号")
    private Long subId = Constant.DEFAULT_ID;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
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