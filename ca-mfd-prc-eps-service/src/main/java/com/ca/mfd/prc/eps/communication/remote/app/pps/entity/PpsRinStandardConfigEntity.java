package com.ca.mfd.prc.eps.communication.remote.app.pps.entity;

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
 * @author eric.zhou
 * @Description: 电池RIN码前14位配置实体
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "电池RIN码前14位配置")
@TableName("PRC_PPS_RIN_STANDARD_CONFIG")
public class PpsRinStandardConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_RIN_STANDARD_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 电池类型
     */
    @Schema(title = "电池类型")
    @TableField("PACK_MODEL")
    private String packModel = StringUtils.EMPTY;


    /**
     * RIN编码前14位
     */
    @Schema(title = "RIN编码前14位")
    @TableField("RIN_CODE")
    private String rinCode = StringUtils.EMPTY;


}