package com.ca.mfd.prc.rc.rcbdc.entity;

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
 * @author inkelink
 * @Description: 路由规则实体
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由规则")
@TableName("PRC_BDC_RULE")
public class RcBdcRuleEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_BDC_RULE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 路由规则代码
     */
    @Schema(title = "路由规则代码")
    @TableField("RULE_CODE")
    private String ruleCode = StringUtils.EMPTY;


    /**
     * 路由规则名称
     */
    @Schema(title = "路由规则名称")
    @TableField("RULE_NAME")
    private String ruleName = StringUtils.EMPTY;


    /**
     * 规则类型 1进路由可用  2出路由可用
     */
    @Schema(title = "规则类型 1进路由可用  2出路由可用")
    @TableField("RULE_TYPE")
    private Integer ruleType = 0;


    /**
     * 参数名配置
     */
    @Schema(title = "参数名配置")
    @TableField("PARAM_NAME")
    private String paramName = StringUtils.EMPTY;


    /**
     * 参数是否必填
     */
    @Schema(title = "参数是否必填")
    @TableField("PARAM_REQUIRED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean paramRequired = false;


    /**
     * 参数方式(1下拉2文本框)
     */
    @Schema(title = "参数方式(1下拉2文本框)")
    @TableField("PARAM_TYPE")
    private Integer paramType = 0;


}