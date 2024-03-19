package com.ca.mfd.prc.eps.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 电池模组入箱实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池模组入箱")
@TableName("PRC_EPS_VEHICLE_WO_DATA_MODULE")
public class EpsVehicleWoDataModuleEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_WO_DATA_MODULE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 数据编号
     */
    @Schema(title = "数据编号")
    @TableField("PRC_EPS_VEHICLE_WO_DATA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcEpsVehicleWoDataId = Constant.DEFAULT_ID;


    /**
     * 操作数据编号
     */
    @Schema(title = "操作数据编号")
    @TableField("PRC_EPS_VEHICLE_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcEpsVehicleWoId = Constant.DEFAULT_ID;


    /**
     * 电池RIN码
     */
    @Schema(title = "电池RIN码")
    @TableField("RIN")
    private String rin = StringUtils.EMPTY;


    /**
     * 安装工位
     */
    @Schema(title = "安装工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 模组类型
     */
    @Schema(title = "模组类型")
    @TableField("MODULE_CODE")
    private String moduleCode = StringUtils.EMPTY;


    /**
     * 模组条码
     */
    @Schema(title = "模组条码")
    @TableField("MODULE_BARCODE")
    private String moduleBarcode = StringUtils.EMPTY;


    /**
     * 安装位置
     */
    @Schema(title = "安装位置")
    @TableField("MODULE_LOCATION")
    private String moduleLocation = StringUtils.EMPTY;


}