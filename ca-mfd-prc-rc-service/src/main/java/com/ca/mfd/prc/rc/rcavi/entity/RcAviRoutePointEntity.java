package com.ca.mfd.prc.rc.rcavi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点")
@TableName("PRC_RC_AVI_ROUTE_POINT")
public class RcAviRoutePointEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_AVI_ROUTE_POINT_ID", type = IdType.INPUT)
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
     * 路由区外键
     */
    @Schema(title = "路由区外键")
    @TableField("PRC_RC_AVI_ROUTE_AREA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("areaId")
    @JsonAlias(value = {"areaId", "AreaId"})
    private Long prcRcAviRouteAreaId = Constant.DEFAULT_ID;


    /**
     * 连接DB块
     */
    @Schema(title = "连接DB块")
    @TableField("DB_NAME")
    private String dbName = StringUtils.EMPTY;


    /**
     * 路由模式(1手动,2自动)
     */
    @Schema(title = "路由模式(1手动,2自动)")
    @TableField("MODE")
    private Integer mode = 0;


    /**
     * 当前策略
     */
    @Schema(title = "当前策略")
    @TableField("PRC_RC_AVI_POLICY_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("policyId")
    @JsonAlias(value = {"policyId", "PolicyId"})
    private Long prcRcAviPolicyId = Constant.DEFAULT_ID;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnable = true;


    /**
     * 位置
     */
    @Schema(title = "位置")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;

    /**
     * plc连接地址
     */
    @Schema(title = "plc连接地址")
    @TableField("PLC_CONNECTER")
    private String plcConnecter = StringUtils.EMPTY;
}