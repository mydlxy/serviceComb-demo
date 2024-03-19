package com.ca.mfd.prc.avi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 离散产品过点信息实体
 * @author inkelink
 * @date 2023年10月31日
 * @变更说明 BY inkelink At 2023年10月31日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "离散产品过点信息")
@TableName("PRC_AVI_TRACKING_RECORD_PARTS")
public class AviTrackingRecordPartsEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_TRACKING_RECORD_PARTS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 订单大类
     */
    @Schema(title = "订单大类")
    @TableField("ORDER_CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderCategory = 0;


    /**
     * 过点时间
     */
    @Schema(title = "过点时间")
    @TableField("INSERT_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date insertDt = new Date();


    /**
     * 班次
     */
    @Schema(title = "班次")
    @TableField("PRC_PPS_SHC_SHIFT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcPpsShcShiftId = Constant.DEFAULT_ID;


    /**
     * 是否处理
     */
    @Schema(title = "是否处理")
    @TableField("IS_PROCESS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isProcess = false;


    /**
     * 过点方式;(1.自动、2.手动)
     */
    @Schema(title = "过点方式;(1.自动、2.手动)")
    @TableField("MODE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer mode = 0;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 线体代码
     */
    @Schema(title = "线体代码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * AVI名称
     */
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;


    /**
     * AVI代码
     */
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;


}