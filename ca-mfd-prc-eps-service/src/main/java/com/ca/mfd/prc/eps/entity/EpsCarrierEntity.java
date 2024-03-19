package com.ca.mfd.prc.eps.entity;

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
 * @Description: 载具信息实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "载具信息")
@TableName("PRC_EPS_CARRIER")
public class EpsCarrierEntity extends BaseEntity {

    /**
     * 主键id
     */
    @Schema(title = "主键id")
    @TableId(value = "PRC_EPS_CARRIER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 载具条码
     */
    @Schema(title = "载具条码")
    @TableField("CARRIER_BARCODE")
    private String carrierBarcode = StringUtils.EMPTY;

    /**
     * 批次号
     */
    @Schema(title = "批次号")
    @TableField("BATCH_NUMBER")
    private String batchNumber = StringUtils.EMPTY;

    /**
     * 是否空闲
     */
    @Schema(title = "是否空闲")
    @TableField("IS_USE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isUse = false;

    /**
     * 是否验证装载数量
     */
    @Schema(title = "是否验证装载数量")
    @TableField("IS_VERIFY_QUANTITY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isVerifyQuantity = false;

    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;

    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;

    /**
     * 物料容纳数量
     */
    @Schema(title = "物料容纳数量")
    @TableField("MATERIAL_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer materialCount = 0;

    /**
     * 实际数量
     */
    @Schema(title = "实际数量")
    @TableField("PRACTICAL_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer practicalCount = 0;


    /**
     * 工序编码（工艺路径获取）
     */
    @Schema(title = "工序编码（工艺路径获取）")
    @TableField("PROCESS_CODE")
    private String processCode = StringUtils.EMPTY;


    /**
     * 物料描述
     */
    @Schema(title = "物料描述")
    @TableField("MATERIAL_DES")
    private String materialDes = StringUtils.EMPTY;


    /**
     * 是否验证同一计划
     */
    @Schema(title = "是否验证同一计划")
    @TableField("IS_PLAN")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isPlan = false;


    /**
     * 是否验证同一物料
     */
    @Schema(title = "是否验证同一物料")
    @TableField("IS_MATERIAL")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isMaterial = false;


}