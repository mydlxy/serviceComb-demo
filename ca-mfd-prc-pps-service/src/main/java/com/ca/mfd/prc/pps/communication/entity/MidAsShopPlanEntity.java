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
 * @Description: AS车间计划实体
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AS车间计划")
@TableName("PRC_MID_AS_SHOP_PLAN")
public class MidAsShopPlanEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_AS_SHOP_PLAN_ID", type = IdType.INPUT)
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
     * 计划工厂代码
     */
    @Schema(title = "计划工厂代码")
    @TableField("PLANT_CODE")
    private String plantCode = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("SEQUENCE_NO")
    private String sequenceNo = StringUtils.EMPTY;


    /**
     * 计划车间代码
     */
    @Schema(title = "计划车间代码")
    @TableField("SCHED_SHOP_CODE")
    private String schedShopCode = StringUtils.EMPTY;


//    /**
//     * 计划产线代码
//     */
//    @Schema(title = "计划产线代码")
//    @TableField("SCHED_LINE_CODE")
//    private String schedLineCode = StringUtils.EMPTY;


    /**
     * 计划工位代码
     */
    @Schema(title = "计划工位代码")
    @TableField("SCHED_WS_CODE")
    private String schedWsCode = StringUtils.EMPTY;


    /**
     * 计划通过日
     */
    @Schema(title = "计划通过日")
    @TableField("PLAN_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date planDate;


    /**
     * 计划通过班次
     */
    @Schema(title = "计划通过班次")
    @TableField("PLAN_SHIFT")
    private String planShift = StringUtils.EMPTY;


    /**
     * 计划通过时间
     */
    @Schema(title = "计划通过时间")
    @TableField("PLAN_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date planTime;


    /**
     * 释放版本代码
     */
    @Schema(title = "释放版本代码")
    @TableField("RELEASE_VER")
    private String releaseVer = StringUtils.EMPTY;


//    /**
//     * 上下线标识（没用，删除）
//     */
//    @Schema(title = "上下线标识（0：上线，1：下线）")
//    @TableField("WS_FLAG")
//    private String wsFlag = StringUtils.EMPTY;


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