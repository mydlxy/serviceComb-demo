package com.ca.mfd.prc.rc.rcps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * LaneCacheVO class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "LaneCacheVO")
public class LaneCacheVO implements Serializable {

    @Schema(title = "主键")
    @JsonAlias(value = {"id", "Id"})
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id;

    @Schema(title = "外键")
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcPsRouteAreaId;

    @Schema(title = "代码")
    @JsonAlias(value = {"laneCode", "LaneCode"})
    private Integer laneCode;

    @Schema(title = "名称")
    @JsonAlias(value = {"laneName", "LaneName"})
    private String laneName;

    @Schema(title = "排序")
    @JsonAlias(value = {"displayNo", "DisplayNo"})
    private Integer displayNo;

    //    @Schema(title = "路由区")
    //    @JsonAlias(value = {"rcRouteAreaId", "RcRouteAreaId"})
    //    private String rcRouteAreaId;

    @Schema(title = "路由区代码")
    @JsonProperty("areaCode")
    @JsonAlias(value = {"areaCode", "AreaCode"})
    private Integer rcRouteAreaCode;

    @Schema(title = "路由区名称")
    @JsonProperty("areaName")
    @JsonAlias(value = {"areaName", "AreaName"})
    private String rcRouteAreaName;

    @Schema(title = "最大容量")
    @JsonAlias(value = {"maxValue", "MaxValue"})
    private Integer maxValue;

    @Schema(title = "最大计算层级")
    @JsonAlias(value = {"maxLevel", "MaxLevel"})
    private Integer maxLevel;

    @Schema(title = "车道属性 1正常车道  2 返回道 3直通道 4维修车道")
    @JsonAlias(value = {"laneType", "LaneType"})
    private Integer laneType;


    @Schema(title = "缓存列表")
    @JsonAlias(value = {"caches", "Caches"})
    private List<CachesItemsVO> caches;

    @Schema(title = "创建人")
    @JsonAlias(value = {"createdUser", "CreatedUser"})
    private String createdUser;

    @Schema(title = "创建人")
    @JsonAlias(value = {"createdBy", "CreatedBy"})
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long createdBy;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonAlias(value = {"creationDate", "CreationDate"})
    private Date creationDate = new Date();

    @Schema(title = "最后更新人")
    @JsonAlias(value = {"lastUpdatedUser", "LastUpdatedUser"})
    private String lastUpdatedUser;

    @Schema(title = "最后更新人员")
    @JsonAlias(value = {"lastUpdatedBy", "LastUpdatedBy"})
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long lastUpdatedBy;

    @Schema(title = "最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonAlias(value = {"lastUpdateDate", "LastUpdateDate"})
    private Date lastUpdateDate = new Date();

    @Schema(title = "最后更新请求标识")
    @JsonAlias(value = {"lastUpdatedTraceid", "LastUpdatedTraceid"})
    private String lastUpdatedTraceid;

    @Schema(title = "并发控制字段")
    @JsonAlias(value = {"updateControlId", "UpdateControlId"})
    private String updateControlId;
}
