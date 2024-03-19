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
 * @Description: 预成组工位物料实体
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "预成组工位物料")
@TableName("PRC_EPS_MODULE_STATION_MATERIAL")
public class EpsModuleStationMaterialEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_STATION_MATERIAL_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 工位
     */
    @Schema(title = "工位")
    @TableField("STATION_CODE")
    private String stationCode = StringUtils.EMPTY;


    /**
     * 电池类型
     */
    @Schema(title = "电池类型")
    @TableField("PACK_MODEL")
    private String packModel = StringUtils.EMPTY;


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
    @TableField("MATERIAL_DES")
    private String materialDes = StringUtils.EMPTY;


    /**
     * 组件编码
     */
    @Schema(title = "组件编码")
    @TableField("COMPONENT_CODE")
    private String componentCode;


    /**
     * 是否缺料
     */
    @Schema(title = "是否缺料")
    @TableField("IS_DEFICIENCY_MATERIAL")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isDeficiencyMaterial = false;


}