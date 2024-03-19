package com.ca.mfd.prc.eps.remote.app.pps.entity;

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
     * 推移时间（小时）
     */
    @Schema(title = "推移时间（小时）")
    @TableField("PASS_TIME")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer passTime = 0;


}