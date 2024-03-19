package com.ca.mfd.prc.eps.remote.app.pps.entity;

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
 * @author inkelink ${email}
 * @Description: VIN配置, 前7位
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "VIN配置,前7位")
@TableName("PRC_PPS_VT_VIN_RULE")
public class PpsVtVinRuleEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_VT_VIN_RULE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 物料编号
     */
    @Schema(title = "物料编号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;

    /**
     * 车型
     */
    @Schema(title = "车型")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;

    /**
     * VIN码前八位
     */
    @Schema(title = "VIN码前八位")
    @TableField("VIN_CODE")
    private String vinCode = StringUtils.EMPTY;


}
