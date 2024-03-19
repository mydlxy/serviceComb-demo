package com.ca.mfd.prc.eps.remote.app.pm.entity;

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
 * @Description: 物料主数据实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "物料主数据")
@TableName("PRC_PM_PRODUCT_MATERIAL_MASTER")
public class PmProductMaterialMasterEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_PRODUCT_MATERIAL_MASTER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 工厂
     */
    @Schema(title = "工厂")
    @TableField("ORG")
    private String org = StringUtils.EMPTY;


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
     * 物料描述英文
     */
    @Schema(title = "物料描述英文")
    @TableField("MATERIAL_EN")
    private String materialEn = StringUtils.EMPTY;


    /**
     * 单位
     */
    @Schema(title = "单位")
    @TableField("UNIT")
    private String unit = StringUtils.EMPTY;


    /**
     * 每包数量
     */
    @Schema(title = "每包数量")
    @TableField("PACKAGE_COUNT")
    private BigDecimal packageCount = BigDecimal.valueOf(0);


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
     * 零件组
     */
    @Schema(title = "零件组")
    @TableField("PART_GROUP")
    private String partGroup = StringUtils.EMPTY;


}