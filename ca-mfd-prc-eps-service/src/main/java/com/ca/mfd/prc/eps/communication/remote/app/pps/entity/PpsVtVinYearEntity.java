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
 * @author inkelink ${email}
 * @Description: VIN号年份配置
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "VIN号年份配置")
@TableName("PRC_PPS_VT_VIN_YEAR")
public class PpsVtVinYearEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_VT_VIN_YEAR_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 年份
     */
    @Schema(title = "年份")
    @TableField("YEAR")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer year = 0;

    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("VIN_YEAR_CODE")
    private String vinYearCode = StringUtils.EMPTY;

}
