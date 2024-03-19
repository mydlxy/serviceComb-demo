package com.ca.mfd.prc.rc.rcbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 路由点移行机信息实体
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点移行机信息")
@TableName("PRC_RC_BS_ROUTE_MOVING_MACHINE")
public class RcBsRouteMovingMachineEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_BS_ROUTE_MOVING_MACHINE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "路由区ID")
    @TableField("PRC_RC_BS_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcBsRouteAreaId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "路由点ID")
    @TableField("PRC_RC_BS_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("pointId")
    @JsonAlias(value = {"pointId", "PointId"})
    private Long prcRcBsRoutePointId = Constant.DEFAULT_ID;


    /**
     * 移行机代码
     */
    @Schema(title = "移行机代码")
    @TableField("MOVING_MACHINE_CODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
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
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer movingMachineType = 1;


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