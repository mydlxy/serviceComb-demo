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
 * @Description: 工艺操作日志
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工艺操作日志")
@TableName("PRC_EPS_VEHICLE_WO_LOG")
public class EpsVehicleWoLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_WO_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 操作编号
     */
    @Schema(title = "操作编号")
    @TableField("PRC_EPS_VEHICLE_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoId;

    /**
     * 结果
     */
    @Schema(title = "结果")
    @TableField("RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;

    /**
     * 操作描述
     */
    @Schema(title = "操作描述")
    @TableField("WO_DESCRIPTION")
    private String woDescription = StringUtils.EMPTY;

}
