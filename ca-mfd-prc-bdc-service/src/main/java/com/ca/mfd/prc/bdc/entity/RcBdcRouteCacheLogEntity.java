package com.ca.mfd.prc.bdc.entity;

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
 * @Description: 路由出车记录实体
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由出车记录")
@TableName("PRC_BDC_ROUTE_CACHE_LOG")
public class RcBdcRouteCacheLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_BDC_ROUTE_CACHE_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 路由区
     */
    @Schema(title = "路由区")
    @TableField("PRC_BDC_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcBdcRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 路由点代码
     */
    @Schema(title = "路由点代码")
    @TableField("AREA_CODE")
    private Integer areaCode = 1;


    /**
     * 路由点名称
     */
    @Schema(title = "路由点名称")
    @TableField("AREA_NAME")
    private String areaName = StringUtils.EMPTY;


    /**
     * 缓存区代码
     */
    @Schema(title = "缓存区代码")
    @TableField("BUFFER_CODE")
    private String bufferCode = StringUtils.EMPTY;


    /**
     * 产品唯一编码
     */
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long displayNo = Constant.DEFAULT_ID;


    /**
     * 结束时间
     */
    @Schema(title = "结束时间")
    @TableField("END_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endDt;


    /**
     * 车道外键
     */
    @Schema(title = "车道外键")
    @TableField("PRC_BDC_ROUTE_LANE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("LaneId")
    @JsonAlias(value = {"laneId", "LaneId"})
    private Long prcBdcRouteLaneId = Constant.DEFAULT_ID;


    /**
     * 车道号
     */
    @Schema(title = "车道号")
    @TableField("LANE_CODE")
    private Integer laneCode = 0;


    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    @TableField("BEGIN_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date beginDt = new Date();
}