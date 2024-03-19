package com.ca.mfd.prc.avi.remote.app.pps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeBoolean;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
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
 * @Description: 订单
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单")
@TableName("PRC_PPS_ORDER")
public class PpsOrderEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ORDER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 订单号
     */
    @Schema(title = "订单号")
    @TableField("ORDER_NO")
    private String orderNo = StringUtils.EMPTY;

    /**
     * 计划号
     */
    @Schema(title = "计划号")
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
     * 车系---选装包
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
    @Schema(title = "特征5")
    @TableField("CHARACTERISTIC5")
    private String characteristic5 = StringUtils.EMPTY;

    /**
     * 特征1(基础车型)
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
     * 预计上线时间
     */
    @Schema(title = "预计上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDateDefNow.class)
    @TableField("ESTIMATED_START_DT")
    private Date estimatedStartDt;

    /**
     * 预计下线时间
     */
    @Schema(title = "预计下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDateDefNow.class)
    @TableField("ESTIMATED_END_DT")
    private Date estimatedEndDt;


    /**
     * 实际上线时间
     */
    @Schema(title = "实际上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("ACTUAL_START_DT")
    private Date actualStartDt;

    /**
     * 实际下线时间
     */
    @Schema(title = "实际下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("ACTUAL_END_DT")
    private Date actualEndDt;


    /**
     * 计划生产顺序号
     */
    @Schema(title = "计划生产顺序号")
    @TableField("DISPLAY_NO")
    private String displayNo = StringUtils.EMPTY;

    /**
     * 生产顺序号(除开整车其它产品用)
     */
    @Schema(title = "生产顺序号(除开整车其它产品用)")
    @TableField("PRODUCTION_NO")
    private String productionNo = StringUtils.EMPTY;

    /**
     * 焊装生产顺序号
     */
    @Schema(title = "焊装生产顺序号")
    @TableField("BODY_NO")
    private String bodyNo = StringUtils.EMPTY;

    /**
     * 涂装生产顺序号
     */
    @Schema(title = "涂装生产顺序号")
    @TableField("PAINT_NO")
    private String paintNo = StringUtils.EMPTY;

    /**
     * 总装生产顺序号
     */
    @Schema(title = "总装生产顺序号")
    @TableField("ASSEMBLY_NO")
    private String assemblyNo = StringUtils.EMPTY;


    /**
     * 唯一码
     */
    @Schema(title = "唯一码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 订单状态
     */
    @Schema(title = "订单状态")
    @TableField("ORDER_STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderStatus = 0;

    /**
     * 订单来源(1.自动；2.导入)
     */
    @Schema(title = "订单来源(1.自动；2.导入)")
    @TableField("ORDER_SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderSource = 0;

    /**
     * 订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件
     */
    @Schema(title = "订单大类：1：整车  2：电池包  3：压铸  4：机加   5：冲压  6：电池上盖 7：备件")
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
     * 订单生产数量
     */
    @Schema(title = "订单生产数量")
    @TableField("ORDER_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer orderQuantity = 1;

    /**
     * 订单完成数量
     */
    @Schema(title = "订单完成数量")
    @TableField("COMPLETE_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer completeQuantity = 1;

    /**
     * BOM版本号
     */
    @Schema(title = "BOM版本号")
    @TableField("BOM_VERSION")
    private String bomVersion = StringUtils.EMPTY;

    /**
     * 特征版本号
     */
    @Schema(title = "特征版本号")
    @TableField("CHARACTERISTIC_VERSION")
    private String characteristicVersion = StringUtils.EMPTY;

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
