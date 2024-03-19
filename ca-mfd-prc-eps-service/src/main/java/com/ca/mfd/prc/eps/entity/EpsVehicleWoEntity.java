package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: 车辆操作信息
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "车辆操作信息")
@TableName("PRC_EPS_VEHICLE_WO")
public class EpsVehicleWoEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_VEHICLE_WO_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 产品编号
     */
    @Schema(title = "产品编号")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;

    /**
     * 车间代码WorkshopCode
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 工位ID
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

//    /**
//     * 工位
//     */
//    @Schema(title = "工位")
//    @TableField("WORKSTATION_NAME")
//    private String workstationName = StringUtils.EMPTY;


    /**
     * 操作
     */
    @Schema(title = "操作")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * 操作描述
     */
    @Schema(title = "操作描述")
    @TableField("WO_DESCRIPTION")
    private String woDescription = StringUtils.EMPTY;

    /**
     * 操作顺序号
     */
    @Schema(title = "操作顺序号")
    @TableField("WO_DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer woDisplayNo = 0;

    /**
     * 操作组
     */
    @Schema(title = "操作组")
    @TableField("WO_GROUP")
    private String woGroup = StringUtils.EMPTY;

    /**
     * 工艺类型
     */
    @Schema(title = "工艺类型")
    @TableField("WO_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer woType = 0;

    /**
     * 操作类型
     */
    @Schema(title = "操作类型")
    @TableField("WO_OPER_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer woOperType = 0;

    /**
     * 缺陷编号
     */
    @Schema(title = "缺陷编号")
    @TableField("DEFECT_ANOMALY_CODE")
    private String defectAnomalyCode = StringUtils.EMPTY;

    /**
     * 操作人ID
     */
    @Schema(title = "操作人ID")
    @TableField("OPER_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long operUserId = Constant.DEFAULT_ID;

    /**
     * 批量追溯
     */
    @Schema(title = "批量追溯")
    @TableField("TRC_BY_GROUP")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean trcByGroup = false;

    /**
     * 操作人
     */
    @Schema(title = "操作人")
    @TableField("OPER_USER_NAME")
    private String operUserName = StringUtils.EMPTY;

    /**
     * 操作时间(可空)
     */
    @Schema(title = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @TableField("OPER_DT")
    private Date operDt = new Date();

    /**
     * 结果(1.OK;2.NG;3.ByPass)
     */
    @Schema(title = "结果(1.OK;2.NG;3.ByPass)")
    @TableField("RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;

    /**
     * 0 未执行 1 执行中
     */
    @Schema(title = "0 未执行 1 执行中")
    @TableField(exist = false)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer exeState = 0;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField(exist = false)
    private String barcode = StringUtils.EMPTY;

    /**
     * 执行配置
     */
    @Schema(title = "执行配置")
    @TableField(exist = false)
    private EpsVehicleWoExecuteEntity woExecuteInfo = new EpsVehicleWoExecuteEntity();
}
