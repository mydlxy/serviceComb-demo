package com.ca.mfd.prc.rc.rcavi.dto;

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
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * CachesItemsVO class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "CachesItemsVO")
public class CachesItemsVO implements Serializable {
    @Schema(title = "主键")
    @JsonAlias(value = {"id", "Id"})
    private String id;

    @Schema(title = "车辆识别码")
    @JsonAlias(value = {"sn", "Sn"})
    private String sn;

    @Schema(title = "顺序号")
    @JsonAlias(value = {"displayNo", "DisplayNo"})
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long displayNo;

    @Schema(title = "路由区")
    @JsonAlias(value = {"areaId", "AreaId"})
    private String rcRouteAreaId;

    @Schema(title = "车道号")
    //@JsonAlias(value = {"rcRouteLaneCode", "RcRouteLaneCode"})
    @JsonAlias(value = {"laneCode", "LaneCode"})
    private Integer rcRouteLaneCode;


    @Schema(title = "车道外键")
    @JsonAlias(value = {"laneId", "LaneId"})
    @JsonProperty("laneId")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcAviRouteLaneId;

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
