package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: 车辆去向指定记录实体
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "车辆去向指定记录")
@TableName("PRC_PQS_QUALITY_ROUTE_RECORD")
public class PqsQualityRouteRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_QUALITY_ROUTE_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 路由点位ID
     */
    @Schema(title = "路由点位ID")
    @TableField("PRC_PQS_QUALITY_ROUTE_POINT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsQualityRoutePointId = Constant.DEFAULT_ID;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 唯一码
     */
    @Schema(title = "唯一码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 区域代码
     */
    @Schema(title = "区域代码")
    @TableField("AREA_CODE")
    private String areaCode = StringUtils.EMPTY;


    /**
     * 区域名称
     */
    @Schema(title = "区域名称")
    @TableField("AREA_NAME")
    private String areaName = StringUtils.EMPTY;


    /**
     * 调度代码
     */
    @Schema(title = "调度代码")
    @TableField("RC_CODE")
    private String rcCode = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 是否处理
     */
    @Schema(title = "是否处理")
    @TableField("IS_HANDLE")
    private Boolean isHandle = false;


}