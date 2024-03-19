package com.ca.mfd.prc.eps.communication.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
import com.ca.mfd.prc.common.convert.JsonDeserializeDateDefNow;
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
 * @Description: 生产计划
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "生产计划")
@TableName("PRC_PPS_PLAN")
public class PpsPlanEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PLAN_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 计划编号
     */
    @Schema(title = "计划编号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;

    /**
     * 产品编码
     */
    @Schema(title = "产品编码")
    @TableField("PRODUCT_CODE")
    private String productCode = StringUtils.EMPTY;

    /**
     * 型号
     */
    @Schema(title = "型号")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;

    /**
     * 车系
     */
    @Schema(title = "车系")
    @TableField("CHARACTERISTIC1")
    private String characteristic1 = StringUtils.EMPTY;

    /**
     * 车身颜色
     */
    @Schema(title = "车身颜色")
    @TableField("CHARACTERISTIC2")
    private String characteristic2 = StringUtils.EMPTY;

    /**
     * 内饰颜色
     */
    @Schema(title = "内饰颜色")
    @TableField("CHARACTERISTIC3")
    private String characteristic3 = StringUtils.EMPTY;

    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("CHARACTERISTIC4")
    private String characteristic4 = StringUtils.EMPTY;

    /**
     * 特征：备件情况下 备件物料号
     */
    @Schema(title = "备件物料号")
    @TableField("CHARACTERISTIC5")
    private String characteristic5 = StringUtils.EMPTY;

    /**
     * 颜色标识
     */
    @Schema(title = "颜色标识")
    @TableField("CHARACTERISTIC6")
    private String characteristic6 = StringUtils.EMPTY;

    /**
     * 车辆状态码
     */
    @Schema(title = "车辆状态码")
    @TableField("CHARACTERISTIC7")
    private String characteristic7 = StringUtils.EMPTY;

    /**
     * 双色车标识(0-单色，1-双色)
     */
    @Schema(title = "双色车标识")
    @TableField("CHARACTERISTIC8")
    private String characteristic8 = StringUtils.EMPTY;

    /**
     * 天窗状态码(N-无，S-小天窗，A-全景)
     */
    @Schema(title = "天窗")
    @TableField("CHARACTERISTIC9")
    private String characteristic9 = StringUtils.EMPTY;

    /**
     * 特征5 备件计划代表产线,电池入箱策略
     */
    @Schema(title = "特征5")
    @TableField("CHARACTERISTIC10")
    private String characteristic10 = StringUtils.EMPTY;

    //Attribute1->选装包

    /**
     * 计划生产数量
     */
    @Schema(title = "计划生产数量")
    @TableField("PLAN_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planQty = 0;


    /**
     * 预计上线时间(可空)
     */
    @Schema(title = "预计上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDateDefNow.class)
    @TableField("ESTIMATED_START_DT")
    private Date estimatedStartDt;

    /**
     * 预计下线时间(可空)
     */
    @Schema(title = "预计下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDateDefNow.class)
    @TableField("ESTIMATED_END_DT")
    private Date estimatedEndDt;

    /**
     * 计划状态
     */
    @Schema(title = "计划状态")
    @TableField("PLAN_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planStatus = 0;

    /**
     * 计划来源 1 上游系统 2 手动添加
     */
    @Schema(title = "计划来源")
    @TableField("PLAN_SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planSource = 1;

    /**
     * 订单大类：1：整车  2：电池包  7:备件
     */
    @Schema(title = "订单大类：1：整车  2：电池包  7:备件")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;

    /**
     * 订单标记
     */
    @Schema(title = "订单标记")
    @TableField("ORDER_SIGN")
    private String orderSign = StringUtils.EMPTY;

    /**
     * 是否冻结
     */
    @Schema(title = "是否冻结")
    @TableField("IS_FREEZE")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isFreeze = false;

    /**
     * 生产路线ID
     */
    @Schema(title = "生产路线ID")
    @TableField("PRC_PPS_PRODUCT_PROCESS_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPpsProductProcessId = Constant.DEFAULT_ID;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * DMS订单号
     */
    @Schema(title = "DMS订单号")
    @TableField("DMS_ORDER_NO")
    private String dmsOrderNo = StringUtils.EMPTY;


    /**
     * BOM版本号
     */
    @Schema(title = "BOM版本号")
    @TableField("BOM_VERSION")
    private String bomVersion = StringUtils.EMPTY;


    /**
     * 特征版本
     */
    @Schema(title = "特征版本")
    @TableField("CHARACTERISTIC_VERSION")
    private String characteristicVersion = StringUtils.EMPTY;

    /**
     * 错误信息
     */
    @Schema(title = "错误信息")
    @TableField("ERROR_MES")
    private String errorMes = StringUtils.EMPTY;

    /**
     * 锁定类型1 自动锁定 2 手动锁定
     */
    @Schema(title = "锁定类型1 自动锁定 2 手动锁定")
    @TableField("LOCK_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer lockType = 1;

    /**
     * 生产顺序号
     */
    @Schema(title = "生产顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    /**
     * 开始AVI
     */
    @Schema(title = "开始AVI")
    @TableField("START_AVI")
    private String startAvi = StringUtils.EMPTY;

    /**
     * 结束AVI
     */
    @Schema(title = "结束AVI")
    @TableField("END_AVI")
    private String endAvi = StringUtils.EMPTY;
}
