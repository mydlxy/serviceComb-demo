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
 * @Description: 路由点实体
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点")
@TableName("PRC_RC_BS_ROUTE_POINT")
public class RcBsRoutePointEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_BS_ROUTE_POINT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 路由点代码
     */
    @Schema(title = "路由点代码")
    @TableField("POINT_CODE")
    private String pointCode = StringUtils.EMPTY;


    /**
     * 路由点名称
     */
    @Schema(title = "路由点名称")
    @TableField("POINT_NAME")
    private String pointName = StringUtils.EMPTY;


    /**
     * 路由点类型 1进缓存 2出缓存
     */
    @Schema(title = "路由点类型 1进缓存 2出缓存")
    @TableField("POINT_TYPE")
    private Integer pointType = 0;


    /**
     * 连接DB块
     */
    @Schema(title = "连接DB块")
    @TableField("DB_NAME")
    private String dbName = StringUtils.EMPTY;


    /**
     * 路由区外键
     */
    @Schema(title = "路由区外键")
    @TableField("PRC_RC_BS_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcBsRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 路由模式(1手动,2自动)
     */
    @Schema(title = "路由模式(1手动,2自动)")
    @TableField("MODE")
    private Integer mode = 0;


    /**
     * 策略外键
     */
    @Schema(title = "策略外键")
    @TableField("PRC_RC_BS_POLICY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("policyId")
    @JsonAlias(value = {"policyId", "PolicyId"})
    private Long prcRcBsPolicyId = Constant.DEFAULT_ID;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;


    /**
     * 是否发布点
     */
    @Schema(title = "是否发布点")
    @TableField("IS_PUBLISH")
    private Boolean isPublish = false;


    /**
     * 是否有转台
     */
    @Schema(title = "是否有转台")
    @TableField("HAS_STAGE")
    private Boolean hasStage;


    /**
     * 转台模式
     */
    @Schema(title = "转台模式")
    @TableField("STAGE_MODE")
    private Integer stageMode;

}