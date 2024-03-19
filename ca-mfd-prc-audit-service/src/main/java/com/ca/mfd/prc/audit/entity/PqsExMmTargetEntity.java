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

import java.math.BigDecimal;

/**
 * @author inkelink
 * @Description: 精致工艺质量月目标设置实体
 * @date 2024年01月29日
 * @变更说明 BY inkelink At 2024年01月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "精致工艺质量月目标设置")
@TableName("PRC_PQS_EX_MM_TARGET")
public class PqsExMmTargetEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_EX_MM_TARGET_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 类别;1、整车  5 冲压
     */
    @Schema(title = "类别;1、整车  5 冲压")
    @TableField("CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer category = 0;


    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 车型描述
     */
    @Schema(title = "车型描述")
    @TableField("MODEL_DESC")
    private String modelDesc = StringUtils.EMPTY;


    /**
     * 评价模式代码
     */
    @Schema(title = "评价模式代码")
    @TableField("EVALUATION_MODE_CODE")
    private String evaluationModeCode = StringUtils.EMPTY;


    /**
     * 评价模式名称
     */
    @Schema(title = "评价模式名称")
    @TableField("EVALUATION_MODE_NAME")
    private String evaluationModeName = StringUtils.EMPTY;


    /**
     * 责任部门代码
     */
    @Schema(title = "责任部门代码")
    @TableField("DEPT_CODE")
    private String deptCode = StringUtils.EMPTY;


    /**
     * 责任部门描述
     */
    @Schema(title = "责任部门描述")
    @TableField("DEPT_NAME")
    private String deptName = StringUtils.EMPTY;


    /**
     * 年份
     */
    @Schema(title = "年份")
    @TableField("YEAR")
    private String year = StringUtils.EMPTY;


    /**
     * 月份
     */
    @Schema(title = "月份")
    @TableField("MONTH")
    private String month = StringUtils.EMPTY;


    /**
     * 目标扣分
     */
    @Schema(title = "目标扣分")
    @TableField("TARGET")
    private BigDecimal target = BigDecimal.valueOf(0);


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}