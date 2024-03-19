package com.ca.mfd.prc.bdc.entity;

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
 * @Description: 路由区实体
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由区")
@TableName("PRC_BDC_ROUTE_AREA")
public class RcBdcRouteAreaEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_BDC_ROUTE_AREA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 路由代码
     */
    @Schema(title = "路由代码")
    @TableField("AREA_CODE")
    private Integer areaCode = 1;


    /**
     * 路由名称
     */
    @Schema(title = "路由名称")
    @TableField("AREA_NAME")
    private String areaName = StringUtils.EMPTY;


    /**
     * 缓存区代码
     */
    @Schema(title = "缓存区代码")
    @TableField("BUFFER_CODE")
    private String bufferCode = StringUtils.EMPTY;


    /**
     * 车辆队列DB
     */
    @Schema(title = "车辆队列DB")
    @TableField("VEHICLE_DB")
    private String vehicleDb = StringUtils.EMPTY;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * PLC链接
     */
    @Schema(title = "PLC链接")
    @TableField("PLC_CONNECTER")
    private String plcConnecter = StringUtils.EMPTY;


    /**
     * 坐标x
     */
    @Schema(title = "坐标x")
    @TableField("POSITION_X")
    private Integer positionX;


    /**
     * 坐标Y
     */
    @Schema(title = "坐标Y")
    @TableField("POSITION_Y")
    private Integer positionY;


    /**
     * 坐标Z
     */
    @Schema(title = "坐标Z")
    @TableField("POSITION_Z")
    private Integer positionZ;

    /**
     * 坐标x排序
     */
    @Schema(title = "坐标x排序")
    @TableField("POSITION_X_SORT")
    private Integer positionXSort;

    /**
     * 坐标y排序
     */
    @Schema(title = "坐标y排序")
    @TableField("POSITION_Y_SORT")
    private Integer positionYSort;

    /**
     * 坐标z排序
     */
    @Schema(title = "坐标z排序")
    @TableField("POSITION_Z_SORT")
    private Integer positionZSort;

    /**
     * 坐标z显示
     */
    @Schema(title = "坐标z显示")
    @TableField("POSITION_Z_SHOW")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer positionZShow = 1;

}