package com.ca.mfd.prc.rc.rcbdc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 路由策略实体
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由策略")
@TableName("PRC_BDC_POLICY")
public class RcBdcPolicyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_BDC_POLICY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 外键
     */
    @Schema(title = "外键")
    @TableField("PRC_BDC_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcRcBdcRouteAreaId = Constant.DEFAULT_ID;


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