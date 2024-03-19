package com.ca.mfd.prc.avi.remote.app.pps.entity;

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
 * @Description: 工单
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "工单")
@TableName("PRC_PPS_ENTRY")
public class PpsEntryEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_ENTRY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 生产路线ID
     */
    @Schema(title = "生产路线ID")
    @TableField("PRC_PPS_PRODUCT_PROCESS_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPpsProductProcessId = Constant.DEFAULT_ID;

    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;

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
     * 产品识别码
     */
    @Schema(title = "产品识别码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 车间订单ID
     */
    @Schema(title = "车间订单ID")
    @TableField("PRC_PPS_ORDER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPpsOrderId = Constant.DEFAULT_ID;

    /**
     * 订单号
     */
    @Schema(title = "订单号")
    @TableField("ORDER_NO")
    private String orderNo = StringUtils.EMPTY;

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
     * 计划单号
     */
    @Schema(title = "计划单号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;

    /**
     * 父级 ID
     */
    @Schema(title = "父级ID")
    @TableField("PARENT_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId = Constant.DEFAULT_ID;

    /**
     * 父级单号
     */
    @Schema(title = "父级单号")
    @TableField("PARENT_NO")
    private String parentNo = StringUtils.EMPTY;

    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;

    /**
     * 状态 状态 1 未上线  2 锁定  3正在生成  4 已经完成   5 报废
     * 整车： 0 未上线  1 半锁定  2 锁定 3 已下发 20 生产开始  30 生产完成  98 报废
     * 冲压： 1 未生产  2 待报工 3 正在生产 4 已经完成
     */
    @Schema(title = "状态(分为整车/冲压)")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * 车间
     */
    @Schema(title = "车间")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;

    /**
     * 线体
     */
    @Schema(title = "线体")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 预计上线时间
     */
    @Schema(title = "预计上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("ESTIMATED_START_DT")
    private Date estimatedStartDt;

    /**
     * 预计下线时间
     */
    @Schema(title = "预计下线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
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
     * 实际下下时间
     */
    @Schema(title = "实际下下时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    @TableField("ACTUAL_END_DT")
    private Date actualEndDt;

    /**
     * 工单类型 1 车间工单 2分线工单
     */
    @Schema(title = "工单类型1车间工单2分线工单")
    @TableField("ENTRY_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer entryType = 0;

    /**
     * 工单来源(1、订单   2、手动创建)
     */
    @Schema(title = "工单来源(1、订单   2、手动创建)")
    @TableField("ENTRY_SOURCE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer entrySource = 0;

    /**
     * 是否生成工艺
     */
    @Schema(title = "是否生成工艺")
    @TableField("IS_CREATE_WO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isCreateWo = false;

    /**
     * 工单订阅码
     */
    @Schema(title = "工单订阅码")
    @TableField("SUBSCRIUBE_CODE")
    private String subscriubeCode = StringUtils.EMPTY;

    /**
     * 工单计划数量
     */
    @Schema(title = "工单计划数量")
    @TableField("PLAN_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer planQuantity = 1;

    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;

    /**
     * 是否处理
     */
    @Schema(title = "是否处理")
    @TableField("IS_DISPOSE")
    @JsonDeserialize(using = JsonDeserializeBoolean.class)
    private Boolean isDispose = false;

    /**
     * 订单生产数量
     */
    @Schema(title = "订单生产数量")
    @TableField(exist = false)
    private Integer orderQuantity = 0;

    /**
     * 合格数量
     */
    @Schema(title = "合格数量")
    @TableField(exist = false)
    private Integer qualifiedQuanitiy = 0;

    /**
     * 不良数量
     */
    @Schema(title = "不良数量")
    @TableField(exist = false)
    private Integer unqualifiedQuanitiy = 0;

    /**
     * 报废数量
     */
    @Schema(title = "报废数量")
    @TableField(exist = false)
    private Integer scrapQuanitiy = 0;

}
