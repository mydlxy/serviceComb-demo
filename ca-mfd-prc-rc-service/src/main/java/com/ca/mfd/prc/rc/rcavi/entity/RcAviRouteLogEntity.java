package com.ca.mfd.prc.rc.rcavi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 路由记录实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由记录")
@TableName("PRC_RC_AVI_ROUTE_LOG")
public class RcAviRouteLogEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_AVI_ROUTE_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "路由区外键")
    @TableField("PRC_RC_AVI_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcAviRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 路由区代码
     */
    @Schema(title = "路由区代码")
    @TableField("AREA_CODE")
    private String areaCode = StringUtils.EMPTY;


    /**
     * 路由区名称
     */
    @Schema(title = "路由区名称")
    @TableField("AREA_NAME")
    private String areaName = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "路由点外键")
    @TableField("PRC_RC_AVI_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("pointId")
    @JsonAlias(value = {"pointId", "PointId"})
    private Long prcRcAviRoutePointId = Constant.DEFAULT_ID;


    /**
     * 路由点代码
     */
    @Schema(title = "路由点代码")
    @TableField("POINT_CODE")
    private String pointCode = StringUtils.EMPTY;


    /**
     * 路由点名称
     */
    @Schema(title = "路由点名称")
    @TableField("POINT_NAME")
    private String pointName = StringUtils.EMPTY;


    /**
     * 策略ID
     */
    @Schema(title = "策略ID")
    @TableField("PRC_RC_AVI_POLICY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("policyId")
    @JsonAlias(value = {"policyId", "PolicyId"})
    private Long prcRcAviPolicyId = Constant.DEFAULT_ID;


    /**
     * 策略代码
     */
    @Schema(title = "策略代码")
    @TableField("POLICY_CODE")
    private String policyCode = StringUtils.EMPTY;


    /**
     * 策略描述
     */
    @Schema(title = "策略描述")
    @TableField("POLICY_NAME")
    private String policyName = StringUtils.EMPTY;


    /**
     * 路由规则ID
     */
    @Schema(title = "路由规则ID")
    @TableField("PRC_RC_AVI_RULE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("ruleId")
    @JsonAlias(value = {"ruleId", "RuleId"})
    private Long prcRcAviRuleId = Constant.DEFAULT_ID;


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
     * 路由值返回
     */
    @Schema(title = "路由值返回")
    @TableField("ROUTE_VALUE")
    private String routeValue = StringUtils.EMPTY;


    /**
     * 路由去向说明
     */
    @Schema(title = "路由去向说明")
    @TableField("ROUTE_TEXT")
    private String routeText = StringUtils.EMPTY;


    /**
     * 产品唯一编码
     */
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 触发时间
     */
    @Schema(title = "触发时间")
    @TableField("TRIGGER_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date triggerDt;
}