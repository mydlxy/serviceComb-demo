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
 * @Description: 电池模组关系实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "电池模组关系")
@TableName("PRC_EPS_MODULE_RELATION")
public class EpsModuleRelationEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_RELATION_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 模组编码
     */
    @Schema(title = "模组编码")
    @TableField("MODULE_CODE")
    private String moduleCode = StringUtils.EMPTY;


    /**
     * 模组条码
     */
    @Schema(title = "模组条码")
    @TableField("MODULE_BARCODE")
    private String moduleBarcode = StringUtils.EMPTY;


    /**
     * 小单元编码
     */
    @Schema(title = "小单元编码")
    @TableField("UNIT_CODE")
    private String unitCode = StringUtils.EMPTY;


    /**
     * 小单元条码
     */
    @Schema(title = "小单元条码")
    @TableField("UNIT_BARCODE")
    private String unitBarcode = StringUtils.EMPTY;


    /**
     * 电芯编码
     */
    @Schema(title = "电芯编码")
    @TableField("CELL_CODE")
    private String cellCode = StringUtils.EMPTY;


    /**
     * 电芯条码
     */
    @Schema(title = "电芯条码")
    @TableField("CELL_BARCODE")
    private String cellBarcode = StringUtils.EMPTY;

    /**
     * 元素类型
     */
    @Schema(title = "元素类型")
    @TableField("CELL_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer cellType = 0;

}