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
 * @Description: 追溯操作记录
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "追溯操作记录")
@TableName("PRC_EPS_VEHICLE_WO_DATA_TRC")
public class EpsVehicleWoDataTrcEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_WO_DATA_TRC_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 数据编号
     */
    @Schema(title = "数据编号")
    @TableField("PRC_EPS_VEHICLE_WO_DATA_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoDataId = Constant.DEFAULT_ID;

    /**
     * 操作数据编号
     */
    @Schema(title = "操作数据编号")
    @TableField("PRC_EPS_VEHICLE_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoId = Constant.DEFAULT_ID;

    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;

    /**
     * 零件号
     */
    @Schema(title = "零件号")
    @TableField("PARTS_NUMBER")
    private String partsNumber = StringUtils.EMPTY;


    /**
     * 序列号
     */
    @Schema(title = "序列号")
    @TableField("SERIAL_NUMBER")
    private String serialNumber = StringUtils.EMPTY;

    /**
     * 批次号
     */
    @Schema(title = "批次号")
    @TableField("BATCH_NUMBER")
    private String batchNumber = StringUtils.EMPTY;

    /**
     * 供应商
     */
    @Schema(title = "供应商")
    @TableField("VENDOR_NUMBER")
    private String vendorNumber = StringUtils.EMPTY;

    /**
     * 组件号
     */
    @Schema(title = "组件号")
    @TableField("COMPONENT_CODE")
    private String componentCode = StringUtils.EMPTY;

    /**
     * 是否是3C零件
     */
    @Schema(title = "是否是3C零件")
    @TableField("IS_CCC")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isCcc = false;


    /**
     * 数据类型 0 正常  1 强绑  2 断点
     */
    @Schema(title = "数据类型 0 正常  1 强绑  2 断点")
    @TableField("SOURCE_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer sourceType = 0;

    /**
     * 零件类别
     */
    @Schema(title = "零件类别")
    @TableField("CATEGORY_NUMBER")
    private String categoryNumber = StringUtils.EMPTY;

}
