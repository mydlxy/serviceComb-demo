package com.ca.mfd.prc.rc.rcsml.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

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
    private String id;

    @Schema(title = "代码")
    @JsonAlias(value = {"code", "Code"})
    private Integer code;

    @Schema(title = "名称")
    @JsonAlias(value = {"name", "Name"})
    private String name;

    @Schema(title = "排序")
    @JsonAlias(value = {"displayNo", "Name"})
    private Integer displayNo;

    @Schema(title = "路由区")
    @JsonAlias(value = {"rcRouteAreaId", "RcRouteAreaId"})
    private String rcRouteAreaId;

    @Schema(title = "路由区代码")
    @JsonAlias(value = {"rcRouteAreaCode", "RcRouteAreaCode"})
    private Integer rcRouteAreaCode;

    @Schema(title = "路由区名称")
    @JsonAlias(value = {"rcRouteAreaName", "RcRouteAreaName"})
    private String rcRouteAreaName;

    @Schema(title = "最大容量")
    @JsonAlias(value = {"maxValue", "MaxValue"})
    private Integer maxValue;

    @Schema(title = "车道属性 1正常车道  2 返回道 3直通道 4维修车道")
    @JsonAlias(value = {"laneType", "LaneType"})
    private Integer laneType;

    @Schema(title = "缓存列表")
    @JsonAlias(value = {"caches", "Caches"})
    private List<CachesItemsVO> caches;

    @Schema(title = "创建人")
    @JsonAlias(value = {"createdBy", "CreatedBy"})
    private String createdBy;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonAlias(value = {"creationDate", "CreationDate"})
    private Date creationDate = new Date();


    @Schema(title = "最后更新人员")
    @JsonAlias(value = {"lastUpdatedBy", "LastUpdatedBy"})
    private String lastUpdatedBy = StringUtils.EMPTY;

    @Schema(title = "最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonAlias(value = {"lastUpdatedDate", "LastUpdatedDate"})
    private Date lastUpdatedDate = new Date();

    @Schema(title = "最后更新请求标识")
    @JsonAlias(value = {"lastUpdatedTraceid", "LastUpdatedTraceid"})
    private String lastUpdatedTraceid;

    @Schema(title = "并发控制字段")
    @JsonAlias(value = {"updateControlId", "UpdateControlId"})
    private String updateControlId;
}
