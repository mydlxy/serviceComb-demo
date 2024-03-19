package com.ca.mfd.prc.avi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.avi.dto.AviOperDto;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 产品过点信息
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "产品过点信息")
//@TableName("AVI_TRACKING_RECORD")
@TableName("PRC_AVI_TRACKING_RECORD")
public class AviTrackingRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_TRACKING_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 外键
     */
    //    @Schema(title = "外键")
    //    @TableField("PRC_AVI_TRACKING_RECORD_OPER_ID")
    //    @JsonSerialize(using = ToStringSerializer.class)
    //    private Long prcAviTrackingRecordOperId = Constant.DEFAULT_ID;

    /**
     * 产品唯一编码
     */
    //    @Schema(title = "唯一码")
    //    @TableField("SN")
    //    private String sn = StringUtils.EMPTY;
    @Schema(title = "产品唯一编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 订单大类
     */
    //    @Schema(title = "订单大类")
    //    @TableField("ORDER_CATEGORY")
    //    private String orderCategory = "1";
    @Schema(title = "订单大类")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = "1";

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
     * 类别(0.正常；1.下线；2.上线)
     */
    //    @Schema(title = "类别(0.正常；1.下线；2.上线)")
    //    @TableField("TYPE")
    //    private Integer type = 0;
    @Schema(title = "类别(0.正常；1.下线；2.上线)")
    @TableField("AVI_TRACKING_RECORD_TYPE")
    private Integer aviTrackIngRecordType = 0;

    /**
     * 班次
     */
    //    @Schema(title = "班次")
    //    @TableField("SHC_SHIFT_ID")
    //    private String shcShiftId = StringUtils.EMPTY;
    @Schema(title = "班次")
    @TableField("PRC_PPS_SHC_SHIFT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPpsShcShiftId = Constant.DEFAULT_ID;

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
     * 过点方式(1.自动;2.手动)
     */
    //    @Schema(title = "过点方式(1.自动;2.手动)")
    //    @TableField("MODE")
    //    private Integer mode = 0;
    @Schema(title = "过点方式(1.自动;2.手动)")
    @TableField("MODE")
    private Integer mode = 0;

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
     * AVI名称
     */
    //    @Schema(title = "AVI名称")
    //    @TableField("PM_AVI_NAME")
    //    private String pmAviName = StringUtils.EMPTY;
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * 点位
     */
    //    @Schema(title = "点位")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;

    /**
     * AVI代码
     */
    //    @Schema(title = "点位代码")
    //    @TableField("PM_AVI_CODE")
    //    private String pmAviCode = StringUtils.EMPTY;
    @Schema(title = "AVI代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * 下个AVI名称
     */
    //    @Schema(title = "下个AVI名称")
    //    @TableField("PM_NEXT_AVI_NAME")
    //    private String pmNextAviName = StringUtils.EMPTY;

    /**
     * 下个点位
     */
    //    @Schema(title = "下个点位")
    //    @TableField("PM_NEXT_AVI_ID")
    //    private String pmNextAviId = StringUtils.EMPTY;

    /**
     * 下个点位代码
     */
    //    @Schema(title = "下个点位代码")
    //    @TableField("PM_NEXT_AVI_CODE")
    //    private String pmNextAviCode = StringUtils.EMPTY;

    @Schema(title = "生成订单号")
    @TableField(exist = false)
    private String orderNo = StringUtils.EMPTY;

    @Schema(title = "计划订单号")
    @TableField(exist = false)
    private String planNo = StringUtils.EMPTY;

    @Schema(title = "vin号")
    @TableField(exist = false)
    private String barcode = StringUtils.EMPTY;

    @Schema(title = "流水号")
    @TableField(exist = false)
    private String seqNo = StringUtils.EMPTY;

    @Schema(title = "车型")
    @TableField(exist = false)
    private String characteristic1 = StringUtils.EMPTY;

    @Schema(title = "选装包")
    @TableField(exist = false)
    private String characteristic2 = StringUtils.EMPTY;

    @Schema(title = "车身颜色")
    @TableField(exist = false)
    private String characteristic3 = StringUtils.EMPTY;

    @Schema(title = "内饰颜色")
    @TableField(exist = false)
    private String characteristic4 = StringUtils.EMPTY;

    @Schema(title = "")
    @TableField(exist = false)
    private String productionNo;

    @Schema(title = "")
    @TableField(exist = false)
    private String bodyNo;

    @Schema(title = "")
    @TableField(exist = false)
    private String paintNo;

    @Schema(title = "")
    @TableField(exist = false)
    private String assemblyNo;

    @Schema(title = "")
    @TableField(exist = false)
    private List<AviOperDto> aviOperList = new ArrayList<>();
}
