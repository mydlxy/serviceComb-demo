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
 * @Description: 路由区实体
 * @date 2023年08月24日
 * @变更说明 BY inkelink At 2023年08月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "路由区")
@TableName("PRC_RC_SML_ROUTE_AREA")
public class RcSmlRouteAreaEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_RC_SML_ROUTE_AREA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 路由点代码
     */
    @Schema(title = "路由点代码")
    @TableField("AREA_CODE")
    private Integer areaCode = 0;


    /**
     * 路由点名称
     */
    @Schema(title = "路由点名称")
    @TableField("AREA_NAME")
    private String areaName = StringUtils.EMPTY;


    /**
     * 缓存区代码
     */
    @Schema(title = "缓存区代码")
    @TableField("BUFFER_CODE")
    private String bufferCode = StringUtils.EMPTY;


    /**
     * 连接 PLC
     */
    @Schema(title = "连接 PLC")
    @TableField("PLC_CONNECTER")
    private String plcConnecter = StringUtils.EMPTY;


    /**
     * 车辆队列DB
     */
    @Schema(title = "车辆队列DB")
    @TableField("VEHICLE_DB")
    private String vehicleDb = StringUtils.EMPTY;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


}