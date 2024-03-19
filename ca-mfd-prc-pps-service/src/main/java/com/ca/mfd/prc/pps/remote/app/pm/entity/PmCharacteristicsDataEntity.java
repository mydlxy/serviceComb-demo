package com.ca.mfd.prc.pps.remote.app.pm.entity;

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
@Schema(description = "")
@TableName("PRC_PM_CHARACTERISTICS_DATA")
public class PmCharacteristicsDataEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_CHARACTERISTICS_DATA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 特征项
     */
    @Schema(title = "特征项")
    @TableField("CHARACTERISTICS_NAME")
    private String characteristicsName = StringUtils.EMPTY;


    /**
     * 特征项（中文）
     */
    @Schema(title = "特征项（中文）")
    @TableField("DESCRIPTION_CN")
    private String descriptionCn = StringUtils.EMPTY;


    /**
     * 特征项（英文）
     */
    @Schema(title = "特征项（英文）")
    @TableField("DESCRIPTION_EN")
    private String descriptionEn = StringUtils.EMPTY;


    /**
     * 特征代码
     */
    @Schema(title = "特征代码")
    @TableField("CHARACTERISTICS_CODE")
    private String characteristicsCode = StringUtils.EMPTY;


    /**
     * 特征值
     */
    @Schema(title = "特征值")
    @TableField("CHARACTERISTICS_VALUE")
    private String characteristicsValue = StringUtils.EMPTY;


    /**
     * 特征值（中文）
     */
    @Schema(title = "特征值（中文）")
    @TableField("VALUE_CN")
    private String valueCn = StringUtils.EMPTY;


    /**
     * 特征值（英文）
     */
    @Schema(title = "特征值（英文）")
    @TableField("VALUE_EN")
    private String valueEn = StringUtils.EMPTY;


    /**
     * 数据来源
     */
    @Schema(title = "数据来源")
    @TableField("SOURCE")
    private Integer source = 0;


}