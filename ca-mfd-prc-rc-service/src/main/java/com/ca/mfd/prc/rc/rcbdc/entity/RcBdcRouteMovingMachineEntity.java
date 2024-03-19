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
 * @Description: 路由点移行机信息实体
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点移行机信息")
@TableName("PRC_BDC_ROUTE_MOVING_MACHINE")
public class RcBdcRouteMovingMachineEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_BDC_ROUTE_MOVING_MACHINE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_BDC_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcRcBdcRouteAreaId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_BDC_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcRcBdcRoutePointId = Constant.DEFAULT_ID;


    /**
     * 移行机代码
     */
    @Schema(title = "移行机代码")
    @TableField("MOVING_MACHINE_CODE")
    private Integer movingMachineCode = 0;


    /**
     * 移行机名称
     */
    @Schema(title = "移行机名称")
    @TableField("MOVING_MACHINE_NAME")
    private String movingMachineName = StringUtils.EMPTY;


    /**
     * 移行机类型：1按车道升序2按车道号降序3先进先出
     */
    @Schema(title = "移行机类型：1按车道升序2按车道号降序3先进先出")
    @TableField("MOVING_MACHINE_TYPE")
    private Integer movingMachineType = 0;


    /**
     * 原位车道
     */
    @Schema(title = "原位车道")
    @TableField("LANE_CODE_HOME")
    private Integer laneCodeHome = 0;


    /**
     * 适用车道
     */
    @Schema(title = "适用车道")
    @TableField("LANE_CODE")
    private String laneCode = StringUtils.EMPTY;


}