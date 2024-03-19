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
 * @author inkelink
 * @Description: 重置车辆日志实体
 * @date 2023年08月25日
 * @变更说明 BY inkelink At 2023年08月25日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "重置车辆日志")
@TableName("PRC_PPS_RESET_VEHICLE_LOG")
public class PpsResetVehicleLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_RESET_VEHICLE_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车辆VIN号
     */
    @Schema(title = "车辆VIN号")
    @TableField("VIN")
    private String vin = StringUtils.EMPTY;


    /**
     * 最后结束的AVI站点
     */
    @Schema(title = "最后结束的AVI站点")
    @TableField("END_AVI_CODE")
    private String endAviCode = StringUtils.EMPTY;

    /**
     * 重置车间
     */
    @Schema(title = "重置车间")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


}