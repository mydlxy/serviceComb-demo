package com.ca.mfd.prc.eps.communication.remote.app.pm.entity;

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
 * @Description: 特征主数据实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "特征主数据")
@TableName("PRC_PM_PRODUCT_CHARACTERISTICS")
public class PmProductCharacteristicsEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_PRODUCT_CHARACTERISTICS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 特征版本外键
     */
    @Schema(title = "特征版本外键")
    @TableField("CHARACTERISTICS_VERSIONS_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long characteristicsVersionsId = Constant.DEFAULT_ID;


    /**
     * 产品编码
     */
    @Schema(title = "产品编码")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 特征项
     */
    @Schema(title = "特征项")
    @TableField("PRODUCT_CHARACTERISTICS_NAME")
    private String productCharacteristicsName = StringUtils.EMPTY;


    /**
     * 特征项描述（中文）
     */
    @Schema(title = "特征项描述（中文）")
    @TableField("DESCRIPTION_CN")
    private String descriptionCn = StringUtils.EMPTY;


    /**
     * 特征项描述（英文）
     */
    @Schema(title = "特征项描述（英文）")
    @TableField("DESCRIPTION_EN")
    private String descriptionEn = StringUtils.EMPTY;


    /**
     * 特征代码
     */
    @Schema(title = "特征代码")
    @TableField("PRODUCT_CHARACTERISTICS_CODE")
    private String productCharacteristicsCode = StringUtils.EMPTY;


    /**
     * 特征值
     */
    @Schema(title = "特征值")
    @TableField("PRODUCT_CHARACTERISTICS_VALUE")
    private String productCharacteristicsValue = StringUtils.EMPTY;


    /**
     * 特征值描述（中文）
     */
    @Schema(title = "特征值描述（中文）")
    @TableField("VALUE_CN")
    private String valueCn = StringUtils.EMPTY;


    /**
     * 特征值描述（英文）
     */
    @Schema(title = "特征值描述（英文）")
    @TableField("VALUE_EN")
    private String valueEn = StringUtils.EMPTY;


    /**
     * 来源
     */
    @Schema(title = "来源")
    @TableField("SOURCE")
    private Integer source = 0;


}