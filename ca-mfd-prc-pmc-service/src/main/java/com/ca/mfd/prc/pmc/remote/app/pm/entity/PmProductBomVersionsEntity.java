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

/**
 * @author inkelink
 * @Description: BOM版本实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "BOM版本")
@TableName("PRC_PM_PRODUCT_BOM_VERSIONS")
public class PmProductBomVersionsEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_PRODUCT_BOM_VERSIONS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 产品物料号
     */
    @Schema(title = "产品物料号")
    @TableField("PRODUCT_MATERIAL_NO")
    private String productMaterialNo = StringUtils.EMPTY;


    /**
     * BOM版本号
     */
    @Schema(title = "BOM版本号")
    @TableField("BOM_VERSIONS")
    private String bomVersions = StringUtils.EMPTY;


    /**
     * 校验码
     */
    @Schema(title = "校验码")
    @TableField("CHECK_CODE")
    private String checkCode = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;


}