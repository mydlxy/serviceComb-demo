package com.ca.mfd.prc.pps.communication.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @author inkelink
 * @Description: AS整车信息实体
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AS整车信息")
@TableName("PRC_MID_AS_VEHICLE")
public class MidAsVehicleEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_AS_VEHICLE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 制造ID
     */
    @Schema(title = "制造ID")
    @TableField("VRN")
    private String vrn = StringUtils.EMPTY;


    /**
     * 工厂组织代码
     */
    @Schema(title = "工厂组织代码")
    @TableField("ORG_CODE")
    private String orgCode = StringUtils.EMPTY;


    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 订单类型
     */
    @Schema(title = "订单类型")
    @TableField("ORDER_TYPE")
    private String orderType = StringUtils.EMPTY;


    /**
     * OTD需求班次
     */
    @Schema(title = "OTD需求班次")
    @TableField("OTD_DEMAND_SHIFT")
    private String otdDemandShift = StringUtils.EMPTY;


    /**
     * OTD需求日期
     */
    @Schema(title = "OTD需求日期")
    @TableField("OTD_DEMAND_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date otdDemandDate;


    /**
     * 总装计划下线工作日期
     */
    @Schema(title = "总装计划下线工作日期")
    @TableField("ASS_OFFLINE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date assOfflineDate;


    /**
     * 总装计划下线工作班次
     */
    @Schema(title = "总装计划下线工作班次")
    @TableField("ASS_OFFLINE_SHIFT_CODE")
    private String assOfflineShiftCode = StringUtils.EMPTY;


    /**
     * 总装计划下线时间（自然时间）
     */
    @Schema(title = "总装计划下线时间（自然时间）")
    @TableField("ASS_OFFLINE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date assOfflineTime;


    /**
     * 定编号
     */
    @Schema(title = "定编号")
    @TableField("CUSTOM_CODE")
    private String customCode = StringUtils.EMPTY;


    /**
     * 销售渠道
     */
    @Schema(title = "销售渠道")
    @TableField("DEMAND_ORIGIN")
    private String demandOrigin = StringUtils.EMPTY;


    /**
     * 需求分源（销售/物流/基地）
     */
    @Schema(title = "需求分源（销售/物流/基地）")
    @TableField("DEMAND_SRC")
    private String demandSrc = StringUtils.EMPTY;


    /**
     * 释放版本代码
     */
    @Schema(title = "释放版本代码")
    @TableField("RELEASE_VER")
    private String releaseVer = StringUtils.EMPTY;


    /**
     * 计划上线日
     */
    @Schema(title = "计划上线日")
    @TableField("VC_ONLINE_SHIFT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOnlineShiftDate;


    /**
     * 计划上线班次
     */
    @Schema(title = "计划上线班次")
    @TableField("VC_ONLINE_SHIFT")
    private String vcOnlineShift = StringUtils.EMPTY;


    /**
     * 计划上线时间
     */
    @Schema(title = "计划上线时间")
    @TableField("VC_ONLINE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOnlineTime;


    /**
     * 计划下线日
     */
    @Schema(title = "计划下线日")
    @TableField("VC_OFFLINE_SHIFT_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOfflineShiftDate;


    /**
     * 计划下线班次
     */
    @Schema(title = "计划下线班次")
    @TableField("VC_OFFLINE_SHIFT")
    private String vcOfflineShift = StringUtils.EMPTY;


    /**
     * 计划下线时间
     */
    @Schema(title = "计划下线时间")
    @TableField("VC_OFFLINE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOfflineDate;


    /**
     * 计划入口工厂代码
     */
    @Schema(title = "计划入口工厂代码")
    @TableField("VC_ENTRY_PLANT_CODE")
    private String vcEntryPlantCode = StringUtils.EMPTY;


    /**
     * 计划入口车间代码
     */
    @Schema(title = "计划入口车间代码")
    @TableField("VC_ENTRY_SHOP_CODE")
    private String vcEntryShopCode = StringUtils.EMPTY;


//    /**
//     * 计划入口产线代码----del
//     */
//    @Schema(title = "计划入口产线代码")
//    @TableField("VC_ENTRY_LINE_CODE")
//    private String vcEntryLineCode = StringUtils.EMPTY;


    /**
     * 计划入口工位代码
     */
    @Schema(title = "计划入口工位代码")
    @TableField("VC_ENTRY_WS_CODE")
    private String vcEntryWsCode = StringUtils.EMPTY;


    /**
     * 计划出口工厂代码
     */
    @Schema(title = "计划出口工厂代码")
    @TableField("VC_EXIT_PLANT_CODE")
    private String vcExitPlantCode = StringUtils.EMPTY;


    /**
     * 计划出口车间代码
     */
    @Schema(title = "计划出口车间代码")
    @TableField("VC_EXIT_SHOP_CODE")
    private String vcExitShopCode = StringUtils.EMPTY;


//    /**
//     * 计划出口产线代码----del
//     */
//    @Schema(title = "计划出口产线代码")
//    @TableField("VC_EXIT_LINE_CODE")
//    private String vcExitLineCode = StringUtils.EMPTY;


    /**
     * 计划出口工位代码
     */
    @Schema(title = "计划出口工位代码")
    @TableField("VC_EXIT_WS_CODE")
    private String vcExitWsCode = StringUtils.EMPTY;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @TableField("OP_CODE")
    private Integer opCode = 0;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    private Integer exeStatus = 0;


    /**
     * 处理信息
     */
    @Schema(title = "处理信息")
    @TableField("EXE_MSG")
    private String exeMsg = StringUtils.EMPTY;


    /**
     * 处理时间
     */
    @Schema(title = "处理时间")
    @TableField("EXE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date exeTime;


}