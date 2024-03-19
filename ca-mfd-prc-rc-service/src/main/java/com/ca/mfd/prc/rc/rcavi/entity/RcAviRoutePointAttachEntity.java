package com.ca.mfd.prc.rc.rcavi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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
 * @Description: 路由点关联附加模块实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点关联附加模块")
@TableName("PRC_RC_AVI_ROUTE_POINT_ATTACH")
public class RcAviRoutePointAttachEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_AVI_ROUTE_POINT_ATTACH_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "路由")
    @TableField("PRC_RC_AVI_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcAviRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 路由点外键
     */
    @Schema(title = "路由点外键")
    @TableField("PRC_RC_AVI_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("pointId")
    @JsonAlias(value = {"pointId", "PointId"})
    private Long prcRcAviRoutePointId = Constant.DEFAULT_ID;


    /**
     * 附加外键
     */
    @Schema(title = "附加外键")
    @TableField("ATTACH_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long attachId = Constant.DEFAULT_ID;


    /**
     * 附加模块代码
     */
    @Schema(title = "附加模块代码")
    @TableField("ATTACH_CODE")
    private String attachCode = StringUtils.EMPTY;


    /**
     * 附加模块名称
     */
    @Schema(title = "附加模块名称")
    @TableField("ATTACH_NAME")
    private String attachName = StringUtils.EMPTY;
}