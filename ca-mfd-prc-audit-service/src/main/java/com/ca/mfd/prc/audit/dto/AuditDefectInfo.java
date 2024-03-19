package com.ca.mfd.prc.audit.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

/**
 * Audit缺陷信息
 *
 * @Author: joel
 * @Date: 2023-08-20-14:22
 * @Description:
 */
@Data
@Schema(description = "Audit缺陷信息")
public class AuditDefectInfo {


    @Schema(title = "图片")
    public String img = Strings.EMPTY;
    /**
     * 显示序号
     */
    @Schema(title = "显示序号")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;
    @Schema(title = "组合代码")
    private String defectAnomalyCode = Strings.EMPTY;
    @Schema(title = "组合名称")
    private String defectAnomalyDescription = Strings.EMPTY;
    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    private String defectComponentCode = Strings.EMPTY;
    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    private String defectComponentDesc = Strings.EMPTY;
    /**
     * 分类代码
     */
    @Schema(title = "分类代码")
    private String defectCode = Strings.EMPTY;
    /**
     * 分类描述
     */
    @Schema(title = "分类描述")
    private String defectDesc = Strings.EMPTY;
    /**
     * 方位代码
     */
    @Schema(title = "方位代码")
    private String defectPositionCode = Strings.EMPTY;
    /**
     * 方位描述
     */
    @Schema(title = "方位描述")
    private String defectPositionDesc = Strings.EMPTY;
    /**
     * 等级
     */
    @Schema(title = "等级")
    private String gradeCode = Strings.EMPTY;
    /**
     * 等级
     */
    @Schema(title = "等级名称")
    private String gradeName;
    /**
     * 扣分
     */
    @Schema(title = "扣分")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer score = 0;
    /**
     * 责任部门编码
     */
    @Schema(title = "责任部门编码")
    private String responsibleDeptCode = Strings.EMPTY;
    /**
     * 责任部门描述
     */
    @Schema(title = "责任部门描述")
    private String ResponsibleDeptName = Strings.EMPTY;
    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = Strings.EMPTY;
    /**
     * 是否返修
     */
    @Schema(title = "是否返修")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isNeedRepair = false;
    /**
     * 风险问题
     */
    @Schema(title = "风险问题")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isImportantDefect = false;
    /**
     * Top问题
     */
    @Schema(title = "Top问题")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isTopDefect = false;

    /**
     * 来源 0 正常缺陷录入 1百格图缺陷录入 2 检查项图片
     */
    @Schema(title = "来源")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer source = 0;
    /**
     * 缺陷ID
     */
    @Schema(title = "缺陷ID")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long anomalyId = Constant.DEFAULT_ID;
    /**
     * 工艺编号
     */
    @Schema(title = "工艺编号")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long woId = Constant.DEFAULT_ID;

    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer level = 0;
    /**
     * 责任区域
     */
    @Schema(title = "责任区域")
    private String dutyArea = StringUtils.EMPTY;
    /**
     * 缺陷代码 组件-方位-代码
     */
    @Schema(title = "缺陷代码")
    private String code = StringUtils.EMPTY;
    /**
     * 描述 组件-方位-代码
     */
    @Schema(title = "描述")
    private String description = StringUtils.EMPTY;
    private String jsonData = StringUtils.EMPTY;

    /**
     * 是否翻库
     */
    @Schema(title = "是否翻库")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isFlippingLibrary = false;


}
