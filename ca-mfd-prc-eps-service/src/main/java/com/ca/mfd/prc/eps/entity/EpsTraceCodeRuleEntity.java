package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 条码追溯规则实体
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "条码追溯规则")
@TableName("PRC_EPS_TRACE_CODE_RULE")
public class EpsTraceCodeRuleEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_TRACE_CODE_RULE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 组件代码
     */
    @Schema(title = "组件代码")
    @TableField("TRACE_COMPONENT_CODE")
    private String traceComponentCode = StringUtils.EMPTY;


    /**
     * 组件名称
     */
    @Schema(title = "组件名称")
    @TableField("TRACE_COMPONENT_DESCRIPTION")
    private String traceComponentDescription = StringUtils.EMPTY;


    /**
     * 匹配BOM
     */
    @Schema(title = "匹配BOM")
    @TableField("MATCH_BOM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean matchBom = false;


    /**
     * 模板
     */
    @Schema(title = "模板")
    @TableField("TEMPLATE")
    private String template = StringUtils.EMPTY;


    /**
     * 规则名称
     */
    @Schema(title = "规则名称")
    @TableField("RULE_NAME")
    private String ruleName = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("SEQUENCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sequence = 0;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 条码长度
     */
    @Schema(title = "条码长度")
    @TableField("LENGTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer length = 0;


    /**
     * 起始位置
     */
    @Schema(title = "起始位置")
    @TableField("PARTS_BEGIN_INDEX")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer partsBeginIndex = 0;


    /**
     * 零件号长度
     */
    @Schema(title = "零件号长度")
    @TableField("PARTS_PATH_LENGTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer partsPathLength = 0;


    /**
     * 起始位置
     */
    @Schema(title = "起始位置")
    @TableField("CATEGORY_BEGIN_INDEX")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer categoryBeginIndex = 0;


    /**
     * 零件号长度
     */
    @Schema(title = "零件号长度")
    @TableField("CATEGORY_PATH_LENGTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer categoryPathLength = 0;


    /**
     * 起始位置
     */
    @Schema(title = "起始位置")
    @TableField("SERIAL_BEGIN_INDEX")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer serialBeginIndex = 0;


    /**
     * 零件号长度
     */
    @Schema(title = "零件号长度")
    @TableField("SERIAL_PATH_LENGTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer serialPathLength = 0;


    /**
     * 起始位置
     */
    @Schema(title = "起始位置")
    @TableField("BATCH_BEGIN_INDEX")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer batchBeginIndex = 0;


    /**
     * 零件号长度
     */
    @Schema(title = "零件号长度")
    @TableField("BATCH_PATH_LENGTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer batchPathLength = 0;


    /**
     * 起始位置
     */
    @Schema(title = "起始位置")
    @TableField("VENDOR_BEGIN_INDEX")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer vendorBeginIndex = 0;


    /**
     * 零件号长度
     */
    @Schema(title = "零件号长度")
    @TableField("VENDOR_PATH_LENGTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer vendorPathLength = 0;


    /**
     * 是否为3C件
     */
    @Schema(title = "是否为3C件")
    @TableField("IS_CCC")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isCcc = false;


    /**
     * 特征表达式
     */
    @Schema(title = "特征表达式")
    @TableField("FEATURE_CODE")
    private String featureCode = StringUtils.EMPTY;


}