package com.ca.mfd.prc.audit.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 精艺检修缺陷库配置实体
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "精艺检修缺陷库配置")
@TableName("PRC_PQS_EX_DEFECT_ANOMALY")
public class PqsExDefectAnomalyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_EX_DEFECT_ANOMALY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 问题属性编码
     */
    @Schema(title = "问题属性编码")
    @TableField("PROBLEM_ATTRIBUTES_CODE")
    private String problemAttributesCode = StringUtils.EMPTY;


    /**
     * 问题属性名称
     */
    @Schema(title = "问题属性名称")
    @TableField("PROBLEM_ATTRIBUTES_NAME")
    private String problemAttributesName = StringUtils.EMPTY;


    /**
     * 问题部位
     */
    @Schema(title = "问题部位")
    @TableField("COMPONENT_DESCRIPTION")
    private String componentDescription = StringUtils.EMPTY;


    /**
     * ICC代码
     */
    @Schema(title = "ICC代码")
    @TableField("DEFECT_ANOMALY_CODE")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC名称
     */
    @Schema(title = "ICC名称")
    @TableField("DEFECT_ANOMALY_DESCRIPTION")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 扣分等级代码
     */
    @Schema(title = "扣分等级代码")
    @TableField("GRADE_CODE")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 扣分分类标准
     */
    @Schema(title = "扣分分类标准")
    @TableField("GRADE_NAME")
    private String gradeName = StringUtils.EMPTY;


    /**
     * 责任部门代码
     */
    @Schema(title = "责任部门代码")
    @TableField("RESPONSIBLE_DEPT_CODE")
    private String responsibleDeptCode = StringUtils.EMPTY;


    /**
     * 责任部门名称
     */
    @Schema(title = "责任部门名称")
    @TableField("RESPONSIBLE_DEPT_NAME")
    private String responsibleDeptName = StringUtils.EMPTY;


    /**
     * 拼音简码
     */
    @Schema(title = "拼音简码")
    @TableField("SHORT_CODE")
    private String shortCode = StringUtils.EMPTY;

    @TableField(exist = false)
    private boolean dataCheck = true;

}