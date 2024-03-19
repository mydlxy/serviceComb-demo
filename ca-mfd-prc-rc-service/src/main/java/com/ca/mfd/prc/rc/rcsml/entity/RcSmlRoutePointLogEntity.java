package com.ca.mfd.prc.rc.rcsml.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由点操作履历")
@TableName("PRC_RC_SML_ROUTE_POINT_LOG")
public class RcSmlRoutePointLogEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_RC_SML_ROUTE_POINT_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_RC_SML_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcRcSmlRoutePointId = Constant.DEFAULT_ID;


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
     *
     */
    @Schema(title = "")
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