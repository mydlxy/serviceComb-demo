package com.ca.mfd.prc.bdc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 路由策略规则明细实体
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由策略规则明细")
@TableName("PRC_BDC_POLICY_RULE")
public class RcBdcPolicyRuleEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_BDC_POLICY_RULE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 外键
     */
    @Schema(title = "外键")
    @TableField("PRC_BDC_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcBdcRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 策略ID
     */
    @Schema(title = "策略ID")
    @TableField("PRC_BDC_POLICY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("policyId")
    @JsonAlias(value = {"policyId", "PolicyId"})
    private Long prcBdcPolicyId = Constant.DEFAULT_ID;


    /**
     * 路由规则ID
     */
    @Schema(title = "路由规则ID")
    @TableField("PRC_BDC_RULE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("ruleId")
    @JsonAlias(value = {"ruleId", "RuleId"})
    private Long prcBdcRuleId = Constant.DEFAULT_ID;


    /**
     * 参数名
     */
    @Schema(title = "参数名")
    @TableField("PARAM_NAME")
    private String paramName = StringUtils.EMPTY;


    /**
     * 参数值
     */
    @Schema(title = "参数值")
    @TableField("PARAM_VALUE")
    private String paramValue = StringUtils.EMPTY;


    /**
     * 动态值
     */
    @Schema(title = "动态值")
    @TableField("DYNAMIC_VALUE")
    private String dynamicValue = StringUtils.EMPTY;


    /**
     * 层级
     */
    @Schema(title = "层级")
    @TableField("LEVEL")
    private Integer level = 0;


    /**
     * 适用车道
     */
    @Schema(title = "适用车道")
    @TableField("LANE_CODE")
    private String laneCode = StringUtils.EMPTY;


    /**
     * 是否逻辑非
     */
    @Schema(title = "是否逻辑非")
    @TableField("IS_NOT")
    private Boolean isNot = false;
}