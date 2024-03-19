package com.ca.mfd.prc.rc.rcbs.entity;

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
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点关联附加模块")
@TableName("PRC_RC_BS_ROUTE_AREA_ATTACH")
public class RcBsRouteAreaAttachEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_RC_BS_ROUTE_AREA_ATTACH_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "路由区外键")
    @TableField("PRC_RC_BS_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcBsRouteAreaId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "附加模块ID")
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


    /**
     * 类型 1进缓存 2出缓存3路由区
     */
    @Schema(title = "类型 1进缓存 2出缓存3路由区")
    @TableField("ATTACH_TYPE")
    private Integer attachType = 0;

    /**
     * 附加模块名称
     */
    @Schema(title = "附加模块路由")
    @TableField("ATTACH_ROUTE")
    private String attachRoute = StringUtils.EMPTY;
}