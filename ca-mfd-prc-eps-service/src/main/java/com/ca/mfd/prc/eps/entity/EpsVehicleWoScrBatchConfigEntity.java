package com.ca.mfd.prc.eps.entity;

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
 * @Description: 批次件自动追溯配置
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "批次件自动追溯配置")
@TableName("PRC_EPS_VEHICLE_WO_SCR_BATCH_CONFIG")
public class EpsVehicleWoScrBatchConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_WO_SCR_BATCH_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 工艺编号
     */
    @Schema(title = "工艺编号")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BAR_CODE")
    private String barCode = StringUtils.EMPTY;

    /**
     * 计数器
     */
    @Schema(title = "计数器")
    @TableField("COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer count = 0;


}
