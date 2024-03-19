package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @author inkelink
 * @Description: 智能操作终端操作日志实体
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "智能操作终端操作日志")
@TableName("PRC_EPS_LOG_OT_OPERATION")
public class EpsLogOtOperationEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_LOG_OT_OPERATION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间名
     */
    @Schema(title = "车间名")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 线体名
     */
    @Schema(title = "线体名")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 工位名
     */
    @Schema(title = "工位名")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 用户
     */
    @Schema(title = "用户")
    @TableField("OPERATION_USER")
    private String operationUser = StringUtils.EMPTY;


    /**
     * 行为
     */
    @Schema(title = "行为")
    @TableField("OPERATION")
    private String operation = StringUtils.EMPTY;


    /**
     * 操作时间
     */
    @Schema(title = "操作时间")
    @TableField("OPRERATION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date oprerationTime;


    /**
     * 操作等级
     */
    @Schema(title = "操作等级")
    @TableField("OPERATION_LEVER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer operationLever = 0;

}