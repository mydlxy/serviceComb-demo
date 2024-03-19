package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
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
 * @Description: 产品过点信息行为记录
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "产品过点信息行为记录")
//@TableName("AVI_TRACKING_RECORD_OPER")
@TableName("PRC_AVI_TRACKING_RECORD_OPER")
public class AviTrackingRecordOperEntity extends BaseEntity {


    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_TRACKING_RECORD_OPER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 车间代码
     */
    //    @Schema(title = "车间代码")
    //    @TableField("PM_AREA_CODE")
    //	private String pmAreaCode = StringUtils.EMPTY;
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
     * 点位
     */
    //    @Schema(title = "点位")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;


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
     * 点位行为配置ID
     */
    //    @Schema(title = "点位行为配置ID")
    //    @TableField("PM_AVI_OPER_ID")
    //    private String pmAviOperId = StringUtils.EMPTY;
    @Schema(title = "点位行为配置ID")
    @TableField("PRC_AVI_OPER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcAviOperId = Constant.DEFAULT_ID;

    /**
     * 产品唯一码
     */
    //    @Schema(title = "产品唯一码")
    //    @TableField("SN")
    //    private String sn = StringUtils.EMPTY;
    @Schema(title = "产品唯一码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 订单大类
     */
    //    @Schema(title = "订单大类")
    //    @TableField("ORDER_CATEGORY")
    //    private String orderCategory = StringUtils.EMPTY;
    @Schema(title = "订单大类")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * 是否处理
     */
    //    @Schema(title = "是否处理")
    //    @TableField("IS_PROCESS")
    //    private Boolean isProcess = false;
    @Schema(title = "是否处理")
    @TableField("IS_PROCESS")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isProcess = false;

    /**
     * 处理次数
     */
    //    @Schema(title = "处理次数")
    //    @TableField("PROCESS_COUNT")
    //    private Integer processCount = 0;
    @Schema(title = "处理次数")
    @TableField("PROCESS_COUNT")
    private Integer processCount = 0;

    /**
     * 过点时间
     */
    //    @Schema(title = "过点时间")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    //    @JsonDeserialize(using = JsonDeserializeDate.class)
    //    @TableField("INSERT_DT")
    //    private Date insertDt;
    @Schema(title = "过点时间")
    @TableField("INSERT_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date insertDt;

    /**
     * 备注
     */
    //    @Schema(title = "备注")
    //    @TableField("REMARK")
    //    private String remark = StringUtils.EMPTY;
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 行为
     */
    //    @Schema(title = "行为")
    //    @TableField("ACTION")
    //    private String action = StringUtils.EMPTY;
    @Schema(title = "行为")
    @TableField("ACTION")
    private String action = StringUtils.EMPTY;

}
