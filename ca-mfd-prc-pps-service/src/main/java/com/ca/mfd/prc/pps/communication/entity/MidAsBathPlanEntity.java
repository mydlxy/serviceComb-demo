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

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author inkelink
 * @Description: AS批次计划实体
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AS批次计划")
@TableName("PRC_MID_AS_BATH_PLAN")
public class MidAsBathPlanEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_AS_BATH_PLAN_ID", type = IdType.INPUT)
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
     * 工序任务号(Attribute3 系统截取)
     */
    @Schema(title = "工序任务号(Attribute3 系统截取)")
    @TableField("PROCESS_TASK_CODE")
    private String processTaskCode = StringUtils.EMPTY;


    /**
     * 工厂组织代码
     */
    @Schema(title = "工厂组织代码")
    @TableField("ORG_CODE")
    private String orgCode = StringUtils.EMPTY;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("SHOP_CODE")
    private String shopCode = StringUtils.EMPTY;


    /**
     * 生产线代码
     */
    @Schema(title = "生产线代码")
    @TableField(exist = false)
    private String lineCode = StringUtils.EMPTY;


    /**
     * 零部件编码
     */
    @Schema(title = "零部件编码")
    @TableField("MATERIAL_CODE")
    private String materialCode = StringUtils.EMPTY;


    /**
     * 零件类型
     */
    @Schema(title = "零件类型")
    @TableField("MATERIAL_TYPE")
    private String materialType = StringUtils.EMPTY;


    /**
     * 制造能力
     */
    @Schema(title = "制造能力")
    @TableField("UNIT_CAPACITY")
    private BigDecimal unitCapacity = BigDecimal.valueOf(0);


    /**
     * 批次量
     */
    @Schema(title = "批次量")
    @TableField("LOT_QUANTITY")
    private Integer lotQuantity = 0;


    /**
     * 工位号(Attribute2 系统对应后的AVICODE)
     */
    @Schema(title = "工位号(Attribute2 系统对应后的AVICODE)")
    @TableField("WS_CODE")
    private String wsCode = StringUtils.EMPTY;


    /**
     * 上下线标识（0：上线，1：下线）
     */
    @Schema(title = "上下线标识（0：上线，1：下线）")
    @TableField("WS_FLAG")
    private String wsFlag = StringUtils.EMPTY;


    /**
     * 计划开始时间
     */
    @Schema(title = "计划开始时间")
    @TableField("PLAN_START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date planStartTime;


    /**
     * 计划结束时间
     */
    @Schema(title = "计划结束时间")
    @TableField("PLAN_END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date planEndTime;


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