package com.ca.mfd.prc.eps.communication.remote.app.pps.entity;

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
 * @Description: 电池RIN码时间配置实体
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "电池RIN码时间配置")
@TableName("PRC_PPS_RIN_TIME_CONFIG")
public class PpsRinTimeConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_RIN_TIME_CONFIG_ID", type = IdType.INPUT)
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
     * 日期编码（YYYY、MM、DD）
     */
    @Schema(title = "日期编码（YYYY、MM、DD）")
    @TableField("DATEPART")
    private String datepart = StringUtils.EMPTY;


    /**
     * 填充下标
     */
    @Schema(title = "填充下标")
    @TableField("POSITION")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer position = 0;


    /**
     * 日期值
     */
    @Schema(title = "日期值")
    @TableField("DATE_VALUE")
    private String dateValue = StringUtils.EMPTY;

    /**
     * 时间代码
     */
    @Schema(title = "时间代码")
    @TableField("TIME_CODE")
    private String timeCode = StringUtils.EMPTY;

}