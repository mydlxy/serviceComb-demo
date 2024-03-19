package com.ca.mfd.prc.rc.rcavi.entity;

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
 * @Description: 路由点操作履历实体
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点操作履历")
@TableName("PRC_RC_AVI_ROUTE_SELECT_LOG")
public class RcAviRouteSelectLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RC_AVI_ROUTE_SELECT_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


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
     * 路由区代码
     */
    @Schema(title = "路由区代码")
    @TableField("AREA_CODE")
    private String areaCode = StringUtils.EMPTY;


    /**
     * 路由区名称
     */
    @Schema(title = "路由区名称")
    @TableField("AREA_NAME")
    private String areaName = StringUtils.EMPTY;


    /**
     * 路由选择外键
     */
    @Schema(title = "路由选择外键")
    @TableField("PRC_RC_AVI_ROUTE_SELECT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    @JsonProperty("selectId")
    @JsonAlias(value = {"selectId", "SelectId"})
    private Long prcRcAviRouteSelectId = Constant.DEFAULT_ID;


    /**
     * 路由选择代码
     */
    @Schema(title = "路由选择代码")
    @TableField("SELECT_CODE")
    private String selectCode = StringUtils.EMPTY;


    /**
     * 路由选择名称
     */
    @Schema(title = "路由选择名称")
    @TableField("SELECT_NAME")
    private String selectName = StringUtils.EMPTY;


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


    /**
     * 操作内容
     */
    @Schema(title = "操作内容")
    @TableField("OPERATION")
    private String operation = StringUtils.EMPTY;


    /**
     * 操作类型
     */
    @Schema(title = "操作类型")
    @TableField("OPERATION_LEVER")
    private Integer operationLever = 0;


    /**
     * 操作名称
     */
    @Schema(title = "操作名称")
    @TableField("OPERATION_NAME")
    private String operationName = StringUtils.EMPTY;


    /**
     * 操作时间
     */
    @Schema(title = "操作时间")
    @TableField("OPERATION_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date operationDt;


}