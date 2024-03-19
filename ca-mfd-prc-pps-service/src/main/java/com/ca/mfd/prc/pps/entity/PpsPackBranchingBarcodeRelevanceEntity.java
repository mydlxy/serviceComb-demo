package com.ca.mfd.prc.pps.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 电池分线条码关联实体
 * @author inkelink
 * @date 2024年02月26日
 * @变更说明 BY inkelink At 2024年02月26日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池分线条码关联")
@TableName("PRC_PPS_PACK_BRANCHING_BARCODE_RELEVANCE")
public class PpsPackBranchingBarcodeRelevanceEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PPS_PACK_BRANCHING_BARCODE_RELEVANCE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 线体编码
     */
    @Schema(title = "线体编码")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 工位组
     */
    @Schema(title = "工位组")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 组件编码
     */
    @Schema(title = "组件编码")
    @TableField("COMPONENT_CODE")
    private String componentCode = StringUtils.EMPTY;


    /**
     * 组件描述
     */
    @Schema(title = "组件描述")
    @TableField("COMPONENT_DES")
    private String componentDes;


    /**
     * 原始条码
     */
    @Schema(title = "原始条码")
    @TableField("OLD_BARCODE")
    private String oldBarcode = StringUtils.EMPTY;


    /**
     * 新条码
     */
    @Schema(title = "新条码")
    @TableField("NEW_BARCODE")
    private String newBarcode = StringUtils.EMPTY;


    /**
     * 订单编码
     */
    @Schema(title = "订单编码")
    @TableField("ORDER_CODE")
    private String orderCode;


    /**
     * 计划类型：1：整车  2：电池包   7:备件 10：电池盖板分装 11：BDU（电池高压盒） 12：DMS（电池管理器）
     */
    @Schema(title = "计划类型：1：整车  2：电池包   7:备件 10：电池盖板分装 11：BDU（电池高压盒） 12：DMS（电池管理器）")
    @TableField("ORDER_CATEGORY")
    private String orderCategory = StringUtils.EMPTY;


}