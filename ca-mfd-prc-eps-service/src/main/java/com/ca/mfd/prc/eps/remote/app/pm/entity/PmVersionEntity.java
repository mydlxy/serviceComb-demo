package com.ca.mfd.prc.eps.remote.app.pm.entity;

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
 * @author inkelink
 * @Description: 建模版本控制实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "建模版本控制")
@TableName("PRC_PM_VERSION")
public class PmVersionEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_VERSION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 版本
     */
    @Schema(title = "版本")
    @TableField("VERSION")
    private Integer version = 0;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 工厂建模
     */
    @Schema(title = "工厂建模")
    @TableField("CONTENT")
    private String content = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLED")
    private Boolean isEnabled = false;


    /**
     * 启用时间
     */
    @Schema(title = "启用时间")
    @TableField("BEGIN_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date beginDt = new Date();


    /**
     * 停用时间
     */
    @Schema(title = "停用时间")
    @TableField("END_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date endDt = new Date();


    /**
     *
     */
    @Schema(title = "")
    @TableField("BEGIN_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long beginUserId = Constant.DEFAULT_ID;


    /**
     * 停用人
     */
    @Schema(title = "停用人")
    @TableField("END_USER_NAME")
    private String endUserName = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("END_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long endUserId = Constant.DEFAULT_ID;


    /**
     * 启用人
     */
    @Schema(title = "启用人")
    @TableField("BEGIN_USER_NAME")
    private String beginUserName = StringUtils.EMPTY;


    /**
     * 安灯参数是否下发PLC
     */
    @Schema(title = "安灯参数是否下发PLC")
    @TableField("IS_DOWN_ANDON")
    private Boolean isDownAndon = false;


    /**
     * 车间名称
     */
    @Schema(title = "车间名称")
    @TableField("WORKSHOP_NAME")
    private String workshopName = StringUtils.EMPTY;


    /**
     * OT参数是否下发PLC
     */
    @Schema(title = "OT参数是否下发PLC")
    @TableField("IS_DOWN_OT")
    private Boolean isDownOt = false;


}