package com.ca.mfd.prc.eps.communication.remote.app.pps.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
@Schema(description= "AS整车信息dto")
public class MidAsVehicleDto {
    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 制造ID
     */
    @Schema(title = "制造ID")
    private String vrn = StringUtils.EMPTY;


    /**
     * 工厂组织代码
     */
    @Schema(title = "工厂组织代码")
    private String orgCode = StringUtils.EMPTY;


    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 订单类型
     */
    @Schema(title = "订单类型")
    private String orderType = StringUtils.EMPTY;


    /**
     * OTD需求班次
     */
    @Schema(title = "OTD需求班次")
    private String otdDemandShift = StringUtils.EMPTY;


    /**
     * OTD需求日期
     */
    @Schema(title = "OTD需求日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date otdDemandDate;


    /**
     * 总装计划下线工作日期
     */
    @Schema(title = "总装计划下线工作日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date assOfflineDate;


    /**
     * 总装计划下线工作班次
     */
    @Schema(title = "总装计划下线工作班次")
    private String assOfflineShiftCode = StringUtils.EMPTY;


    /**
     * 总装计划下线时间（自然时间）
     */
    @Schema(title = "总装计划下线时间（自然时间）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date assOfflineTime;


    /**
     * 定编号
     */
    @Schema(title = "定编号")
    private String customCode = StringUtils.EMPTY;


    /**
     * 销售渠道
     */
    @Schema(title = "销售渠道")
    private String demandOrigin = StringUtils.EMPTY;


    /**
     * 需求分源（销售/物流/基地）
     */
    @Schema(title = "需求分源（销售/物流/基地）")
    private String demandSrc = StringUtils.EMPTY;


    /**
     * 释放版本代码
     */
    @Schema(title = "释放版本代码")
    private String releaseVer = StringUtils.EMPTY;


    /**
     * 计划上线日
     */
    @Schema(title = "计划上线日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOnlineShiftDate;


    /**
     * 计划上线班次
     */
    @Schema(title = "计划上线班次")
    private String vcOnlineShift = StringUtils.EMPTY;


    /**
     * 计划上线时间
     */
    @Schema(title = "计划上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOnlineTime;


    /**
     * 计划下线日
     */
    @Schema(title = "计划下线日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOfflineShiftDate;


    /**
     * 计划下线班次
     */
    @Schema(title = "计划下线班次")
    private String vcOfflineShift = StringUtils.EMPTY;


    /**
     * 计划下线时间
     */
    @Schema(title = "计划下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date vcOfflineDate;


    /**
     * 计划入口工厂代码
     */
    @Schema(title = "计划入口工厂代码")
    private String vcEntryPlantCode = StringUtils.EMPTY;


    /**
     * 计划入口车间代码
     */
    @Schema(title = "计划入口车间代码")
    private String vcEntryShopCode = StringUtils.EMPTY;

    /**
     * 计划入口工位代码
     */
    @Schema(title = "计划入口工位代码")
    private String vcEntryWsCode = StringUtils.EMPTY;


    /**
     * 计划出口工厂代码
     */
    @Schema(title = "计划出口工厂代码")
    private String vcExitPlantCode = StringUtils.EMPTY;


    /**
     * 计划出口车间代码
     */
    @Schema(title = "计划出口车间代码")
    private String vcExitShopCode = StringUtils.EMPTY;


    /**
     * 计划出口工位代码
     */
    @Schema(title = "计划出口工位代码")
    private String vcExitWsCode = StringUtils.EMPTY;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    private Integer opCode = 0;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    private Integer exeStatus = 0;


    /**
     * 处理信息
     */
    @Schema(title = "处理信息")
    private String exeMsg = StringUtils.EMPTY;


    /**
     * 处理时间
     */
    @Schema(title = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date exeTime;
}
