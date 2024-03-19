package com.ca.mfd.prc.rc.rcsml.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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
 * @Description: 路由策略计算缓存实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由策略计算缓存")
@TableName("PRC_RC_SML_POLICY_RULE_CACHE")
public class RcSmlPolicyRuleCacheEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_RC_SML_POLICY_RULE_CACHE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_RC_SML_POLICY_RULE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcSmlPolicyRuleId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_RC_SML_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcSmlRoutePointId = Constant.DEFAULT_ID;


    /**
     * 路由点类型 1进缓存 2出缓存
     */
    @Schema(title = "路由点类型 1进缓存 2出缓存")
    @TableField("POINT_TYPE")
    private Integer pointType = 0;


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
     * 请求序号
     */
    @Schema(title = "请求序号")
    @TableField("REQ_NO")
    private Integer reqNo = 0;


}