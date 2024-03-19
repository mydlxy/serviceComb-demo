package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @Description: 产品跟踪站点终端操作日志
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "产品跟踪站点终端操作日志")
//@TableName("AVI_OPERATION_LOG")
@TableName("PRC_AVI_OPERATION_LOG")
public class AviOperationLogEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_OPERATION_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间名
     */
    //    @Schema(title = "车间名")
    //    @TableField("PM_AREA_NAME")
    //	private String pmAreaName = StringUtils.EMPTY;
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;

    /**
     * 车间ID
     */
    //    @Schema(title = "车间ID")
    //    @TableField("PM_AREA_ID")
    //	private String pmAreaId = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 线体名
     */
    //    @Schema(title = "线体名")
    //    @TableField("PM_WORK_CENTER_NAME")
    //	private String pmWorkCenterName = StringUtils.EMPTY;
    @Schema(title = "线体名称")
    @TableField("LINE_NAME")
    private String lineName = StringUtils.EMPTY;

    /**
     * 线体ID
     */
    //    @Schema(title = "线体ID")
    //    @TableField("PM_WORK_CENTER_ID")
    //    private String pmWorkCenterId = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    //    @Schema(title = "AVI名称")
    //    @TableField("PM_AVI_NAME")
    //    private String pmAviName = StringUtils.EMPTY;
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * AVI_ID
     */
    //    @Schema(title = "AVI_ID")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;

    /**
     * AVI_IP
     */
    //    @Schema(title = "AVI_IP")
    //    @TableField("PM_AVI_IP_ADDRESS")
    //    private String pmAviIpAddress = StringUtils.EMPTY;
    @Schema(title = "AVI_IP")
    @TableField("AVI_IP_ADDRESS")
    private String aviIpAddress = StringUtils.EMPTY;

    /**
     * 用户
     */
    //    @Schema(title = "用户")
    //    @TableField("OPERATION_USER")
    //    private String operationUser = StringUtils.EMPTY;
    @Schema(title = "用户")
    @TableField("OPERATION_USER")
    private String operationUser = StringUtils.EMPTY;

    /**
     * 行为
     */
    //    @Schema(title = "行为")
    //    @TableField("OPERATION")
    //    private String operation = StringUtils.EMPTY;
    @Schema(title = "行为")
    @TableField("OPERATION")
    private String operation = StringUtils.EMPTY;

    /**
     * 操作时间
     */
    //    @Schema(title = "操作时间")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    //    @JsonDeserialize(using = JsonDeserializeDate.class)
    //    @TableField("OPRERATION_TIME")
    //    private Date oprerationTime;
    @Schema(title = "操作时间")
    @TableField("OPRERATION_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date oprerationTime;


    /**
     * 操作等级
     */
    //    @Schema(title = "操作等级")
    //    @TableField("OPERATION_LEVER")
    //    private Integer operationLever = 0;
    @Schema(title = "操作等级")
    @TableField("OPERATION_LEVER")
    private Integer operationLever = 0;

}
