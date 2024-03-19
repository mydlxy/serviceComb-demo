package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 问题预警配置实体
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Data
@EqualsAndHashCode(callSuper = false, of = {"policyNo"})
@Schema(description= "问题预警配置")
@TableName("PRC_PQS_ISSUE_POLICY")
public class PqsIssuePolicyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_ISSUE_POLICY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 策略编号
     */
    @Schema(title = "策略编号")
    @TableField("POLICY_NO")
    private String policyNo = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 缺陷等级
     */
    @Schema(title = "缺陷等级")
    @TableField("GRADE_CODE")
    private String gradeCode = StringUtils.EMPTY;


    /**
     * 分类标准
     */
    @Schema(title = "分类标准")
    @TableField("GRADE_NAME")
    private String gradeName = StringUtils.EMPTY;


    /**
     * 组合缺陷代码
     */
    @Schema(title = "组合缺陷代码")
    @TableField("DEFECT_ANOMALY_CODE")
    private String defectAnomalyCode = StringUtils.EMPTY;


    /**
     * ICC缺陷
     */
    @Schema(title = "ICC缺陷")
    @TableField("DEFECT_ANOMALY_DESCRIPTION")
    private String defectAnomalyDescription = StringUtils.EMPTY;


    /**
     * 计数
     */
    @Schema(title = "计数")
    @TableField("COUNTER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer counter = 0;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnabled = false;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 数据校验
     */
    @TableField(exist = false)
    private boolean dataCheck = true;
}