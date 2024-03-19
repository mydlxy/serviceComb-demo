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
 * @Description: 工艺数据
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工艺数据")
@TableName("PRC_EPS_VEHICLE_WO_DATA")
public class EpsVehicleWoDataEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_WO_DATA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 产品编码
     */
    @Schema(title = "产品编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 车间编号
     */
    @Schema(title = "车间编号")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体名称
     */
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * 线体编号
     */
    @Schema(title = "线体编号")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;

    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 设备名称
     */
    @Schema(title = "设备名称")
    @TableField("DECVICE_NAME")
    private String decviceName = StringUtils.EMPTY;

    /**
     * 关联数据表名称
     */
    @Schema(title = "关联数据表名称")
    @TableField("DATA_TABLE_NAME")
    private String dataTableName = StringUtils.EMPTY;

    /**
     * 工艺ID（为空代码没有对应工艺）
     */
    @Schema(title = "工艺ID（为空代码没有对应工艺）")
    @TableField("PRC_EPS_VEHICLE_WO_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcEpsVehicleWoId = Constant.DEFAULT_ID;

    /**
     * 0 未知 1 OK 2 NG 3 Bypass
     */
    @Schema(title = "0未知1OK2NG3Bypass")
    @TableField("RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField(exist = false)
    private String barcode = StringUtils.EMPTY;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField(exist = false)
    private String sequenceNo = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField(exist = false)
    private String remark = StringUtils.EMPTY;

}
