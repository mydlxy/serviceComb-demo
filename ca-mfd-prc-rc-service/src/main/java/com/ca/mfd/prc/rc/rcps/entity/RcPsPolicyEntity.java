package com.ca.mfd.prc.rc.rcps.entity;

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
 * @Description: 路由策略实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由策略")
@TableName("PRC_RC_PS_POLICY")
public class RcPsPolicyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_PS_POLICY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 外键
     */
    @Schema(title = "外键")
    @TableField("PRC_RC_PS_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcPsRouteAreaId = Constant.DEFAULT_ID;


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
     * 策略类型 1 进路由  2 出路由
     */
    @Schema(title = "策略类型 1 进路由  2 出路由")
    @TableField("POLICY_TYPE")
    private Integer policyType = 0;


    /**
     * 显示顺序
     */
    @Schema(title = "显示顺序")
    @TableField("DISPLAY_NO")
    private Integer displayNo = 0;
}