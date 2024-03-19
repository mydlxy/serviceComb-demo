package com.ca.mfd.prc.eps.entity;

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
 * @Description: 采集暂存数据实体
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "采集暂存数据")
@TableName("PRC_EPS_VEHICLE_EQUMENT_HOLD")
public class EpsVehicleEqumentHoldEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_EQUMENT_HOLD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 追溯设备工艺配置外键
     */
    @Schema(title = "追溯设备工艺配置外键")
    @TableField("PRC_EPS_VEHICLE_EQUMENT_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleEqumentConfigId = Constant.DEFAULT_ID;

    /**
     * 工位
     */
    @Schema(title = "工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;

    /**
     * 采集的设备编码
     */
    @Schema(title = "采集的设备编码")
    @TableField("EQUMENT_NO")
    private String equmentNo = StringUtils.EMPTY;

    /**
     * 设备数据
     */
    @Schema(title = "设备数据")
    @TableField("WO_DATA")
    private String woData = StringUtils.EMPTY;


}