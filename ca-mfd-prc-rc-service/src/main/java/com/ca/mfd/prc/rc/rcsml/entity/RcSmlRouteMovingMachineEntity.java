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
 * @Description: 路由点移行机信息实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点移行机信息")
@TableName("PRC_RC_SML_ROUTE_MOVING_MACHINE")
public class RcSmlRouteMovingMachineEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_RC_SML_ROUTE_MOVING_MACHINE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_RC_SML_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcSmlRoutePointId = Constant.DEFAULT_ID;


    /**
     * 策略代码
     */
    @Schema(title = "策略代码")
    @TableField("POINT_CODE")
    private String pointCode = StringUtils.EMPTY;


    /**
     * 策略名称
     */
    @Schema(title = "策略名称")
    @TableField("POINT_NAME")
    private String pointName = StringUtils.EMPTY;


    /**
     * 策略代码
     */
    @Schema(title = "策略代码")
    @TableField("MOVING_MACHINE_CODE")
    private Integer movingMachineCode = 0;


    /**
     * 策略名称
     */
    @Schema(title = "策略名称")
    @TableField("MOVING_MACHINE_NAME")
    private String movingMachineName = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("MOVING_MACHINE_TYPE")
    private Integer movingMachineType = 0;


    /**
     * 适用车道
     */
    @Schema(title = "适用车道")
    @TableField("LANE_CODE_HOME")
    private Integer laneCodeHome = 0;


    /**
     * 适用车道
     */
    @Schema(title = "适用车道")
    @TableField("LANE_CODE")
    private String laneCode = StringUtils.EMPTY;


}