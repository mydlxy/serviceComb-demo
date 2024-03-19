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
 * @Description: 模组绑定物料实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "模组绑定物料")
@TableName("PRC_EPS_MODULE_MATERIAL_BINDING")
public class EpsModuleMaterialBindingEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_MATERIAL_BINDING_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 计划号
     */
    @Schema(title = "计划号")
    @TableField("PLAN_NO")
    private String planNo = StringUtils.EMPTY;


    /**
     * 工单号
     */
    @Schema(title = "工单号")
    @TableField("ENTRY_NO")
    private String entryNo = StringUtils.EMPTY;


    /**
     * 组件编码
     */
    @Schema(title = "组件编码")
    @TableField("COMPONENT_CODE")
    private String componentCode = StringUtils.EMPTY;


    /**
     * 物料号
     */
    @Schema(title = "物料号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 物料条码
     */
    @Schema(title = "物料条码")
    @TableField("MATERIAL_BARCODE")
    private String materialBarcode = StringUtils.EMPTY;


    /**
     * 产品条码
     */
    @Schema(title = "产品条码")
    @TableField("PRODUCT_BARCODE")
    private String productBarcode = StringUtils.EMPTY;


}