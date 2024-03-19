package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
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
 * @author inkelink ${email}
 * @Description: 冻结产品
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "冻结产品")
//@TableName("AVI_FROZEN")
@TableName("PRC_AVI_FROZEN")
public class AviFrozenEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_FROZEN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 冻结时间
     */
    //    @Schema(title = "冻结时间")
    //    @TableField("FROZEN_DT")
    //	private String frozenDt = StringUtils.EMPTY;
    @Schema(title = "冻结时间")
    @TableField("FROZEN_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date frozenDt;

    /**
     * 解冻时间
     */
    //    @Schema(title = "解冻时间")
    //    @TableField("UN_FROZEN_DT")
    //    private String unFrozenDt = StringUtils.EMPTY;
    @Schema(title = "解冻时间")
    @TableField("UN_FROZEN_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    //@JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date unFrozenDt;

    /**
     * 车辆识别码
     */
    //    @Schema(title = "车辆识别码")
    //    @TableField("TPS")
    //    private String tps = StringUtils.EMPTY;
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 点位
     */
    //    @Schema(title = "点位")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;

    /**
     * 车间代码
     */
    //    @Schema(title = "车间代码")
    //    @TableField("PM_AREA_CODE")
    //    private String pmAreaCode = StringUtils.EMPTY;
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    //    @Schema(title = "线体代码")
    //    @TableField("PM_WORK_CENTER_CODE")
    //    private String pmWorkCenterCode = StringUtils.EMPTY;
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    //    @Schema(title = "AVI代码")
    //    @TableField("PM_AVI_CODE")
    //    private String pmAviCode = StringUtils.EMPTY;
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

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
     * 原因
     */
    //    @Schema(title = "原因")
    //    @TableField("REASON")
    //    private String reason = StringUtils.EMPTY;
    @Schema(title = "原因")
    @TableField("REASON")
    private String reason = StringUtils.EMPTY;

    /**
     * 是否确认
     */
    //    @Schema(title = "是否确认")
    //    @TableField("IS_CONFIG")
    //    private Boolean isConfig = false;
    @Schema(title = "是否确认")
    @TableField("IS_CONFIG")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isConfig = false;

    /**
     * 确认时间
     */
    //    @Schema(title = "确认时间")
    //    @TableField("CONFIG_DT")
    //    private String configDt = StringUtils.EMPTY;
    @Schema(title = "确认时间")
    @TableField("CONFIG_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    //@JsonDeserialize(using = JsonDeserializeDate.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date configDt;

    /**
     * 确认人ID
     */
    //    @Schema(title = "确认人ID")
    //    @TableField("CONFIG_USER_ID")
    //    private String configUserId = StringUtils.EMPTY;
    @Schema(title = "确认人ID")
    @TableField("CONFIG_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long configUserId = Constant.DEFAULT_ID;

    /**
     * 确认人
     */
    //    @Schema(title = "确认人")
    //    @TableField("CONFIG_USER_NAME")
    //    private String configUserName = StringUtils.EMPTY;
    @Schema(title = "确认人")
    @TableField("CONFIG_USER_NAME")
    private String configUserName = StringUtils.EMPTY;

    /**
     * 解冻人ID
     */
    //    @Schema(title = "解冻人ID")
    //    @TableField("UN_USER_ID")
    //    private String unUserId = StringUtils.EMPTY;
    @Schema(title = "解冻人ID")
    @TableField("UN_USER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long unUserId = Constant.DEFAULT_ID;

    /**
     * 解冻人 姓名
     */
    //    @Schema(title = "解冻人姓名")
    //    @TableField("UN_USER_NAME")
    //    private String unUserName = StringUtils.EMPTY;
    @Schema(title = "解冻人姓名")
    @TableField("UN_USER_NAME")
    private String unUserName = StringUtils.EMPTY;

}
