package com.ca.mfd.prc.pm.communication.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @Description: AS班次信息中间表实体
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AS班次信息中间表")
@TableName("PRC_MID_AS_SHIFT")
public class MidAsShiftEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_AS_SHIFT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 记录表ID
     */
    @Schema(title = "记录表ID")
    @TableField("PRC_MID_API_LOG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcMidApiLogId = Constant.DEFAULT_ID;


    /**
     * 班次名称
     */
    @Schema(title = "班次名称")
    @TableField("SHIFT_NAME")
    private String shiftName = StringUtils.EMPTY;


    /**
     * 班次代码(AS.班次模式代码)
     */
    @Schema(title = "班次代码(AS.班次模式代码)")
    @TableField("SHIFT_CODE")
    private String shiftCode = StringUtils.EMPTY;


    /**
     * 开始时间
     */
    @Schema(title = "开始时间")
    @TableField("START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date startTime = new Date();


    /**
     * 结束时间
     */
    @Schema(title = "结束时间")
    @TableField("END_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date endTime = new Date();


    /**
     * 是否跨天
     */
    @Schema(title = "是否跨天")
    @TableField("IS_CROSS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isCross = false;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @TableField("OP_CODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer opCode = 0;


    /**
     * 处理状态0：未处理，1：成功， 2：失败，3：不处理
     */
    @Schema(title = "处理状态0：未处理，1：成功， 2：失败，3：不处理")
    @TableField("EXE_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
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
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date exeTime = new Date();


}