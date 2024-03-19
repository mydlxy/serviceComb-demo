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
 * @author inkelink
 * @Description: 追溯设备工艺配置服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "追溯设备工艺配置服务")
@TableName("PRC_EPS_VEHICLE_EQUMENT_CONFIG")
public class EpsVehicleEqumentConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_EQUMENT_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 如果是轮询 轮询周期按照 毫秒
     */
    @Schema(title = "如果是轮询 轮询周期按照 毫秒")
    @TableField("COLLECT_CYCLE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer collectCycle = 0;

    /**
     * 采集方式 0.轮询，1 触发
     */
    @Schema(title = "采集方式 0.轮询，1 触发")
    @TableField("COLLECT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer collectType = 0;

    /**
     * 设备存储表
     */
    @Schema(title = "设备存储表")
    @TableField("EQUMENT_TABLEENUM")
    private String equmentTableEnum = StringUtils.EMPTY;

    /**
     * 采集URL
     */
    @Schema(title = "采集URL")
    @TableField("COLLECT_URL")
    private String collectUrl = StringUtils.EMPTY;

    /**
     * ESWITCHTOKEN
     */
    @Schema(title = "ESWITCHTOKEN")
    @TableField("ESWICH_TOKEN")
    private String eswichToken = StringUtils.EMPTY;

    /**
     * 工位编码
     */
    @Schema(title = "工位编码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 设备名称
     */
    @Schema(title = "设备名称")
    @TableField("EQUMENT_NAME")
    private String equmentName = StringUtils.EMPTY;

    /**
     * 设备编号
     */
    @Schema(title = "设备编号")
    @TableField("EQUMENT_NO")
    private String equmentNo = StringUtils.EMPTY;

    /**
     * 工艺区段
     */
    @Schema(title = "工艺区段")
    @TableField("WO_ARE")
    private String woAre = StringUtils.EMPTY;

    /**
     * 工艺代码
     */
    @Schema(title = "工艺代码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;


    /**
     * 采集产品唯一码字段
     */
    @Schema(title = "采集产品唯一码字段")
    @TableField("COLLECT_VIN_PROPERTY")
    private String collectVinProperty = StringUtils.EMPTY;
}