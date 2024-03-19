package com.ca.mfd.prc.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @author eric.zhou
 * @Description: VIN推迟时间配置实体
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "VIN推迟时间配置")
@TableName("PRC_PPS_VT_VIN_TIME")
public class PpsVtVinTimeEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_VT_VIN_TIME_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 国家名称
     */
    @Schema(title = "国家名称")
    @TableField("STATE_NAME")
    private String stateName = StringUtils.EMPTY;

    /**
     * 国家标识
     */
    @Schema(title = "国家标识")
    @TableField("STATE_IDENTIFY")
    private String stateIdentify = StringUtils.EMPTY;

    /**
     * 国家定编标识
     */
    @Schema(title = "国家定编标识")
    @TableField("STATE_ORDER_SIGN")
    private String stateOrderSign = StringUtils.EMPTY;


    /**
     * 实际年份
     */
    @Schema(title = "实际年份")
    @TableField("NOW_YEAR")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer nowYear;

    /**
     * 开始月份
     */
    @Schema(title = "开始月份")
    @TableField("START_MONTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer startMonth;

    /**
     * 结束月份
     */
    @Schema(title = "结束月份")
    @TableField("END_MONTH")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer endMonth;

    /**
     * VIN年份
     */
    @Schema(title = "VIN年份")
    @TableField("VIN_YEAR")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer vinYear;

}