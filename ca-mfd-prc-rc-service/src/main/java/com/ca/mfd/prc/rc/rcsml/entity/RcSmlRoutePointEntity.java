package com.ca.mfd.prc.rc.rcsml.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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
 * @Description: 路由点实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点")
@TableName("PRC_RC_SML_ROUTE_POINT")
public class RcSmlRoutePointEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_RC_SML_ROUTE_POINT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


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
     *
     */
    @Schema(title = "")
    @TableField("PRC_RC_SML_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcSmlRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 缓存区代码
     */
    @Schema(title = "缓存区代码")
    @TableField("BUFFER_CODE")
    private String bufferCode = StringUtils.EMPTY;


    /**
     * 路由区代码
     */
    @Schema(title = "路由区代码")
    @TableField("AREA_CODE")
    private Integer areaCode = 0;


    /**
     * 路由区描述
     */
    @Schema(title = "路由区描述")
    @TableField("AREA_NAME")
    private String areaName = StringUtils.EMPTY;


    /**
     * 路由模式(1手动,2自动)
     */
    @Schema(title = "路由模式(1手动,2自动)")
    @TableField("MODE")
    private Integer mode = 0;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_RC_SML_POLICY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcSmlPolicyId = Constant.DEFAULT_ID;


    /**
     * 路由策略代码
     */
    @Schema(title = "路由策略代码")
    @TableField("POLICY_CODE")
    private String policyCode = StringUtils.EMPTY;


    /**
     * 路由策略描述
     */
    @Schema(title = "路由策略描述")
    @TableField("POLICY_NAME")
    private String policyName = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_PUBLISH")
    private Boolean isPublish = false;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("HAS_STAGE")
    private Boolean hasStage = false;


    /**
     * 路由点类型 1进缓存 2出缓存
     */
    @Schema(title = "路由点类型 1进缓存 2出缓存")
    @TableField("STAGE_MODE")
    private Integer stageMode = 0;


}