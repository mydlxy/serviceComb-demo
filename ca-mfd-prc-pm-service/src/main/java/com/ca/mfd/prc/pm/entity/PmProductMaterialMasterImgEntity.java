package com.ca.mfd.prc.pm.entity;

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
 * @Description: 物料图片实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "物料图片")
@TableName("PRC_PM_PRODUCT_MATERIAL_MASTER_IMG")
public class PmProductMaterialMasterImgEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_PRODUCT_MATERIAL_MASTER_IMG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("PM_WORKSHOP_CODE")
    private String pmWorkshopCode = StringUtils.EMPTY;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料名称
     */
    @Schema(title = "物料名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 文件名称
     */
    @Schema(title = "文件名称")
    @TableField("IMG_FILE_NAME")
    private String imgFileName = StringUtils.EMPTY;


    /**
     * 文件路径
     */
    @Schema(title = "文件路径")
    @TableField("IMG_FILE_PATH")
    private String imgFilePath = StringUtils.EMPTY;


}