package com.ca.mfd.prc.pm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 物料切换配置实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "物料切换配置")
@TableName("PRC_PM_MATERIAL_SWITCHOVER_CONFIG")
public class PmMaterialSwitchoverConfigEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableField("MATERIAL_SWITCHOVER_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long materialSwitchoverConfigId = Constant.DEFAULT_ID;


    /**
     * 原物料号
     */
    @Schema(title = "原物料号")
    @TableField("OLD_MATERIAL_NO")
    private String oldMaterialNo = StringUtils.EMPTY;


    /**
     * 原物料名称
     */
    @Schema(title = "原物料名称")
    @TableField("OLD_MATERIAL_NAME")
    private String oldMaterialName = StringUtils.EMPTY;


    /**
     * 现物料号
     */
    @Schema(title = "现物料号")
    @TableField("NEW_MATERIAL_NO")
    private String newMaterialNo = StringUtils.EMPTY;


    /**
     * 现物料名称
     */
    @Schema(title = "现物料名称")
    @TableField("NEW_MATERIAL_NAME")
    private String newMaterialName = StringUtils.EMPTY;


}