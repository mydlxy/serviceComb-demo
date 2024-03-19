package com.ca.mfd.prc.rc.rcavi.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "RcRouteCacheItemVO")
public class RcRouteCacheItemVO implements Serializable {
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(title = "产品唯一标识")
    private String sn;

    @Schema(title = "车道ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long laneId;

    @Schema(title = "车道号")
    private int laneCode;

    @Schema(title = "顺序号")
    private String displayNo;

    @Schema(title = "缓存序列号")
    private String publishDisplayNo;

    @Schema(title = "路由区")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long areaId;

    @Schema(title = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date beginDt;

    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date creationDate;


    @Schema(title = "创建人名称")
    private String createdUser = StringUtils.EMPTY;

    @Schema(title = "创建人")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long createdBy = Constant.DEFAULT_USER_ID;

    @Schema(title = "最后更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date lastUpdateDate = new Date();

    @Schema(title = "最后更新人名称")
    private String lastUpdatedUser = StringUtils.EMPTY;

    @Schema(title = "最后更新人员")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long lastUpdatedBy = Constant.DEFAULT_USER_ID;

    @Schema(title = "是否可用, N表示不可用，Y表示可用")
    private String flag = Constant.SYS_FLAGS_YES;

    @Schema(title = "预留字段1")
    private String attribute1 = StringUtils.EMPTY;

    @Schema(title = "预留字段2")
    private String attribute2 = StringUtils.EMPTY;

    @Schema(title = "预留字段3")
    private String attribute3 = StringUtils.EMPTY;

    /**
     * 预留字段4
     */
    @Schema(title = "预留字段4")
    private String attribute4 = StringUtils.EMPTY;

    /**
     * 预留字段5
     */
    @Schema(title = "预留字段5")
    private String attribute5 = StringUtils.EMPTY;

    /**
     * 预留字段6
     */
    @Schema(title = "预留字段6")
    private String attribute6 = StringUtils.EMPTY;

    /**
     * 预留字段7
     */
    @Schema(title = "预留字段7")
    private String attribute7 = StringUtils.EMPTY;

    /**
     * 预留字段8
     */
    @Schema(title = "预留字段8")
    private String attribute8 = StringUtils.EMPTY;

    /**
     * 预留字段9
     */
    @Schema(title = "预留字段9")
    private String attribute9 = StringUtils.EMPTY;

    /**
     * 预留字段10
     */
    @Schema(title = "预留字段10")
    private String attribute10 = StringUtils.EMPTY;
}
