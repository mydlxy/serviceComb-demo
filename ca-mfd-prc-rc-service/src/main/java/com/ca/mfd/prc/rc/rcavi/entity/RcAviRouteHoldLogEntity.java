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
 * @Description: 路由区暂存日志表实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由区暂存日志表")
@TableName("PRC_RC_AVI_ROUTE_HOLD_LOG")
public class RcAviRouteHoldLogEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_AVI_ROUTE_HOLD_LOG_ID", type = IdType.INPUT)
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
     * 路由区代码
     */
    @Schema(title = "路由区代码")
    @TableField("POINT_CODE")
    private String pointCode = StringUtils.EMPTY;


    /**
     * 路由区名称
     */
    @Schema(title = "路由区名称")
    @TableField("POINT_NAME")
    private String pointName = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("HOLD_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long holdId = Constant.DEFAULT_ID;


    /**
     * 暂存人姓名
     */
    @Schema(title = "暂存人姓名")
    @TableField("HOLD_NAME")
    private String holdName = StringUtils.EMPTY;


    /**
     * 暂存时间
     */
    @Schema(title = "暂存时间")
    @TableField("HOLD_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date holdDt;


    /**
     * 暂存原因
     */
    @Schema(title = "暂存原因")
    @TableField("HOLD_REASON")
    private String holdReason = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("CANCEL_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long cancelId = Constant.DEFAULT_ID;


    /**
     * 取消人姓名
     */
    @Schema(title = "取消人姓名")
    @TableField("CANCEL_NAME")
    private String cancelName = StringUtils.EMPTY;


    /**
     * 取消时间
     */
    @Schema(title = "取消时间")
    @TableField("CANCEL_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date cancelDt;

    /**
     * 取消原因
     */
    @Schema(title = "取消原因")
    @TableField("CANCEL_REASON")
    private String cancelReason = StringUtils.EMPTY;

    /**
     * 车辆识别码
     */
    @Schema(title = "车辆识别码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;
}