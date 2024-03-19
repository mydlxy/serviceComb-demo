package com.ca.mfd.prc.pps.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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
 * @Description: 系统定时任务明细
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统定时任务明细")
@TableName("PRC_SYS_SCHEDULING_TASK")
public class SysSchedulingTaskEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_SCHEDULING_TASK_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 任务分组
     */
    @Schema(title = "任务分组")
    @TableField("TASK_GROUP_NAME")
    private String taskGroupName = StringUtils.EMPTY;

    /**
     * 应用标识
     */
    @Schema(title = "应用标识")
    @TableField("APP_ID")
    private String appId = StringUtils.EMPTY;

    /**
     * 前车辆识别码
     */
    @Schema(title = "前车辆识别码")
    @TableField("TASK_NAME")
    private String taskName = StringUtils.EMPTY;

    /**
     * 任务访问地址
     */
    @Schema(title = "任务访问地址")
    @TableField("TASK_URL")
    private String taskUrl = StringUtils.EMPTY;

    /**
     * 任务请求方式
     */
    @Schema(title = "任务请求方式")
    @TableField("TASK_REQUEST")
    private Integer taskRequest = 0;

    /**
     * 任务执行表达式
     */
    @Schema(title = "任务执行表达式")
    @TableField("TASK_CRON")
    private String taskCron = StringUtils.EMPTY;

    /**
     * 任务描述
     */
    @Schema(title = "任务描述")
    @TableField("TASK_DESCRIBE")
    private String taskDescribe = StringUtils.EMPTY;

    /**
     * 任务类别
     */
    @Schema(title = "任务类别")
    @TableField("TASK_TYPE")
    private Integer taskType = 0;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

}
