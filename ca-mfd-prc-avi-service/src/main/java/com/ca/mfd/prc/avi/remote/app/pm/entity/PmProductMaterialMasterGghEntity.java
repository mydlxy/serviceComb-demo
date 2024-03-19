package com.ca.mfd.prc.avi.remote.app.pm.entity;

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
 * @Description: 整车物料号对应公告号表实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "整车物料号对应公告号表")
@TableName("PRC_PM_PRODUCT_MATERIAL_MASTER_GGH")
public class PmProductMaterialMasterGghEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_PRODUCT_MATERIAL_MASTER_GGH_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 整车物料号
     */
    @Schema(title = "整车物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 公告号
     */
    @Schema(title = "公告号")
    @TableField("GGH")
    private String ggh = StringUtils.EMPTY;


    /**
     * 备用1
     */
    @Schema(title = "备用1")
    @TableField("SPARE1")
    private String spare1 = StringUtils.EMPTY;


    /**
     * 备用2
     */
    @Schema(title = "备用2")
    @TableField("SPARE2")
    private String spare2 = StringUtils.EMPTY;


    /**
     * 备用3
     */
    @Schema(title = "备用3")
    @TableField("SPARE3")
    private String spare3 = StringUtils.EMPTY;


}