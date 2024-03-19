package com.ca.mfd.prc.pmc.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author inkelink
 * @Description: BOM实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "BOM")
@TableName("PRC_PM_PRODUCT_BOM")
public class PmProductBomEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_PRODUCT_BOM_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("BOM_VERSIONS_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bomVersionsId = Constant.DEFAULT_ID;


    /**
     * 物料中文名
     */
    @Schema(title = "物料中文名")
    @TableField("MATERIAL_CN")
    private String materialCn = StringUtils.EMPTY;


    /**
     * 物料名称名称(英文)
     */
    @Schema(title = "物料名称名称(英文)")
    @TableField("MATERIAL_EN")
    private String materialEn = StringUtils.EMPTY;


    /**
     * 数量
     */
    @Schema(title = "数量")
    @TableField("QUANTITY")
    private BigDecimal quantity = BigDecimal.valueOf(0);


    /**
     * 单位
     */
    @Schema(title = "单位")
    @TableField("UNIT")
    private String unit = StringUtils.EMPTY;


    /**
     * 工厂代码
     */
    @Schema(title = "工厂代码")
    @TableField("ORGANIZATION_CODE")
    private String organizationCode = StringUtils.EMPTY;


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
     * 物料编码
     */
    @Schema(title = "物料编码")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 重量
     */
    @Schema(title = "重量")
    @TableField("WEIGHT")
    private BigDecimal weight = BigDecimal.valueOf(0);


    /**
     * 重量单位
     */
    @Schema(title = "重量单位")
    @TableField("WEIGHT_UNIT")
    private String weightUnit = StringUtils.EMPTY;


    /**
     * 每包数量
     */
    @Schema(title = "每包数量")
    @TableField("PACKAGE_COUNT")
    private BigDecimal packageCount = BigDecimal.valueOf(0);


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("STATION_CODE")
    private String stationCode = StringUtils.EMPTY;


}